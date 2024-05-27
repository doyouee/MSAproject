package com.inzent.commonMethod.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpSender {
	public static Map<String, Object> sndHTTP(String url, HttpMethod type, Map<String, Object> parameters, Map<String, Object> headerValue) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String param = "";
		try {
			if( parameters != null && !parameters.isEmpty()){
				if (HttpMethod.GET.equals(type)||HttpMethod.DELETE.equals(type)) {
					for(String key : parameters.keySet()){
						param +=  key + "=" + URLEncoder.encode((String) parameters.get(key), "UTF-8")+"&" ;
					}
					resultMap = sndHTTP(url, type, param.substring(0, param.length() - 1), headerValue);
				} else if ((HttpMethod.POST.equals(type)||HttpMethod.PUT.equals(type))) {
					ObjectMapper mapper = new ObjectMapper();
					param = mapper.writeValueAsString(parameters);
					resultMap = sndHTTP(url, type, param, headerValue);
				}
			}else{
				resultMap = sndHTTP(url, type,"", headerValue);
			}
		} catch (Exception e) {
			resultMap = ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
			StringWriter errsw = new StringWriter();
			e.printStackTrace(new PrintWriter(errsw));
		}
		return resultMap;
	}

	public static Map<String, Object> sndHTTP(String url, HttpMethod type, String parameters, Map<String, Object> headerValue) {
		URL urlObj = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BufferedReader br = null;

		try {
			if (HttpMethod.GET.equals(type)||HttpMethod.DELETE.equals(type)) {
				if (parameters == null || parameters.isEmpty()) {
					urlObj = new URL(url);
				} else {
					urlObj = new URL(url + "?" + parameters);
				}
			} else {
				urlObj = new URL(url);
			}

			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod(type.name()); // http 메서드
			conn.setRequestProperty("Content-Type", "application/json"); // header Content-Type 정보
			conn.setConnectTimeout(15000);
			conn.setDoOutput(true); // 서버로부터 받는 값이 있다면 true

			for (String key : headerValue.keySet()) {
				conn.setRequestProperty(key, headerValue.get(key).toString());
			}
			if ((HttpMethod.POST.equals(type)||HttpMethod.PUT.equals(type)) && parameters != null && !parameters.isEmpty()) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(parameters.getBytes(StandardCharsets.UTF_8));
				outputStream.close();
			}
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 서버로부터 데이터 읽어오기
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
					sb.append(line);
				}
				resultMap = (Map<String, Object>) new ObjectMapper().readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
			} else {
				resultMap = ResponseMessage.setMessage(Consts.HTTP_ERROR, conn.getResponseCode());

			}
		} catch (Exception e) {
			resultMap = ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
			StringWriter errsw = new StringWriter();
			e.printStackTrace(new PrintWriter(errsw));
		}finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				resultMap = ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
				StringWriter errsw = new StringWriter();
				e.printStackTrace(new PrintWriter(errsw));
			}
		}
		return resultMap;
	}
}
