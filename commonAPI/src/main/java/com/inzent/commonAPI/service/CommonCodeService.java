package com.inzent.commonAPI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonAPI.mapper.CommonCodeMapper;
import com.inzent.commonAPI.vo.CommonCodeVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 공통 코드 CRUD 서비스
 * @author 황유진
 */

@Service
public class CommonCodeService {
    @Autowired
    private CommonCodeMapper commonCodeMapper;

    public ObjectMapper objectMapper = new ObjectMapper();

    public List<CommonCodeVO> getCommonCode(CommonCodeVO commonCodeVO){
        return commonCodeMapper.getCommonCode(commonCodeVO,Pagination.setPage(commonCodeVO.getPageNum()));
    }

    public List<CommonCodeVO> getCODECommonCode(ArrayList<String> codeId){
        return commonCodeMapper.getCODECommonCode(codeId);
    }

    public List<CommonCodeVO> getONECommonCode(String codeId){
        return  commonCodeMapper.getONECommonCode(codeId);
    }

    public List<CommonCodeVO> getTOPCommonCode(){

        return  commonCodeMapper.getTOPCommonCode();
    }

    public String getIdDuplication(Map<String, Object> commonCodeMap){
        CommonCodeVO commonCodeVO = objectMapper.convertValue(commonCodeMap.get(Consts.REQUEST_MESSAGE), CommonCodeVO.class);
        return commonCodeMapper.getIdDuplication(commonCodeVO);
    }
    public void insertCommonCode(Map<String, Object> commonCodeMap){
        CommonCodeVO commonCodeVO = objectMapper.convertValue(commonCodeMap.get(Consts.REQUEST_MESSAGE), CommonCodeVO.class);
        commonCodeMapper.insertCommonCode(commonCodeVO);
    }
    
    public void updateCommonCode(Map<String, Object> commonCodeMap){
        CommonCodeVO commonCodeVO = objectMapper.convertValue(commonCodeMap.get(Consts.REQUEST_MESSAGE), CommonCodeVO.class);
        commonCodeMapper.updateCommonCode(commonCodeVO);
    }

    public int  deleteCommonCode (String codeId){
        return commonCodeMapper.deleteCommonCode(codeId);
    }
}
