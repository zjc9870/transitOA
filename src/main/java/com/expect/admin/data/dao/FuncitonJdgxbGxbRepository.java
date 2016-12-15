package com.expect.admin.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.FunctionJdgxbGxb;


public interface FuncitonJdgxbGxbRepository extends JpaRepository<FunctionJdgxbGxb, String> {

	public FunctionJdgxbGxb findByFunctionId(String functionId);
}
