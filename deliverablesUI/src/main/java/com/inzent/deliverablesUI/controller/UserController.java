package com.inzent.deliverablesUI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;

@Controller
public class UserController extends MasterController {

    @Value("${API_KEY}")
    public String apiKey;

    @Value("${API_URL}")
    public String apiUrl;

    @GetMapping("/user")
    public ModelAndView user(HttpServletResponse response, HttpSession session, Model model) throws Exception {
        ModelAndView mav = new ModelAndView("user");
        try {
            if (!isLogin(mav, session)) {
                return mav;
            }
            Map<String, Object> responseMap = new HashMap<String, Object>();
            //권한
            List<Map<String, Object>> sessionAuthoritySelect = (List<Map<String, Object>>) session.getAttribute(Globals.USER_AUTHORITY_LIST);
            Map<String, Object> myAuthority = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            List<Map<String, Object>> addSessionAuthoritySelect = new ArrayList<Map<String, Object>>();

            for (Map<String, Object> obj : sessionAuthoritySelect) {
                if (Integer.parseInt((String) myAuthority.get("authorityId")) <= Integer.parseInt((String) obj.get("authorityId"))) {
                    addSessionAuthoritySelect.add(obj);
                }
            }
            mav.addObject("myAuthority",new ObjectMapper().writeValueAsString(myAuthority));
            mav.addObject("pageNum",1);
            mav.addObject("authorityListMap", addSessionAuthoritySelect);
            mav.addObject("teamId", myAuthority.get("teamId"));
            mav.addObject("loginAuthority", Integer.parseInt((String) myAuthority.get("authorityId")));
            //유저 조회
            responseMap = getUserListByAuthority(response, new HashMap<String, Object>(), session);
            if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                Map<String, Object> userMap = (Map<String, Object>) responseMap.get(Consts.RESPONSE_MESSAGE);
                mav.addObject("userList", (List<Map<String, Object>>) userMap.get("userList"));
                mav.addObject("userAllListSize", ((List<Map<String, Object>>) userMap.get("userAll")).size());
                //부서 목록
                if (Integer.parseInt((String) myAuthority.get("authorityId")) != 2) {
                    responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=T01&type=code_Id", CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                        mav.addObject("teamListMap", responseMap.get(Consts.RESPONSE_MESSAGE));
                    }
                } else {
                    responseMap = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, "codeId=" + myAuthority.get("teamId") + "&type=one", CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                        mav.addObject("teamListMap", responseMap.get(Consts.RESPONSE_MESSAGE));
                    }
                }
            }
            mav.addObject(Consts.RESPONSE_CODE, responseMap.get(Consts.RESPONSE_CODE));
            if (!responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                mav.addObject(Consts.RESPONSE_MESSAGE, responseMap.get(Consts.RESPONSE_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return mav;
    }

    public Map<String, Object> getUserListByAuthority(HttpServletResponse response, Map<String, Object> requestMessage, HttpSession session) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));
            Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            String userAuthorityId = userInfo.get("authorityId").toString();

            if ("2".equals(userAuthorityId)) {
                requestMessage.put("teamId", userInfo.get("teamId"));
            }
            if (requestMessage.get("pageNum") == null) {
                requestMessage.put("pageNum", "1");
            }
            resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, requestMessage, headerMap);
            if (resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                result.put("userList", resultResponse.get(Consts.RESPONSE_MESSAGE));

                List<Map<String, Object>> userAll = new ArrayList<>();
                requestMessage.remove("pageNum");
                resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, requestMessage, headerMap);
                if (resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    userAll = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
                    result.put("userAll", userAll);
                    resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/userList")
    @ResponseBody
    public Map<String, Object> userList(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            String userAuthorityId = userInfo.get("authorityId").toString();
            resultResponse = getUserListByAuthority(response, requestMessage, session);
            if (resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                Map<String, Object> userMap = (Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE);
                if ("2".equals(userAuthorityId) && !userInfo.get("codeName").toString().contains(requestMessage.get("codeName").toString())) {
                    result.put("userList", new ArrayList<>());
                    result.put("userAllListSize", 0);
                } else {
                    result.put("userList", userMap.get("userList"));
                    result.put("userAllListSize", ((List<Map<String, Object>>) userMap.get("userAll")).size());
                }
                result.put("pageNum",requestMessage.get("pageNum"));
                resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @DeleteMapping("/user")
    @ResponseBody
    public Map<String, Object> userDelete(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String,Object> userInfo  = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            requestMessage.put("login", userInfo.get("authorityId"));
            resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.DELETE, requestMessage, CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @PostMapping("/user")
    @ResponseBody
    public Map<String, Object> userInsert(HttpServletResponse response, HttpSession session, @RequestBody Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.POST, RequestMessage.setMessage(requestMessage), CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/getUserInfo")
    @ResponseBody
    public Map<String, Object> userInfo(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, requestMessage, CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }

        return resultResponse;
    }

    @PutMapping("/user")
    @ResponseBody
    public Map<String, Object> userUpdate(HttpServletResponse response, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String,Object> userInfo  = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            requestMessage.put("login", userInfo.get("authorityId"));
            resultResponse = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.PUT, RequestMessage.setMessage(requestMessage), CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }
}