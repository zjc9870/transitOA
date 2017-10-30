package com.expect.admin.data.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	/**
	 * 根据ids获取角色
	 * 
	 * @param ids
	 *            用,分隔的id
	 */
	public Set<Role> findByIdIn(String[] roles);
	/**
	 * 根据角色的所属公司id获取角色
	 * @param ssgsId
	 * @return
	 */
	public List<Role> findBySsgs_id(String ssgsId);

}
