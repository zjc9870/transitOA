package com.expect.admin.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.StringUtil;

/**
 * 个人信息管理
 * @author zcz
 *
 */
@Controller
@RequestMapping("/admin/user/")
public class IndividualController {
	
//	private final Logger log = LoggerFactory.getLogger(IndividualController.class);
	
	@Autowired
	private UserService userService;
	
	private final String viewPath = "admin/personalCon/";
	
	@GetMapping("/getIndividualMessage")
	public ModelAndView getIndividualMessage() {
		UserVo userVo = userService.getLoginUser();
		if(userVo == null) return new ModelAndView("admin/login");
		ModelAndView mv = new ModelAndView(viewPath + "personalCon");
		mv.addObject("userVo", userVo);
		return mv;
	}
	
	@PostMapping("/updateIndividualMessage")
	@ResponseBody 
	public DataTableRowVo updateIndividualMessage(UserVo userVo, HttpServletResponse response) throws IOException{
		if(!StringUtil.isBlank(userVo.getAvatarId()))
			userService.updateAvatar(userVo.getId(), userVo.getAvatarId());
		return userService.update(userVo);
	}
}