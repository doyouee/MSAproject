package com.inzent.commonAPI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonAPI.service.CommonService;
import com.inzent.commonMethod.common.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 공통 관리
 * - API KEY 정보 관리 @author 황유진
 * - 토큰 유효성 검사 @author 조유진
 */

@RestController
public class CommonController {
	@Autowired
	private CommonService commonService;

	@GetMapping("/apiInfo")
	public @ResponseBody Map<String,Object> getApiInfo(@RequestHeader(value="API_KEY")  String apiKey) {
		try{
			if("TRUE".equalsIgnoreCase(commonService.getApiExistence(apiKey))){
				return ResponseMessage.setMessage(Consts.SUCCESS, commonService.getApiInfo());
			}else{
				return  ResponseMessage.setMessage(Consts.ERROR, Consts.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }

	}
	
	public @ResponseBody Map<String,Object> getApiId(String apiKey){
		try{
			return ResponseMessage.setMessage(Consts.SUCCESS, commonService.getApiId(apiKey));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }
	}
	
	@GetMapping("/confirm")
	public @ResponseBody Map<String,Object> tokenAuthorityConfirm(@RequestHeader(value="TOKEN_VALUE") String tokenValue, @RequestHeader(value="API_KEY")  String apiKey
			, @RequestHeader(value="MAPPING_URL") String mappingUrl, @RequestHeader(value="METHOD") String method, HttpServletResponse response) {
		try {
			if(commonService.authorityConfirm(tokenValue, apiKey, mappingUrl, method)) {
				return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
			}else {
				response.setStatus(403);
				return ResponseMessage.setMessage(Consts.ERROR, Consts.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(403);
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString().substring(0,e.toString().indexOf(":")));
        }
		
	}

}
