package com.inzent.deliverablesAPI.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.Pagination;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.mapper.DeliverablesReasonMapper;
import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import com.inzent.deliverablesAPI.vo.DeliverablesReasonVO;
import com.inzent.deliverablesAPI.vo.DeliverablesRegVO;

/**
 * 산출물 사유 CRUD 서비스 
 * @author 박주한
 * */

@Service
public class DeliverablesReasonService {

    @Autowired
    private DeliverablesReasonMapper deliverablesReasonMapper;

    @Autowired
    private DeliverablesListService deliverablesListService;

    @Value("${FILEPATH}")
    private String filePath;

    public List<DeliverablesRegVO> getNotRegiReason(String projectId, Integer pageNum) {
        // 준필수 파일 DELIVERABLES_ID 가져와 list에 담기
        List<DeliverablesListVO> deliverIdList = deliverablesReasonMapper.getDeliverIdList();
        List<String> notRegiReasonList = new ArrayList<>();

        for(int i=0; i<deliverIdList.size(); i++) {
            // 준필수 파일의 DELIVERABLES_ID만 리스트에 담기
            notRegiReasonList.add(deliverIdList.get(i).getDeliverablesId());
        }

        List<DeliverablesRegVO> result = deliverablesReasonMapper.getFilesInfo(notRegiReasonList, projectId, Pagination.setModalPage(pageNum));

        return result;
    }

	public List<DeliverablesRegVO> getDeliverIdFiles(String deliverId, String projectId) {
		return deliverablesReasonMapper.getDeliverIdFiles(deliverId, projectId);
	}
	
	public List<DeliverablesRegVO> getDeliverIdReason(String deliverId, String projectId) {
		return deliverablesReasonMapper.getDeliverIdReason(deliverId, projectId);
	}

    public Map<String, Object> insertDeliverables (Map<String, Object> deliverablesReasonMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<DeliverablesReasonVO> deliverablesReasonVO = objectMapper.convertValue(deliverablesReasonMap.get(Consts.REQUEST_MESSAGE), objectMapper.getTypeFactory().defaultInstance().constructCollectionType(List.class, DeliverablesReasonVO.class));
        String responseMessage = "";
        for (int i=0; i<deliverablesReasonVO.size(); i++) {
            List<DeliverablesRegVO> result = getDeliverIdFiles(deliverablesReasonVO.get(i).getDeliverablesId(), deliverablesReasonVO.get(i).getProjectId());
            if(result.isEmpty()) {
                responseMessage = insertReason(deliverablesReasonVO.get(i));
            } else {
                responseMessage = updateReason(deliverablesReasonVO.get(i));
            }
            if(!responseMessage.isEmpty()){
                return ResponseMessage.setMessage(Consts.ERROR, responseMessage);
            }
        }
        return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
    }
    
    public String insertReason(DeliverablesReasonVO deliverablesReasonVO) {
        String responseMessage = "";
        // 경로명 받아오기
        List<DeliverablesListVO> list = deliverablesListService.getDeliverId(deliverablesReasonVO.getDeliverablesId());
        String uploadFolderName = list.get(0).getDeliverablesName();
        String tempFilePath = filePath + deliverablesReasonVO.getProjectId() + File.separator + uploadFolderName;

        List<DeliverablesRegVO> deliverIdFilesList = getDeliverIdFiles(deliverablesReasonVO.getDeliverablesId(), deliverablesReasonVO.getProjectId());
        if (deliverIdFilesList.isEmpty()) {
            deliverablesReasonMapper.insertReason(deliverablesReasonVO, tempFilePath);
        }
        else if (!deliverIdFilesList.isEmpty() && deliverIdFilesList.get(0).getContentType().equals("1")) {
            responseMessage = "해당 준파일은 누락 사유를 가지고 있음.";
        } else if (!deliverIdFilesList.isEmpty() && deliverIdFilesList.get(0).getContentType().equals("0")) {
            responseMessage = "해당 준파일은 파일을 가지고 있음.";
        }
        return responseMessage;
    }

    public String updateReason(DeliverablesReasonVO deliverablesReasonVO) {
        String responseMessage = "";
        List<DeliverablesRegVO> list = getDeliverIdFiles(deliverablesReasonVO.getDeliverablesId(), deliverablesReasonVO.getProjectId());
        if (list.isEmpty()) {
            responseMessage = "수정하려는 데이터가 존재하지 않습니다.";
        } else {
        	deliverablesReasonVO.setRegistrationId(list.get(0).getRegistrationId());
            deliverablesReasonMapper.updateReason(deliverablesReasonVO);
        }
        return responseMessage;
    }
}
