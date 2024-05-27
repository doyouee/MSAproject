package com.inzent.deliverablesUI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.vo.CommonCodeVO;
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
public class CommonCodeController extends MasterController {

    @Value("${API_URL}")
    public String apiUrl;
    @Value("${API_KEY}")
    public String apikey;

    @GetMapping("/commonCode")
    public ModelAndView commonCode(HttpServletResponse response, HttpSession session) throws Exception{
        ModelAndView mav = new ModelAndView("commonCode");
        try {
            if (!isLogin(mav, session)) {
                return mav;
            }
            Map<String, Object> resultResponse = new HashMap<String, Object>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", "all");
            mav.addObject("pageNum",1);
            //전체 공통코드 조회
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                mav.addObject("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                param.put("pageNum", "1");
                //페이지네이션
                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                    mav.addObject("commonCode",resultResponse.get(Consts.RESPONSE_MESSAGE));
                    //상위공통코드 조회
                    resultResponse = HttpSender.sndHTTP(apiUrl+ "/commonCode", HttpMethod.GET,"type=top", CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                        mav.addObject("commonTop" , resultResponse.get(Consts.RESPONSE_MESSAGE));

                        //공통코드 그룹 조회
                        resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCodeGroup", HttpMethod.GET, (String) null, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                        if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                            mav.addObject("commonCodeGroup",resultResponse.get(Consts.RESPONSE_MESSAGE));

                        }
                    }
                    mav.addObject("mode","commonCodeManager");
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return mav;
    }
    @PostMapping("/commonCode")
    @ResponseBody
    public  Map<String, Object>  insertCommonCode(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeVO commonCodeVO) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            if(commonCodeVO.getCodeTopId().equals("-")){
                commonCodeVO.setCodeTopId(null);
            }
            commonCodeVO.setPageNum("1");

            Map<String, Object> result = new HashMap<String, Object>();
            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.POST, RequestMessage.setMessage(commonCodeVO), CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                Map<String, Object> param = new HashMap<>();
                param.put("type", "all");
                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                    result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                    param.put("pageNum", "1");

                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                        result.put("commonCode", resultResponse.get(Consts.RESPONSE_MESSAGE));
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
    @PutMapping("/commonCode")
    @ResponseBody
    public  Map<String, Object>  updateCommonCode(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeVO commonCodeVO) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            if(commonCodeVO.getCodeTopId().equals("-")){
                commonCodeVO.setCodeTopId(null);
            }
            commonCodeVO.setPageNum("1");
            Map<String, Object> result = new HashMap<String, Object>();

            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.PUT, RequestMessage.setMessage(commonCodeVO), CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){

                Map<String, Object> param = new HashMap<>();
                param.put("type", "all");
                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());

                    param.put("pageNum", "1");
                    resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                        result.put("commonCode", resultResponse.get(Consts.RESPONSE_MESSAGE));
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

    @DeleteMapping("/commonCode")
    @ResponseBody
    public Map<String, Object> deleteCommonCode(HttpServletResponse response, HttpSession session, @ModelAttribute CommonCodeVO commonCodeVO ) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            commonCodeVO.setPageNum("1");
            resultResponse = HttpSender.sndHTTP(apiUrl+"/commonCode" , HttpMethod.DELETE, "codeId=" + commonCodeVO.getCodeId(), CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                Map<String, Object> param = new HashMap<>();
                param.put("type", "all");
                resultResponse = HttpSender.sndHTTP(apiUrl+"/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
                    param.put("pageNum", commonCodeVO.getPageNum());
                    resultResponse = HttpSender.sndHTTP(apiUrl+"/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                    if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                        result.put("commonCode", resultResponse.get(Consts.RESPONSE_MESSAGE));
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

    @GetMapping("/commonCodeList")
    @ResponseBody
    public Map<String, Object> commonCodeList(HttpServletResponse response, HttpSession session,@RequestParam Map<String, Object> requestMessage) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> param = requestMessage;
            param.put("type", "all");

            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET,param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                result.put("commonCode", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)));
                param.remove("pageNum");
                resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));
                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    result.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
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


    @GetMapping("/commonCodeOne")
    @ResponseBody
    public Map<String, Object> commonCodeOne(HttpServletResponse response, HttpSession session, @RequestParam String codeId) throws Exception{
        Map<String, Object> resultResponse = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("type", "one");
            param.put("codeId", codeId);

            resultResponse = HttpSender.sndHTTP(apiUrl + "/commonCode", HttpMethod.GET, param, CommonUtil.getHttpHeaderMap(apikey, session.getAttribute(Globals.TOKEN_VALUE)));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

}