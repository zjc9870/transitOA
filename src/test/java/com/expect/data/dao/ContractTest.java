package com.expect.data.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Contract;
import com.expect.util.BaseTest;

public class ContractTest extends BaseTest {

	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Test
	public void test() {
		Contract contract = contractRepository.findOne("2c9132dd590d4b5401590d4bf1170001");
		Attachment attachment = attachmentRepository.findOne("2c914fca591759c9015917ad84d90005");
		Attachment attachment2 = attachmentRepository.findOne("2c914fca591759c9015917ad84db0006");
		assertNotNull(attachment);
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		attachmentList.add(attachment2);
//		attachmentList.add(attachment);
//		contract.setAttachments(attachmentList);
		contractRepository.save(contract);
	}

}
