package com.expect.admin.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.expect.admin.service.DraftService;

@Controller
@RequestMapping("/draft")
public class DraftController {
	
	@Autowired
	private DraftService draftService;
	
	@RequestMapping("/getDrafts")
	public String getDrafts(HttpServletRequest request, ModelMap model) {
		
		return "";
	}

}
