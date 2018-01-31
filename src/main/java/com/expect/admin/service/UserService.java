package com.expect.admin.service;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.*;
import com.expect.admin.service.vo.AttachmentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.RequestUtil;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private LogLoginRepository logLoginRepository;
	@Autowired
	private RoleService roleService;
	@Autowired  
	private HttpSession session;
	@Autowired
	private AttachmentRepository attachmentRepository;

	/**
	 * 根据id获取用户
	 */
	public UserVo getUserById(String id) {
		UserVo userVo = getLoginUser();
		if (StringUtils.isEmpty(id) || StringUtil.equals(id, "-1")) {
			UserVo newUserVo= new UserVo();
			newUserVo.setSsgsId(userVo.getSsgsId());
			newUserVo.setSsgsName(userVo.getSsgsName());
			return newUserVo;
		} else {
			User user = userRepository.findOne(id);
			return UserConvertor.convert(user);
		}
	}
	
	public User getUsernameAndPasswordById(String id){
		User user = userRepository.findOne(id);
		return user;
	}

	/**
	 * 获取所有的用户
	 */
	@Cacheable(cacheName = "USER_CACHE")
	public List<UserVo> getAllUsers() {
		List<User> users = userRepository.findAll();
		return UserConvertor.convert(users);
	}
	
	/**
	 * 获取当前登录的用户
	 * @return
	 */
	public UserVo getLoginUser() {
		User user;
		if(SecurityContextHolder.getContext().getAuthentication()!=null){
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}else{
			user = (User) ( (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT") ).getAuthentication().getPrincipal();
		}
		if(user == null) return null;
		return UserConvertor.convert(user);
	}

	/**
	 * 获取当前用户签名附件
	 * @return
     */
	public List<AttachmentVo> getQmAttachment(){
		UserVo userVo = getLoginUser();
		String id = userVo.getId();
		User user = userRepository.getOne(id);
		List<AttachmentVo> attachmentVos= UserConvertor.getUserAttachment(user);
		return attachmentVos;
	}

	public List<AttachmentVo> getQmAttachmentByUser(User user){
		return UserConvertor.getUserAttachment(user);
	}

	public List<AttachmentVo> getQmAttachmentByUserId(String userId){
		User user = userRepository.getOne(userId);
		return UserConvertor.getUserAttachment(user);
	}

	@Cacheable(cacheName = "USER_CACHE")
	public List<UserVo> getUserBySsgsId(String ssgsId) {
		List<User> userList;
		if(StringUtil.isBlank(ssgsId)) {
			throw new BaseAppException("获取某公司部门时公司id为空");
		}
		if(roleService.isSuperRole()){
			return getAllUsers();
		} else {
			userList = userRepository.findBySsgs_id(ssgsId);
		}
		if(userList == null || userList.isEmpty()) {
			return new ArrayList<>();
		}
		return UserConvertor.convert(userList);
	}
	/**
	 * 保存用户
	 */
	@Transactional
	@TriggersRemove(cacheName = {"USER_CACHE"}, removeAll = true)
	public DataTableRowVo save(UserVo userVo,String[] attachmentId,String ssgsId) {
		User checkUser = userRepository.findByUsername(userVo.getUsername());
		DataTableRowVo dtrv = new DataTableRowVo();
		if (checkUser != null) {
			dtrv.setMessage("用户名存在");
			return dtrv;
		}

		//增加用户姓名唯一性检查
//		User checkUserByFullName = userRepository.findByFullName(userVo.getFullName());
//		if (checkUserByFullName !=null){
//			dtrv.setMessage("用户姓名存在");
//			return dtrv;
//		}

		User user = UserConvertor.convert(userVo);
		//将图片附件id和user_id绑定
		if (attachmentId != null && attachmentId.length > 0) {
			List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
			if (!attachmentList.isEmpty()) user.setAttachments(new HashSet<>(attachmentList));
		}
		if(!StringUtil.isBlank(userVo.getSsgsId())){ 
			Department ssgs = departmentRepository.findOne(userVo.getSsgsId());
			user.setSsgs(ssgs);
		}
		//将用户ssgs_id更新
		if (ssgsId.length() !=0 && ssgsId !=null){
			user.setSsgs(departmentRepository.findOne(ssgsId));
		}
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
	@TriggersRemove(cacheName = {"USER_CACHE"}, removeAll = true)
	public DataTableRowVo update(UserVo userVo,String[] attachmentId,String ssgsId) {
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

		//		获取初始密码
		String userOriginalPass = user.getPassword();
		UserConvertor.convert(user, userVo);


		if(StringUtil.isBlank(userVo.getPassword())){
			user.setPassword(userOriginalPass);
		}

		//将图片附件id和user_id绑定
		if (attachmentId != null && attachmentId.length > 0) {
			List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
			if (!attachmentList.isEmpty()) user.setAttachments(new HashSet<>(attachmentList));
		}


		//将用户ssgs_id更新
		if (ssgsId.length() !=0 && ssgsId !=null){
			user.setSsgs(departmentRepository.findOne(ssgsId));
		}
		userRepository.save(user);
		UserConvertor.convertDtrv(dtrv, user);
		dtrv.setMessage("修改成功");
		dtrv.setResult(true);
		return dtrv;
	}

	/**
	 * 删除
	 */
	@Transactional
	@TriggersRemove(cacheName = {"USER_CACHE"}, removeAll = true)
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
	public ResultVo deleteBatch(String ids) {
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
	 * 修改头像
	 */
	@Transactional
	public ResultVo updateAvatar(String id, String avatarId) {
		ResultVo rv = new ResultVo();
		rv.setMessage("修改失败");
		int result = userRepository.updateAvatarById(id, avatarId);
		if (result > 0) {
			rv.setMessage("修改成功");
			rv.setResult(true);
			rv.setObj(id);
		}
		return rv;
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
//		Department department = departmentRepository.findOne(departmentId);
//		user.setDepartment(department);

		userRepository.flush();
		resultVo.setMessage("用户部门修改成功");
		resultVo.setResult(true);
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
	 * 日志记录
	 */
	public void loginLog(String userId, String username, String ip) {
		// 日志记录
		Date time = new Date();
		LogLogin logLogin = new LogLogin();
		logLogin.setUserId(userId);
		logLogin.setIp(ip);
		logLogin.setTime(time);
		logLogin.setUsername(username);
		logLoginRepository.save(logLogin);
	}
	
	/**
	 * 对用户密码进行加密
	 * @param originalPassword 不能为空
	 */
	public String encodePassword(String originalPassword) {
		BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();
		return encoder.encode(originalPassword);
	}


	 /**
	 * 对加密密码和验证输入的密码进行校验
	 * @param rawPassword
	 * @param encoderPassword
	 * @return
	 */
	public boolean validatePassword(String rawPassword, String encoderPassword){
		BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();
		return encoder.matches(rawPassword,encoderPassword);
	}

	public String getUsernameById(String id) {
		User user = userRepository.findOne(id);
		if(user==null) return null;
		return user.getUsername();
	}

	/**
	 * 登录成功后的回调，右spring-security管理
	 */
	public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			User user = (User) authentication.getPrincipal();
			logger.info(user.getUsername() + "登录成功!");

			String ip = RequestUtil.getIpAddr(request);
			loginLog(user.getId(), user.getUsername(), ip);
			request.getSession().setAttribute("user", user);

			response.sendRedirect("/admin/home");
		}
	}

	//根据用户名获取用户姓名
	public String getFullnameByUserName(String userName){
		User user = userRepository.findByUsername(userName);
		if(user==null) return null;
		else return user.getFullName();
	}

	//根据用户获取该用户所在公司的子公司负责人（这里是用于公文审核的功能）
	public UserVo getZgsFzrByUser(UserVo userVo) {
		List<User> users = userRepository.findBySsgs_id(userVo.getSsgsId());
		for (User user : users) {
			UserVo userVo1 = UserConvertor.convert(user);
//			if (userVo1.getRoleName().contains("其他公司负责人") || userVo1.getRoleName().contains("东交部门负责人审核")
//					|| (userVo1.getRoleName().contains("负责人")&&userVo1.getSsgsName().equals("南京江宁公共交通集团有限公司"))) {
			if (userVo1.getRoleName().contains("发文审核员") ) {
				return userVo1;
			}
		}
		return null;
	}
}
