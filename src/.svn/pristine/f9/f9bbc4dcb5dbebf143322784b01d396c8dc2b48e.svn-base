package com.expect.admin.data.dataobject;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 部门
 */
@Entity
@Table(name = "c_department")
public class Department {

	private String id;
	private String name;
	private String code;
	private String description;
	private User manager;
	private Department parentDepartment;
	private List<Department> childDepartments;
	private Set<User> users;
	
	private Department ssgs;//所属公司
	private String category;//部门的类型（1 普通部门  2：子公司）

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", length = 15)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "description", length = 512)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "manager_id")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Department getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(Department parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	@OneToMany(mappedBy = "parentDepartment")
	public List<Department> getChildDepartments() {
		return childDepartments;
	}

	public void setChildDepartments(List<Department> childDepartments) {
		this.childDepartments = childDepartments;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "departments")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Column(name = "category", length = 2)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@ManyToOne
	@JoinColumn(name = "ssgs_id")
	public Department getSsgs() {
		return ssgs;
	}

	public void setSsgs(Department ssgs) {
		this.ssgs = ssgs;
	}
	
	
	
	

}
