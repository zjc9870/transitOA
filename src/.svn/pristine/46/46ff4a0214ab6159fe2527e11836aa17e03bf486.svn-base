package com.expect.admin.data.dao;

import java.util.List;

import com.expect.admin.data.dataobject.Lcrzb;
import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.DraftSwUserLcrzbGxb;

public interface DraftSwUserLcrzbGxbRepository extends JpaRepository<DraftSwUserLcrzbGxb, String> {

	public List<DraftSwUserLcrzbGxb> findByUserIdAndDraftSwId(String userId,String draftSwId);
	
	public DraftSwUserLcrzbGxb findByUserIdAndDraftSwIdAndRyflAndLcrzIsNull(String userId, String draftSwId, String ryfl);
	/**
	 * 获取某个收文记录的某类人员的未审批的记录
	 * @param draftSwId收文id
	 * @param ryfl 人员分类
	 * @return
	 */
	public List<DraftSwUserLcrzbGxb> findByDraftSwIdAndRyflAndLcrzIsNull(String draftSwId, String ryfl);
	
	public List<DraftSwUserLcrzbGxb> findBylcrz_cljg(String cljg);
	
	/**
	 * 根据收文id获取所有相关的操作记录信息
	 * @param draftSwId收文的ID
	 * @return
	 */
	List<DraftSwUserLcrzbGxb> findByDraftSwId(String draftSwId);

	DraftSwUserLcrzbGxb findByLcrz(Lcrzb Lcrz);
}
