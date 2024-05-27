package com.inzent.deliverablesAPI.mapper;

import java.util.*;

import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeliverablesListMapper {
	// 산출물 목록 조회
    List<DeliverablesListVO> getList();

	// 산출물 명 조회
    public List<DeliverablesListVO> getDeliverId(String deliverId);

    // 하위 디렉터리 or 파일 조회
    public List<DeliverablesListVO> getUnderInfo(String deliverId);

	// 산출물명 상위 경로명 조회
    public List<DeliverablesListVO> getPathInfo(String deliverId);

	// 산출물 목록의 디렉터리 또는 파일 등록
    void insertList(@Param("deliver") DeliverablesListVO deliverablesListVO);

	// 산출물 목록의 디렉토리 또는 파일 수정
    void updateList(@Param("deliver") DeliverablesListVO deliverablesListVO);

	// 산출물 목록의 디렉토리 또는 파일 삭제
    void deleteList(String id);
}
