package com.codevo.snipercar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class WebController {
	
	@RequestMapping("/")
	public String redirect() {
	    return "redirect:/web";
	}
	
	@RequestMapping("/web/**")
	public String index() {
		return "index.html";
	}

}
