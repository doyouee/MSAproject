package com.inzent.deliverablesAPI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.service.DeliverablesReasonService;
import com.inzent.deliverablesAPI.vo.DeliverablesReasonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 산출물 사유 관리 CRUD
 * @author 박주한
 * */

@RestController
public class DeliverablesReasonController {

    @Autowired
    private DeliverablesReasonService deliverablesReasonService;

    @GetMapping(value = "/deliverablesReason")
    public Map<String,Object> getDeliverables(@RequestParam(value = "projectId") String projectId, @RequestParam(value="pageNum", required = false) Integer pageNum) {
        try {
            return ResponseMessage.setMessage(Consts.SUCCESS, deliverablesReasonService.getNotRegiReason(projectId, pageNum));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
        }
    }

    @PostMapping(value = "/deliverablesReason")
    public Map<String,Object> insertDeliverables(@RequestBody Map<String, Object> deliverablesReasonMap) {
        try {
            return deliverablesReasonService.insertDeliverables(deliverablesReasonMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
        }
    }
}
