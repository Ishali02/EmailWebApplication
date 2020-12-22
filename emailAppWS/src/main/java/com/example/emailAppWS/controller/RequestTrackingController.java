package com.example.emailAppWS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailAppWS.model.Login;
import com.example.emailAppWS.model.RequestTracking;
import com.example.emailAppWS.model.ResponseData;
import com.example.emailAppWS.model.UserRole;
import com.example.emailAppWS.service.LoginService;
import com.example.emailAppWS.service.RequestTrackingService;

@RestController
@RequestMapping("/request")
public class RequestTrackingController {

	@Autowired
	private RequestTrackingService requestTrackingService;
	
	@RequestMapping(value="/userRequestSubmit",method =RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseData userRequestSubmit(RequestTracking data)
	{
		return requestTrackingService.userRequestSubmit(data);
	}
	
	@RequestMapping(value="/sendApprovalMail/{requestId}",method =RequestMethod.GET)
	public ResponseData sendApprovalMail(@PathVariable(value="requestId") String requestId)
	{
		return requestTrackingService.sendApprovalMail(requestId);
	}
	
	@RequestMapping(value="/requestTrack/{userId}",method =RequestMethod.GET)
	public List<RequestTracking> requestTrack(@PathVariable(value="userId") String userId)
	{
		return requestTrackingService.requestTrack(userId);
	}
}
