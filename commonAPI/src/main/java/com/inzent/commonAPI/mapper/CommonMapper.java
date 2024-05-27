package com.inzent.commonAPI.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.inzent.commonAPI.vo.ApiInfoVO;
import com.inzent.commonAPI.vo.MenuVO;
import com.inzent.commonAPI.vo.UserAuthorityVO;

@Mapper
public interface CommonMapper {
	//전체  API List 조회
	List<ApiInfoVO> getApiInfo();
	
	// 메뉴 조회
	List<MenuVO> getMenu(Map<String, Object> getMenuInfo);
	
	//API_KEY 로 API_NAME 반환
	String getApiId(@Param("apiKey") String apiKey);

	//특정 API_KEY가 존재하는지 확인 후 true/false 반환
	String getApiExistence(@Param("apiKey") String apiKey);
	
	// 매핑URL의 권한 여부 확인
	String authorityConfirm(Map<String, Object> confirmInfo);
	
	// 사용자 권한 종류 조회
	List<UserAuthorityVO> getUserAuthority();
}
