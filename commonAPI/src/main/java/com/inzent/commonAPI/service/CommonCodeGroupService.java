package com.inzent.commonAPI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonAPI.mapper.CommonCodeGroupMapper;
import com.inzent.commonAPI.vo.CommonCodeGroupVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 공통 코드 그룹 CRUD 서비스
 * @author 황유진
 */

@Service
public class CommonCodeGroupService {
    @Autowired
    private CommonCodeGroupMapper commonCodeGroupMapper;
    public ObjectMapper objectMapper = new ObjectMapper();

    public List<CommonCodeGroupVO> getCommonCodeGroup(CommonCodeGroupVO commonCodeGroupVO){
        return commonCodeGroupMapper.getCommonCodeGroup(commonCodeGroupVO, Pagination.setPage(commonCodeGroupVO.getPageNum()));
    }

    public int getIdDuplication( Map<String, Object> commonCodeGroupMap){
        CommonCodeGroupVO commonCodeGroupVO = objectMapper.convertValue(commonCodeGroupMap.get(Consts.REQUEST_MESSAGE), CommonCodeGroupVO.class);
        return commonCodeGroupMapper.getIdDuplication(commonCodeGroupVO.getCodeGroupId());
    }
    
    public String getNameDuplication(Map<String, Object> commonCodeGroupMap){
        CommonCodeGroupVO commonCodeGroupVO = objectMapper.convertValue(commonCodeGroupMap.get(Consts.REQUEST_MESSAGE), CommonCodeGroupVO.class);
        return commonCodeGroupMapper.getNameDuplication(commonCodeGroupVO.getCodeGroupName());
    }
    
    public void insertCommonCodeGroup( Map<String, Object> commonCodeGroupMap){
        CommonCodeGroupVO commonCodeGroupVO = objectMapper.convertValue(commonCodeGroupMap.get(Consts.REQUEST_MESSAGE), CommonCodeGroupVO.class);
        commonCodeGroupMapper.insertCommonCodeGroup(commonCodeGroupVO);
    }
    
    public void updateCommonCodeGroup( Map<String, Object> commonCodeGroupMap){
        CommonCodeGroupVO commonCodeGroupVO = objectMapper.convertValue(commonCodeGroupMap.get(Consts.REQUEST_MESSAGE), CommonCodeGroupVO.class);
        commonCodeGroupMapper.updateCommonCodeGroup(commonCodeGroupVO);
    }

    public int  deleteCommonCodeGroup (String codeGroupId){
        return commonCodeGroupMapper.deleteCommonCodeGroup(codeGroupId);
    }


}
