package com.example.emailAppWS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@ComponentScan("com.example.*")
@SpringBootApplication(scanBasePackages={
		"com.example.emailAppWS.service.LoginService"})
public class EmailAppWsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EmailAppWsApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(
				EmailAppWsApplication.class);
		        sa.run(args);
		//SpringApplication.run(EmailAppWsApplication.class, args);
	}

}
