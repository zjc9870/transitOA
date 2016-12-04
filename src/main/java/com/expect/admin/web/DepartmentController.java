package com.expect.admin.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.DepartmentService;
import com.expect.admin.service.convertor.DepartmentConvertor;
import com.expect.admin.service.vo.DepartmentVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.CheckboxsVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;

@Controller
@RequestMapping("/admin/department")
public class DepartmentController {

	private final String viewName = "admin/system/department/";

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/departmentManagePage")
	public ModelAndView userManagePage() {
		List<DepartmentVo> departments = departmentService.getDepartments();
		List<DataTableRowVo> dtrvs = DepartmentConvertor.convertDtrv(departments);
		ModelAndView modelAndView = new ModelAndView(viewName + "manage");
		modelAndView.addObject("departments", dtrvs);
		return modelAndView;
	}

	/**
	 * 获取departmentSelect的html
	 */
	@RequestMapping("/getDepartmentSelectHtml")
	@ResponseBody
	public SelectOptionVo getDeaprtmentSelect(String id) {
		List<DepartmentVo> departments = departmentService.getDepartments();
		DepartmentVo checkedDepartment = null;
		if (!StringUtils.isEmpty(id)) {
			for (int i = departments.size() - 1; i >= 0; i--) {
				if (id.equals((departments.get(i).getId()))) {
					checkedDepartment = departments.remove(i);
					break;
				}
			}
		}
		SelectOptionVo sov = DepartmentConvertor.convertSov(departments, checkedDepartment);
		return sov;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo save(DepartmentVo departmentVo) {
		return departmentService.save(departmentVo);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo update(DepartmentVo departmentVo) {
		return departmentService.update(departmentVo);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo delete(String id) {
		return departmentService.delete(id);
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo batchDelete(String ids) {
		return departmentService.batchDelete(ids);
	}

	/**
	 * 获取departmentCheckbox的html
	 */
	@RequestMapping(value = "/getDepartmentCheckboxHtml", method = RequestMethod.POST)
	@ResponseBody
	public CheckboxsVo getDepartmentCheckboxHtml(String userId) {
		List<DepartmentVo> departments = departmentService.getAllBottomDepartments();
		List<String> ids = new ArrayList<>();
		List<DepartmentVo> userDepartments = departmentService.getDepartmentsByUserId(userId);
		if (!CollectionUtils.isEmpty(userDepartments)) {
			for (DepartmentVo department : userDepartments) {
				ids.add(department.getId());
			}
		}
		CheckboxsVo cbv = DepartmentConvertor.convertCbv(departments, ids);
		return cbv;
	}

}
