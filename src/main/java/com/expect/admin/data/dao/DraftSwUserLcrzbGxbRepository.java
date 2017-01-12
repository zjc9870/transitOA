package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.DraftSwUserLcrzbGxb;

public interface DraftSwUserLcrzbGxbRepository extends JpaRepository<DraftSwUserLcrzbGxb, String> {

	public List<DraftSwUserLcrzbGxb> findByUserIdAndDraftSwId(String userId,String draftSwId);
	
	public List<DraftSwUserLcrzbGxb> findBylcrz_cljg(String cljg);
}
