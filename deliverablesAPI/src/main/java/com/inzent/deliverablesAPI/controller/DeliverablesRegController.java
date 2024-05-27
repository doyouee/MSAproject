package com.inzent.deliverablesAPI.controller;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.service.DeliverablesRegService;
import com.inzent.deliverablesAPI.vo.DeliverablesRegVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 산출물 등록 관리 CRUD @author 박주한
 * zip 다운로드 @author 이도영
 * */
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class DeliverablesRegController {

    @Autowired
    private DeliverablesRegService deliverablesRegService;

    @GetMapping(value = "/deliverablesReg")
    public Map<String, Object> getDeliverables(@RequestParam(required = false, value = "deliverablesId") String deliverablesId, @RequestParam(required = false, value = "projectId") String projectId) {
        try {
            return deliverablesRegService.getReg(deliverablesId, projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
        }
    }

    @PostMapping(value = "/deliverablesReg", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, Object> insertDeliverables(@RequestPart(required = false, value = "fileList") List<MultipartFile> fileList, @RequestPart(required = false, value = "registrationInfo") DeliverablesRegVO deliverablesRegVO) {
        try {
            return deliverablesRegService.insertReg(fileList, deliverablesRegVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
        }
    }

    @DeleteMapping(value = "/deliverablesReg")
    public Map<String, Object> deleteDeliverables(@RequestParam(value = "registrationId") String registrationId, @RequestParam(value = "deleteUserEmail") String deleteUserEmail, @RequestParam(value = "deleteUserName") String deleteUserName){

        try{
            // DB 내 삭제 정보 업데이트
            deliverablesRegService.deleteReg(registrationId, deleteUserEmail, deleteUserName);
            // 서버의 파일 삭제
            deliverablesRegService.deleteFileInServer(registrationId);

            return ResponseMessage.setMessage(Consts.SUCCESS,Consts.NO_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
        }
    }

    @GetMapping("/deliverablesCreateFile")
    public Map<String, Object> getDeliverablesCreateFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String projectId, @RequestParam String projectName) {
    	return deliverablesRegService.getDeliverablesCreateFile(request, response, projectId, projectName);
    }

    @GetMapping("/deliverablesDownload")
    public ResponseEntity<InputStreamResource> getProjectDeliverablesDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam String projectId, @RequestParam String projectName) {
        return deliverablesRegService.getProjectDeliverablesDownload(request, response, projectId, projectName);
    }
}
