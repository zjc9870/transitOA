package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.DraftSw;

public interface DraftSwRepository extends JpaRepository<DraftSw, String> {

	
	/**
	 * 获取收文发起人 的处于某状态的收文
	 * @param userId
	 * @param condition
	 * @return
	 */
	List<DraftSw> findBySwr_idAndSwztOrderByTjsjDesc(String userId, String condition);
	/**
	 * 根据收文状态获取收文列表
	 * @param condition
	 * @return
	 */
	List<DraftSw> findBySwzt(String condition);
	List<DraftSw> findBySwr_idAndSwztAndSwfl(String userId, String swzt, String swfl);

	List<DraftSw> findBySwr_idAndSwztAndSwflOrderByFqsjDesc(String userId, String swzt, String swfl);
	List<DraftSw> findBySwr_idAndSwfl(String userId, String swfl);
	/**
	 * 查一切
	 * @param spe
	 * @param page
	 * @return
	 */
	List<DraftSw> findAllByOrderByFqsjDesc(Specification<DraftSw> spe, Pageable page);
	List<DraftSw> findAllByOrderByFqsjDesc(Specification<DraftSw> spe);


	List<DraftSw> findAllByOrderByFqsjDesc();

	List<DraftSw> findAll(Specification<DraftSw> specification);
}
 