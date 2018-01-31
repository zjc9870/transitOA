package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Lcjdb;
import org.springframework.data.jpa.repository.Query;

public interface LcjdbRepository extends JpaRepository<Lcjdb, String> {

	public List<Lcjdb> findBySslc(String sslc);
	
	/**
	 * 
	 * @param sslc 所属流程
	 * @param category 节点分类
	 * @return 流程节点
	 */
	public Lcjdb findBySslcAndCategory(String sslc, String category);

	@Query(value="select * from Lcjdb order by sslc_id desc limit 0,1",nativeQuery=true)
	public Lcjdb findMaxLcId();
}
