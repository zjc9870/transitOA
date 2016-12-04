package com.expect.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.expect.admin.service.FunctionService;
import com.expect.admin.service.vo.FunctionVo;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private FunctionService functionService;

	@RequestMapping("/login")
	public String login() {
		return "admin/login";
	}

	@RequestMapping("/home")
	public String home(Model model) {
		List<FunctionVo> functions = functionService.getFunctionsByUser();
		model.addAttribute("functions", functions);
		return "admin/home";
	}
	
	@RequestMapping("/home1")
	public String home1(){
		return "admin/index";
	}

}
