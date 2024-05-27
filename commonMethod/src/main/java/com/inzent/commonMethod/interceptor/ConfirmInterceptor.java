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
import java.util.HashMap;
import java.util.Map;

public class ConfirmInterceptor extends InterceptorRegistry implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String api_url;
	private String api_key;

	public ConfirmInterceptor(String api_url, String api_key ) {
		this.api_url = api_url;
		this.api_key = api_key;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			Map<String, Object> headerValue = new HashMap<String, Object>();
			headerValue.put("TOKEN_VALUE", request.getHeader("TOKEN_VALUE"));
			headerValue.put("API_KEY", api_key);
			headerValue.put("MAPPING_URL", request.getRequestURI());
			headerValue.put("METHOD", request.getMethod());

			Map<String, Object> sndResult = HttpSender.sndHTTP(api_url + "/confirm", HttpMethod.GET, (String) null, headerValue);

			if (sndResult.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
				logger.debug("ConfirmInterceptor SUCCESS");
				return true;
			} else {
				response.setStatus((Integer) sndResult.get(Consts.RESPONSE_MESSAGE));
				logger.error("ConfirmInterceptor ERROR");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR CODE : {}", e.getMessage());
			response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
			return false;
		}
	}
}
