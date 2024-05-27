package com.inzent.commonAPI.token;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * 토큰 생성 및 삭제
 * @author 조유진
 */

@Component
public class JwtTokenProvider {
	
	public static Map<String, Object> tokenList = new HashMap<String,Object>(); // 사용자 별 토큰 리스트
	
	@Value("${spring.jwt.secret}")
    private String secretKey;
	
    private long tokenValidTime = 10 * 60 * 1000L; 	// 토큰 유효시간 10분
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
	// JWT 토큰 생성
    public String createToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        Date now = new Date();
        
		String tokenValue = Jwts.builder().setClaims(claims) // 정보 저장
				.setIssuedAt(new Date()) // 토큰 발행 시간 정보
				.signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
				.compact();
		tokenList.put(tokenValue, new Date(now.getTime() + tokenValidTime)); // 토큰 리스트 저장
		
        return tokenValue;
    }
    
    // 토큰 유효성 검사
    public boolean validateToken(String tokenValue) {
        try {
        	boolean validation = false;
        	if(tokenList.get(tokenValue) != null) {
        		long tokenExprireDate = ((Date) tokenList.get(tokenValue)).getTime();
            	long now = new Date().getTime();
            	if(now > tokenExprireDate) {
            		// 토큰이 만료됐을 경우 해당 토큰 삭제
            		removeToken(tokenValue);
            	}else {
            		// 토큰이 만료되지 않을 경우 만료일 연장
            		tokenList.put(tokenValue, new Date(now + tokenValidTime));
            		validation = true;
            	}
        	}
        	
        	return validation;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
    
    // 토큰 값에서 유저 이메일 구하기
    public String getUserEmail(String token) {
    	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    // 토큰 값 삭제
    public void removeToken(String tokenValue) {
    	if(tokenList.get(tokenValue) != null) {
    		tokenList.remove(tokenValue);
    	}
    }
	
//    @Scheduled(fixedDelay = 10000) // 10초마다 실행 (test)
    @Scheduled(cron = "0 0 2 * * *") 
    public void removeTokenSchdule() {
		// 매일 2시 스케줄 실행하여 만료된 토큰 삭제
		List<String> removeList = new ArrayList<String>();
		
		long now = new Date().getTime();
		for(String key : tokenList.keySet()) {
    		long tokenExprireDate = ((Date) tokenList.get(key)).getTime();
    		if(now > tokenExprireDate) {
    			removeList.add(key);
        	}
		}
		
		for(String removeToken : removeList) {
			// 토큰이 만료됐을 경우 해당 토큰 삭제
			removeToken(removeToken);
		}
		logger.debug("Remove Token Schedule TokenList Size : " + tokenList.size());
	}
}
