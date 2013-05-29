package com.apress.prospringmvc.bookstore.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the homepage
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
@Controller
public class MainController {
	@RequestMapping("index.htm")
	public String main() {
		return "main";
	}
}
