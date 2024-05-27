package com.inzent.deliverablesUI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.vo.CommonCodeGroupVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommonCodeGroupController  extends MasterController {

    @Value("${API_URL}")
    public String apiUrl;
    @Value("${API_KEY}")
    public String apikey;

    @GetMapping("/commonCodeGroup")
    public ModelAndView commonCodeGroup(HttpServletResponse response, HttpSession session) throws Exception{
        ModelAndView mav = new ModelAndView("commonCode");
        try {
            if (!isLogin(mav, session)) {
                return mav;
            }
            Map<String, Object> resultResponse = new HashMap<String, Object>();
            Map<String, Object> param = new HashMap<>();
            param.put("pageNum", "1");
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                mav.addObject("commonCodeGroup", resultResponse.get(Consts.RESPONSE_MESSAGE));
                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                    mav.addObject("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                }
            }
            mav.addObject("mode","commonGroupManager");
            mav.addObject("pageNum",1);

        }catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return mav;
    }

    @PostMapping("/commonCodeGroup")
    @ResponseBody
    public  Map<String, Object>  insertCommonCodeGroup(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeGroupVO commonCodeGroupVO)  throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            commonCodeGroupVO.setPageNum("1");
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.POST, RequestMessage.setMessage(commonCodeGroupVO) ,
                    CommonUtil.getHttpHeaderMap(apikey,session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                Map<String, Object> param = new HashMap<>();
                param.put("pageNum" ,commonCodeGroupVO.getPageNum());
                resultResponse = HttpSender.sndHTTP(apiUrl+"/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                    result.put("commonCodeGroup", resultResponse.get(Consts.RESPONSE_MESSAGE));
                    resultResponse = HttpSender.sndHTTP(apiUrl+"/commonCodeGroup", HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                        resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }

        return resultResponse;
    }
    @PutMapping("/commonCodeGroup")
    @ResponseBody
    public  Map<String, Object>  updateCommonCodeGroup(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeGroupVO commonCodeGroupVO)  throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> param = new HashMap<>();
            commonCodeGroupVO.setPageNum("1");
            param.put("pageNum" ,commonCodeGroupVO.getPageNum());
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.PUT, RequestMessage.setMessage(commonCodeGroupVO), CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){

                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                    result.put("commonCodeGroup", resultResponse.get(Consts.RESPONSE_MESSAGE));
                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                        resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @DeleteMapping("/commonCodeGroup")
    @ResponseBody
    public Map<String, Object> deleteCommonCodeGroup(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeGroupVO commonCodeGroupVO )  throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();

            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup" , HttpMethod.DELETE, "codeGroupId=" + commonCodeGroupVO.getCodeGroupId(), CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                Map<String, Object> param = new HashMap<>();
                param.put("pageNum" ,"1");

                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup" ,HttpMethod.GET, param, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){

                    result.put("commonCodeGroup",resultResponse.get(Consts.RESPONSE_MESSAGE));
                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup" ,HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                        resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/commonCodeGroupList")
    @ResponseBody
    public Map<String, Object> commonCodeGroupList (HttpServletResponse response, HttpSession session ,@RequestParam Map<String, Object> requestMessage) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> param = requestMessage;
            result.put("pageNum",requestMessage.get("pageNum"));
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup" , HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                if (requestMessage.get("codeGroupName") != "") {
                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    result.put("commonCodeGroup",resultResponse.get(Consts.RESPONSE_MESSAGE));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        param.remove("pageNum");
                        resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                        result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                        resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                    }
                } else {
                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        result.put("commonCodeGroup",resultResponse.get(Consts.RESPONSE_MESSAGE));
                        resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }

        return resultResponse;
    }

    @GetMapping("/commonCodeGroupOne")
    @ResponseBody
    public Map<String, Object> commonCodeGroupOne (HttpServletResponse response, HttpSession session,  @RequestParam String codeGroupName) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> param = new HashMap<>();
            param.put("codeGroupName",codeGroupName);

            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup" , HttpMethod.GET, param, CommonUtil.getHttpHeaderMap( apikey, session.getAttribute(Globals.TOKEN_VALUE)));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }
}