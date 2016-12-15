package com.expect.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.FuncitonJdgxbGxbRepository;
import com.expect.admin.service.vo.FunctionJdgxbGxbVo;

@Service
public class FunctionJdgxbGxbService {
	
	@Autowired
	private FuncitonJdgxbGxbRepository funcitonJdgxbGxbRepository;
	
	public FunctionJdgxbGxbVo getByFunctionId(String functionId){
		return new FunctionJdgxbGxbVo(
				funcitonJdgxbGxbRepository.findByFunctionId(functionId));
	}

}
