package com.inzent.commonAPI.controller;


import com.inzent.commonAPI.service.CommonCodeGroupService;
import com.inzent.commonAPI.vo.CommonCodeGroupVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 공통 코드 그룹 관리 CRUD 
 * @author 황유진
 */

@RestController
public class CommonCodeGroupController {
	@Autowired
	private CommonCodeGroupService commonCodeGroupService;
	
	@GetMapping("/commonCodeGroup")
	public @ResponseBody Map<String,Object> getCommonCodeGroup(@RequestParam(value="pageNum", required = false) Integer pageNum , @RequestParam(value="codeGroupName", required = false)String codeGroupName) {
		try {
			CommonCodeGroupVO commonCodeGroupVO = new CommonCodeGroupVO();
			if(pageNum != null  ){
				commonCodeGroupVO.setPageNum(pageNum);
			}if( codeGroupName != null ){
				commonCodeGroupVO.setCodeGroupName(codeGroupName);
			}
			return ResponseMessage.setMessage(Consts.SUCCESS, commonCodeGroupService.getCommonCodeGroup(commonCodeGroupVO));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }
    }
	
	@PostMapping("/commonCodeGroup")
	public @ResponseBody Map<String,Object> insertCommonCodeGroup (@RequestBody Map<String, Object> commonCodeGroupMap) {

		int idDup = commonCodeGroupService.getIdDuplication(commonCodeGroupMap);
		String nameDup = commonCodeGroupService.getNameDuplication(commonCodeGroupMap);
		try {
			if(idDup>0 && nameDup != null){
				return ResponseMessage.setMessage(Consts.ERROR,"ALL");
			}else if(idDup>0){
				return  ResponseMessage.setMessage(Consts.ERROR,"ID");
			}else if (nameDup != null) {
				return  ResponseMessage.setMessage(Consts.ERROR,"NAME");
			} else {
				commonCodeGroupService.insertCommonCodeGroup(commonCodeGroupMap);
				return ResponseMessage.setMessage(Consts.SUCCESS ,Consts.NO_ERROR) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }


    }
	
	@PutMapping("/commonCodeGroup")
	public @ResponseBody Map<String,Object> updateCommonCodeGroup (@RequestBody Map<String, Object> commonCodeGroupMap) {
		Map<String, Object> data = (Map<String, Object>) commonCodeGroupMap.get(Consts.REQUEST_MESSAGE);
		try {
			if (commonCodeGroupService.getNameDuplication(commonCodeGroupMap)!=null) {
				if(commonCodeGroupService.getNameDuplication(commonCodeGroupMap).equals(data.get("codeGroupId"))){
					commonCodeGroupService.updateCommonCodeGroup(commonCodeGroupMap);
					return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
				}else{
					return  ResponseMessage.setMessage(Consts.ERROR,"그룹명이 중복되었습니다");
				}

			} else {
				commonCodeGroupService.updateCommonCodeGroup(commonCodeGroupMap);
				return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());

        }

	}
	
	@DeleteMapping("/commonCodeGroup")
	public @ResponseBody Map<String,Object> deleteCommonCodeGroup(@RequestParam String codeGroupId) {
		try{
			int result = commonCodeGroupService.deleteCommonCodeGroup(codeGroupId);
			if(result> 0){
				return ResponseMessage.setMessage(Consts.SUCCESS,Consts.NO_ERROR);
			}else{
				return ResponseMessage.setMessage(Consts.ERROR,"사용중인 그룹은 삭제할 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }

	}
}
