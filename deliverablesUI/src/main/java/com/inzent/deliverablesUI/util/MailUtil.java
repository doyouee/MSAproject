package com.inzent.deliverablesUI.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.deliverablesUI.common.Globals;

public class MailUtil {

    public static Map<String, Object> inspectionSendMail(HttpServletResponse response, String apiUrl, String mailApiKey, Map<String, Object> headerMap, HttpSession session, Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
        	String[] sendMailProjectList = requestMessage.get("projectId").toString().split(",");
            for (String projectId : sendMailProjectList) {
                responseMap = sendMailComplete(apiUrl, mailApiKey, headerMap, session, requestMessage, projectId);
                if(!responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                	break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return responseMap;
    }

    //메일 전송
    public static Map<String, Object> sendMailComplete(String apiUrl, String mailApiKey, Map<String, Object> headerMap, HttpSession session, Map<String, Object> requestMessage, String projectId) throws Exception {
        Map<String, Object> deliverablesHeaderMap = CommonUtil.getHttpHeaderMap(mailApiKey, session.getAttribute(Globals.TOKEN_VALUE));
        Map<String, Object> responseMap = new HashMap<String, Object>();
        List<Map<String, Object>> project = new ArrayList<>();
        Map<String, Object> myAccount = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
        String myEmail = myAccount.get("userEmail").toString();
        String emails = "";

        responseMap = HttpSender.sndHTTP(apiUrl + "/user", HttpMethod.GET, "", deliverablesHeaderMap);
        if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
            List<Map<String, Object>> userList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);

            responseMap = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/project", HttpMethod.GET, "projectId=" + projectId, headerMap);
            if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                project = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
                responseMap = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/projectUser", HttpMethod.GET, "projectId=" + projectId, headerMap);
                if (responseMap.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                    List<Map<String, Object>> projectUserList = (List<Map<String, Object>>) responseMap.get(Consts.RESPONSE_MESSAGE);
                    for (Map<String, Object> userMap : userList) {
                        if (!myEmail.equals(userMap.get("userEmail"))) {
                            if (userMap.get("authorityId").equals("0") || userMap.get("authorityId").equals("1")) {
                                emails += userMap.get("userEmail").toString() + ",";
                            } else if (userMap.get("authorityId").equals("2") && userMap.get("teamId").equals(project.get(0).get("teamId").toString())) {
                                emails += userMap.get("userEmail").toString() + ",";
                            } else if (userMap.get("authorityId").equals("3")) {
                                for (Map<String, Object> projectUser : projectUserList) {
                                    if (userMap.get("userEmail").equals(projectUser.get("userEmail"))) {
                                        emails += projectUser.get("userEmail") + ",";
                                    }
                                }
                            }
                        }
                    }
                }
                emails = emails.substring(0, emails.length() - 1);

                List<String> contentList = new ArrayList<String>();
                contentList.add(project.get(0).get("projectName").toString()); // 프로젝트 명
                if ("2".equals(requestMessage.get("inspectionStatus"))) {
                    contentList.add("완료");
                } else if ("3".equals(requestMessage.get("inspectionStatus"))) {
                    contentList.add("반려");
                    contentList.add("사유 : " + requestMessage.get("inspectionRejectionReason"));
                }
                Map<String, Object> mailResult = new HashMap<>();
                mailResult.put("sendTo", emails);
                mailResult.put("sendContent", contentList);
                ObjectMapper mapper = new ObjectMapper();
                String param = mapper.writeValueAsString(mailResult);
                responseMap = HttpSender.sndHTTP(apiUrl + "/sendMail", HttpMethod.POST, param, deliverablesHeaderMap);

            }
        }
        return responseMap;
    }
}
