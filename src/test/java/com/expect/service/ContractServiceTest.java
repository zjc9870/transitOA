package com.expect.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.expect.admin.service.ContractService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.util.BaseTest;

public class ContractServiceTest extends BaseTest {

	@Autowired
	private ContractService contractService;
	@Test
	public void testSearchContract() {
		List<ContractVo> contractVoList = contractService.searchContract("", "", null, null, null, null);
		assertNotNull("contractVoList is null", contractVoList);
		assertEquals(30, contractVoList.size());
	}
	

}
