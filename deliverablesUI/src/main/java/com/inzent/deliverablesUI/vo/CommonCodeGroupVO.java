package com.inzent.deliverablesUI.vo;

/**
 * 공통 코드 그룹 정보
 * @author 황유진
 */

public class CommonCodeGroupVO {
    private  String codeGroupId;
    private  String codeGroupName;
    private  String subSystem;
    String pageNum;

    public CommonCodeGroupVO() {
	}

    public CommonCodeGroupVO(String codeGroupId, String codeGroupName, String subSystem, String pageNum) {
        this.codeGroupId = codeGroupId;
        this.codeGroupName = codeGroupName;
        this.subSystem = subSystem;
        this.pageNum = pageNum;

    }

	public String getCodeGroupId() {
        return codeGroupId;
    }

    public void setCodeGroupId(String codeGroupId) {
        this.codeGroupId = codeGroupId;
    }

    public String getCodeGroupName() {
        return codeGroupName;
    }

    public void setCodeGroupName(String codeGroupName) {
        this.codeGroupName = codeGroupName;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}