package com.inzent.commonMethod.interceptor;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonApiInterceptor extends InterceptorRegistry implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static List<Map<String, Object>> apiList = new ArrayList<>();
	private static String expiredate = null;
	private String api_url;
	private String api_key;

	public CommonApiInterceptor(String api_url, String api_key) {
		this.api_url = api_url;
		this.api_key = api_key;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date now = new Date();

			int hours = 24;
			if (expiredate != null) {
				Date date = sdf.parse(expiredate);
				long difference = now.getTime() - date.getTime();
				hours = (int) (difference / (1000 * 60 * 60));
			}

			if (apiList.isEmpty() || hours >= 24) {
				Map<String, Object> headerMap = new HashMap<>();
				headerMap.put("API_KEY", api_key);
				headerMap.put("TOKEN_VALUE", request.getHeader("TOKEN_VALUE"));
				Map<String, Object> sndResult = HttpSender.sndHTTP(api_url + "/apiInfo", HttpMethod.GET, (String) null, headerMap);

				if (sndResult.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
					expiredate = sdf.format(now);
					apiList = (List<Map<String, Object>>) sndResult.get(Consts.RESPONSE_MESSAGE);
				} else {
					response.setStatus((Integer) sndResult.get(Consts.RESPONSE_MESSAGE));
					return false;
				}
			}

			boolean exist = false;
			for (Map<String, Object> apiMap : apiList) {
				if (apiMap.get("apiKey").equals(api_key)) {
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
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR CODE : {}", e.getMessage());
			response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
			return false;
		}
	}

	public static List<Map<String, Object>> getApiList() {
		return apiList;
	}
}
