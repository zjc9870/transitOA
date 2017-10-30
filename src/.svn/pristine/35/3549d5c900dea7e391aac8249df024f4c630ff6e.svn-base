package com.expect.admin.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.expect.admin.service.PersonChooseService;
import com.expect.admin.service.vo.component.html.JsTreeVo;

@Controller
public class PersonChooseController {

	
	@Autowired
	private  PersonChooseService personChooseService;

	@RequestMapping("/getPersonTree")
	@ResponseBody
	public List<JsTreeVo> getAllDepartment(HttpServletResponse response) throws IOException {
		return personChooseService.getUserTree();
	}
}
