package com.inzent.projectAPI.vo;

import java.util.ArrayList;

/**
 * 프로젝트 VO
 * @author 박민희, 이도영
 */


public class ProjectVO {
    private String projectId;
    private String projectCode;
    private String projectName;
    private String hubContractCount;
    private String agentContractCount;
    private String deleteYn;
    private String customerName;
    private String teamId;
    private String projectStatus;
    private String inspectionStatus;
    private String inspectionDate;
    private String inspectionUserEmail;
    private String inspectionUserName;
    private String inspectionRejectionReason;

    private String manager;


    public ProjectVO(String projectId, String projectCode, String projectName, String hubContractCount, String agentContractCount, String deleteYn, String customerName, String teamId, String projectStatus, String inspectionStatus, String inspectionDate, String inspectionUserEmail, String inspectionUserName, String inspectionRejectionReason, String manager) {
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.hubContractCount = hubContractCount;
        this.agentContractCount = agentContractCount;
        this.deleteYn = deleteYn;
        this.customerName = customerName;
        this.teamId = teamId;
        this.projectStatus = projectStatus;
        this.inspectionStatus = inspectionStatus;
        this.inspectionDate = inspectionDate;
        this.inspectionUserEmail = inspectionUserEmail;
        this.inspectionUserName = inspectionUserName;
        this.inspectionRejectionReason = inspectionRejectionReason;
        this.manager = manager;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getHubContractCount() {
        return hubContractCount;
    }

    public void setHubContractCount(String hubContractCount) {
        this.hubContractCount = hubContractCount;
    }

    public String getAgentContractCount() {
        return agentContractCount;
    }

    public void setAgentContractCount(String agentContractCount) {
        this.agentContractCount = agentContractCount;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }


}
