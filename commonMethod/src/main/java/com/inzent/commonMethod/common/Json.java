package com.inzent.commonMethod.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class Json {

	public static String map2Json(List<Map<String, Object>> map) {
		JSONArray jsonArray = new JSONArray();

		for (Map<String, Object> o : map) {
			JSONObject jsonObject = new JSONObject();
			for (Map.Entry<String, Object> entry : o.entrySet()) {
				try {
					String key = entry.getKey();
					String value = null;
					if(entry.getValue() != null) {
						value = entry.getValue().toString();
					}
					jsonObject.put(key, value);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}

	public static Map<String, Object> jsonToMap(String jsonString) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
		return objectMapper.readValue(jsonString, typeReference);
	}

}
