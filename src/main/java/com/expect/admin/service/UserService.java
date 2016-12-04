package com.expect.admin.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.RequestUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * 根据id获取用户
	 */
	public UserVo getUserById(String id) {
		UserVo user = UserConvertor.convert(userRepository.findOne(id));
		return user;
	}

	/**
	 * 获取所有的用户
	 */
	public List<UserVo> getAllUsers() {
		List<User> users = userRepository.findAll();
		return UserConvertor.convert(users);
	}

	/**
	 * 保存用户
	 */
	@Transactional
	public DataTableRowVo save(UserVo userVo) {
		User checkUser = userRepository.findByUsername(userVo.getUsername());
		DataTableRowVo dtrv = new DataTableRowVo();
		if (checkUser != null) {
			dtrv.setMessage("用户存在");
			return dtrv;
		}
		User user = UserConvertor.convert(userVo);
		// 数据库日至记录开始
		User result = userRepository.save(user);
		if (result != null) {
			dtrv.setMessage("保存成功");
			dtrv.setResult(true);
			UserConvertor.convertDtrv(dtrv, result);
		} else {
			dtrv.setMessage("保存失败");
		}
		return dtrv;
	}

	/**
	 * 更新
	 */
	@Transactional
	public DataTableRowVo update(UserVo userVo) {
		DataTableRowVo dtrv = new DataTableRowVo();
		dtrv.setMessage("修改失败");

		if (StringUtils.isEmpty(userVo.getId())) {
			return dtrv;
		}
		User user = userRepository.findOne(userVo.getId());
		if (user == null) {
			return dtrv;
		}
		User checkUser = userRepository.findByUsername(userVo.getUsername());
		if (checkUser != null) {
			if (!user.getId().equals(checkUser.getId())) {
				dtrv.setMessage("用户存在");
				return dtrv;
			}
		}
		UserConvertor.convert(user, userVo);

		UserConvertor.convertDtrv(dtrv, user);
		dtrv.setMessage("修改成功");
		dtrv.setResult(true);
		return dtrv;
	}

	/**
	 * 删除
	 */
	@Transactional
	public ResultVo delete(String id) {
		ResultVo resultVo = new ResultVo();
		User user = userRepository.findOne(id);
		if (user == null) {
			resultVo.setMessage("删除失败");
			return resultVo;
		}
		userRepository.delete(user);
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		return resultVo;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            用,号隔开
	 */
	@Transactional
	public ResultVo batchDelete(String ids) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("删除失败");
		if (StringUtils.isEmpty(ids)) {
			return resultVo;
		}
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			userRepository.delete(id);
		}
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		return resultVo;
	}

	/**
	 * 根据用户名修改登录信息
	 */
	@Transactional
	public int updateLoginInfoByUsername(String lastLoginIp, String lastLoginTime, String username) {
		return userRepository.updateLoginInfoByUsername(lastLoginIp, lastLoginTime, username);
	}

	/**
	 * 更新用户角色
	 */
	@Transactional
	public ResultVo updateUserRole(String userId, String roleId) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("用户角色修改失败");

		if (StringUtils.isEmpty(userId)) {
			return resultVo;
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			return resultVo;
		}
		String[] roleIdArr = roleId.split(",");
		Set<Role> roles = roleRepository.findByIdIn(roleIdArr);
		user.setRoles(roles);

		userRepository.flush();
		resultVo.setMessage("用户角色修改成功");
		resultVo.setResult(true);
		return resultVo;
	}

	/**
	 * 更新用户部门
	 */
	@Transactional
	public ResultVo updateUserDepartment(String userId, String departmentId) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("用户部门修改失败");

		if (StringUtils.isEmpty(userId)) {
			return resultVo;
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			return resultVo;
		}
		String[] departmentIdArr = departmentId.split(",");
		Set<Department> departments = departmentRepository.findByIdIn(departmentIdArr);
		user.setDepartments(departments);

		userRepository.flush();
		resultVo.setMessage("用户部门修改成功");
		resultVo.setResult(true);
		return resultVo;
	}

	@Transactional
	public ResultVo updateAvatar(String id, byte[] avatar) {
		ResultVo resultVo = new ResultVo();
		int result = userRepository.updateAvatarById(id, avatar);
		if (result > 0) {
			resultVo.setResult(true);
			resultVo.setMessage("上传成功");
		} else {
			resultVo.setResult(false);
			resultVo.setMessage("上传失败");
		}
		return resultVo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		return user;
	}

	/**
	 * 登录成功后的回调，右spring-security管理
	 */
	public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			User user = (User) authentication.getPrincipal();
			System.out.println("登录成功！");
			String lastLoginIp = RequestUtil.getIpAddr(request);
			String lastLoginTime = DateUtil.format(new Date(), DateUtil.fullFormat);
			updateLoginInfoByUsername(lastLoginIp, lastLoginTime, user.getUsername());

			Cookie usernameCookie = new Cookie("username", user.getUsername());
			usernameCookie.setMaxAge(24 * 60 * 60 * 30);
			response.addCookie(usernameCookie);
			Cookie passwordCookie = new Cookie("password", user.getPassword());
			passwordCookie.setMaxAge(24 * 60 * 60 * 30);
			response.addCookie(passwordCookie);

			response.sendRedirect("/admin/home");
		}
	}
}
