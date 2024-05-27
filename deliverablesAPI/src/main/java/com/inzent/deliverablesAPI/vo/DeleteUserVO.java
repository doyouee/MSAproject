package com.inzent.deliverablesAPI.vo;

/**
 * 삭제자 정보
 * @author 박주한
 */

public class DeleteUserVO {
    private String deleteUserId;
    private String deleteUserName;

    public String getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(String deleteUserId) {
        this.deleteUserId = deleteUserId;
    }

    public String getDeleteUserName() {
        return deleteUserName;
    }

    public void setDeleteUserName(String deleteUserName) {
        this.deleteUserName = deleteUserName;
    }
}
