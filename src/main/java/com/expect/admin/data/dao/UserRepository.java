package com.expect.admin.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.expect.admin.data.dataobject.User;

public interface UserRepository extends JpaRepository<User, String> {

	/**
	 * 根据用户名查询用户
	 */
	public User findByUsername(String username);

	/**
	 * 登录成功后，修改登录信息
	 * 
	 * @return 返回更新数据的条数
	 */
	@Modifying
	@Query("update User u set u.lastLoginIp=?1,u.lastLoginTime=?2 where u.username=?3")
	public int updateLoginInfoByUsername(String lastLoginIp, String lastLoginTime, String username);

	/**
	 * 修改用户头像
	 */
	@Modifying
	@Query("update User u set u.avatar=?2 where u.id=?1")
	public int updateAvatarById(String id, byte[] avatar);
}
