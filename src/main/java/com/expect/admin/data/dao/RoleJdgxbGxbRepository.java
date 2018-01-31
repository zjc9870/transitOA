package com.expect.admin.data.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.RoleJdgxbGxb;


public interface RoleJdgxbGxbRepository extends JpaRepository<RoleJdgxbGxb, String> {

	public RoleJdgxbGxb findByRoleId(String roleId);
	/**
	 * 用于根据用户的角色确定用户可以审批的文件的状态
	 * @param bz  备注
	 * @param roles 用户的角色列表
	 * @param wjzl 用户要操作的文件种类
	 * @return
	 */
	public List<RoleJdgxbGxb> findByBzAndWjzlAndRoleIdIn(String bz, String wjzl, Collection<String> roles);
	
//	@Query("select f1 from RoleJdgxbGxb f1, Function f2 where f1.roleId = f2.id and f2.name = ?1")
//	RoleJdgxbGxb findByFunctionName(String roleName);

	public List<RoleJdgxbGxb> findByJdId(String jdId);
}
