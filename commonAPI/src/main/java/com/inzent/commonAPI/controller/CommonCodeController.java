package com.inzent.commonAPI.controller;


import com.inzent.commonAPI.service.CommonCodeService;

import com.inzent.commonAPI.vo.CommonCodeVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * 공통 코드 관리 CRUD 
 * @author 황유진
 */

@RestController
public class CommonCodeController {
	@Autowired
	private CommonCodeService commonCodeService;

	@GetMapping("/commonCode")
	public @ResponseBody Map<String,Object> getCommonCode(@RequestParam String type ,
														  @RequestParam(required = false)  String codeId,@RequestParam(value="pageNum", required = false) Integer pageNum, @RequestParam(value="codeName", required = false)String codeName) {
		/*
		 *  type 지정 
		 *  - all : 공통코드 그룹을 포함한 전체 공통코드 조회
		 *  - code_id : 공통코드ID와 동일한 공통 그룹을 가지고 있는 모든 리스트 출력
		 *  - top : 상위공통코드만 조회
		 *  - one : 하나의 공통코드 조회
		 *  */
		try{
			if("ALL".equalsIgnoreCase(type)){
				CommonCodeVO commonCodeVO = new CommonCodeVO();
				if(pageNum != null){
					commonCodeVO.setPageNum(pageNum);
				}if( codeName != null ){
					commonCodeVO.setCodeName(codeName);
				}

				return 	ResponseMessage.setMessage(Consts.SUCCESS,commonCodeService.getCommonCode(commonCodeVO));
			}else if("CODE_ID".equalsIgnoreCase(type)){
				String[] codeArr ={};
				ArrayList<String> code = new ArrayList<String>();
				codeArr = codeId.split(",");
				for (String i :codeArr){
					code.add(i);
				}
				return ResponseMessage.setMessage(Consts.SUCCESS,commonCodeService.getCODECommonCode(code));
			}else if("TOP".equalsIgnoreCase(type)){
				return ResponseMessage.setMessage(Consts.SUCCESS,commonCodeService.getTOPCommonCode());
			}else if("ONE".equalsIgnoreCase(type)){
				return ResponseMessage.setMessage(Consts.SUCCESS,commonCodeService.getONECommonCode(codeId));
			}
			return  ResponseMessage.setMessage(Consts.ERROR,"type을 지정해주세요");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }
	}
	
	@PostMapping("/commonCode")
	public @ResponseBody Map<String,Object> insertCommonCode(@RequestBody  Map<String, Object> commonCodeMap) {
		try {
			if(commonCodeService.getIdDuplication(commonCodeMap)!= null){
				return ResponseMessage.setMessage(Consts.ERROR,"그룹이 중복되었습니다.");
			}else{
				commonCodeService.insertCommonCode(commonCodeMap);
				return ResponseMessage.setMessage(Consts.SUCCESS,Consts.NO_ERROR);

			}
		} catch (Exception e) {
			 e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }
	}
	
	@PutMapping("/commonCode")
	public @ResponseBody Map<String,Object> updateCommonCode(@RequestBody Map<String, Object> commonCodeMap) {
		Map<String, Object> data = (Map<String, Object>) commonCodeMap.get(Consts.REQUEST_MESSAGE);
		try {
			if(commonCodeService.getIdDuplication(commonCodeMap)!=null){
				if(commonCodeService.getIdDuplication(commonCodeMap).equals(data.get("codeId"))){
					commonCodeService.updateCommonCode(commonCodeMap);
					return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
				}else{
					return ResponseMessage.setMessage(Consts.ERROR,"그룹이 중복되었습니다.");
				}
			}else {
				commonCodeService.updateCommonCode(commonCodeMap);
				return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }

	}
	
	@DeleteMapping("/commonCode")
	public @ResponseBody Map<String,Object> deleteCommonCode(@RequestParam String codeId) {
		try{
			int result = commonCodeService.deleteCommonCode(codeId);
			if(result> 0){
				return ResponseMessage.setMessage(Consts.SUCCESS,Consts.NO_ERROR);
			}else{
				return  ResponseMessage.setMessage(Consts.ERROR,"사용중인 상위공통코드는 삭제할 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR,e.toString());
        }
	}
}
