package com.expect.admin.service.convertor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.DepartmentVo;
import com.expect.admin.service.vo.component.html.CheckboxsVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableButtonFactory;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.StringUtil;

public class DepartmentConvertor {

	/**
	 * vo to do
	 */
	public static Department convert(DepartmentVo departmentVo) {
		Department department = new Department();
		BeanUtils.copyProperties(departmentVo, department);
		return department;
	}

	/**
	 * vo to do
	 */
	public static void convert(DepartmentVo departmentVo, Department department) {
		department.setCode(departmentVo.getCode());
		department.setName(departmentVo.getName());
		department.setDescription(departmentVo.getDescription());
	}

	/**
	 * do to vo
	 */
	public static DepartmentVo convert(Department department) {
		DepartmentVo departmentVo = new DepartmentVo();
		if (department == null) {
			return departmentVo;
		}
		BeanUtils.copyProperties(department, departmentVo);
		if (department.getManager() != null) {
			departmentVo.setManagerId(department.getManager().getId());
			departmentVo.setManagerName(department.getManager().getUsername());
		}
		if (department.getParentDepartment() != null) {
			departmentVo.setParentId(department.getParentDepartment().getId());
			departmentVo.setParentName(department.getParentDepartment().getName());
		}
		if(department.getSsgs() != null) {
			departmentVo.setSsgsId(department.getSsgs().getId());
			departmentVo.setSsgsName(department.getSsgs().getName());
		}
		return departmentVo;
	}

	/**
	 * dos to vos
	 */
	public static List<DepartmentVo> convert(Collection<Department> departments) {
		List<DepartmentVo> departmentVos = new ArrayList<>();
		for (Department department : departments) {
			if(StringUtil.equals("super", department.getId())) continue;
			DepartmentVo departmentVo = convert(department);
			departmentVos.add(departmentVo);
		}
		return departmentVos;
	}

	/**
	 * vo to dtrv
	 */
	public static void convertDtrv(DataTableRowVo dtrv, DepartmentVo departmentVo) {
		dtrv.setObj(departmentVo);
		dtrv.setCheckbox(true);
		dtrv.addData(departmentVo.getCode());
		dtrv.addData(departmentVo.getName());
		dtrv.addData(departmentVo.getManagerName());
		dtrv.addData(departmentVo.getParentName());
		dtrv.addData(departmentVo.getDescription());
		// 设置操作的button
		StringBuilder sb = new StringBuilder();
		sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + departmentVo.getId() + "'"));
		sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + departmentVo.getId() + "'"));
		dtrv.addData(sb.toString());
	}

	/**
	 * vos to dtrvs
	 */
	public static List<DataTableRowVo> convertDtrv(List<DepartmentVo> departmentVos) {
		List<DataTableRowVo> dtrvs = new ArrayList<DataTableRowVo>();
		if (!CollectionUtils.isEmpty(departmentVos)) {
			for (DepartmentVo departmentVo : departmentVos) {
				DataTableRowVo dtrv = new DataTableRowVo();
				convertDtrv(dtrv, departmentVo);
				dtrvs.add(dtrv);
			}
		}
		return dtrvs;
	}

	/**
	 * do to dtrv
	 */
	public static void convertDtrv(DataTableRowVo dtrv, Department department, Department parentDepartment,
			User manager) {
		DepartmentVo departmentVo = convert(department);
		dtrv.setObj(departmentVo);
		dtrv.setCheckbox(true);
		dtrv.addData(department.getCode());
		dtrv.addData(department.getName());
		if (manager != null) {
			dtrv.addData(manager.getUsername());
		} else {
			dtrv.addData("");
		}
		if (parentDepartment != null) {
			dtrv.addData(parentDepartment.getName());
		} else {
			dtrv.addData("");
		}
		dtrv.addData(department.getDescription());
		// 设置操作的button
		StringBuilder sb = new StringBuilder();
		sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + department.getId() + "'"));
		sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + department.getId() + "'"));
		dtrv.addData(sb.toString());
	}

	/**
	 * vos to sov
	 */
	public static SelectOptionVo convertSov(List<DepartmentVo> departmentVos, DepartmentVo checkedDepartment) {
		SelectOptionVo sov = new SelectOptionVo();
		if (!CollectionUtils.isEmpty(departmentVos)) {
			sov.addOption("", "设置为上级部门");
			for (DepartmentVo departmentVo : departmentVos) {
				if (checkedDepartment != null && !StringUtils.isEmpty(checkedDepartment.getParentId())) {
					if (checkedDepartment.getParentId().equals(departmentVo.getId())) {
						sov.addOption(departmentVo.getId(), departmentVo.getName(), true);
					} else {
						sov.addOption(departmentVo.getId(), departmentVo.getName());
					}
				} else {
					sov.addOption(departmentVo.getId(), departmentVo.getName());
				}
			}
		}
		return sov;
	}

	/**
	 * vo to cbv
	 */
	public static CheckboxsVo convertCbv(List<DepartmentVo> departments, List<String> ids) {
		CheckboxsVo checkboxsVo = new CheckboxsVo("department");
		if (!CollectionUtils.isEmpty(departments)) {
			for (DepartmentVo department : departments) {
				if (CollectionUtils.isEmpty(ids)) {
					checkboxsVo.addCheckbox(department.getName(), department.getId());
					continue;
				}
				boolean flag = false;
				for (String id : ids) {
					if (id.equals(department.getId())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					checkboxsVo.addCheckbox(department.getName(), department.getId(), true);
				} else {
					checkboxsVo.addCheckbox(department.getName(), department.getId());
				}
			}
		}
		return checkboxsVo;
	}
}
