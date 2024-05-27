package com.inzent.deliverablesUI.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.deliverablesUI.vo.UserVO;

@Controller
public class MasterController {
	
	protected boolean isLogin(ModelAndView mav, HttpSession session) {
		boolean check = false;
		try {
			if(null != session && null != session.getAttribute(Globals.TOKEN_VALUE) && null != session.getAttribute(Globals.MENU)
					&& null != session.getAttribute(Globals.USER_INFO)) {
				Map<String, Object> allMenuList = (Map<String, Object>) session.getAttribute(Globals.MENU);
				mav.addObject("topMenuList", allMenuList.get("topMenu"));
				mav.addObject("subMenuList", allMenuList.get("subMenu"));
				mav.addObject("userInfo", new ObjectMapper().convertValue(session.getAttribute(Globals.USER_INFO), UserVO.class));
				
				String currentTopMenuId = "";
				Map<String, Object> subMenuMap = (Map<String, Object>) allMenuList.get("subMenu");
				for(String key : subMenuMap.keySet()) {
					List<Map<String, Object>> subMenuList = (List<Map<String, Object>>) subMenuMap.get(key);
					for(Map<String, Object> subMenu : subMenuList) {
						if(subMenu.containsValue("/" + mav.getViewName())) {
							currentTopMenuId = (String) subMenu.get("topMenuId");
							session.setAttribute("currentTopMenuId", currentTopMenuId);
							break;
						}
					}
					if(!currentTopMenuId.isEmpty()) {
						break;
					}
				}
				check = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			if(null != session) {
				session.invalidate();
			}
		}finally {
			if(!check) {
				mav.setViewName("login");
			}
		}
		return check;
	}

}