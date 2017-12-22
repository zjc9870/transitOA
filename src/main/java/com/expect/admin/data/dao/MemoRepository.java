package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Memo;

/**
 * 备忘录JPA
 */
public interface MemoRepository extends JpaRepository<Memo, String> {

	/**
	 * 根据年月，用户id获取备忘录
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param userId
	 *            用户id
	 * @return 备忘录list
	 */
	public List<Memo> findByYearAndMonthAndUserIdOrderByTime(String year, String month, String userId);

}
