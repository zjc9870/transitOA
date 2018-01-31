package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.FunctionRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Function;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.RoleConvertor;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.JsTreeVo;
import com.expect.admin.utils.StringUtil;

/**
 * 角色Service
 */
@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private FunctionRepository functionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserService userService;

	/**
	 * 获取所有的角色
	 */
	public List<RoleVo> getRoles() {
		List<Role> roles = roleRepository.findAll();
		List<RoleVo> roleVos = RoleVo.convert(roles);
		return roleVos;
	}
	
	/**
	 *  获取有某个角色的用户列表
	 * @param roleId 角色id
	 * @return
	 */
	public List<UserVo> getUserOfRole(String roleId) {
		if(StringUtil.isBlank(roleId)) return new ArrayList<>(0);
		Role role = roleRepository.findOne(roleId);
		Set<User> userSet = role.getUsers();
		if(userSet == null || userSet.isEmpty()) return new ArrayList<>(0);
		return UserConvertor.convert(userSet);
	}

	/**
	 * 根据id获取角色
	 */
	public RoleVo getRoleById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		Role role = roleRepository.findOne(id);
		return RoleConvertor.convert(role);
	}

	/**
	 * 根据userId获取该用户的所有部门
	 */
	public List<RoleVo> getRolesByUserId(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ArrayList<>();
		}
		return RoleConvertor.convert(user.getRoles());
	}
	
	/**
	 * 根据管理员的所属公司获取某公司的角色列表
	 * 超级管理员获取所有公司的所有角色
	 * @param ssgs 所属公司（ "super" ：超级管理员）
	 * @return
	 */
	public List<RoleVo> getRolesLoginUser(String ssgs) {
		if(StringUtil.isBlank(ssgs)) throw new BaseAppException("获取某公司角色时公司id为空");
		List<Role> roles;
		if(StringUtil.equals(ssgs, "super")) roles = roleRepository.findAll();
//		departmentRepository.findOne(ssgs);
		else roles = roleRepository.findBySsgs_id(ssgs);
		if(roles == null || roles.isEmpty()) return new ArrayList<>();
		return RoleConvertor.convert(roles);
	}

	/**
	 * 保存角色
	 */
	@Transactional
	public ResultVo save(String name, String ssgsId) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("增加失败");
		if (StringUtils.isEmpty(name)) {
			resultVo.setMessage("角色名不能为空");
			return resultVo;
		}
		Role role = new Role();
		role.setName(name);
		
		if(!StringUtil.isBlank(ssgsId)) {
			Department department = departmentRepository.findOne(ssgsId);
			role.setSsgs(department);
		}

		Role result = roleRepository.save(role);
		if (result != null) {
			resultVo.setResult(true);
			resultVo.setMessage("增加成功");
			resultVo.setObj(result);
		}
		return resultVo;
	}

	/**
	 * 修改角色
	 */
	@Transactional
	public ResultVo update(String id, String name) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("修改失败");
		if (StringUtils.isEmpty(name)) {
			return resultVo;
		}
		Role role = roleRepository.findOne(id);

		if (role == null) {
			return resultVo;
		}
		role.setName(name);

		resultVo.setResult(true);
		resultVo.setMessage("修改成功");
		resultVo.setObj(role);
		return resultVo;
	}

	/**
	 * 删除角色
	 */
	public ResultVo delete(String id) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("删除失败");
		Role role = roleRepository.findOne(id);
		if (role == null) {
			return resultVo;
		}

		roleRepository.delete(id);
		resultVo.setMessage("删除成功");
		resultVo.setResult(true);
		return resultVo;
	}

	/**
	 * 根据roleId获取functions
	 */
	public List<JsTreeVo> getFunctionTreeByRoleId(String roleId) {
		UserVo userVo = userService.getLoginUser();
		if (StringUtils.isEmpty(roleId)) {
			return new ArrayList<>();
		}
		Role role = roleRepository.findOne(roleId);
		if (role == null) {
			return new ArrayList<>();
		}
		Set<Function> functions = role.getFunctions();
		List<JsTreeVo> resultJsTreeVos = new ArrayList<>();
		List<Function> parentFunctions = functionRepository.findByParentFunctionIsNull();
		// 设置第一级功能
		for (Function firFunction : parentFunctions) {
			JsTreeVo firJsTree = new JsTreeVo();
			if(!gnglQxpd(userVo.getUsername(), firFunction.getName())) continue;
			setFunctionTree(firFunction, firJsTree);
			// 设置第二级功能
			Set<Function> secFunctions = firFunction.getChildFunctions();
			if (!CollectionUtils.isEmpty(secFunctions)) {
				List<JsTreeVo> secJsTreeVos = new ArrayList<>(secFunctions.size());
				for (Function secFunction : secFunctions) {
					JsTreeVo secJsTree = new JsTreeVo();
					if(!gnglQxpd(userVo.getUsername(), secFunction.getName())) continue;
					setFunctionTree(secFunction, secJsTree);
					// 设置第三级功能
					Set<Function> thiFunctions = secFunction.getChildFunctions();
					if (!CollectionUtils.isEmpty(thiFunctions)) {
						List<JsTreeVo> thiTreeVos = new ArrayList<>(thiFunctions.size());
						for (Function thiFunction : thiFunctions) {
							if(!gnglQxpd(userVo.getUsername(), thiFunction.getName())) continue;
							JsTreeVo thiJsTree = new JsTreeVo();
							setFunctionTree(thiFunction, thiJsTree);
							if (setFunctionTreeSelected(functions, thiFunction)) {
								thiJsTree.setSelected(true);
							}
							thiTreeVos.add(thiJsTree);
						}
						secJsTree.setChildren(thiTreeVos);
					} else {
						if (setFunctionTreeSelected(functions, secFunction)) {
							secJsTree.setSelected(true);
						}
					}
					secJsTreeVos.add(secJsTree);
				}
				firJsTree.setChildren(secJsTreeVos);
			} else {
				if (setFunctionTreeSelected(functions, firFunction)) {
					firJsTree.setSelected(true);
				}
			}
			resultJsTreeVos.add(firJsTree);
		}
		// 排序
		Collections.sort(resultJsTreeVos);
		for (JsTreeVo firJsTreeVo : resultJsTreeVos) {
			List<JsTreeVo> secJsTreeVos = firJsTreeVo.getChildren();
			if (!CollectionUtils.isEmpty(secJsTreeVos)) {
				Collections.sort(secJsTreeVos);
				for (JsTreeVo secTreeVo : secJsTreeVos) {
					List<JsTreeVo> thiJsTreeVos = secTreeVo.getChildren();
					Collections.sort(thiJsTreeVos);
				}
			}
		}
		return resultJsTreeVos;
	}

	/**
	 * 功能管理功能权限判断 只有super可以使用
	 * @param userVo
	 * @param thiFunction
	 * @return true 用户有使用功能管理的权限
	 * flase 用户没有使用功能管理的权限
	 * 
	 */
	private boolean gnglQxpd(String userName, String functionName) {
		return !(StringUtil.equals(functionName, "功能管理") && 
				!isSuperRole());
	}

	/**
	 * 判断登录用户有没有超级管理员权限
	 */
	public boolean isSuperRole() {
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			if(StringUtil.equals(role.getName(), "超级管理员")) return true;
		}
		return false;
	}


	private void setFunctionTree(Function function, JsTreeVo jsTreeVo) {
		jsTreeVo.setId(function.getId());
		jsTreeVo.setText(function.getName());
		jsTreeVo.setSequence(function.getSequence());
		jsTreeVo.setIcon(function.getIcon());
	}

	private boolean setFunctionTreeSelected(Set<Function> functions, Function currentFunction) {
		for (Function function : functions) {
			if (function.getId().equals(currentFunction.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 修改角色的功能
	 */
	public ResultVo updateRoleFunctions(String roleId, String functionIds) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("修改失败");
		if (StringUtils.isEmpty(roleId)) {
			return resultVo;
		}
		Role role = roleRepository.getOne(roleId);
		if (role == null) {
			return resultVo;
		}
		// 修改角色的功能
		if (StringUtils.isEmpty(functionIds)) {
			role.setFunctions(null);
		} else {
			String[] functionIdArr = functionIds.split(",");
			Set<Function> functions = functionRepository.findByIdIn(functionIdArr);
			role.setFunctions(functions);
		}
		roleRepository.flush();

		resultVo.setMessage("修改成功");
		resultVo.setResult(true);
		return resultVo;
	}

	public Set<Function> getFunctionsByRoleId(String roleId){
		return roleRepository.findOne(roleId).getFunctions();
	}
}
