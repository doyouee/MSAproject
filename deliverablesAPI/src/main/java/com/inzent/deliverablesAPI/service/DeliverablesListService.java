package com.inzent.deliverablesAPI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.mapper.DeliverablesListMapper;
import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 산출물 목록 CRUD 서비스 
 * @author 박주한
 * */

@Service
public class DeliverablesListService {
    @Autowired
    private DeliverablesListMapper deliverablesListMapper;
    public ObjectMapper objectMapper = new ObjectMapper();

    public List<DeliverablesListVO> getList() {
        return deliverablesListMapper.getList();
    }

    public List<DeliverablesListVO> getDeliverId(String deliverId) {
        return deliverablesListMapper.getDeliverId(deliverId);
    }

    public List<DeliverablesListVO> getPathInfo(String deliverId) {
        return deliverablesListMapper.getPathInfo(deliverId);
    }

    public Map<String,Object> insertList(Map<String, Object> deliverablesMap) throws Exception {
        DeliverablesListVO deliverablesListVO = objectMapper.convertValue(deliverablesMap.get(Consts.REQUEST_MESSAGE), DeliverablesListVO.class);
        List<DeliverablesListVO> result = deliverablesListMapper.getList();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getDeliverablesName().equals(deliverablesListVO.getDeliverablesName())
                    && result.get(i).getDeliverablesTopId().equals(deliverablesListVO.getDeliverablesTopId())
                    && result.get(i).getFileGubun() == deliverablesListVO.getFileGubun()) {
                return ResponseMessage.setMessage(Consts.ERROR, "중복되는 폴더 명이 존재합니다.");
            }
        }
        deliverablesListMapper.insertList(deliverablesListVO);
        return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
    }

    public Map<String,Object> updateList(Map<String, Object> deliverablesMap) throws Exception {
        DeliverablesListVO deliverablesListVO = objectMapper.convertValue(deliverablesMap.get(Consts.REQUEST_MESSAGE), DeliverablesListVO.class);
        List<DeliverablesListVO> list = deliverablesListMapper.getDeliverId(deliverablesListVO.getDeliverablesId());
        if (list.isEmpty()) {
            return ResponseMessage.setMessage(Consts.ERROR, "수정하려는 데이터가 존재하지 않습니다.");
        } else {
            List<DeliverablesListVO> result = deliverablesListMapper.getList();

            for(int i=0; i<result.size(); i++) {
                if (deliverablesListVO.getDeliverablesTopId()==null || deliverablesListVO.getDeliverablesTopId()=="") {
                    if (result.get(i).getDeliverablesName().equals(deliverablesListVO.getDeliverablesName())
                            && result.get(i).getFileGubun() == deliverablesListVO.getFileGubun()
                            && result.get(i).getRequiredItems() == deliverablesListVO.getRequiredItems()
                            && result.get(i).getDeliverablesTopId() == null) {
                        return ResponseMessage.setMessage(Consts.ERROR, "중복되는 폴더 명이 존재합니다.");
                    }
                } else {
                    if (result.get(i).getDeliverablesName().equals(deliverablesListVO.getDeliverablesName())
                            && result.get(i).getFileGubun() == deliverablesListVO.getFileGubun()
                            && result.get(i).getRequiredItems() == deliverablesListVO.getRequiredItems()
                            && result.get(i).getDeliverablesTopId().equals(deliverablesListVO.getDeliverablesTopId())) {
                        return ResponseMessage.setMessage(Consts.ERROR, "중복되는 폴더 명이 존재합니다.");
                    }
                }
                deliverablesListMapper.updateList(deliverablesListVO);
            }
        }
        return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
    }

    public Map<String,Object> deleteList(String id) {
        List<DeliverablesListVO> list = deliverablesListMapper.getDeliverId(id);

        if (list.isEmpty()) {
            return ResponseMessage.setMessage(Consts.ERROR, "삭제하려는 데이터가 존재하지 않습니다.");
        } else {
            if(deliverablesListMapper.getUnderInfo(id).isEmpty()) {
                deliverablesListMapper.deleteList(id);
            } else {
                return ResponseMessage.setMessage(Consts.ERROR, "하위 디렉터리 또는 파일이 존재합니다.");
            }
        }

        return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
    }

}
