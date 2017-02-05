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
	
	@RequestMapping("/submit")
	public ModelAndView submit() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit");
		return mv;
	}
	
	@RequestMapping("/submit_record")
	public ModelAndView submit_record() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_record");
		return mv;
	}
	
	@RequestMapping("/approve")
	public ModelAndView approve() {
		ModelAndView mv = new ModelAndView(viewName + "contract_approve");
		return mv;
	}
	
	@RequestMapping("/submit_detail")
	public ModelAndView submit_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_detail");
		return mv;
	}
	
	@RequestMapping("/approve_detail")
	public ModelAndView approve_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_approve_detail");
		return mv;
	}
	
	@RequestMapping("/success")
	public ModelAndView success() {
		ModelAndView mv = new ModelAndView(viewName + "success");
		return mv;
	}
}
