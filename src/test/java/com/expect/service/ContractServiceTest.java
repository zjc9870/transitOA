
package com.expect.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.expect.admin.service.ContractService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.util.BaseTest;

public class ContractServiceTest extends BaseTest {

	@Autowired
	private ContractService contractService;
	@Autowired
	private RoleJdgxbGxbService roleJdgxbGxbService;
	
	@Before
	public void before() {
	}
//	@Test
	@Ignore
	public void testSearchContract() {
		List<ContractVo> contractVoList = contractService.searchContract("", "", null, null, null, null, null);
		assertNotNull("contractVoList is null", contractVoList);
		assertEquals(1, contractVoList.size());
	}
	
//	@Test
	@Ignore
	public void getListTimeTest() {
		String lx = "dsp";
		long start = System.currentTimeMillis();
		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		contractService.getContractByUserIdAndCondition("2c913b71590fcb3201590fd15ada0007",
				condition.getJdId(), lx);
		long end = System.currentTimeMillis();
		System.out.println((end - start));
	}
	
	@Test
	public void isHtbhUniqueTest() {
		assertTrue(contractService.isHtbhUnique("0000000"));
		assertFalse(contractService.isHtbhUnique("sds"));
	}
	

}

