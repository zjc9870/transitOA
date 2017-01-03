package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.DraftSw;

public interface DraftSwRepository extends JpaRepository<DraftSw, String> {

	
	/**
	 * 获取用户未提交合同记录
	 * @param userId
	 * @param condition
	 * @return
	 */
	List<DraftSw> findBySwr_idAndSwztOrderByTjsjDesc(String userId, String condition);
	

}
 