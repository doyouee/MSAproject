package com.inzent.deliverablesAPI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.service.DeliverablesListService;
import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 산출물 목록 관리 CRUD
 * @author 박주한
 * */

@RestController
public class DeliverablesListController {

	@Autowired
	private DeliverablesListService deliverablesListService;

	@GetMapping(value = "/deliverablesList")
	public Map<String,Object> getDeliverables() throws Exception{
		try {
			return ResponseMessage.setMessage(Consts.SUCCESS, deliverablesListService.getList());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@PostMapping(value = "/deliverablesList")
	public Map<String,Object> insertDeliverables(@RequestBody Map<String, Object> deliverablesMap) {
		try {
			return deliverablesListService.insertList(deliverablesMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@PutMapping(value = "/deliverablesList")
	public Map<String,Object> updateDeliverables(@RequestBody Map<String, Object> deliverablesMap) {
		try {
			return deliverablesListService.updateList(deliverablesMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@DeleteMapping(value = "/deliverablesList")
	public Map<String,Object> deleteDeliverables(@RequestParam(value = "deliverablesId") String deliverablesId){
		try {
			return deliverablesListService.deleteList(deliverablesId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}
}
