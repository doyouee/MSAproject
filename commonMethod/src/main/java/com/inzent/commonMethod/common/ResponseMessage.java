package com.inzent.commonMethod.common;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {
	public static Map<String, Object> setMessage(String code, Object message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Consts.RESPONSE_CODE, code);
		result.put(Consts.RESPONSE_MESSAGE, message);

		return result;
	}
}
