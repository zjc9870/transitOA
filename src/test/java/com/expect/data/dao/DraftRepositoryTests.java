//package com.expect.data.dao;
//
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.expect.admin.data.dao.DraftRepository;
//import com.expect.admin.data.dataobject.Draft;
//import com.expect.util.BaseTest;
//
//public class DraftRepositoryTests extends BaseTest {
//	
//	@Autowired
//	DraftRepository draftRepository;
//
////	@Test
//	@Ignore
//	public void saveTest(){
//		Assert.assertEquals(0, draftRepository.count());
//		Draft draft = new Draft();
//		draft.setBh("2222222");
//		draft.setSfjtng("Y");
//		draft.setNgshzt("1");
//		draftRepository.save(draft);
//		Assert.assertEquals(1, draftRepository.count());
//	}
//	
////	@Test
//	@Ignore
//	public void updateTest(){
//		Draft d = draftRepository.findAll().get(0);
//		d.setBmfzrhgyj("sssssssssssssssssss");
//		draftRepository.save(d);
//		Assert.assertEquals(1, draftRepository.count());
//	}
//	
////	@Test
//	@Ignore
//	public void findTest(){
//		Assert.assertEquals(1, draftRepository.findBySfjtngAndNgshzt("Y", "1").size());
//	}
//	
//	@Test
////	@Ignore
//	public void getContract() {
//		Draft d = draftRepository.findById("1");
//		Assert.assertEquals("风好大风", d.getNgr());
////		System.out.println();
//	}
//}
