package com.expect.admin.web;

import java.io.File;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.expect.admin.plugins.ueditor.ActionEnter;

@Controller
@RequestMapping("/ueditor")
public class UEditorController {

	@Autowired
	private ActionEnter actionEnter;

	@RequestMapping("/init")
	@ResponseBody
	public String index(MultipartFile upfile, HttpServletRequest request) {
		String rootPath = UEditorController.class.getResource("/").getPath();
		rootPath = rootPath + "static" + File.separator + "plugins";
		actionEnter.init(request, rootPath);
		return actionEnter.exec(upfile);
	}

}
