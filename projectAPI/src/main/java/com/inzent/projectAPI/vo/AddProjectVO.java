package com.inzent.projectAPI.vo;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트 추가 VO
 * @author 박민희
 */

public class AddProjectVO {
    private String projectId;
    private String deleteYn;
    private String projectName;
    private String customerName;
    private String projectCode;
    private String hubContractCount;
    private String agentContractCount;
    private String projectStatus;
    private String inspectionStatus;
    private String teamId;
    private List<Map<String, Object>> manager;

    public AddProjectVO(){}

    public AddProjectVO(String projectId, String deleteYn, String projectName, String customerName, String projectCode, String hubContractCount, String agentContractCount, String projectStatus, String inspectionStatus, String teamId, List<Map<String, Object>>  manager) {
        this.projectId = projectId;
        this.deleteYn = deleteYn;
        this.projectName = projectName;
        this.customerName = customerName;
        this.projectCode = projectCode;
        this.hubContractCount = hubContractCount;
        this.agentContractCount = agentContractCount;
        this.projectStatus = projectStatus;
        this.inspectionStatus = inspectionStatus;
        this.teamId = teamId;
        this.manager = manager;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<Map<String, Object>> getManager() {
        return manager;
    }

    public void setManager(List<Map<String, Object>> manager) {
        this.manager = manager;
    }
}

