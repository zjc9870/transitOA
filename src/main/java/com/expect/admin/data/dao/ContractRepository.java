package com.expect.admin.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expect.admin.data.dataobject.Contract;

public interface ContractRepository extends JpaRepository<Contract, String>{

	public Contract findById(String id);
	
	public List<Contract> findAll(Specification<Contract> spec);
	
	public List<Contract> findByHtflAndHtshzt(String htfl, String htshzt);
	
	List<Contract> findByHtshzt(String condition);
	
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2")
	public List<Contract> findByUserAndCondition(String userId, String condition);
	
//	@Query("select distinct c from Contract c where c.id = l.clnrid and l.user.id = ?1 and c.htshzt = ?2")
//	public List<Contract> findDspContract(String condition)
	/**
	 * 获取某人用户未提交的合同记录
	 * @param userId
	 * @param condition
	 * @return
	 */
	List<Contract> findByNhtr_idAndHtshzt(String userId, String condition);
	
	/**
	 * 某用户已回填的记录
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.dyjd = 'T' and"
			+ " l.clsj between ?2 and ?3")
	List<Contract> findYhtContract(String userId, Date start, Date end);
	
	/**
	 * 某用户已审批的合同记录
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.sfxs = 'Y' and"
			+ " l.clsj between ?2 and ?3")
	List<Contract> findYspContract(String userId, Date start, Date end);
	
	/**
	 * 已退回
	 * @param userId
	 * @param curCondition
	 * @return
	 */
	@Query("select distinct c from Contract c, Lcrzb l where c.htshzt =  ?2 and c.sfth = 'Y' and c.id = l.clnrid "
			+ "and l.user.id = ?1")
	public List<Contract> findYthContract(String userId, String curCondition);
	
	/**
	 * 更新合同编号
	 * @param id
	 * @param htbh
	 */
	@Query("update Contract c set c.bh = ?2 where c.id = ?1")
	void updateHtbh(String id, String htbh);
	
}
