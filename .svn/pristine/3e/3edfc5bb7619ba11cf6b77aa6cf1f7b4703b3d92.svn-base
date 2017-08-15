package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.DepartmentConvertor;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.DepartmentVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.JsTreeVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

/**
 * 部门Service
 */
@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 子公司分类
	 */
	private final String ZGS_CATEGORY = "2";

	/**
	 * 获取所有的部门信息
	 */
	@Cacheable(cacheName = "DEPARTMENT_CACHE")
	public List<DepartmentVo> getDepartments() {
		List<Department> departments = departmentRepository.findAll();
		List<DepartmentVo> departmentVos = DepartmentConvertor.convert(departments);
		return departmentVos;
	}
	
	/**
	 * 获取某个公司的所有部门
	 * @param ssgs 所属公司Id（父部门Id）<br>
	 * 如果ssgs为 super(超级管理员) 就返回所有部门
	 * @return
	 */
	@Cacheable(cacheName = "DEPARTMENT_CACHE")
	public List<DepartmentVo> getGsDepartmentsBySsgs(String ssgs) {
		if(StringUtil.isBlank(ssgs)) throw new BaseAppException("获取某公司部门时公司id为空");
		if(roleService.isSuperRole()) return getDepartments();
		List<Department> departmentList = departmentRepository.findBySsgs_id(ssgs);
		if(departmentList == null || departmentList.isEmpty()) return new ArrayList<>();
		List<DepartmentVo> departmentVos = DepartmentConvertor.convert(departmentList);
		return departmentVos;
	}
	
	/**
	 * 如果是超级管理员 子公司选项的内容就是所有公司，父部门的选项就是所有部门, 部门负责人就是集团所有人<br>
	 * 如果不是吵架管理员 子公司选项就是管理员的所属公司， 父部门选项就行子公司的所有部门， 部门负责人是子公司的所有人
	 * 新增部门是的初始化信息
	 * @return
	 */
	public DepartmentVo getANewDepartmentVo(){
		DepartmentVo departmentVo = new DepartmentVo();
		setSsgs(departmentVo);
		UserVo userVo = userService.getLoginUser();
		//父部门
		List<DepartmentVo> departments = getGsDepartmentsBySsgs(userVo.getSsgsId());
		SelectOptionVo parentDepartmentSov = DepartmentConvertor.convertSov(departments, null);
		departmentVo.setParentDepartmentSov(parentDepartmentSov);
		
		//部门负责人
		List<UserVo> users = userService.getUserBySsgsId(userVo.getSsgsId());
		SelectOptionVo managerSov = null;
		managerSov = UserConvertor.convertSov(users, null);
		departmentVo.setManagerSov(managerSov);
		
		setGsfl(departmentVo);
		
		return departmentVo;
	}

	/**
	 * 根据id获取部门
	 */
	public DepartmentVo getDepartmentById(String id) {
		List<DepartmentVo> departments = getDepartments();
		DepartmentVo checkedDepartment = null;
		if (!StringUtils.isEmpty(id)) {
			for (int i = departments.size() - 1; i >= 0; i--) {
				if (id.equals((departments.get(i).getId()))) {
					checkedDepartment = departments.remove(i);
					break;
				}
			}
		}
		List<UserVo> users = userService.getAllUsers();
		SelectOptionVo managerSov = null;

		DepartmentVo departmentVo = null;
		if (StringUtils.isEmpty(id)) {
			departmentVo = new DepartmentVo();
			managerSov = UserConvertor.convertSov(users, null);
		} else {
			Department department = departmentRepository.findOne(id);
			departmentVo = DepartmentConvertor.convert(department);
			managerSov = UserConvertor.convertSov(users, departmentVo.getManagerName());
		}

		SelectOptionVo parentDepartmentSov = DepartmentConvertor.convertSov(departments, checkedDepartment);
		departmentVo.setParentDepartmentSov(parentDepartmentSov);
		departmentVo.setManagerSov(managerSov);
		
		//所属公司
		setSsgs(departmentVo);
		//公司分类
		setGsfl(departmentVo);
		
		return departmentVo;
	}

	/**
	 * 设置公司分类
	 * @param departmentVo
	 */
	private void setGsfl(DepartmentVo departmentVo) {
		boolean isZgs = StringUtil.equals(departmentVo.getCategory(), "2");//是否是子公司
		departmentVo.getCategorySov().addOption("1", "普通部门", !isZgs);
		departmentVo.getCategorySov().addOption("2", "子公司", isZgs);
	}

	/**
	 * 设置departmentVO的所属公司
	 * @param departmentVo
	 */
	private void setSsgs(DepartmentVo departmentVo) {
		UserVo loginUserVo = userService.getLoginUser();
		if(roleService.isSuperRole()) {
				List<Department> zgs = departmentRepository.findByCategory(ZGS_CATEGORY);//子公司
				for (Department department : zgs) {
					if(StringUtil.equals(department.getId(), departmentVo.getSsgsId())){
						departmentVo.getSsgsSov().addOption(department.getId(), department.getName(), true);
						continue;
					}
					departmentVo.getSsgsSov().addOption(department.getId(), department.getName());
				}
		}
		else {
			Department ssgsDepartment = departmentRepository.findOne(loginUserVo.getSsgsId());
			departmentVo.getSsgsSov().addOption(ssgsDepartment.getId(), 
					ssgsDepartment.getName(), true);
			
		}
	}

	/**
	 * 根据userId获取该用户的所有部门
	 */
	public List<DepartmentVo> getDepartmentsByUserId(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ArrayList<>();
		}
		return DepartmentConvertor.convert(user.getDepartments());
	}

	/**
	 * 获取最底层部门
	 */
	public List<DepartmentVo> getAllBottomDepartments() {
		List<Department> departments = departmentRepository.findByLastDepartment();
		return DepartmentConvertor.convert(departments);
	}

	/**
	 * 保存部门
	 */
	@Transactional
	@TriggersRemove(cacheName = { "DEPARTMENT_CACHE" }, removeAll = true)
	public DataTableRowVo save(DepartmentVo departmentVo) {
		DataTableRowVo dtrv = new DataTableRowVo();
		dtrv.setMessage("增加失败");

		if (StringUtils.isEmpty(departmentVo.getCode())) {
			dtrv.setMessage("部门代码不能为空");
			return dtrv;
		}
		Department checkCodeDepartment = departmentRepository.findByCode(departmentVo.getCode());
		if (checkCodeDepartment != null) {
			dtrv.setMessage("部门代码存在");
			return dtrv;
		}

		if (StringUtils.isEmpty(departmentVo.getName())) {
			dtrv.setMessage("部门名称不能为空");
			return dtrv;
		}
		Department parentDepartment = null;
		if (!StringUtils.isEmpty(departmentVo.getParentId())) {
			parentDepartment = departmentRepository.findOne(departmentVo.getParentId());
		}
		User manager = null;
		if (!StringUtils.isEmpty(departmentVo.getManagerId())) {
			manager = userRepository.findOne(departmentVo.getManagerId());
		}
		
		Department ssgs = null;
		if(!StringUtils.isEmpty(departmentVo.getSsgsId())){
			ssgs = departmentRepository.findOne(departmentVo.getSsgsId());
		}
		Department department = DepartmentConvertor.convert(departmentVo);
		department.setParentDepartment(parentDepartment);
		department.setManager(manager);
		department.setSsgs(ssgs);

		Department result = departmentRepository.save(department);
		if (result != null) {
			dtrv.setMessage("增加成功");
			dtrv.setResult(true);
			DepartmentConvertor.convertDtrv(dtrv, result, parentDepartment, manager);
		}
		return dtrv;
	}

	/**
	 * 更新部门
	 */
	@Transactional
	@TriggersRemove(cacheName = { "DEPARTMENT_CACHE" }, removeAll = true)
	public DataTableRowVo update(DepartmentVo departmentVo) {
		DataTableRowVo dtrv = new DataTableRowVo();
		dtrv.setMessage("修改失败");

		if (StringUtils.isEmpty(departmentVo.getCode())) {
			dtrv.setMessage("部门代码不能为空");
			return dtrv;
		}
		if (StringUtils.isEmpty(departmentVo.getName())) {
			dtrv.setMessage("部门名称不能为空");
			return dtrv;
		}

		Department checkDepartment = departmentRepository.findOne(departmentVo.getId());
		if (checkDepartment == null) {
			dtrv.setMessage("该部门不存在");
			return dtrv;
		}

		// 部门代码有修改，判断部门代码是否存在
		if (StringUtils.isEmpty(checkDepartment.getCode())
				|| !checkDepartment.getCode().equals(departmentVo.getCode())) {
			Department checkCodeDepartment = departmentRepository.findByCode(departmentVo.getCode());
			if (checkCodeDepartment != null) {
				dtrv.setMessage("部门代码存在");
				return dtrv;
			}
		}
		Department parentDepartment = null;
		if (!StringUtils.isEmpty(departmentVo.getParentId())) {
			parentDepartment = departmentRepository.findOne(departmentVo.getParentId());
		}
		User manager = null;
		if (!StringUtils.isEmpty(departmentVo.getManagerId())) {
			manager = userRepository.findOne(departmentVo.getManagerId());
		}

		// 把原来的name记录下来，如果和现在的不一样，并且修改的department是父部门，那就查询到所有的子部门
		String name = checkDepartment.getName();
		DepartmentConvertor.convert(departmentVo, checkDepartment);
		checkDepartment.setParentDepartment(parentDepartment);
		checkDepartment.setManager(manager);

		if (!name.equals(departmentVo.getName())) {
			List<Department> childDepartments = departmentRepository.findByParentDepartmentId(departmentVo.getId());
			if (!CollectionUtils.isEmpty(childDepartments)) {
				for (Department department : childDepartments) {
					dtrv.addAddData(department.getId());
				}
			}
		}
		dtrv.setMessage("修改成功");
		dtrv.setResult(true);
		DepartmentConvertor.convertDtrv(dtrv, checkDepartment, parentDepartment, manager);
		return dtrv;
	}

	/**
	 * 删除部门
	 */
	@Transactional
	@TriggersRemove(cacheName = { "DEPARTMENT_CACHE" }, removeAll = true)
	public ResultVo delete(String id) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("删除失败");

		// 把所有所有的子功能id查询到，传给前台，一起删除
		List<Department> childDepartments = departmentRepository.findByParentDepartmentId(id);
		List<String> childIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(childDepartments)) {
			for (Department childDepartment : childDepartments) {
				childIds.add(childDepartment.getId());
			}
		}
		Department department = departmentRepository.findOne(id);
		if (department == null) {
			return resultVo;
		}

		departmentRepository.delete(department);
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		resultVo.setObj(childIds);
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
		// 把所有所有的子功能id查询到，传给前台，一起删除
		List<Department> childDepartments = departmentRepository.findByParentDepartmentIdIn(idArr);
		List<String> childIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(childDepartments)) {
			for (Department childDepartment : childDepartments) {
				childIds.add(childDepartment.getId());
			}
		}

		for (String id : idArr) {
			departmentRepository.delete(id);
		}
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		resultVo.setObj(childIds);
		return resultVo;
	}
	
	/**
	 * 获取部门的树形结构
	 * @return
	 */
	public List<JsTreeVo> getDepartmentTree() {
		List<Department> departmentList = departmentRepository.findByParentDepartmentId(null);
		if(departmentList == null || departmentList.size() == 0) return new ArrayList<>();
		List<JsTreeVo> firJstree = new ArrayList<>(departmentList.size());
		for (Department department : departmentList) {//一级部门
			JsTreeVo firJsTreeVo = new JsTreeVo();
			setDepartmentTree(department, firJsTreeVo);
			List<Department> secDepartmentList = department.getChildDepartments();
			if(secDepartmentList == null || secDepartmentList.size() == 0) continue;
			List<JsTreeVo> secJsTree = new ArrayList<>(secDepartmentList.size());
			for (Department secDepartment : secDepartmentList) {//二级部门
				JsTreeVo secJsTreeVo = new JsTreeVo();
				setDepartmentTree(secDepartment, secJsTreeVo);
				secJsTree.add(secJsTreeVo);
			}
			firJsTreeVo.setChildren(secJsTree);
			firJstree.add(firJsTreeVo);
		}
		return firJstree;
	}
	
	private void setDepartmentTree(Department department, JsTreeVo jsTreeVo) {
		jsTreeVo.setId(department.getId());
		jsTreeVo.setText(department.getName());
	}

}

