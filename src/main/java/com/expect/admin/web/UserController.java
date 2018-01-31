package com.expect.admin.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expect.admin.service.DepartmentService;
import com.expect.admin.service.vo.DepartmentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.config.Settings;
import com.expect.admin.service.AttachmentService;
//import com.expect.admin.service.DepartmentService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.AttachmentVo;
//import com.expect.admin.service.vo.DepartmentVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.IOUtil;
//import com.expect.admin.utils.JsonResult;
//import com.expect.admin.utils.ResponseBuilder;
//import com.expect.admin.utils.StringUtil;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping("/admin/user")
public class UserController {

	private final String viewName = "admin/system/user/";

	@Autowired
	private UserService userService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private Settings settings;
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 用户管理页面
	 */
	@RequestMapping(value = "/userManagePage", method = RequestMethod.GET)
	public ModelAndView userManagePage() {
		UserVo userVo = userService.getLoginUser();
		if(userVo == null) return new ModelAndView("admin/login");
//		List<UserVo> users = userService.getAllUsers();
		List<UserVo> users = userService.getUserBySsgsId(userVo.getSsgsId());
		List<DataTableRowVo> dtrvs = UserConvertor.convertDtrvs(users);
		ModelAndView modelAndView = new ModelAndView(viewName + "manage");
		modelAndView.addObject("users", dtrvs);
		return modelAndView;
	}

	/**
	 * 用户表单页面
	 */
	@RequestMapping(value = "/userFormPage", method = RequestMethod.GET)
	public ModelAndView userForm(String id) {
		UserVo userVo = userService.getUserById(id);
		ModelAndView modelAndView = new ModelAndView(viewName + "form/userForm");
		userVo.setPassword("");
		List<DepartmentVo> departmentVos = departmentService.getDepartments();
		modelAndView.addObject("userVo", userVo);
		modelAndView.addObject("departmentVos",departmentVos);
		return modelAndView;
	}

	/**
	 * 获取userSelect的html
	 */
	@RequestMapping("/getUserSelectHtml")
	@ResponseBody
	public SelectOptionVo getUserSelect(String username) {
		List<UserVo> users = userService.getAllUsers();
		SelectOptionVo sov = UserConvertor.convertSov(users, username);
		return sov;
	}

	/**
	 * 用户-保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo save(UserVo userVo,
							   @RequestParam(name = "attachmentId" ,required = false) String[] attachmentId,
	                           @RequestParam(name ="ssgsId",required = false) String ssgsId) {
		if(!StringUtil.isBlank(userVo.getPassword())){
			String encodePassword = userService.encodePassword(userVo.getPassword());
			userVo.setPassword(encodePassword);
		}
		return userService.save(userVo,attachmentId,ssgsId);
	}

	/**
	 * 用户-更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo update(UserVo userVo,
								 @RequestParam(name = "attachmentId" ,required = false) String[] attachmentId,
								 @RequestParam(name ="ssgsId",required = false) String ssgsId) {
	    if(!StringUtil.isBlank(userVo.getPassword())) {
	        String encodeNewPassword = userService.encodePassword(userVo.getPassword());
            userVo.setPassword(encodeNewPassword);
	    }
		return userService.update(userVo,attachmentId,ssgsId);
	}

	/**
	 * 用户-删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo delete(String id) {
		return userService.delete(id);
	}

	/**
	 * 用户-批量删除
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo deleteBatch(String ids) {
		return userService.deleteBatch(ids);
	}

	/**
	 * 修改用户角色
	 */
	@RequestMapping(value = "/updateUserRole", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo updateUserRole(String userId, String roleId) {
		ResultVo resultVo = userService.updateUserRole(userId, roleId);
		return resultVo;
	}

	/**
	 * 修改用户部门
	 */
	@RequestMapping(value = "/updateUserDepartment", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo updateUserDepartment(String userId, String departmentId) {
		ResultVo resultVo = userService.updateUserDepartment(userId, departmentId);
		return resultVo;
	}

	/**
	 * 检查头像
	 */
	@RequestMapping(value = "/checkAvatar", method = RequestMethod.GET)
	@ResponseBody
	public ResultVo checkAvatar(String userId) {
		UserVo user = userService.getUserById(userId);
		if (user != null && !StringUtils.isEmpty(user.getAvatarId())) {
			String avatarId = user.getAvatarId();
			AttachmentVo avatar = attachmentService.getAttachmentById(avatarId);
			if (avatar != null) {
				return new ResultVo(true);
			} else {
				return new ResultVo(false);
			}
		} else {
			return new ResultVo(false);
		}
	}

	/**
	 * 显示头像
	 */
	@RequestMapping(value = "/showAvatar", method = RequestMethod.GET)
	public void showAvatar(String userId, HttpServletResponse response) {
		UserVo user = userService.getUserById(userId);
		if (user != null && !StringUtils.isEmpty(user.getAvatarId())) {
			String avatarId = user.getAvatarId();
			AttachmentVo avatar = attachmentService.getAttachmentById(avatarId);
			if (avatar != null) {
				byte[] avatarByte = IOUtil.inputDataFromFile(avatar.getPath() + File.separator + avatar.getId());
				try {
					response.getOutputStream().write(avatarByte);
					response.getOutputStream().flush();
					response.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 修改头像
	 */
	@RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo uploadAvatar(MultipartFile files, String userAvatarId, HttpServletRequest request) {
		String avatarPath = settings.getAvatarPath();
		FileResultVo frv = attachmentService.save(files, avatarPath);
		if (!frv.isResult()) {
			return frv;
		}
		ResultVo rv = userService.updateAvatar(userAvatarId, frv.getId());
		return rv;
	}
	
//	@RequestMapping(value = "/getUserMessage", method = RequestMethod.GET)
//	public ModelAndView getUserMessage() {
//		ModelAndView mv = new ModelAndView(viewName + "form/userForm2");
//		UserVo userVo = userService.getLoginUser();
//		mv.addObject("userVo", userVo);
//		return mv;
//	}
	
//	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
//	public void modifyPassword(HttpServletResponse response,
//			@RequestParam(name = "oldPassword", required = true)String oldPassword,
//			@RequestParam(name = "newPassword", required = true)String newPassword) throws IOException {
//		UserVo userVo = userService.getLoginUser();
//		String message;
//		boolean isSuccess = false;
//		if(userVo == null){
//			message = "用户未登录！";
//		}
//		else if(!StringUtil.equals(oldPassword, userVo.getPassword())) {
//			message = "原密码错误，修改密码失败";
//		}else {
//			userVo.setPassword(newPassword);
//			userService.update(userVo);
//			isSuccess = true;
//			message = "密码修改成功！";
//		}
//		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(isSuccess, message).build());
//	}
	
	/**
	 * 测试人员选择页面
	 */
	@RequestMapping("/testPage")
	public ModelAndView test() {
		ModelAndView modelAndView = new ModelAndView(viewName + "test");
		return modelAndView;
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

}
