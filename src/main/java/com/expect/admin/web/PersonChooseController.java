package com.expect.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.expect.admin.service.PersonChooseService;

@Controller
public class PersonChooseController {

	private final String viewName = "admin/system/user/";
	
	@Autowired
	private  PersonChooseService personChooseService;

	@RequestMapping("/testPage")
	public String getAllDepartment(ModelMap model) {
		model.addAttribute("department", personChooseService.findAllDepartment());
		return viewName + "test";
	}
}
