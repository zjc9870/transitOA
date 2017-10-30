package com.expect.admin.data.dataobject;

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

import com.expect.admin.utils.StringUtil;

/**
 * 功能
 */
@Entity
@Table(name = "c_function")
public class Function {

	private String id;
	private String name;
	private String icon;
	private String url;
	private String description;
	private int sequence;
	private Function parentFunction;
	private Set<Function> childFunctions;
	private Set<Role> roles;

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

	@Column(name = "name", length = 31)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "icon", length = 31)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "url", length = 63)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "sequence", precision = 5)
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Column(name = "description", length = 511)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Function getParentFunction() {
		return parentFunction;
	}

	public void setParentFunction(Function parentFunction) {
		this.parentFunction = parentFunction;
	}

	@OneToMany(mappedBy = "parentFunction")
	public Set<Function> getChildFunctions() {
		return childFunctions;
	}

	public void setChildFunctions(Set<Function> childFunctions) {
		this.childFunctions = childFunctions;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "functions")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sequence;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (!StringUtil.equals(id, other.getId()))
			return false;
		return true;
	}

}
