package com.inzent.commonAPI.controller;

import com.inzent.commonAPI.service.LoginService;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.commonMethod.common.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 로그인 및 로그아웃 관리
 * @author 조유진
 */

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public @ResponseBody Map<String, Object> login(@RequestHeader(value="API_KEY")  String apiKey, @RequestBody  Map<String, Object> loginInfoMap) {
    	try {
    		return loginService.login(apiKey, loginInfoMap);
    	} catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString().substring(0,e.toString().indexOf(":")));
        }
    }

    @DeleteMapping("/logout")
    public @ResponseBody Map<String, Object> logout(@RequestHeader(value = "TOKEN_VALUE") String tokenValue) {
    	try {
    		return ResponseMessage.setMessage(Consts.SUCCESS, loginService.logout(tokenValue));
    	} catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString().substring(0,e.toString().indexOf(":")));
        }
    }
}
