package com.inzent.commonAPI.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.inzent.commonAPI.token.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 토큰 유효성 검사 Interceptor
 * @author 조유진
 */

public class TokenInterceptor extends InterceptorRegistry implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private JwtTokenProvider jwtTokenProvider;
	
	public TokenInterceptor(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			if(jwtTokenProvider.validateToken(request.getHeader("TOKEN_VALUE"))) {
				logger.debug("TokenInterceptor SUCCESS");
				return true;
			}else {
				logger.error("TokenInterceptor ERROR");
				response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR CODE : {}", e.getMessage());
			response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
			return false;
		}
    }
}
