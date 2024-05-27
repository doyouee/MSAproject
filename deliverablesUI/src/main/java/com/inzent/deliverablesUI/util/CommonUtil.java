package com.inzent.deliverablesUI.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inzent.deliverablesUI.common.Globals;

public class CommonUtil {
	
	private static List<Map<String, Object>> apiList = null;
	
	public static void setApiList(List<Map<String, Object>> apiList) {
		CommonUtil.apiList = apiList;
	}

	public static Map<String, Object> getHttpHeaderMap(String apiKey, Object token){
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put(Globals.HEADER_TOKEN_VALUE, token);
		headerMap.put(Globals.API_KEY, apiKey);
		
		return headerMap;
	}
	
	public static String getProjectApiUrl() {
		return getApiUrl("ProjectAPI");
	}
	
	public static String getDeliverablesApiUrl() {
		return getApiUrl("DeliverablesAPI");
	}
	
	public static String getProjectApiKey() {
		return getApiKey("ProjectAPI");
	}
	
	public static String getDeliverablesApiKey() {
		return getApiKey("DeliverablesAPI");
	}
	
	public static String getApiUrl(String apiCode) {
		String url = "";
		for(Map<String, Object> apiInfo : apiList) {
			if(apiInfo.get("apiCode").equals(apiCode)) {
				url = apiInfo.get("apiUrl").toString();
				break;
			}
		}
		return url;
	}
	
	public static String getApiKey(String apiCode) {
		String key = "";
		for(Map<String, Object> apiInfo : apiList) {
			if(apiInfo.get("apiCode").equals(apiCode)) {
				key = apiInfo.get("apiKey").toString();
				break;
			}
		}
		return key;
	}
}
