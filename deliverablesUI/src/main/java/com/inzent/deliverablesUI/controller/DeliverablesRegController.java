package com.inzent.deliverablesUI.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.HttpSender;
import com.inzent.commonMethod.common.Json;
import com.inzent.commonMethod.common.RequestMessage;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesUI.common.Globals;
import com.inzent.deliverablesUI.common.MasterController;
import com.inzent.deliverablesUI.util.CommonUtil;
import com.inzent.deliverablesUI.util.DeliverablesListUtil;
import com.inzent.deliverablesUI.util.MailUtil;


@Controller
public class DeliverablesRegController extends MasterController {
    @Value("${API_URL}")
    public String apiUrl;
    @Value("${API_KEY}")
    public String apiKey;

    Json json = new Json();

    @GetMapping("/deliverablesReg")
    @ResponseBody
    public ModelAndView getList(HttpServletResponse response, HttpSession session) throws Exception{
        ModelAndView mav = new ModelAndView("deliverablesReg");
        try {
            if (!isLogin(mav, session)) {
                return mav;
            }
            Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));
            Map<String, Object> resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesList", HttpMethod.GET, "", headerMap);
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                List<Map<String, Object>> deliverablesList = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
                resultResponse = getProjectListByAuthority(response, new HashMap<>(), session);

                if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                	DeliverablesListUtil.getdeliverablesList(mav, deliverablesList);
                	mav.addObject("projectJson", new ObjectMapper().writeValueAsString(((Map<String, Object>) resultResponse.get(Consts.RESPONSE_MESSAGE)).get("projectByAuthority")));
                	mav.addObject("userInfo", session.getAttribute(Globals.USER_INFO));
                }
            }
            mav.addObject(Consts.RESPONSE_CODE, resultResponse.get(Consts.RESPONSE_CODE));
            if(!resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)){
                mav.addObject(Consts.RESPONSE_MESSAGE, resultResponse.get(Consts.RESPONSE_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return mav;
    }

    public Map<String, Object> getProjectListByAuthority(HttpServletResponse response, Map<String, Object> requestMessage, HttpSession session) throws Exception{
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey,	session.getAttribute(Globals.TOKEN_VALUE));
            Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            String userAuthorityId = userInfo.get("authorityId").toString();
            Map<String, Object> result = new HashMap<String, Object>();
            if ("2".equals(userAuthorityId)) {
                requestMessage.put("teamId", userInfo.get("teamId"));
            } else if ("3".equals(userAuthorityId)) {
                requestMessage.put("userEmail", userInfo.get("userEmail"));
            }
            resultResponse = HttpSender.sndHTTP(CommonUtil.getProjectApiUrl() + "/project", HttpMethod.GET, requestMessage, headerMap);
            if (resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                result.put("projectByAuthority", resultResponse.get(Consts.RESPONSE_MESSAGE));
                resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @PostMapping("/deliverablesReason")
    @ResponseBody
    public Map<String, Object> postDeliverablesReason(HttpServletResponse response, HttpSession session, @RequestBody List<Map<String,Object>> requestMessage) throws Exception{
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl()+"/deliverablesReason", HttpMethod.POST, RequestMessage.setMessage(requestMessage),
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/fileList")
    @ResponseBody
    public Map<String, Object> fileList(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesReg", HttpMethod.GET, "projectId=" + request.getParameter("projectId") + "&deliverablesId=" + request.getParameter("deliverablesId"),
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE);
                List<Map<String, Object>> result = new ArrayList<>();
                // 파일 등록 날짜 포맷 변환
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < resultList.size(); i++) {
                    Map<String, Object> resultMap = resultList.get(i);
                    if (resultMap.get("notRegistrationReason") == null || resultMap.get("notRegistrationReason").equals("")) {
                        String registrationDate = resultMap.get("registrationDate").toString();
                        Date parseDate = sdf1.parse(registrationDate);
                        resultMap.put("registrationDate", sdf2.format(parseDate));
                        result.add(resultMap);
                    }
                    resultResponse = ResponseMessage.setMessage(Consts.SUCCESS, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/inspectList")
    @ResponseBody
    public Map<String, Object> inspectList(HttpServletResponse response, HttpServletRequest request, HttpSession session)  throws Exception{
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            String deliverablesApiUrl = CommonUtil.getDeliverablesApiUrl();
            String projectId = request.getParameter("projectId");
            Map<String, Object> headerMap = CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE));

            resultResponse = HttpSender.sndHTTP(deliverablesApiUrl + "/deliverablesReason", HttpMethod.GET, "projectId=" + projectId, headerMap);
            if(resultResponse.get(Consts.RESPONSE_CODE).equals(Consts.SUCCESS)) {
                resultResponse.put("size", ((List<Map<String, Object>>) resultResponse.get(Consts.RESPONSE_MESSAGE)).size());
            }
        } catch (Exception e) {
            response.sendRedirect("/error");
            e.printStackTrace();
        }
        return resultResponse;
    }

    @PostMapping("/deliverablesRegPostText")
    @ResponseBody
    public Map<String, Object> postText(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> requestMessage) throws Exception {
        Map<String, Object> resultResponse  = new HashMap<>();
        try {
            Map<String, Object> userList = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            requestMessage.put("textContents", requestMessage.get("textContents").toString().replaceAll("\\n", "<br>"));
            requestMessage.put("contentType", "1");
            requestMessage.put("registrationUserEmail", userList.get("userEmail").toString());
            requestMessage.put("registrationUserName", userList.get("userName").toString());

            Map<String, Object> registrationInfo = new HashMap<>();
            registrationInfo.put("registrationInfo", requestMessage);
            registrationInfo.put("fileList", null);
            resultResponse = (Map<String, Object>) textSender(response, CommonUtil.getDeliverablesApiUrl() + "/deliverablesReg", HttpMethod.POST, registrationInfo, null,
                    CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }


    @PostMapping("/deliverablesRegPostFile")
    @ResponseBody
    public Map<String, Object> postFile(HttpServletResponse httpResponse, HttpServletRequest request, HttpSession session,@RequestPart(value = "registrationInfo") Map<String, Object> registrationInfo, @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList) throws Exception {
        Map<String, Object> resultResponse = null;
        try {
            Map<String, Object> userList = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);

            registrationInfo.put("registrationUserEmail", userList.get("userEmail").toString());
            registrationInfo.put("registrationUserName", userList.get("userName").toString());

            JsonNode response;
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(3000);
            factory.setBufferRequestBody(false);
            RestTemplate restTemplate = new RestTemplate(factory);

            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            try {
                for (MultipartFile file : fileList) {
                	map.add("fileList", file.getResource());
                }
                map.add("registrationInfo", registrationInfo);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                headers.set(Globals.API_KEY, apiKey);
                headers.set(Globals.HEADER_TOKEN_VALUE, (String)session.getAttribute(Globals.TOKEN_VALUE));

                HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
                response = restTemplate.postForObject(CommonUtil.getDeliverablesApiUrl() + "/deliverablesReg", requestEntity, JsonNode.class);
                resultResponse = Json.jsonToMap(String.valueOf(response));

            } catch (HttpStatusCodeException e) {
                e.printStackTrace();
                String errorResponse = e.getResponseBodyAsString();
                httpResponse.sendRedirect("/error");
                throw new RuntimeException(errorResponse);
            } catch (Exception e) {
                e.printStackTrace();
                httpResponse.sendRedirect("/error");
                throw new RuntimeException(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.sendRedirect("/error");
        }
        return resultResponse;
    }

    public Map<String, Object> textSender(HttpServletResponse httpResponse, String url, HttpMethod type, Map<String, Object> parameters, Map<String, Object> userList, Map<String, Object> headerValue) throws Exception {
        String resultResponse = "";
        try {
            String charset = "UTF-8";
            String param = "";
            Map<String, Object> registrationInfo = (Map<String, Object>) parameters.get("registrationInfo");

            ObjectMapper mapper = new ObjectMapper();
            if(userList != null) {
                registrationInfo.put("registrationUserEmail", userList.get("userEmail").toString());
                registrationInfo.put("registrationUserName", userList.get("userName").toString());
            }

            try {
                param = mapper.writeValueAsString(registrationInfo);
            } catch (Exception var20) {
                httpResponse.sendRedirect("/error");
                throw new RuntimeException(var20);
            }

            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";
            URL urlObj = null;

            BufferedReader br2 = null;
            try {
                urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();
                conn.setRequestMethod(type.name());
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setRequestProperty("User-Agent", "curl/7.74.0");
                Iterator var26 = headerValue.keySet().iterator();

                String br = "";
                while(var26.hasNext()) {
                    br = (String)var26.next();
                    conn.setRequestProperty(br, headerValue.get(br).toString());
                }

                try {
                    OutputStream output = conn.getOutputStream();
                    try {
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
                        try {
                            writer.append("--" + boundary).append(CRLF);
                            writer.append("Content-Disposition: form-data; name=\"registrationInfo\"").append(CRLF);
                            writer.append("Content-Type: application/json; charset=" + charset).append(CRLF);
                            writer.append(CRLF).append(param);
                            writer.append(CRLF).flush();
                            writer.append("--" + boundary + "--").append(CRLF).flush();
                        } catch (Throwable var21) {
                            try {
                                writer.close();
                            } catch (Throwable var19) {
                                var21.addSuppressed(var19);
                                httpResponse.sendRedirect("/error");
                            }
                            httpResponse.sendRedirect("/error");
                            throw var21;
                        }
                        writer.close();
                    } catch (Throwable var22) {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (Throwable var18) {
                                var22.addSuppressed(var18);
                                httpResponse.sendRedirect("/error");
                            }
                        }
                        httpResponse.sendRedirect("/error");
                        throw var22;
                    }
                    if (output != null) {
                        output.close();
                    }
                } catch (Exception e) {
                    httpResponse.sendRedirect("/error");
                    throw new RuntimeException(e);
                }
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    br2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    resultResponse = br2.readLine();
                    return Json.jsonToMap(resultResponse);
                } else {
                    br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    resultResponse = br2.readLine();
                    return Json.jsonToMap(resultResponse);
                }
            } catch (Exception var25) {
                StringWriter errsw = new StringWriter();
                var25.printStackTrace(new PrintWriter(errsw));
                httpResponse.sendRedirect("/error");
            }
            resultResponse = br2.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.sendRedirect("/error");
        }
        return Json.jsonToMap(resultResponse);
    }

    @DeleteMapping("/deliverablesReg")
    @ResponseBody
    public Map<String, Object> deleteList(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> resultResponse = new HashMap<>();
        try {
            Map<String, Object> userList = (Map<String, Object>) session.getAttribute(Globals.USER_INFO);
            resultResponse = HttpSender.sndHTTP(CommonUtil.getDeliverablesApiUrl() + "/deliverablesReg", HttpMethod.DELETE, "registrationId=" + request.getParameter("registrationId") + "&deleteUserEmail=" +
                    userList.get("userEmail").toString() + "&deleteUserName=" + URLEncoder.encode(userList.get("userName").toString(), "UTF-8"), CommonUtil.getHttpHeaderMap(apiKey, session.getAttribute(Globals.TOKEN_VALUE)));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
        }
        return resultResponse;
    }

    @GetMapping("/fileView")
    public ResponseEntity<Resource> fileView(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestParam(value="fileViewFormFilePath") String filePath) throws Exception{
        try {
            // .pdf, .gif, .jpeg, .png 파일만 지원
            File file = new File(filePath);
            String fileName = file.getName();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            String type = null;
            if(fileName.endsWith(".pdf")) {
                type = MediaType.APPLICATION_PDF_VALUE;
            }else if(fileName.endsWith(".gif")){
                type = MediaType.IMAGE_GIF_VALUE;
            }else if(fileName.endsWith(".jpeg")) {
                type = MediaType.IMAGE_JPEG_VALUE;
            }else if(fileName.endsWith(".png")) {
                type = MediaType.IMAGE_PNG_VALUE;
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                    .header(HttpHeaders.CONTENT_TYPE, type)
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("/error");
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
