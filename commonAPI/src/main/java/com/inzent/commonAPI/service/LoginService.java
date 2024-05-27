package com.inzent.commonAPI.service;

import java.util.HashMap;
import java.util.Map;

import com.inzent.commonMethod.common.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonAPI.common.Encrypt;
import com.inzent.commonAPI.mapper.LoginMapper;
import com.inzent.commonAPI.token.JwtTokenProvider;
import com.inzent.commonAPI.vo.UserVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;

/**
 * 로그인 및 로그아웃
 * @author 조유진
 */

@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    public Map<String, Object> login(String apiKey, Map<String, Object> loginInfoMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserVO userVo = objectMapper.convertValue(loginInfoMap.get(Consts.REQUEST_MESSAGE), UserVO.class);
            userVo.setUserPassword(Encrypt.encrypt(userVo.getUserPassword()));

            if (loginMapper.loginConfirm(userVo).equals("1")) { // login 정보가 존재할 경우 token 값 return
                Map<String, Object> responseMessage = new HashMap<String, Object>();
                responseMessage.put("token", jwtTokenProvider.createToken(userVo.getUserEmail()));
                responseMessage.put("menu", commonService.getMenu(apiKey, userVo.getUserEmail()));
                responseMessage.put("userInfo", userService.getUserSearchKeyword(userVo.getUserEmail(), null, null, null, "email", null).get(Consts.RESPONSE_MESSAGE));
                responseMessage.put("userAuthorityList", commonService.getUserAuthority());
                return ResponseMessage.setMessage(Consts.SUCCESS, responseMessage);
            } else { // login 정보가 없을 경우 ERROR return
                return ResponseMessage.setMessage(Consts.ERROR, "로그인 정보가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.ERROR, e.getMessage());
        }
    }

    public String logout(String tokenValue) {
        jwtTokenProvider.removeToken(tokenValue);
        return Consts.NO_ERROR;
    }
}
