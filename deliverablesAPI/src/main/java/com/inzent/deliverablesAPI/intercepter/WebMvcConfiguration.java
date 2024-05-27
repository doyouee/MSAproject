package com.inzent.deliverablesAPI.intercepter;

import com.inzent.commonMethod.interceptor.CommonApiInterceptor;
import com.inzent.commonMethod.interceptor.ConfirmInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public  class WebMvcConfiguration  implements WebMvcConfigurer {
    @Value("${API_URL}")
    private String apiUrl ;

    @Value("${API_KEY}")
    private String apiKey ;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry)  {
        interceptorRegistry.addInterceptor(new CommonApiInterceptor(apiUrl, apiKey)).addPathPatterns("/**").order(1);
        interceptorRegistry.addInterceptor(new ConfirmInterceptor(apiUrl, apiKey)).addPathPatterns("/**").order(2);
    }

}
