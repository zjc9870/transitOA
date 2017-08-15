package com.expect.admin.data.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Function;

public interface FunctionRepository extends JpaRepository<Function, String> {

	/**
	 * 根据parentId获取所有的functions
	 */
	public List<Function> findByParentFunctionId(String parentId);

	/**
	 * 根据parentIds获取所有的functions
	 */
	public List<Function> findByParentFunctionIdIn(String[] parentIds);

	/**
	 * 获取所有最顶层部门
	 */
	public List<Function> findByParentFunctionIsNull();

	/**
	 * 获取给定id的所有function
	 */
	public Set<Function> findByIdIn(String[] ids);

}
