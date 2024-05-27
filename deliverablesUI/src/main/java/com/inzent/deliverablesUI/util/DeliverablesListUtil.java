package com.inzent.deliverablesUI.util;

import java.util.*;

import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeliverablesListUtil {
	
	public static void getdeliverablesList(ModelAndView mav, List<Map<String, Object>> deliverablesList) throws Exception {
		List<String> dirArray = new ArrayList<String>();
    	String topId = "";
    	List<String> visibleHiddenArray = new ArrayList<String>();
    	List<String> lastChildArray = new ArrayList<String>();
    	for(Map<String, Object> deliverablesMap : deliverablesList) {
    		deliverablesMap.put("isParent", true);
			deliverablesMap.put("open", true);
			dirArray.add(deliverablesMap.get("deliverablesName").toString());
			
    		String thisRequiredItems = deliverablesMap.get("requiredItems").toString();
    		if(thisRequiredItems.equals("0") || thisRequiredItems.equals("1") || thisRequiredItems.equals("2")) {
    			lastChildArray.add(deliverablesMap.get("deliverablesName").toString());
    		}
    		
    		topId += deliverablesMap.get("deliverablesTopId") + ",";
    	}
    	for(Map<String, Object> deliverablesMap : deliverablesList) {
    		if(!topId.contains(deliverablesMap.get("deliverablesId") + ",")) {
    			visibleHiddenArray.add(deliverablesMap.get("deliverablesId").toString());
    		}
    	}
    	mav.addObject("zNodes", new ObjectMapper().writeValueAsString(deliverablesList));
    	mav.addObject("dirArray", new ObjectMapper().writeValueAsString(dirArray));
    	mav.addObject("visibleHiddenArray", new ObjectMapper().writeValueAsString(visibleHiddenArray));
    	mav.addObject("lastChildArray", new ObjectMapper().writeValueAsString(lastChildArray));
	}

}
