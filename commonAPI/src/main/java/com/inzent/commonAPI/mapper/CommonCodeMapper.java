package com.inzent.commonAPI.mapper;


import com.inzent.commonAPI.vo.CommonCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommonCodeMapper {
	//전체 공통코드 조회
	List<CommonCodeVO> getCommonCode(@Param("code") CommonCodeVO commonCodeVO,@Param("page") Map<String, Object> page);

	//하나의 공통코드ID와 동일한 공통 그룹을 가지고 있는 모든 리스트 출력
	List<CommonCodeVO> getCODECommonCode(@Param("codeId") ArrayList<String> codeId);

	//하나의 공통코드 조회
	List<CommonCodeVO> getONECommonCode(@Param("codeId") String codeId);

	//상위 공통코드 조회
	List<CommonCodeVO> getTOPCommonCode();

	//id중복조회
	String getIdDuplication(@Param("code") CommonCodeVO commonCodeVO);

	//공통코드 등록
	void insertCommonCode(@Param("code") CommonCodeVO commonCodeVO);

	//공통코드 수정 (code_id 기준)
	void updateCommonCode(@Param("code") CommonCodeVO commonCodeVO);

	//공통코드 삭제 (code_id 기준)
	int deleteCommonCode(@Param("codeId") String codeId);
}
