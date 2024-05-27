package com.inzent.commonAPI.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonAPI.mapper.CommonMapper;
import com.inzent.commonAPI.token.JwtTokenProvider;
import com.inzent.commonAPI.vo.ApiInfoVO;
import com.inzent.commonAPI.vo.MenuVO;
import com.inzent.commonAPI.vo.UserAuthorityVO;

/**
 * 공통 서비스
 * - API 정보조회 @author 황유진
 * - 메뉴 조회 @author 박민희
 * - 토큰 유효성검사 @author 조유진
 */

@Service
public class CommonService {
    @Autowired
    private CommonMapper commonMapper;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    public List<ApiInfoVO>  getApiInfo() {
        return  commonMapper.getApiInfo();
    }

    public String getApiId(String apiKey){
        return commonMapper.getApiId(apiKey);
    }

    public String getApiExistence(String apiKey){
        return  commonMapper.getApiExistence(apiKey);
    }
    
    public List<UserAuthorityVO> getUserAuthority(){
        return  commonMapper.getUserAuthority();
    }

    public Map<String, Object> getMenu(String headerApiKey, String userEmail) {
    	Map<String, Object> menuList = new HashMap<String,Object>();
    	List<Map<String, Object>> topMenuList = new ArrayList<Map<String,Object>>();
    	Map<String, Object> topMenuMap = new HashMap<String, Object>();
    	List<Map<String, Object>> subMenuMap = new ArrayList<Map<String, Object>>();
    	ObjectMapper objectMapper = new ObjectMapper();
    	Map<String, List<Map<String, Object>>> subMenuList = new HashMap<String, List<Map<String, Object>>>();
    	Map<String, Object> getMenuMap = new HashMap<String, Object>();
    	getMenuMap.put("userEmail", userEmail);
    	getMenuMap.put("apiKey", headerApiKey);
    	List<MenuVO> allMenuList = commonMapper.getMenu(getMenuMap);

    	for(MenuVO allMenu : allMenuList) {
    		if(allMenu.getTopMenuId() == null) {
    			topMenuMap = objectMapper.convertValue(allMenu, Map.class);
    			topMenuList.add(topMenuMap);
    			
    			for (MenuVO subMenu : allMenuList) {
					if (allMenu.getMenuId().equals(subMenu.getTopMenuId())) {
						subMenuMap = new ArrayList<Map<String, Object>>();
						if (subMenuList.get(allMenu.getMenuName()) != null) {
							subMenuMap = subMenuList.get(allMenu.getMenuName());
						}
						subMenuMap.add(objectMapper.convertValue(subMenu, Map.class));
						subMenuList.put(allMenu.getMenuName(), subMenuMap);
					}
				}
    		}
    	}
    	menuList.put("topMenu", topMenuList);
    	menuList.put("subMenu", subMenuList);
        return menuList;
    }
    
    public boolean authorityConfirm(String tokenValue, String apiKey, String mappingUrl, String method) {
    	try {
	        Map<String, Object> confirmInfo = new HashMap<String, Object>();
	        confirmInfo.put("method", method);
	        confirmInfo.put("mappingUrl", mappingUrl);
	        confirmInfo.put("userEmail", jwtTokenProvider.getUserEmail(tokenValue));
	        confirmInfo.put("apiKey", apiKey);
	        String confirm = commonMapper.authorityConfirm(confirmInfo);

	        if(confirm.equals("1")) {
	        	return true;
	        }else {
	        	return false;
	        }
		} catch (Exception e) {
			e.printStackTrace();
        	return false;
		}
    }
}
