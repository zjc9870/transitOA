package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Lcb;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Lcjdgxb;

public interface LcjdgxbRepository extends JpaRepository<Lcjdgxb, String> {

	public List<Lcjdgxb> findByKsjd(String ksjd);
	public Lcjdgxb findByLcbsAndKsjd(String lcbs, String ksjd);
}
