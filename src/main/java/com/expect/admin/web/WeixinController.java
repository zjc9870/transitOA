package com.expect.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/weixin")
public class WeixinController {

	private final String viewName = "weixin/";
	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView(viewName + "login");
		return mv;
	}
}
