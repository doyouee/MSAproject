package com.inzent.deliverablesAPI.vo;

import java.util.ArrayList;

/**
 * 산출물 사유 정보
 * @author 박주한
 */

public class DeliverablesReasonVO {
    private String registrationId;
    private String projectId;
    private String deliverablesId;
    private String deliverablesName;
    private String requiredItems;
    private String contentType;
    private String description;
    private String filePath;
    private String deliverablesTitle;
    private String notRegistrationReason;
    private String registrationUserEmail;
    private String registrationUserName;
    private String registrationDate;
    private String deleteUserEmail;
    private String deleteUserName;
    private String deleteDate;


    public DeliverablesReasonVO(){}

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

    public String getDeliverablesName() {
        return deliverablesName;
    }

    public void setDeliverablesName(String deliverablesName) {
        this.deliverablesName = deliverablesName;
    }

    public String getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(String requiredItems) {
        this.requiredItems = requiredItems;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDeliverablesTitle() {
        return deliverablesTitle;
    }

    public void setDeliverablesTitle(String deliverablesTitle) {
        this.deliverablesTitle = deliverablesTitle;
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
}
