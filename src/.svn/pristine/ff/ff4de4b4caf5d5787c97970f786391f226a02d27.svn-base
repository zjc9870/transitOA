package com.expect.admin.service.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.Role;

public class RoleVo {

	private String id;
	private String name;

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

	public static RoleVo convert(Role role) {
		RoleVo roleVo = new RoleVo();
		BeanUtils.copyProperties(role, roleVo);
		return roleVo;
	}

	public static List<RoleVo> convert(List<Role> roles) {
		List<RoleVo> roleVos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roles)) {
			for (Role role : roles) {
				RoleVo roleVo = convert(role);
				roleVos.add(roleVo);
			}
		}
		return roleVos;
	}

}
