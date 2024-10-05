package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeanCheckController {

	@Autowired
	private ApplicationContext applicationContext;

	@GetMapping("/checkBean")
	public String checkBean() {
		return applicationContext.containsBean("mailSender") ? "Bean is available" : "Bean is not available";
	}
}