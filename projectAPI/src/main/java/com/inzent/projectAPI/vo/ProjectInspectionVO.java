package com.inzent.projectAPI.vo;

import java.util.ArrayList;

/**
 * 프로젝트 검수 상태 수정 VO
 * @author 이도영
 */

public class ProjectInspectionVO {
    public String projectId;
    public String inspectionStatus;
    public String inspectionDate;
    private String inspectionUserEmail;
    private String inspectionUserName;
    private String inspectionRejectionReason;
    private ArrayList<String> projectIdList;

    public ProjectInspectionVO(){}
    public ProjectInspectionVO(String projectId, String inspectionStatus, String inspectionDate, String inspectionUserEmail, String inspectionUserName, String inspectionRejectionReason, ArrayList<String> projectIdList) {
        this.projectId = projectId;
        this.inspectionStatus = inspectionStatus;
        this.inspectionDate = inspectionDate;
        this.inspectionUserEmail = inspectionUserEmail;
        this.inspectionUserName = inspectionUserName;
        this.inspectionRejectionReason = inspectionRejectionReason;
        this.projectIdList = projectIdList;
    }

    public ArrayList<String> getProjectIdList() {
		return projectIdList;
	}
    
	public void setProjectIdList(ArrayList<String> projectIdList) {
		this.projectIdList = projectIdList;
	}
	
	public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionUserEmail() {
        return inspectionUserEmail;
    }

    public void setInspectionUserEmail(String inspectionUserEmail) {
        this.inspectionUserEmail = inspectionUserEmail;
    }

    public String getInspectionUserName() {
        return inspectionUserName;
    }

    public void setInspectionUserName(String inspectionUserName) {
        this.inspectionUserName = inspectionUserName;
    }

    public String getInspectionRejectionReason() {
        return inspectionRejectionReason;
    }

    public void setInspectionRejectionReason(String inspectionRejectionReason) {
        this.inspectionRejectionReason = inspectionRejectionReason;
    }
}
