package com.inzent.commonMethod.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class RequestMessage {
	public static String setMessage(Object message) {
		String requestMessage = "";
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(Consts.REQUEST_MESSAGE, message);

			ObjectMapper objectMapper = new ObjectMapper();
			requestMessage = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
			e.printStackTrace();
        }
        return requestMessage;
	}
}
