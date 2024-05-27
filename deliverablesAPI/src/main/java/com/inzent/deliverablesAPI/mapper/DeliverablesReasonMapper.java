package com.inzent.deliverablesAPI.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import com.inzent.deliverablesAPI.vo.DeliverablesReasonVO;
import com.inzent.deliverablesAPI.vo.DeliverablesRegVO;

@Mapper
public interface DeliverablesReasonMapper {
	// 준필수 파일 산출물 아이디 조회
    public List<DeliverablesListVO> getDeliverIdList();

    // 산출물 아이디 조회
    public List<DeliverablesRegVO> getDeliverRegIdList(String registrationId);

	// 준필수 파일이 해당 파일 가지고 있는지 여부 확인
    public List<DeliverablesRegVO> getDeliverIdFiles(String deliverId, String projectId);
    
    // 준필수파일 사유를 가지고 있는 파일 정보 가져오기
    public List<DeliverablesRegVO> getDeliverIdReason(String deliverId, String projectId);

	// 등록된 파일이 없는 준필수 파일의 일부 정보 가져오기
    public List<DeliverablesRegVO> getFilesInfo(List<String> notRegiReasonList, String projectId, @Param("page") Map<String, Object> page);

    // 준필수 파일 사유 입력
    void insertReason(@Param("post") DeliverablesReasonVO deliverablesReasonVO, String filePath);

    // 준필수 파일 사유 수정
    void updateReason(@Param("post") DeliverablesReasonVO deliverablesReasonVO);

}
