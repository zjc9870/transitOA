package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Lcjdgxb;
import org.springframework.data.jpa.repository.Query;

public interface LcjdgxbRepository extends JpaRepository<Lcjdgxb, String> {

	public List<Lcjdgxb> findByKsjd(String ksjd);
	public Lcjdgxb findByLcbsAndKsjd(String lcbs, String ksjd);
	public List<Lcjdgxb> findByLcbsOrderByXssx(String lcbs);
	@Query(value="select * from Lcjdgxb  order by lc_id desc limit 0,1",nativeQuery=true)
	public Lcjdgxb fingMaxLcId();
}
