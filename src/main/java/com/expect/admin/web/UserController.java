package com.expect.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.UserService;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.ResponseBuilder;

@Controller
@RequestMapping("/admin/user")
public class UserController {

	private final String viewName = "admin/system/user/";

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/userManagePage", method = RequestMethod.GET)
	public ModelAndView userManagePage() {
		List<UserVo> users = userService.getAllUsers();
		List<DataTableRowVo> dtrvs = UserConvertor.convertDtrvs(users);
		ModelAndView modelAndView = new ModelAndView(viewName + "manage");
		modelAndView.addObject("users", dtrvs);
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

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo save(UserVo userVo) {
		return userService.save(userVo);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo update(UserVo userVo) {
		return userService.update(userVo);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo delete(String id) {
		return userService.delete(id);
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo batchDelete(String ids) {
		return userService.batchDelete(ids);
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
		if (user != null && user.getAvatar() != null) {
			return new ResultVo(true);
		} else {
			return new ResultVo(false);
		}
	}

	/**
	 * 显示头像
	 */
	@RequestMapping(value = "/showAvatar", method = RequestMethod.GET)
	@ResponseBody
	public byte[] showAvatar(String userId) {
		UserVo user = userService.getUserById(userId);
		if (user != null && user.getAvatar() != null) {
			return user.getAvatar();
		}
		return null;
	}

	/**
	 * 上传头像
	 */
	@RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
	public void uploadAvatar(HttpServletResponse response, MultipartFile avatar, String userId) {
		ResultVo resultVo = new ResultVo();
		if (avatar == null) {
			resultVo.setMessage("上传失败");
		} else {
			try {
				resultVo = userService.updateAvatar(userId, avatar.getBytes());
				resultVo.setObj(userId);
			} catch (IOException e) {
				e.printStackTrace();
				resultVo.setMessage("上传失败");
			}
		}
		try {
			ResponseBuilder.writeJsonResponseForAjaxUpload(response, resultVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
