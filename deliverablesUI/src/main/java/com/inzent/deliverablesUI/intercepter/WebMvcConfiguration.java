package com.inzent.deliverablesUI.intercepter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	@Value("${API_URL}")
    private String apiUrl ;

    @Value("${API_KEY}")
    private String apiKey ;
    
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new ApiInterceptor(apiUrl, apiKey)).addPathPatterns("/**").excludePathPatterns("/login", "/loginProcess", "/userPasswordReset", 
        		"/css/**" , "/js/**", "/fonts/**", "/img/**", "/vendor/**").order(1);
    }
}
