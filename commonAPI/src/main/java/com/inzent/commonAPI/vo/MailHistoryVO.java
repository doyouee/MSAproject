package com.inzent.commonAPI.vo;

/**
 * 메일 이력 정보
 * @author 조유진
 */

public class MailHistoryVO {
	private String mailHistoryId;
	private String mailTitle;
	private String mailReciever;
	private String mailContent;
    private String mailStatus;
    
    public MailHistoryVO(String mailHistoryId, String mailTitle, String mailReciever, String mailContent, String mailStatus) {
    	this.mailHistoryId = mailHistoryId;
    	this.mailTitle = mailTitle;
        this.mailReciever = mailReciever;
        this.mailContent = mailContent;
        this.mailStatus = mailStatus;
    }

	public String getMailHistoryId() {
		return mailHistoryId;
	}

	public void setMailHistoryId(String mailHistoryId) {
		this.mailHistoryId = mailHistoryId;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailReciever() {
		return mailReciever;
	}

	public void setMailReciever(String mailReciever) {
		this.mailReciever = mailReciever;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}
}
