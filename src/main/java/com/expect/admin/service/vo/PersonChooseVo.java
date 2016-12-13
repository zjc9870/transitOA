package com.expect.admin.service.vo;

import java.util.List;
import java.util.Set;

import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.User;

public class PersonChooseVo {

	private String id;
	private String name;
	private List<Department> childDepartments;
	private Set<User> users;
	
	private String category;
	
	

	public PersonChooseVo() {
		super();
	}
	
	public PersonChooseVo(Department dep) {
		super();
		this.id = dep.getId();
		this.category = dep.getCategory();
		this.childDepartments = dep.getChildDepartments();
		this.users = dep.getUsers();
		this.name = dep.getName();
	}

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

	public List<Department> getChildDepartments() {
		return childDepartments;
	}

	public void setChildDepartments(List<Department> childDepartments) {
		this.childDepartments = childDepartments;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
