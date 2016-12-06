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
	 * 修改用户头像
	 */
	@Modifying
	@Query("update User u set u.avatar.id=?2 where u.id=?1")
	public int updateAvatarById(String id, String avatarId);
}
