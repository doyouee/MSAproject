package com.inzent.deliverablesAPI.vo;

/**
 * 산출물 등록 정보
 * @author 박주한
 */

public class DeliverablesRegVO {
    private String registrationId;
    private String projectId;
    private String deliverablesId;
    private String contentType;
    private String deliverablesTitle;
    private String fileName;
    private String filePath;
    private String textContents;
    private String notRegistrationReason;
    private String registrationUserEmail;
    private String registrationUserName;
    private String registrationDate;
    private String deleteUserEmail;
    private String deleteUserName;
    private String deleteDate;

    public DeliverablesRegVO(String registrationId, String projectId, String deliverablesId, String contentType, String deliverablesTitle, String fileName, String filePath, String textContents, String notRegistrationReason, String registrationUserEmail, String registrationUserName, String registrationDate, String deleteUserEmail, String deleteUserName, String deleteDate) {
        this.registrationId = registrationId;
        this.projectId = projectId;
        this.deliverablesId = deliverablesId;
        this.contentType = contentType;
        this.deliverablesTitle = deliverablesTitle;
        this.fileName = fileName;
        this.filePath = filePath;
        this.textContents = textContents;
        this.notRegistrationReason = notRegistrationReason;
        this.registrationUserEmail = registrationUserEmail;
        this.registrationUserName = registrationUserName;
        this.registrationDate = registrationDate;
        this.deleteUserEmail = deleteUserEmail;
        this.deleteUserName = deleteUserName;
        this.deleteDate = deleteDate;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDeliverablesId() {
        return deliverablesId;
    }

    public void setDeliverablesId(String deliverablesId) {
        this.deliverablesId = deliverablesId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDeliverablesTitle() {
        return deliverablesTitle;
    }

    public void setDeliverablesTitle(String deliverablesTitle) {
        this.deliverablesTitle = deliverablesTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTextContents() {
        return textContents;
    }

    public void setTextContents(String textContents) {
        this.textContents = textContents;
    }

    public String getNotRegistrationReason() {
        return notRegistrationReason;
    }

    public void setNotRegistrationReason(String notRegistrationReason) {
        this.notRegistrationReason = notRegistrationReason;
    }

    public String getRegistrationUserEmail() {
        return registrationUserEmail;
    }

    public void setRegistrationUserEmail(String registrationUserEmail) {
        this.registrationUserEmail = registrationUserEmail;
    }

    public String getRegistrationUserName() {
        return registrationUserName;
    }

    public void setRegistrationUserName(String registrationUserName) {
        this.registrationUserName = registrationUserName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getDeleteUserEmail() {
        return deleteUserEmail;
    }

    public void setDeleteUserEmail(String deleteUserEmail) {
        this.deleteUserEmail = deleteUserEmail;
    }

    public String getDeleteUserName() {
        return deleteUserName;
    }

    public void setDeleteUserName(String deleteUserName) {
        this.deleteUserName = deleteUserName;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        return "DeliverablesRegVO{" +
                "registrationId='" + registrationId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", deliverablesId='" + deliverablesId + '\'' +
                ", contentType=" + contentType +
                ", deliverablesTitle='" + deliverablesTitle + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", textContents='" + textContents + '\'' +
                ", notRegistrationReason='" + notRegistrationReason + '\'' +
                ", registrationUserEmail='" + registrationUserEmail + '\'' +
                ", registrationUserName='" + registrationUserName + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", deleteUserEmail='" + deleteUserEmail + '\'' +
                ", deleteUserName='" + deleteUserName + '\'' +
                ", deleteDate='" + deleteDate + '\'' +
                '}';
    }
}
