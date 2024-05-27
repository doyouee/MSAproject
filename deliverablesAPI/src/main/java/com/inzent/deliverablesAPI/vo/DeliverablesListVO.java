package com.inzent.deliverablesAPI.vo;

/**
 * 산출물 목록 정보
 * @author 박주한
 */

public class DeliverablesListVO {
    private String deliverablesId;
    private String deliverablesName;
    private String deliverablesTopId;
    private char requiredItems;
    private char fileGubun;
    private String description;
    private char deleteYn;

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

    public String getDeliverablesTopId() {
        return deliverablesTopId;
    }

    public void setDeliverablesTopId(String deliverablesTopId) {
        this.deliverablesTopId = deliverablesTopId;
    }

    public char getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(char requiredItems) {
        this.requiredItems = requiredItems;
    }

    public char getFileGubun() {
        return fileGubun;
    }

    public void setFileGubun(char fileGubun) {
        this.fileGubun = fileGubun;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(char deleteYn) {
        this.deleteYn = deleteYn;
    }
}
