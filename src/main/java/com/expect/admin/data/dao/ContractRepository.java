package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expect.admin.data.dataobject.Contract;

public interface ContractRepository extends JpaRepository<Contract, String>{

	public Contract findById(String id);
	
	/**
	 * 合同编号
	 * @param htbh
	 * @return
	 */
	public Contract findByBh(String htbh);
	
	public List<Contract> findAll(Specification<Contract> spec);
	
	public List<Contract> findByHtflAndHtshzt(String htfl, String htshzt);
	
	List<Contract> findByHtshztOrderBySqsjDesc(String condition);
	
	/**
	 * 这个方法用来统计某一年有多少个电子序号，用来生成电子序号的后四位
	 * 如参数 "2017%" 用来统计2017年生成的电子序号数 
	 * @param partOfSequenceNumber 电子序号的前四位 即 年份%（如"2017%"）
	 * @return
	 */
	int countBySequenceNumberLike(String partOfSequenceNumber);
	
	@Query("select c from Contract c where c.nhtr.id = ?1 and c.htshzt <> 'T' and c.htshzt <> 'Y' and c.htshzt <> ?2 order by c.sqsj desc")
	public List<Contract> findSqjlWspList(String userId, String condition);
	
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2 order by c.sqsj desc")
	public List<Contract> findByUserAndCondition(String userId, String condition);
	
//	@Query("select distinct c from Contract c where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2")
//	public List<Contract> findDspContract(String condition)
	/**
	 * 根据你合同人id 和合同的状态获取合同列表
	 * 获取某人用户未提交的合同记录
	 * @param userId
	 * @param condition
	 * @return
	 */
	List<Contract> findByNhtr_idAndHtshztOrderBySqsjDesc(String userId, String condition);
	
	/**
	 * 某用户已回填的记录
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.dyjd = 'T' order by c.sqsj desc")
	List<Contract> findYhtContract(String userId);

	/**
	 * 待回填记录
	 * @return
//     */
//	@Query("select distinct c from Contract c,Lcrzb l where c.id = l.clnrid and l.dyjd = '5' and l.cljg = '通过' order by c.sqsj desc ")
//	List<Contract> findDhtContract();
//	@Query("select distinct c from Contract c,Lcrzb l1 where c.id = l1.clnrid and l1.dyjd = '5' and l1.cljg = '通过' and c.htshzt='Y' and c.id in (select l2.clnrid from Lcrzb l2 where l2.dyjd='4' and l2.user.id= ?1) order by c.sqsj desc")
//	List<Contract> findDhtContract(String userId);
	@Query("select distinct c from Contract c,Lcrzb l1 where c.id = l1.clnrid and l1.dyjd in (select r1.jdId from RoleJdgxbGxb r1 where r1.roleId='2c913b71590fcb3201590fd63f20002a') and " +
			"l1.cljg = '通过' and c.htshzt='Y' and c.id in (select l2.clnrid from Lcrzb l2 where l2.dyjd in (select r2.jdId from RoleJdgxbGxb r2 where r2.roleId='2c913b71590fcb3201590fd6076e0024') and l2.user.id= ?1) order by c.sqsj desc")
	List<Contract> findDhtContract(String userId);
	/**
	 * 某用户已审批的合同记录
	 * 对于一些退回的合同，可能会存在在待审批，已审批中出现同一条记录的情况，
	 * 现在通过“and c.htshzt <> ?2” 去除这种情况
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
//	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.cljg = '通过'")
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and c.htshzt <> ?2 order by c.sqsj desc")
	List<Contract> findYspContract(String userId, String condition);
	
	/**
	 * 已退回
	 * @param userId
	 * @param curCondition
	 * @return
	 */
	@Query("select distinct c from Contract c, Lcrzb l where c.htshzt =  ?2 and c.sfth = 'Y' and c.id = l.clnrid "
			+ "and l.user.id = ?1 order by c.sqsj desc")
	public List<Contract> findYthContract(String userId, String curCondition);
	
	/**
	 * 更新合同编号
	 * @param id
	 * @param htbh
	 */
	@Query("update Contract c set c.bh = ?2 where c.id = ?1")
	void updateHtbh(String id, String htbh);
	
}
