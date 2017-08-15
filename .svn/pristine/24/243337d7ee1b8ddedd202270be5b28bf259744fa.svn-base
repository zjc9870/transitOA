package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Dmb;

public interface DmbRepository extends JpaRepository<Dmb, String> {

	/**
	 * 根据类别编号和代码编号获取某个代码
	 * @param lbbh 类别编号
	 * @param dmbh 代码编号
	 * @return
	 */
	Dmb findByLbbhAndDmbh(String lbbh, String dmbh);
	
	
	/**
	 * 获取某类别下所有的代码
	 * @param lbbh 类别编号
	 * @return
	 */
	List<Dmb> findByLbbh(String lbbh);
}
