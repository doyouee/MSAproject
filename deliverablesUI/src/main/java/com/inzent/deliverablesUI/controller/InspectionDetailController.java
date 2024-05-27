package com.inzent.deliverablesUI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.util.DeliverablesListUtil;
import com.inzent.deliverablesUI.util.MailUtil;

@Controller
public class InspectionDetailController extends MasterController {

    @Value("${API_URL}")
    public String apiUrl;
    @Value("${API_KEY}")
    public String apiKey;

    @GetMapping("/inspectionDetail")
    public ModelAndView getList(HttpServletResponse response, HttpSession session, @RequestParam(value="inspectionProjectId") String projectId,  @RequestParam(value="inspectionSearchVal", required = false) String inspectionSearchVal)  throws Exception{
        ModelAndView mav = new ModelAndView("inspectionDetail");
        try {
			if (!isLogin(mav, session)) {
				return mav;
			}
			Map<String, Object> responseMap = new HashMap<String, Object>();
            Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
            
			if(inspectionSearchVal != null && inspectionSearchVal != ""){
				mav.addObject("search", inspectionSearchVal);
			}
            responseMap = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.GET, "", headerMap);
            if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
            	List<Map<String, Object>> deliverablesList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
            	
            	responseMap = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/project", HttpMethod.GET, "projectId=" + projectId, headerMap);
            	if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
            		List<Map<String, Object>> projectList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
            		
            		responseMap = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesReason", HttpMethod.GET, "projectId=" + projectId, headerMap);
            		if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
            			List<Map<String, Object>> deliverablesReasonList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
            			List<Map<String, Object>> reasonListResult = new ArrayList<>();
            			for(Map<String,Object> deliverablesReason : deliverablesReasonList) {
            				if("1".equals(deliverablesReason.get("requiredItems"))){
            					reasonListResult.add(deliverablesReason);
            				}
            			}
            			mav.addObject("projectJson", projectList);
            			mav.addObject("deliverablesReasonList", reasonListResult);
            			mav.addObject("projectId", projectId);
                        mav.addObject("userInfo", session.getAttribute(Globals.USER_INFO));
            			mav.addObject("pageNum", 1);
            			DeliverablesListUtil.getdeliverablesList(mav, deliverablesList);
            		}
            	}
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

	@PutMapping("/inspection")
	public @ResponseBody Map<String, Object> updateInspectionStatus(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
			boolean createFile = false;
			String mailApiKey = "";
			if(requestMessage.get("inspectionStatus").equals("1")) { // 검수요청 일 경우 zip 파일 생성
				resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesCreateFile", HttpMethod.GET, requestMessage, headerMap);
				createFile = true;
				mailApiKey = CommonUtil.getDeliverablesApiKey();
			}else {
				mailApiKey = CommonUtil.getProjectApiKey();
			}
			if(!createFile || (createFile && resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS))) {
				Map<String, Object> inspectionMap = new HashMap<String, Object>();
				for(String key : requestMessage.keySet()) {
					if(!key.equals("teamId") && !key.equals("projectName")) {
						inspectionMap.put(key, requestMessage.get(key));
					}
				}
				resultResponse = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/inspection", HttpMethod.PUT, RequestMessage.setMessage(inspectionMap), headerMap);
				if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					resultResponse = MailUtil.inspectionSendMail(response, apiUrl, mailApiKey, headerMap, session, requestMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}
}
