package com.expect.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expect.admin.config.Settings;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/admin/personalCon")
public class IndividualController {
	
//	private final Logger log = LoggerFactory.getLogger(IndividualController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private Settings settings;
	@Autowired
	private AttachmentService attachmentService;
	
	private final String viewPath = "admin/personalCon/";
	
	@GetMapping("/getIndividualMessage")
	public ModelAndView getIndividualMessage() {
		UserVo userVo = userService.getLoginUser();
		if(userVo == null) return new ModelAndView("admin/login");
		UserVo realUser = userService.getUserById(userVo.getId());
		ModelAndView mv = new ModelAndView(viewPath + "personalCon");
		mv.addObject("userVo", realUser);
		return mv;
	}
	
	@PostMapping("/updateIndividualMessage")
	@ResponseBody 
	public DataTableRowVo updateIndividualMessage(UserVo userVo, 
			@RequestParam(name = "newPassword", required = false)String newPassword,
			HttpServletResponse response) throws IOException{
		UserVo loginUserVo = userService.getLoginUser();
		if(!StringUtil.isBlank(newPassword)){
			String encodeNewPassword = userService.encodePassword(newPassword);
			userVo.setPassword(encodeNewPassword);
		}
		else userVo.setPassword(loginUserVo.getPassword());
		return userService.update(userVo,null, null);
	}
	/**
	 * 签名图片附件上传
	 */
	@RequestMapping(value = "/uploadIndividualAttachment", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo upload(MultipartFile files, HttpServletRequest request) {
		String path = settings.getAttachmentPath();
//		path = Base64Util.decode(path);
		FileResultVo frv = attachmentService.save(files, path);
		return frv;
	}
	/**
	 * 签名图片附件下载
	 */
	@RequestMapping(value = "/downloadIndividualAttachment", method = RequestMethod.GET)
	public String downloadIndividualAttachment(
			@RequestParam(name = "attachmentVosId", required = true)String attachmentVosId,
			HttpServletResponse reponse) {

		return "forward:/admin/attachment/download?id=" + attachmentVosId;
	}

	/**
	 * 个人信息修改前的密码校验
	 * @param originalPassword
	 * @return
	 */
	@RequestMapping(value = "/validation", method = RequestMethod.GET)
	@ResponseBody
	public ResultVo validate(String originalPassword){
		ResultVo resultVo = new ResultVo();
		UserVo userVoLogin = userService.getLoginUser();
		String loginOriginalPassword = userVoLogin.getPassword();
		if(userService.validatePassword(originalPassword,loginOriginalPassword)){
			resultVo.setResult(true);
		}
		return resultVo;


	}

}
