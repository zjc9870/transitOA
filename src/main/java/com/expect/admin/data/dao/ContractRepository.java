package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Contract;

public interface ContractRepository extends JpaRepository<Contract, String>{

	public Contract findById(String id);
	
	public List<Contract> findByHtflAndHtshzt(String htfl, String htshzt);
}
