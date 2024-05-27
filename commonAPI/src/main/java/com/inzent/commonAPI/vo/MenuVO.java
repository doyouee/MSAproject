package com.inzent.commonAPI.vo;

/**
 * 메뉴 정보
 * @author 박민희
 */

public class MenuVO {
    private String menuId;
    private String menuName;
    private String topMenuId;
    private String subSystem;
    private String mappingUrl;
    private String icon;

    public MenuVO(String menuId, String menuName, String topMenuId, String subSystem, String mappingUrl, String icon) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.topMenuId = topMenuId;
        this.subSystem = subSystem;
        this.mappingUrl = mappingUrl;
        this.icon = icon;
    }

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getTopMenuId() {
		return topMenuId;
	}

	public void setTopMenuId(String topMenuId) {
		this.topMenuId = topMenuId;
	}

	public String getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}

	public String getMappingUrl() {
		return mappingUrl;
	}

	public void setMappingUrl(String mappingUrl) {
		this.mappingUrl = mappingUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}

