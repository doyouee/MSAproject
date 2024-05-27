package com.inzent.commonAPI.intercepter;

import com.inzent.commonAPI.mapper.CommonMapper;
import com.inzent.commonAPI.vo.ApiInfoVO;
import com.inzent.commonAPI.vo.CommonCodeGroupVO;
import com.inzent.commonAPI.vo.CommonCodeVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * API Key 조회 Interceptor
 * @author 황유진
 */

class ApiInterceptor  implements AsyncHandlerInterceptor {

    public static List<ApiInfoVO> apiList = new ArrayList<>();
    private CommonMapper commonMapper;
    private static String expiredate = null;
    private  Logger logger = LoggerFactory.getLogger(this.getClass());
    public ApiInterceptor( CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String api_key = request.getHeader("API_KEY");
            logger.debug("[ REQUEST - API_KEY ] : {}", api_key);

            //유효시간(24시간)지났는지 현재시간과 비교하여 체크
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();

            int hours = 24;
            if(expiredate != null){
                Date date = sdf.parse(expiredate);
                logger.debug("[ EXPIREDATE ] : {}", date);
                long difference = now.getTime() - date.getTime();
                hours = (int) (difference / (1000 * 60 * 60));
            }
            if (apiList.isEmpty() || hours >= 24) {
                List<ApiInfoVO> sndResult = commonMapper.getApiInfo();
                if(sndResult.isEmpty()){
                    logger.error(" API LIST IS EMPTY");
                    return false;
                }else {
                    expiredate = sdf.format(now);
                    apiList = sndResult;
                }
            }

            boolean exist = false;
            for (ApiInfoVO apiInfoVO : apiList){
                if(apiInfoVO.getApiKey().equals(api_key)){
                    exist = true;
                    break;
                }
            }
            if (exist){
                logger.debug("ApiInterceptor SUCCESS");
                return true;
            }else {
                logger.error("ApiInterceptor ERROR");
                response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ERROR CODE  : {}", e.getMessage());
            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);;
            return false;
        }
    }

}