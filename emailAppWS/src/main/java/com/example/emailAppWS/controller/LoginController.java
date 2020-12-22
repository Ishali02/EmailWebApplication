package com.example.emailAppWS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Component;
import com.example.emailAppWS.model.Login;
import com.example.emailAppWS.model.RaiseRequestData;
import com.example.emailAppWS.model.ResponseData;
import com.example.emailAppWS.service.LoginService;
import com.example.emailAppWS.model.UserRole;

//@Component
@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="/loginAuth",method =RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public RaiseRequestData loginAuth(Login data)
	{
		return loginService.loginAuth(data);
	}
	
	@RequestMapping(value="/signUpAuth",method =RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseData signUpAuth(Login data)
	{
		return loginService.signUpAuth(data);
	}

	@RequestMapping(value="/signUpComplete/{stagingID}",method =RequestMethod.GET)
	public ResponseData signUpComplete(@PathVariable(value="stagingID") String stagingID)
	{
		return loginService.signUpComplete(stagingID);
	}
}
