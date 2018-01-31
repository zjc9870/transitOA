package com.expect.admin.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;

public interface LcrzbRepository extends JpaRepository<Lcrzb, String> {

	/**
	 * 根据处理内容获得流程日志列表（处理时间升序）
	 * @param clnrid
	 * @return
	 */
	List<Lcrzb> findByClnridOrderByClsjAsc(String clnrid);
	List<Lcrzb> findByClnridAndSfxsOrderByClsjAsc(String clnrid, String sfxs);
	List<Lcrzb> findByUserOrderByClsjAsc(User user);
	/**
	 * 获取某用户在时间段内的流程操作日志 按操作时间倒叙排列
	 * @param user
	 * @param start
	 * @param end
	 * @return
	 */
	List<Lcrzb> findByUserAndClsjBetweenOrderByClsjDesc(User user, Date start, Date end);
	List<Lcrzb> findByClnrflAndClnridOrderByClsjDesc(String clnrfl, String clnrid);
	List<Lcrzb> findByClnridOrderByClsjDesc(String clnrid);

}
