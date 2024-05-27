package com.inzent.projectAPI.vo;

/**
 * 프로젝트 사용자 VO
 * @author 박민희
 */

public class ProjectUserVO {
    private String projectId;
    private String userEmail;
    private String userName;

    public ProjectUserVO(){}
    public ProjectUserVO(String projectId, String userEmail, String userName) {
        this.projectId = projectId;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
