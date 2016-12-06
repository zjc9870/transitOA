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
import com.expect.admin.service.convertor.DepartmentConvertor;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.DepartmentVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;

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

	/**
	 * 获取所有的部门信息
	 */
	public List<DepartmentVo> getDepartments() {
		List<Department> departments = departmentRepository.findAll();
		List<DepartmentVo> departmentVos = DepartmentConvertor.convert(departments);
		return departmentVos;
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
		return departmentVo;
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
		Department department = DepartmentConvertor.convert(departmentVo);
		department.setParentDepartment(parentDepartment);
		department.setManager(manager);

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

}

