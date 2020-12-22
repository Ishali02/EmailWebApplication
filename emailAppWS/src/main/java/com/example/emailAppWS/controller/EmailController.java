package com.example.emailAppWS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailAppWS.model.EmailData;
import com.example.emailAppWS.service.EmailService;

@RestController
@RequestMapping("/Email")
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value="/sendEmail",method =RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String sendEmail(EmailData data)
	{
		return emailService.sendEmail(data);
	}
}
