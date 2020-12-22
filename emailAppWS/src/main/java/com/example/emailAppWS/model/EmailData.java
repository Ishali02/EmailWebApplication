package com.example.emailAppWS.model;

public class EmailData {

	private String recepient;
	private String subject;
	private String message;
	private String link;
	private String stagingId;
	public String getRecepient() {
		return recepient;
	}
	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
