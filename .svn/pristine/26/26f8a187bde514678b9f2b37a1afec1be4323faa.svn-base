package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Draft;

public interface DraftRepository extends JpaRepository<Draft, String>{

	/**
	 * 获取特定种类（是否集团拟稿） 特定状态的拟稿
	 * @param sfjt    是否集团拟稿（Y,N）
	 * @param ngshzt（拟稿审核状态）
	 * @return
	 */
	
	public List<Draft> findByNgflAndNgshzt(String ngfl, String ngshzt);
	
	public Draft findById(String id);
	
}
