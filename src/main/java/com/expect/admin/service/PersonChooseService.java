package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.service.vo.PersonChooseVo;

@Service
public class PersonChooseService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	public List<PersonChooseVo> findAllDepartment() {
		List<PersonChooseVo> personChooseVolist =new  ArrayList<PersonChooseVo>();
		List<Department> departmentList = departmentRepository.findByParentDepartmentId(null);
		for (Department department : departmentList) {
			personChooseVolist.add(new PersonChooseVo(department));
		}
		return personChooseVolist;
	}
	

}
