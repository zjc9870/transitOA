package com.expect.admin.service.vo;

import com.expect.admin.service.vo.component.html.SelectOptionVo;

public class DepartmentVo {

	private String id;
	private String name;
	private String code;
	private String description;
	private String managerId;
	private String managerName;
	private String parentId;
	private String parentName;
	private SelectOptionVo parentDepartmentSov = new SelectOptionVo();
	private SelectOptionVo managerSov = new SelectOptionVo();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public SelectOptionVo getParentDepartmentSov() {
		return parentDepartmentSov;
	}

	public void setParentDepartmentSov(SelectOptionVo parentDepartmentSov) {
		this.parentDepartmentSov = parentDepartmentSov;
	}

	public SelectOptionVo getManagerSov() {
		return managerSov;
	}

	public void setManagerSov(SelectOptionVo managerSov) {
		this.managerSov = managerSov;
	}

}
