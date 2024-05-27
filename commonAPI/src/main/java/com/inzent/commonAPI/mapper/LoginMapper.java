package com.inzent.commonAPI.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.inzent.commonAPI.vo.UserVO;

@Mapper
public interface LoginMapper {
	// 로그인 사용자 정보 확인
	String loginConfirm(UserVO userVo);
}
