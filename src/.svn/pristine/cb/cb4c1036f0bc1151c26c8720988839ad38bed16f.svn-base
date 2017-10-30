package com.expect.admin.service.convertor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import com.expect.admin.data.dataobject.Role;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.component.html.CheckboxsVo;

public class RoleConvertor {

	/**
	 * do to vo
	 */
	public static List<RoleVo> convert(Collection<Role> roles) {
		List<RoleVo> roleVos = new ArrayList<>();
		if (CollectionUtils.isEmpty(roles)) {
			return roleVos;
		}
		for (Role role : roles) {
			RoleVo roleVo = convert(role);
			roleVos.add(roleVo);
		}
		return roleVos;
	}

	/**
	 * dos to vos
	 */
	public static RoleVo convert(Role role) {
		RoleVo roleVo = new RoleVo();
		try {
			BeanUtils.copyProperties(roleVo, role);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return roleVo;
	}

	/**
	 * vo to cbv
	 */
	public static CheckboxsVo convertCbv(Collection<RoleVo> roles, List<String> ids) {
		CheckboxsVo checkboxsVo = new CheckboxsVo("role");
		if (!CollectionUtils.isEmpty(roles)) {
			for (RoleVo role : roles) {
				if (CollectionUtils.isEmpty(ids)) {
					checkboxsVo.addCheckbox(role.getName(), role.getId());
					continue;
				}
				boolean flag = false;
				for (String id : ids) {
					if (id.equals(role.getId())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					checkboxsVo.addCheckbox(role.getName(), role.getId(), true);
				} else {
					checkboxsVo.addCheckbox(role.getName(), role.getId());
				}
			}
		}
		return checkboxsVo;
	}

}
