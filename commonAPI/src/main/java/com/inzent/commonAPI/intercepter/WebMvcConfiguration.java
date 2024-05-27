package com.inzent.commonAPI.intercepter;

import com.inzent.commonAPI.mapper.CommonMapper;
import com.inzent.commonAPI.service.CommonService;
import com.inzent.commonAPI.token.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor 설정
 *
 * @author 황유진
 */

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${API_KEY}")
    private String apiKey;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        /*
         * 1. api key 확인 (HEADER/API_KEY), 404 ERROR
         * 2. token 확인 (HEADER/TOKEN_VALUE), 401 ERROR
         * 3. 권한 확인 (HEADER/USER_ID), 403 ERROR
         */
        interceptorRegistry.addInterceptor(new ApiInterceptor(commonMapper)).addPathPatterns("/**").order(1);
        interceptorRegistry.addInterceptor(new TokenInterceptor(jwtTokenProvider)).addPathPatterns("/**").excludePathPatterns("/login", "/userPasswordReset", "/apiInfo").order(2);
        interceptorRegistry.addInterceptor(new AuthorityInterceptor(commonService, apiKey)).addPathPatterns("/**").excludePathPatterns("/login", "/userPasswordReset", "/apiInfo").order(3);
    }
}