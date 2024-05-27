package com.inzent.deliverablesUI.intercepter;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.util.CommonUtil;

public class ApiInterceptor extends InterceptorRegistry implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static List<Map<String, Object>> apiList = new ArrayList<>();
	private static String expiredate = null;
	private String apiUrl;
	private String apiKey;

	public ApiInterceptor(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			// session Check
			HttpSession session = request.getSession(false);
			if(session == null || session.getAttribute(Globals.TOKEN_VALUE) == null) {
				response.sendRedirect("/login");
				return false; 
			}
			
			// Api List Expire Date Check
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date now = new Date();
			int hours = 24;
			if (expiredate != null) {
				Date date = sdf.parse(expiredate);
				long difference = now.getTime() - date.getTime();
				hours = (int) (difference / (1000 * 60 * 60));
			}
			
			// Api List Check
			if (apiList.isEmpty() || hours >= 24) {
				Map<String, Object> sndResult = HttpSender.sndHTTP(apiUrl + "/apiInfo", HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
				
				if (sndResult.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
					expiredate = sdf.format(now);
					apiList = (List<Map<String, Object>>) sndResult.get(Consts.RESPONSE_MESSAGE);
				} else {
					response.setStatus((Integer) sndResult.get(Consts.RESPONSE_MESSAGE));
					response.sendRedirect("/login");
					return false;
				}
			}
			
			// Api List Setting
			if(!apiList.isEmpty()) {
				CommonUtil.setApiList(apiList);
			}

			// Api Key Check
			boolean exist = false;
			for (Map<String, Object> apiMap : apiList) {
				if (apiMap.get("apiKey").equals(apiKey)) {
					exist = true;
					break;
				}
			}
			if (exist) {
				logger.debug("CommonApiInterceptor SUCCESS");
				return true;
			} else {
				logger.error("CommonApiInterceptor ERROR");
				response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
				response.sendRedirect("/login");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR CODE : {}", e.getMessage());
			response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
			response.sendRedirect("/login");
			return false;
		}
	}
}
