package com.expect.admin.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qifeng on 17/3/28.
 */
public interface FwtzRepository extends JpaRepository<Fwtz, String> {
    public List<Fwtz> findByTzdx(String tzdx);
    public List<Fwtz> findByTzid(String tzid);
}