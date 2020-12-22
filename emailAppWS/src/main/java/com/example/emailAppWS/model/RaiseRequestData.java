package com.example.emailAppWS.model;

import java.util.List;

public class RaiseRequestData {
	private List<Software> swList;
	private List<UserRole> urList;
	private String userId;
	
	public List<Software> getSwList() {
		return swList;
	}
	public void setSwList(List<Software> swList) {
		this.swList = swList;
	}
	public List<UserRole> getUrList() {
		return urList;
	}
	public void setUrList(List<UserRole> urList) {
		this.urList = urList;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
