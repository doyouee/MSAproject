package com.inzent.deliverablesAPI.mapper;

import java.util.*;

import com.inzent.deliverablesAPI.vo.DeliverablesRegVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeliverablesRegMapper {

	// 등록된 산출물 조회
    List<DeliverablesRegVO> getReg(String deliverablesId, String projectId);

	// 등록된 산출물 중 하나만 조회
    List<DeliverablesRegVO> getRegOne(String id);

	// 산출물 파일 정보 디비 저장 후 파일을 서버에 저장
    void insertReg(@Param("post") DeliverablesRegVO deliverablesRegVO, String deliverName, String filename, String filePath);

	// DB에 삭제 정보 UPDATE
    void deleteReg(String registrationId, String deleteUserEmail, String deleteUserName);

    // 사유 있는 상태에서 파일 등록 시, DB 내 사유 데이터 삭제 DELETE
    void deleteReason(String registrationId, String deleteUserEmail, String deleteUserName);

	// 선택한 파일의 경로명 불러오기
    List<HashMap<String, Object>> getFileInfo(String deliverablesTitle);

    // 다운로드용 산출물 조회
    List<DeliverablesRegVO> getDownloadReg(String projectId);
}
