package com.inzent.deliverablesUI.vo;

/**
 * 공통 코드 정보
 * @author 황유진
 */

public class CommonCodeVO {
	private String codeId;
    private String codeName;
    private  String codeGroupId;
    private  String codeTopId;
    private  String codeGroupName;
    private  String subSystem;
    private  String codeTopName;
    String pageNum;
    
    public CommonCodeVO() {
	}
    
    public CommonCodeVO(String codeId, String codeName, String codeGroupId, String codeTopId, String codeGroupName, String subSystem, String codeTopName, String pageNum) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.codeGroupId = codeGroupId;
        this.codeTopId = codeTopId;
        this.codeGroupName = codeGroupName;
        this.subSystem = subSystem;
        this.codeTopName = codeTopName;
        this.pageNum = pageNum;

    }

	public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeGroupId() {
        return codeGroupId;
    }

    public void setCodeGroupId(String codeGroupId) {
        this.codeGroupId = codeGroupId;
    }

    public String getCodeTopId() {
        return codeTopId;
    }

    public void setCodeTopId(String codeTopId) {
        this.codeTopId = codeTopId;
    }

    public String getCodeTopName() {
        return codeTopName;
    }

    public void setCodeTopName(String codeTopName) {
        this.codeTopName = codeTopName;
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
