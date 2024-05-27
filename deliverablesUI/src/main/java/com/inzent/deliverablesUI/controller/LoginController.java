package com.inzent.deliverablesUI.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.vo.UserVO;

@Controller
public class LoginController {

	@Value("${API_KEY}")
	public String apiKey;

	@Value("${API_URL}")
	public String apiUrl;

	@GetMapping(value = "/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}

	@PostMapping(value = "/loginProcess")
	public @ResponseBody Map<String, Object> loginProcess(HttpSession session, HttpServletResponse response, @ModelAttribute("UserVO") UserVO userVo) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put(Globals.API_KEY, apiKey);
			
			resultResponse = HttpSender.sndHTTP(apiUrl+"/login", HttpMethod.POST, RequestMessage.setMessage(objectMapper.convertValue(userVo, Map.class)), headerMap);
			if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
				Map<String, Object> userInfoMap = (Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE);
				session.setAttribute(Globals.TOKEN_VALUE, userInfoMap.get(Globals.TOKEN_VALUE));
				session.setAttribute(Globals.MENU, userInfoMap.get(Globals.MENU));
				session.setAttribute(Globals.USER_INFO, (Map<String, Object>) userInfoMap.get(Globals.USER_INFO));
				session.setAttribute(Globals.USER_AUTHORITY_LIST, userInfoMap.get(Globals.USER_AUTHORITY_LIST));
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}

	@GetMapping(value = "/logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if(null != session && null != session.getAttribute(Globals.TOKEN_VALUE)){
				HttpSender.sndHTTP(apiUrl + "/logout", HttpMethod.DELETE, "", CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
				session.invalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return "redirect:login";
	}
	
	@PostMapping(value = "/userPasswordReset")
	public @ResponseBody Map<String, Object> passwordReset(HttpSession session, HttpServletResponse response, @RequestParam(value="userEmail") String userEmail) throws Exception {
		Map<String, Object> resultResponse = new HashMap<String, Object>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UserVO userVo = new UserVO();
			Map<String, Object> headerMap = new HashMap<String, Object>();
			
			userVo.setUserEmail(userEmail);
			headerMap.put(Globals.API_KEY, apiKey);
			resultResponse = HttpSender.sndHTTP(apiUrl + "/userPasswordReset", HttpMethod.PUT, RequestMessage.setMessage(objectMapper.convertValue(userVo, Map.class)), headerMap);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error");
		}
		return resultResponse;
	}
}
