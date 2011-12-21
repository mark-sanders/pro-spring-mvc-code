package com.apress.prospringmvc.pizzarus.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.execution.Event;

@Controller
public class IndexController {

	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
	public Event indexPage() {
		return new EventFactorySupport().success(this);
	}
}