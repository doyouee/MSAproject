package com.inzent.deliverablesUI.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * 사용자 정보 VO
 * @author 장윤하
 */

@JsonIgnoreProperties({"login","preAuthorityId"})
public class UserVO {
    private String userPassword;
    private String userName;
    private String userEmail;
    private String teamId;
    private String authorityId;
    private String deleteYn;
    private String authorityName;
    private String codeName;
    private String newPassword;

    private String login;

    private  String preAuthorityId;
    public UserVO() {

    }

    public UserVO(String userPassword, String userName, String userEmail, String teamId, String authorityId, String deleteYn, String authorityName, String codeName, String newPassword, String login, String preAuthorityId) {
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.teamId = teamId;
        this.authorityId = authorityId;
        this.deleteYn = deleteYn;
        this.authorityName = authorityName;
        this.codeName = codeName;
        this.newPassword = newPassword;
        this.login = login;
        this.preAuthorityId = preAuthorityId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPreAuthorityId() {
        return preAuthorityId;
    }

    public void setPreAuthorityId(String preAuthorityId) {
        this.preAuthorityId = preAuthorityId;
    }
}