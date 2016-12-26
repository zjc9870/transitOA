package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expect.admin.data.dataobject.Contract;

public interface ContractRepository extends JpaRepository<Contract, String>{

	public Contract findById(String id);
	
	public List<Contract> findAll(Specification<Contract> spec);
	
	public List<Contract> findByHtflAndHtshzt(String htfl, String htshzt);
	
	List<Contract> findByHtshztOrderBySqsjDesc(String condition);
	
	@Query("select c from Contract c where c.nhtr.id = ?1 and c.htshzt <> 'T' and c.htshzt <> 'Y' and c.htshzt <> ?2 order by c.sqsj desc")
	public List<Contract> findSqjlWspList(String userId, String condition);
	
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2 order by c.sqsj desc")
	public List<Contract> findByUserAndCondition(String userId, String condition);
	
//	@Query("select distinct c from Contract c where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2")
//	public List<Contract> findDspContract(String condition)
	/**
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
	 * 某用户已审批的合同记录
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
//	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.cljg = '通过'")
	@Query("select c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.cljg = ?2 order by c.sqsj desc")
	List<Contract> findYspContract(String userId, String cljg);
	
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
