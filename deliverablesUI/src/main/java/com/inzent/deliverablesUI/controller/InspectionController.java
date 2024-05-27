package com.inzent.deliverablesUI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inzent.commonMethod.common.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;


@Controller
public class InspectionController extends MasterController {

	@Value("${API_KEY}")
	String apiKey;

	@GetMapping("/inspection")
	public ModelAndView getInspection(HttpServletResponse response, HttpSession session , @RequestParam(value="inspectionSearchVal", required = false) String inspectionSearchVal)  throws Exception{
		ModelAndView mav = new ModelAndView("inspection");
		try {
			if (!isLogin(mav, session)) {
				return mav;
			}
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> param = new HashMap<>();
			if(inspectionSearchVal != null && inspectionSearchVal != ""){
				mav.addObject("search", inspectionSearchVal);
				param = Json.jsonToMap(inspectionSearchVal);

			}
			mav.addObject("pageNum",1);
			param.put("inspectionStatus", "1");
			responseMap = getProjectListByAuthority(response ,param, session, "project");
			if(responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				Map<String, Object> projectMap = (Map<String, Object>) responseMap.get(Consts.RESPONSE_MESSAGE);
				List<Map<String, Object>> firstProjectList = (List<Map<String, Object>>) projectMap.get("firstProject");
				if (firstProjectList.isEmpty()) {
					mav.addObject("projectList", null);
					mav.addObject("projectAllListSize", 0);
				} else {
					mav.addObject("projectList", firstProjectList);
					mav.addObject("projectAllListSize", ((List<Map<String, Object>>) projectMap.get("projectAll")).size());
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


	public Map<String, Object> getProjectListByAuthority(HttpServletResponse response, Map<String, Object> requestMessage, HttpSession session, String type)  throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			String projectApiUrl = CommonUtil.getProjectApiUrl();
			Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
			Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
			String userAuthorityId = userInfo.get("authorityId").toString();

			if ("project".equals(type)) {
				requestMessage.put("pageNum", "1");
			}
			
			if ("2".equals(userAuthorityId)) {
				requestMessage.put("teamId", userInfo.get("teamId"));
			} else if ("3".equals(userAuthorityId)) {
				requestMessage.put("userEmail", userInfo.get("userEmail"));
			}

			Map<String, Object> result = new HashMap<String, Object>();
			resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, requestMessage, headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				result.put("firstProject", resultResponse.get(Consts.RESPONSE_MESSAGE));
				
				requestMessage.remove("pageNum");
				resultResponse = HttpSender.sndHTTP(projectApiUrl + "/project", HttpMethod.GET, requestMessage, headerMap);
				if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
					result.put("projectAll", resultResponse.get(Consts.RESPONSE_MESSAGE));
					resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            response.sendRedirect("/error");
		}
		return resultResponse;
	}


	@GetMapping("/inspectionList")
	public @ResponseBody Map<String, Object> getInspectionList(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage)  throws Exception{
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			requestMessage.put("inspectionStatus","1");

			resultResponse = getProjectListByAuthority(response,requestMessage, session, "projectList");
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
				Map<String, Object> projectMap = (Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				result.put("projectList", projectMap.get("firstProject"));
				result.put("projectAllListSize", ((List<Map<String, Object>>) projectMap.get("projectAll")).size());
				result.put("projectListType", "inspection");
				requestMessage.remove("pageNum");
				List<Map<String,Object>> search = new ArrayList<>();
				search.add(requestMessage);
				result.put("search", Json.map2Json(search));
				resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
            response.sendRedirect("/error");
		}
		return resultResponse;
	}

}
