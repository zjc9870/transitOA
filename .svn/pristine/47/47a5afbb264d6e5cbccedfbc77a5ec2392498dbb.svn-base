package com.expect.admin.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.expect.admin.service.FunctionService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.FunctionVo;
import com.expect.admin.service.vo.UserVo;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private FunctionService functionService;
	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public String login() {
		return "admin/login";
	}

	@RequestMapping("/home")
	public String home(Model model) {
		List<FunctionVo> functions = functionService.getFunctionsByUser();
		functionService.filter(functions);
		UserVo userVo = userService.getLoginUser();
		model.addAttribute("userVo", userVo);
		model.addAttribute("functions", functions);
		return "admin/home";
	}

	@RequestMapping("/home1")
	public String home1(Model model) {
		UserVo userVo = userService.getLoginUser();
		model.addAttribute("userVo", userVo);
		return "admin/homeContent";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		request.getSession().removeAttribute("user");
		return "redirect:/admin/login";
	}
}
