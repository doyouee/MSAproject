package com.inzent.commonAPI.mapper;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.inzent.commonAPI.vo.MailHistoryVO;
import com.inzent.commonAPI.vo.MailInfoVO;


@Mapper
public interface MailMapper {
	// 메일 정보 조회
	MailInfoVO getMailTemplate(Map<String, Object> apiKeyMap);
	
	// 메일 이력 저장
	void setMailHistory(MailHistoryVO mailHistoryVo);
	
}
