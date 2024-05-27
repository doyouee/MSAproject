package com.inzent.projectAPI.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.Pagination;
import com.inzent.projectAPI.common.Globals;
import com.inzent.projectAPI.vo.*;

import com.inzent.commonMethod.common.HttpSender;
import com.inzent.projectAPI.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 프로젝트 관리
 * 프로젝트 CRUD 및 사용자 CU @author 박민희
 * 프로젝트 R, 검수상태 U @author 이도영
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Value("${API_KEY}")
    public String apiKey;
    @Value("${API_URL}")
    public String apiUrl;

    public ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> getProject(String tokenValue, String projectId, String projectCode, String projectName, String teamName, String inspectionStatus, String teamId, String userEmail, String managerEmail, Integer pageNum) {
        Map<String, Object> headerValue = new HashMap<>();
        headerValue.put("TOKEN_VALUE", tokenValue);
        headerValue.put("API_KEY", apiKey);
        ArrayList<String> managerEmailArr = null;

        //공통코드 조회 - T01, P01
        Map<String, Object> result = HttpSender.sndHTTP(apiUrl+"/commonCode" , HttpMethod.GET, "type=code_id&codeId=" + Globals.GroupId, headerValue);
        List<Map<String,Object>> team = (List<Map<String, Object>>) result.get(Consts.RESPONSE_MESSAGE);
        ArrayList<String> searchTeamId = new ArrayList<String>();
        if( teamName != null  &&  teamName != "" ){
            for(Map<String ,Object> o : team){
                if(o.get("codeName").toString().contains(teamName)){
                	searchTeamId.add(o.get("codeId").toString());
                }
            }
        }

        if( managerEmail != null && managerEmail != ""){
            managerEmailArr = new ArrayList<String>(Arrays.asList(managerEmail.split(",")));
        }

        List<ProjectVO> projectList = projectMapper.getProject(projectId, projectCode, projectName, inspectionStatus, teamId, userEmail, 
        		managerEmailArr, searchTeamId, teamName, Pagination.setPage(pageNum));

        List<Map<String, Object>> prjAllList = objectMapper.convertValue(projectList,  new TypeReference<List<Map<String,Object>>>(){});
        return getTeamNameList(result, prjAllList);
    }

    public List<Map<String, Object>> getTeamNameList(Map<String, Object> result, List<Map<String, Object>> projectList){
        List<Map<String, Object>> returnlist = (List<Map<String, Object>>) result.get(Consts.RESPONSE_MESSAGE);
        List<Map<String, Object>> commonList = new ArrayList<>();

        for (Map<String, Object> teamIdlist : projectList) {

            for (Map<String, Object> codeidList : returnlist) {
                if (teamIdlist.get("teamId").toString().equals(codeidList.get("codeId").toString())) {
                    teamIdlist.put("teamName", codeidList.get("codeName"));
                }if (teamIdlist.get("projectStatus").toString().equals(codeidList.get("codeId").toString())) {
                    teamIdlist.put("projectStatusName", codeidList.get("codeName"));
                }
                // 검수 상태 매칭
                if(teamIdlist.get("inspectionStatus").toString().equals("0")){
                    teamIdlist.put("inspectionStatusName", "미검수");
                } else if(teamIdlist.get("inspectionStatus").toString().equals("1")){
                    teamIdlist.put("inspectionStatusName", "검수 요청");
                } else if(teamIdlist.get("inspectionStatus").toString().equals("2")){
                    teamIdlist.put("inspectionStatusName", "검수 완료");
                } else if(teamIdlist.get("inspectionStatus").toString().equals("3")){
                    teamIdlist.put("inspectionStatusName", "반려");
                }
            }
            commonList.add(teamIdlist);
        }
        return commonList;
    }

    public Map<String, Object> insertProject(Map<String, Object> addProjectMap) {
        AddProjectVO addProjectVO = objectMapper.convertValue(addProjectMap.get(Consts.REQUEST_MESSAGE), AddProjectVO.class);

        List<Map<String, Object>> projectList = projectMapper.getProjectAll();
        Map<String , Object> result = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        for(Map<String, Object> list : projectList) {
            map = objectMapper.convertValue(list, Map.class);
            if(map.get("PROJECT_NAME").toString().equals(addProjectVO.getProjectName())){
                if (map.get("DELETE_YN").toString().equals("Y")){
                    result.put("message" , "삭제된 프로젝트명 입니다. 관리자에게 문의해주세요");
                }else{
                    result.put("message" , "중복된 프로젝트명 입니다. 다시 입력해주세요");
                }
                result.put("type","name");
                return result;
            }else if (map.get("PROJECT_CODE").toString().equals(addProjectVO.getProjectCode())){
                result.put("message" , "프로젝트 코드가 중복되었습니다.");
                result.put("type","code");
                return result;
            }
        }

        projectMapper.insertProject(addProjectVO);
        result.put("message" , Consts.SUCCESS);
        result.put("projectId" , addProjectVO.getProjectId());
        return result;
    }

    public void updateProject(Map<String, Object> editProjectMap) {
        try{
            EditProjectVO editProjectVO = objectMapper.convertValue(editProjectMap.get(Consts.REQUEST_MESSAGE), EditProjectVO.class);
            projectMapper.updateProject(editProjectVO);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteProject(String projectId){
        try {
            projectMapper.deleteProject(projectId);
            ProjectUserVO projectUserVO = new ProjectUserVO();
            projectUserVO.setProjectId(projectId);
            projectMapper.deleteProjectUser(projectUserVO);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> getProjectUser(String projectId, String userEmail) {
        List<ProjectUserVO> userList = projectMapper.getProjectUser(projectId, userEmail);
        List<Map<String, Object>> userAllList = new ArrayList<>();
        for(ProjectUserVO list : userList) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            map = objectMapper.convertValue(list, Map.class);
            userAllList.add(map);
        }
        return userAllList;
    }

    public void updateProjectUser(Map<String, Object> userMap) {
        try {
            List<ProjectUserVO> projectUserVO = objectMapper.convertValue(userMap.get(Consts.REQUEST_MESSAGE), objectMapper.getTypeFactory().defaultInstance().constructCollectionType(List.class, ProjectUserVO.class));
            projectMapper.deleteProjectUser(projectUserVO.get(0));
            projectMapper.insertProjectUser(projectUserVO);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void updateInspection(Map<String, Object> inspectionMap) throws Exception {
    	ProjectInspectionVO projectInspectionVO = objectMapper.convertValue(inspectionMap.get(Consts.REQUEST_MESSAGE), ProjectInspectionVO.class);
    	projectInspectionVO.setProjectIdList(new ArrayList<String>(Arrays.asList(projectInspectionVO.getProjectId().split(","))));
        projectMapper.updateInspection(projectInspectionVO);
    }

}
