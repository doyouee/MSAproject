package com.inzent.deliverablesUI.vo;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트 수정 VO
 * @author 황유진
 */

public class EditProjectVO {
    private String projectId;
    private String hubContractCount;
    private String agentContractCount;
    private String teamId;
    private String customerName;
    private String projectStatus;
    private List<Map<String, Object>> manager;

    public EditProjectVO(){
    }

    public EditProjectVO(String projectId, String hubContractCount, String agentContractCount, String teamId, String customerName, String projectStatus, List<Map<String, Object>> manager) {
        this.projectId = projectId;
        this.hubContractCount = hubContractCount;
        this.agentContractCount = agentContractCount;
        this.teamId = teamId;
        this.customerName = customerName;
        this.projectStatus = projectStatus;
        this.manager = manager;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<Map<String, Object>> getManager() {
        return manager;
    }

    public void setManager(List<Map<String, Object>> manager) {
        this.manager = manager;
    }
}

