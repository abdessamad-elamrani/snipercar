package com.codevo.snipercar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@RequestMapping("/web")
public class WebController {
	
	@RequestMapping("/**")
	public String index() {
//		return new ModelAndView("index");
	    return "index.html";
	}

}
