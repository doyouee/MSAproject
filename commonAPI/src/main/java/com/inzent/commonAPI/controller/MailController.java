package com.inzent.commonAPI.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.inzent.commonAPI.service.MailService;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;

/**
 * 메일 전송
 * @author 조유진
 */

@RestController
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	@PostMapping("/sendMail")
	public Map<String,Object> sendMail(@RequestHeader(value="API_KEY") String apiKey, @RequestBody String sendInfo) {
		try {
			if(mailService.sendMail(apiKey, sendInfo).equals("S")) {
				return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
			}else {
				return ResponseMessage.setMessage(Consts.ERROR, Consts.ERROR);
			}
    	} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString().substring(0,e.toString().indexOf(":")));
        }
	}
}
