package com.inzent.deliverablesUI.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inzent.deliverablesUI.util.DeliverablesListUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;

@Controller
public class DeliverablesListController extends MasterController {

    @Value("${API_URL}")
    public String apiUrl;
    @Value("${API_KEY}")
    public String apiKey;
    
    @GetMapping("/deliverablesList")
    public ModelAndView getList(HttpServletResponse response, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView("deliverablesList");
        try {
            if (!isLogin(mav, session)) {
                return mav;
            }
            String json = null;
            Map<String, Object> responseMap = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.GET, "",
            		CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                List<Map<String, Object>> deliverablesList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
                DeliverablesListUtil.getdeliverablesList(mav, deliverablesList);
                mav.addObject("userList", new ObjectMapper().writeValueAsString((Map<String, Object>) session.getAttribute("userInfo")));
            }
            mav.addObject(Consts.RESPONSE_CODE, responseMap.get(Consts.RESPONSE_CODE));
            if(!responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                mav.addObject(Consts.RESPONSE_MESSAGE, responseMap.get(Consts.RESPONSE_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return mav;
    }
    
    @PostMapping("/deliverablesList")
    @ResponseBody
    public Map<String, Object> postList(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            String deliverablesId = request.getParameter("deliverablesId");
            String requiredItems = request.getParameter("requiredItems");
            String deliverablesName = request.getParameter("deliverablesName");
            String description = request.getParameter("description");
            Map<String, Object> message = new HashMap<>();

            message.put("fileGubun", "0");
            message.put("deliverablesName", deliverablesName);
            if(requiredItems == null && !"undefined".equals(deliverablesId)) {
                message.put("deliverablesTopId", deliverablesId);
            } else if(!(requiredItems == null && "undefined".equals(deliverablesId))) {
                message.put("deliverablesTopId", deliverablesId);
                message.put("requiredItems", requiredItems);
                message.put("description", description);
            }

            if (request.getParameter("prevTitle").isEmpty()){
                message.remove("deliverablesTopId");
                resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.POST, RequestMessage.setMessage(message),
                        CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
            } else {
                Map<String, Object> reset = new HashMap<>();
                reset.put("deliverablesId", deliverablesId);
                reset.put("deliverablesName", request.getParameter("prevTitle"));
                reset.put("requiredItems", null);
                resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.PUT, RequestMessage.setMessage(reset),
                        CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
                if (resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.POST, RequestMessage.setMessage(message),
                            CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
    	return resultResponse;
    }
    
    @PutMapping("/deliverablesList")
    @ResponseBody
    public Map<String, Object> putList(HttpServletResponse response, HttpServletRequest request, HttpSession session)  throws Exception {
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            String requiredItems = request.getParameter("requiredItems");
            Map<String, Object> message = new HashMap<>();
            message.put("deliverablesId", request.getParameter("deliverablesId"));
            message.put("deliverablesTopId", request.getParameter("deliverablesTopId"));
            message.put("deliverablesName", request.getParameter("deliverablesName"));
            message.put("fileGubun","0");
            if ("0".equals(requiredItems) || "1".equals(requiredItems) || "2".equals(requiredItems)) {
                message.put("requiredItems", requiredItems);
                message.put("description", request.getParameter("description"));
            } else {
                message.put("requiredItems", null);
                message.put("description", null);
            }
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.PUT, RequestMessage.setMessage(message),
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }
    
    @DeleteMapping("/deliverablesList")
    @ResponseBody
    public Map<String, Object> deleteList(HttpServletResponse response, HttpServletRequest request, HttpSession session)  throws Exception {
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.DELETE, "deliverablesId="+request.getParameter("deliverablesId"),
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
    	return resultResponse;
    }

    @GetMapping("/deliverablesSearch")
    public @ResponseBody Map<String, Object> getInspectionList(HttpServletResponse response, HttpSession session, HttpServletRequest request)  throws Exception{
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            String topId = request.getParameter("deliverablesId");
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.GET, "",
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
                for(Map<String, Object> a : list){
                    for(Map.Entry<String, Object> entry:a.entrySet()){
                        if (entry.getKey().equals("deliverablesTopId")) {
                            if (entry.getValue()!= null && entry.getValue().equals(topId)){
                                resultResponse.put(Consts.RESPONSE_CODE,Consts.ERROR);
                            }
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
    
    @GetMapping("/getRejectionStatus")
	public @ResponseBody Map<String, Object> getInspectionList(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage)  throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
			
			// 검수요청 상태인 프로젝트 구하기
			Map<String, Object> inspectionRequsetMessage = new HashMap<String, Object>();
			Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
			String userAuthorityId = userInfo.get("authorityId").toString();
			inspectionRequsetMessage.put("inspectionStatus","1");
			if ("2".equals(userAuthorityId)) {
				inspectionRequsetMessage.put("teamId", userInfo.get("teamId"));
			}
			resultResponse = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/project", HttpMethod.GET, inspectionRequsetMessage, headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				List<Map<String,Object>> projectAllList = (List<Map<String,Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				String rejectProjectId = "";
				
				// 검수요청 상태의 프로젝트가 없을 경우 반려할 프로젝트 체크
				if(projectAllList.size() != 0) {
					// 검수상태의 모든 프로젝트 아이디 담기
					String rejectProjectIdAll = "";
					for(Map<String, Object> projectAll : projectAllList) {
						rejectProjectIdAll += projectAll.get("projectId") + ",";
					}
					int requiredItems = Integer.parseInt(requestMessage.get("requiredItems").toString()); // 현재 폴더 상태
					if(requestMessage.get("prevChecked") == null && requiredItems != 2) { 
						//이전상태가 없고 필수 또는 준필수로 변경할 때 반려
						rejectProjectId = rejectProjectIdAll;
					}else if(requestMessage.get("prevChecked") != null && ((Integer.parseInt(requestMessage.get("prevChecked").toString()) == 1 && requiredItems == 0) ||
							(Integer.parseInt(requestMessage.get("prevChecked").toString()) == 2 && requiredItems != 2))) {
						// 이전상태가 준필수이고 필수로 변경할 때 준필수 사유가 작성되어있는 프로젝트만 반려 또는, 이전상태가 선택이고 필수 또는 준필수로 변경할 때 산출물이 등록되지 않은 프로젝트만 반려
						if(requestMessage.get("deliverablesId") != null) {
							resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesReg", HttpMethod.GET, "deliverablesId=" + requestMessage.get("deliverablesId"), headerMap);
							if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
								List<Map<String,Object>> deliverablesRegList  = (List<Map<String,Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
								if(deliverablesRegList.size() == 0) {
									rejectProjectId = rejectProjectIdAll;
								}else {
									int prevChecked = Integer.parseInt(requestMessage.get("prevChecked").toString());
									for(Map<String, Object> deliverablesReg : deliverablesRegList) {
										if(prevChecked == 1 && rejectProjectIdAll.contains(deliverablesReg.get("projectId") + ",") && deliverablesReg.get("notRegistrationReason") != null 
												&& !deliverablesReg.get("notRegistrationReason").toString().isEmpty()) {
											rejectProjectId += deliverablesReg.get("projectId") + ",";
										}else if(prevChecked == 2) {
											rejectProjectIdAll = rejectProjectIdAll.replace(deliverablesReg.get("projectId") + ",", "");
											rejectProjectId = rejectProjectIdAll;
										}
									}
								}
							}
						}
					}
				}
				if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					if(!rejectProjectId.isEmpty()) {
						rejectProjectId = rejectProjectId.substring(0, rejectProjectId.length() - 1);
					}
					result.put("rejectProjectId", rejectProjectId);
					resultResponse = ResponseMessage.setMessage(resultResponse.get(Consts.RESPONSE_CODE).toString(), result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            response.sendRedirect("/error");
		}
		return resultResponse;
	} 
}
