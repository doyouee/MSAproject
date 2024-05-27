package com.inzent.commonAPI.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.inzent.commonAPI.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 메뉴별 권한 조회 Interceptor
 * @author 조유진
 */

public class AuthorityInterceptor extends InterceptorRegistry implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private CommonService commonService;
	private String apiKey;

	public AuthorityInterceptor(CommonService commonService, String apiKey){
		this.commonService = commonService;
		this.apiKey = apiKey;
	}

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
	        if(commonService.authorityConfirm(request.getHeader("TOKEN_VALUE"), apiKey, request.getRequestURI(), request.getMethod())) {
				logger.debug("AuthorityInterceptor SUCCESS");
				return true;
			}else {
				logger.error("AuthorityInterceptor ERROR");
				response.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR CODE : {}", e.getMessage());
			response.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
        	return false;
		}
    }
}
