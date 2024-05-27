package com.inzent.deliverablesUI.controller;

import com.inzent.commonMethod.common.*;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.vo.AddProjectVO;
import com.inzent.deliverablesUI.vo.EditProjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProjectController extends MasterController {
	
	@Value("${API_KEY}")
	public String apiKey;
	
	@Value("${API_URL}")
	public String apiUrl;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/project")
	public ModelAndView project(HttpSession session, HttpServletResponse response, @RequestParam(value="searchVal", required = false) String searchVal, @RequestParam(value = "pageNum" , required = false) String pageNum) throws Exception {
		ModelAndView mav = new ModelAndView("project");
		try {
			if (!isLogin(mav, session)) {
				return mav;
			}
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> user = (Map<String, Object>) session.getAttribute("userInfo");
			mav.addObject("authorityId", user.get("authorityId"));
			mav.addObject("codeName", user.get("codeName"));
			mav.addObject("myUserName", user.get("userName"));
			mav.addObject("myUserEmail", user.get("userEmail"));
			mav.addObject("teamId", user.get("teamId"));
			mav.addObject("projectListType", "project");
			Map<String, Object> requestMessage = new HashMap<>();
			if(pageNum != null){
				mav.addObject("pageNum",pageNum);
				if(searchVal != null){
					mav.addObject("search", searchVal);
					requestMessage = Json.jsonToMap(searchVal);
					requestMessage.put("pageNum",pageNum);
					responseMap = getProjectListByAuthority(response, requestMessage, session, "project");

				}else{
					requestMessage.put("pageNum",pageNum);
					responseMap = getProjectListByAuthority(response,requestMessage , session, "project");
				}
			}else{
				mav.addObject("pageNum","1");
				requestMessage.put("pageNum","1");
				responseMap = getProjectListByAuthority(response,requestMessage , session, "project");
			}

			if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				Map<String, Object> projectMap = (Map<String, Object>) responseMap.get(Consts.RESPONSE_MESSAGE);
				mav.addObject("projectList", (List<Map<String, Object>>) projectMap.get("firstProject"));
				mav.addObject("projectAllListSize", ((List<Map<String, Object>>) projectMap.get("projectAll")).size());

				Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));

				//프로젝트 상태값 조회
				responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=P01&type=code_id", headerMap);
				if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					mav.addObject("commonStatus" , responseMap.get(Consts.RESPONSE_MESSAGE));

					//부서명 조회
					responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=T01&type=code_id", headerMap);
					if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						mav.addObject("commonTeam" , responseMap.get(Consts.RESPONSE_MESSAGE));
					}
				}

			}
			mav.addObject(Consts.RESPONSE_CODE, responseMap.get(Consts.RESPONSE_CODE));
			if(!responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				mav.addObject(Consts.RESPONSE_MESSAGE, responseMap.get(Consts.RESPONSE_MESSAGE));
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return mav;
	}
	
	public Map<String, Object> getUserAllMap(HttpServletResponse response, Map<String, Object> param, Map<String, Object> headerMap) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			// 사용자 전체 목록 조회 (프로젝트 담당자)
			resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, param, headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				List<Map<String, Object>> userAllList = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				for(Map<String, Object> userAll : userAllList) {
					userAll.put("userId", userAll.get("userEmail").toString().replaceAll("@", "").replaceAll("\\.", ""));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	public Map<String, Object> getProjectListByAuthority(HttpServletResponse response, Map<String, Object> requestMessage, HttpSession session, String type) throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
			Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
			String userAuthorityId = userInfo.get("authorityId").toString();

			if ("3".equals(userAuthorityId)) {
				requestMessage.put("userEmail", userInfo.get("userEmail"));
			}
			
			// 모든 프로젝트 구하기
			Map<String, Object> allRequestMessage = new HashMap<String, Object>();
			allRequestMessage.putAll(requestMessage);
			allRequestMessage.remove("pageNum");
			resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, allRequestMessage, headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				List<Map<String, Object>> projectAllList = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				
				if ("2".equals(userAuthorityId)) {
					// 같은 팀 사용자 정보 구하기
					String myTeamId = (String) userInfo.get("teamId");
					resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, "teamId=" + myTeamId, headerMap);
					if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						List<Map<String, Object>> myTeamUserList = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
						
						List<Map<String, Object>> firstProjectList = new ArrayList<Map<String,Object>>();
						for(Map<String, Object> projectAll : projectAllList) {
							if(projectAll.get("teamId").equals(myTeamId)) { // user와 같은 팀이 속한 프로젝트
								firstProjectList.add(projectAll);
							}else { // user와 다른 팀이 속한 프로젝트이고 같은 팀의 담당자가 속한 프로젝트
								for(Map<String, Object> myTeamUser : myTeamUserList) {
									if(projectAll.get("manager").toString().contains("/" + myTeamUser.get("userEmail"))) {
										if (!firstProjectList.contains(projectAll)){
											firstProjectList.add(projectAll);
										}
									}
								}
							}
						}
						result.put("projectAll", firstProjectList);
						Map<String, Integer> pagingMap = setPage(Integer.parseInt((String) requestMessage.get("pageNum")));
						if(firstProjectList.size() < pagingMap.get(Consts.PAGE_END)) {
							pagingMap.put(Consts.PAGE_END, firstProjectList.size());
						}
						firstProjectList = firstProjectList.subList(pagingMap.get(Consts.PAGE_START), pagingMap.get(Consts.PAGE_END));
						result.put("firstProject", firstProjectList);
						resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
					}
				}else {
					resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, requestMessage, headerMap);
					if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						result.put("firstProject", (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE));
						result.put("projectAll", projectAllList);
						resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}
	
	// 페이징 처리
	public static Map<String, Integer> setPage(Integer pageNum){
        Map<String, Integer> page = new HashMap<>();
        if(pageNum != null){
            int pageStart = (pageNum-1) * Consts.PAGE_SIZE;
            int pageEnd = pageStart + (Consts.PAGE_SIZE);

            page.put(Consts.PAGE_START, pageStart);
            page.put(Consts.PAGE_END, pageEnd);
        }
        return  page;
    }

	@GetMapping( "/projectDetail")
	public ModelAndView projectDetail(HttpServletResponse response,HttpSession session, @RequestParam(value="projectId") String projectId, @RequestParam(value="searchVal", required = false) String searchVal, @RequestParam(value = "pageNum" , required = false) String pageNum) throws Exception {
		ModelAndView mav = new ModelAndView("projectDetail");
		try {
			if (!isLogin(mav, session)) {
				return mav;
			}


			Map<String, Object> responseMap = new HashMap<String, Object>();
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));

			Map<String, Object> user = (Map<String, Object>) session.getAttribute("userInfo");
			mav.addObject("authorityId", user.get("authorityId"));
			if(searchVal != null){
				mav.addObject("search", searchVal);

			}
			if(pageNum != null){
				mav.addObject("pageNum", pageNum);

			}
			// 프로젝트 상세 조회
			responseMap = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, "projectId=" + projectId, headerMap);
			if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				Map<String, Object> projectInfo = ((List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE)).get(0);
				
				// 프로젝트 검수일 날짜 형 변환
				if(projectInfo.get("inspectionDate") != null) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = dateFormat.parse(projectInfo.get("inspectionDate").toString());
					projectInfo.put("inspectionDate", newDateFormat.format(date));
				}
				mav.addObject("projectInfo", projectInfo);
				//프로젝트 상태값 조회
				responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=P01&type=code_id", headerMap);
				if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					mav.addObject("commonStatus" , responseMap.get(Consts.RESPONSE_MESSAGE));

					//부서명 조회
					responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=T01&type=code_id", headerMap);
					if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						mav.addObject("commonTeam" , responseMap.get(Consts.RESPONSE_MESSAGE));

						responseMap = getUserAllMap(response, null, headerMap);
						if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
							mav.addObject("userAllList", responseMap.get(Consts.RESPONSE_MESSAGE));
						}
					}
				}
				// 프로젝트 담당자 조회
				responseMap = HttpSender.sndHTTP(projectApiUrl + "/projectUser" , HttpMethod.GET, "projectId=" + projectId, headerMap);
				if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					List<Map<String, Object>> projectUserList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
					String projectUserName = "";


					for(Map<String, Object> projectUser : projectUserList){
						projectUserName += projectUser.get("userName") + ",";
					}
					if(!projectUserName.isEmpty()) {
						projectUserName = projectUserName.substring(0, projectUserName.length() - 1);
					}
					mav.addObject("projectUser", projectUserName);


				}
			}
			mav.addObject(Consts.RESPONSE_CODE, responseMap.get(Consts.RESPONSE_CODE));
			if(!responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				mav.addObject(Consts.RESPONSE_MESSAGE, responseMap.get(Consts.RESPONSE_MESSAGE));
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return mav;
	}

	@PostMapping( "/project")
	@ResponseBody
	public Map<String, Object> insertProject(HttpServletResponse response, HttpSession session, @ModelAttribute AddProjectVO addProjectVO) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			List<Map<String, Object>> manager = addProjectVO.getManager();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));

			resultResponse = HttpSender.sndHTTP(projectApiUrl+"/project", HttpMethod.POST, RequestMessage.setMessage(addProjectVO), headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				result.put("projectListType", "project");
				for (Map<String, Object> map : manager){
					map.put("projectId", ( (Map<String, Object>)resultResponse.get(Consts.RESPONSE_MESSAGE)).get("projectId"));
					map.remove("userId");
				}
				
				resultResponse = HttpSender.sndHTTP(projectApiUrl + "/projectUser", HttpMethod.PUT, RequestMessage.setMessage(manager),headerMap);
				if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					resultResponse = getProjectListByAuthority(response, new HashMap<String, Object>(), session, "project");
					if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						Map<String, Object> projectMap = (Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE);
						result.put("projectList", (List<Map<String, Object>>) projectMap.get("firstProject"));
						result.put("projectAllListSize", ((List<Map<String, Object>>) projectMap.get("projectAll")).size());
						resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	@PutMapping("/project")
	@ResponseBody
	public  Map<String,Object> updateProject(HttpServletResponse response, HttpSession session, @ModelAttribute EditProjectVO editProjectVO ) throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));

			List<Map<String, Object>> manager = editProjectVO.getManager();
			resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.PUT, RequestMessage.setMessage(editProjectVO), headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				for (Map<String, Object> map : manager){
					map.put("projectId", editProjectVO.getProjectId());
					map.remove("userId");
				}
				result.put("projectListType", "project");
				resultResponse = HttpSender.sndHTTP(projectApiUrl+"/projectUser", HttpMethod.PUT, RequestMessage.setMessage(manager),headerMap);
				if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
						
						resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, "projectId="+editProjectVO.getProjectId(), headerMap);
						if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
							result.put("oneProject", resultResponse.get(Consts.RESPONSE_MESSAGE));
							
							resultResponse = HttpSender.sndHTTP(projectApiUrl + "/projectUser" , HttpMethod.GET, "projectId=" + editProjectVO.getProjectId(), headerMap);
							if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
								List<Map<String,Object>> userData = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
								if(userData.size() != 0 ){
									result.put("projectUserData", Json.map2Json(userData));
									String projectUserName = "";
									for (Map<String,Object> user : userData){
										projectUserName += user.get("userName")+",";
									}
									result.put("projectUserName", projectUserName.substring(0, projectUserName.length() - 1));
								}else{
									result.put("projectUserData", "null");
								}
								resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
							}
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	@DeleteMapping("/project")
	@ResponseBody
	public  Map<String,Object> deleteProject(HttpServletResponse response, HttpSession session, @RequestParam String projectId ) throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));

			resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.DELETE, "projectId="+projectId, headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				resultResponse = getProjectListByAuthority(response, new HashMap<String, Object>(), session, "project");
					resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	@GetMapping("/projectList")
	@ResponseBody
	public Map<String, Object> projectList(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage)  throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			resultResponse = getProjectListByAuthority(response, requestMessage, session, "projectList");
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				Map<String, Object> projectMap = (Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				result.put("projectList", projectMap.get("firstProject"));
				result.put("projectAllListSize", ((List<Map<String, Object>>) projectMap.get("projectAll")).size());
				result.put("projectListType", "project");
				resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	@GetMapping("/zipFileDownload")
	public ResponseEntity<InputStreamResource> zipFileDownload(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception{
		try {
			Map<String, Object> headerValue = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));
			String parameter = "";
			for(String key : requestMessage.keySet()){
				parameter +=  key + "=" + URLEncoder.encode((String) requestMessage.get(key), "UTF-8")+"&" ;
			}
			URL urlObj = new URL(CommonUtil.getDeliverablesApiUrl() + "/deliverablesDownload" + "?" + parameter);

			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod(HttpMethod.GET.name());
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(1800000);
			conn.setDoOutput(true);

			for (String key : headerValue.keySet()) {
				conn.setRequestProperty(key, headerValue.get(key).toString());
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String fileName = new String(Base64.getDecoder().decode(conn.getHeaderField("Content-FileName")), Charset.forName("UTF-8"));

				HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	            headers.setContentLength(conn.getContentLengthLong());
	            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
	                      .filename(fileName, Charset.forName("UTF-8"))
	                      .build();
	            headers.setContentDisposition(contentDisposition);
	            headers.add(HttpHeaders.SET_COOKIE, "fileDownload=true");

				return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(conn.getContentLengthLong())
	                .body(new InputStreamResource(conn.getInputStream()));
			} else {
				return ResponseEntity.internalServerError().body(null);
			}
		} catch (Exception e) {
			StringWriter errsw = new StringWriter();
			e.printStackTrace(new PrintWriter(errsw));
			response.sendRedirect("/error");
			return ResponseEntity.internalServerError().body(null);
		}
	}


	@GetMapping( "/projectUserSearch")
	@ResponseBody
	public Map<String, Object> projectUserSearch(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			resultResponse = getUserAllMap(response, requestMessage, CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}

		return resultResponse;
	}



	@GetMapping( "/oneProject")
	@ResponseBody
	public Map<String, Object> oneProject(HttpServletResponse response, HttpSession session, @RequestParam String projectId) throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
			resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, "projectId="+projectId, headerMap);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}
}
