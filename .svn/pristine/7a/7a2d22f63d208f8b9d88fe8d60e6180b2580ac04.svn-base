package com.expect.admin.data.dataobject;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 角色
 */
@Entity
@Table(name = "c_role")
public class Role {

	private String id;
	private String name;
	private String remark;
	private Set<User> users;
	private Set<Function> functions;
	private Department ssgs;//角色所属公司
	private Department ssbm;//角色所属部门

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

	@Column(name = "remark", length = 512)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "roles")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "c_role_function", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "function_id"))
	public Set<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<Function> functions) {
		this.functions = functions;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "ssgs_id")
	public Department getSsgs() {
		return ssgs;
	}

	public void setSsgs(Department ssgs) {
		this.ssgs = ssgs;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "ssbm_id" )
	public Department getSsbm() {
		return ssbm;
	}

	public void setSsbm(Department ssbm) {
		this.ssbm = ssbm;
	}

}

