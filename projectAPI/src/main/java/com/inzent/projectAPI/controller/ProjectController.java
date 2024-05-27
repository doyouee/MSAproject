package com.inzent.projectAPI.controller;

import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.commonMethod.common.Consts;

import com.inzent.projectAPI.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inzent.projectAPI.service.ProjectService;

import java.util.Map;

/**
 * 프로젝트 관리
 * 프로젝트 CRUD 및 사용자 CU @author 박민희
 * 검수상태 U @author 이도영
 */
@RestController
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	@GetMapping("/project")
	public @ResponseBody Map<String, Object> getProject( @RequestHeader(value="TOKEN_VALUE") String tokenValue ,
														 @RequestParam(value="pageNum", required = false) Integer pageNum ,
														 @RequestParam(value="projectId", required = false) String projectId,
														 @RequestParam(value="projectCode", required = false) String projectCode,
														 @RequestParam(value="projectName", required = false) String projectName,
														 @RequestParam(value="teamName", required = false) String teamName,
														 @RequestParam(value="inspectionStatus", required = false) String inspectionStatus,
														 @RequestParam(value="teamId", required = false) String teamId,
														 @RequestParam(value="userEmail", required = false) String userEmail,
														 @RequestParam(value="managerEmail", required = false) String managerEmail
														 ) {
		String name = "";
		if (projectName != null && projectName != "" ){
			name =projectName;
		}
		try{
			return ResponseMessage.setMessage(Consts.SUCCESS, projectService.getProject(tokenValue, projectId, projectCode, name, teamName, inspectionStatus, teamId, userEmail,managerEmail, pageNum));
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@PostMapping("/project")
	public @ResponseBody Map<String,Object> insertProject(@RequestBody Map<String, Object> addProjectMap) {
		try{
			Map<String,Object> insertResult = projectService.insertProject(addProjectMap);
			if(insertResult.get("message").equals("SUCCESS")) {
				insertResult.put("message", Consts.NO_ERROR);
				return ResponseMessage.setMessage(Consts.SUCCESS, insertResult);
			} else {
				return ResponseMessage.setMessage(Consts.ERROR, insertResult);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());

		}
	}

	@PutMapping("/project")
	public @ResponseBody Map<String,Object> updateProject(@RequestBody Map<String, Object> editProjectMap) {
		try{
			projectService.updateProject(editProjectMap);
			return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@DeleteMapping("/project")
	public @ResponseBody Map<String,Object> deleteProject(@RequestParam String projectId) {
		try{
			projectService.deleteProject(projectId);
			return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@GetMapping("/projectUser")
	public @ResponseBody Map<String,Object> getProjectUser(@RequestParam(value="projectId", required = false) String projectId, @RequestParam(value="userEmail", required = false) String userEmail) {
		try{
			return ResponseMessage.setMessage(Consts.SUCCESS, projectService.getProjectUser(projectId, userEmail));
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@PutMapping("/projectUser")
	public @ResponseBody Map<String,Object> updateProjectUser(@RequestBody Map<String, Object> userMap) {
		try{
			projectService.updateProjectUser(userMap);
			return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

	@PutMapping("/inspection")
	public @ResponseBody Map<String,Object> updateInspection(@RequestBody Map<String, Object> inspectionMap) {
		try{
			projectService.updateInspection(inspectionMap);
			return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString());
		}
	}

}
