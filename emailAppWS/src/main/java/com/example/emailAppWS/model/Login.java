package com.example.emailAppWS.model;

public class Login {
	private String userName;
	private String userPwd;
	private String emailId;
	private String link;
	private String stagingId;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getStagingId() {
		return stagingId;
	}
	public void setStagingId(String stagingId) {
		this.stagingId = stagingId;
	}
}
