package com.inzent.projectAPI.mapper;

import com.inzent.projectAPI.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper {
	// 프로젝트 조회
	List<ProjectVO> getProject(@Param("projectId") @Nullable String projectId, @Param("projectCode") @Nullable String projectCode, 
			@Param("projectName") @Nullable String projectName, @Param("inspectionStatus") @Nullable String inspectionStatus, 
			@Param("teamId") @Nullable String teamId, @Param("userEmail") @Nullable String userEmail,
			@Param("managerEmail") @Nullable ArrayList<String> managerEmail, @Param("searchTeamId") @Nullable ArrayList<String> searchTeamId, 
			@Param("teamName") @Nullable String teamName, @Param("page") Map<String, Object> page);
	List<Map<String, Object>> getProjectAll();

	// 프로젝트 등록
	void insertProject(AddProjectVO addProjectVO);

	// 프로젝트 수정
	void updateProject(EditProjectVO editProjectVO);

	// 프로젝트 삭제
	void deleteProject(@Param("projectId") String projectId);

	//사용자 조회
	List<ProjectUserVO> getProjectUser(@Param("projectId") @Nullable String projectId, @Param("userEmail") @Nullable String userEmail);

	// 사용자 등록 및 수정
	void insertProjectUser(List<ProjectUserVO> projectUserVO);
	void deleteProjectUser(ProjectUserVO projectUserVO);

	// 프로젝트 검수 상태 수정
	void updateInspection(ProjectInspectionVO projectInspectionVO);

}
