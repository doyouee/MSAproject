package com.inzent.commonAPI.vo;

/**
 * 메일 정보
 * @author 조유진
 */

public class MailInfoVO {
	private String mailInfoId;
	private String mailTitle;
    private String mailTemplate;
    private String subSystem;
    
    public MailInfoVO(String mailInfoId, String mailTitle, String mailTemplate, String subSystem) {
        this.mailInfoId = mailInfoId;
        this.mailTitle = mailTitle;
        this.mailTemplate = mailTemplate;
        this.subSystem = subSystem;
    }

	public String getMailInfoId() {
		return mailInfoId;
	}

	public void setMailInfoId(String mailInfoId) {
		this.mailInfoId = mailInfoId;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	public String getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}
}
