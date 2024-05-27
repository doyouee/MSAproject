package com.inzent.commonAPI.mapper;

import com.inzent.commonAPI.vo.CommonCodeGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonCodeGroupMapper {
	//전체 공통코드그룹 조회
	List<CommonCodeGroupVO> getCommonCodeGroup(@Param("group") CommonCodeGroupVO commonCodeGroupVO,@Param("page") Map<String, Object> page );

	//공통코드그룹 ID 중복 조회
	int getIdDuplication(@Param("codeGroupId") String codeGroupId);

	//공통코드그룹 명 중복 조회
	String getNameDuplication(@Param("codeGroupName") String codeGroupName );
	
	//공통코드그룹 등록
	void insertCommonCodeGroup(@Param("group") CommonCodeGroupVO commonCodeGroupVO);

	//공통코드그룹 수정 (code_id 기준)
	void updateCommonCodeGroup(@Param("group") CommonCodeGroupVO commonCodeGroupVO);

	//공통코드그룹 삭제 (code_id 기준)
	int deleteCommonCodeGroup(@Param("codeGroupId")String codeGroupId);

}
