package com.expect.admin.data.dao;

import java.util.List;

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
	 * 根据用户的所属公司id获取用户列表
	 * 获取某个公司的用户
	 * @param ssgsId
	 * @return
	 */
	public List<User> findBySsgs_id(String ssgsId);

	/**
	 * 修改用户头像
	 */
	@Modifying
	@Query("update User u set u.avatar.id=?2 where u.id=?1")
	public int updateAvatarById(String id, String avatarId);

	public User findByFullName(String fullName);
}
