package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.Fwtz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expect.admin.data.dataobject.Fwtz;

import java.util.List;

/**
 * Created by qifeng on 17/3/28.
 */
public interface FwtzRepository extends JpaRepository<Fwtz, String> {
    public List<Fwtz> findByTzdx(String tzdx);
    public List<Fwtz> findByTzid(String tzid);

    @Query("select f from Fwtz as f where f.fwid = ?1 and f.tzid = (select max (b.tzid) from Fwtz as b where b.fwid =?1)")
    public List<Fwtz> findFwtz(String paramString);

    @Query("select f.tzid from Fwtz as f where f.fwid = ?1")
    public List<String> findTzidByDocumentId(String paramString);

    public List<Fwtz> findByFwid(String paramString);

    public List<Fwtz> findByFwidAndTzdx(String fwid, String tzdx);

    public List<Fwtz> findByFwidAndTzdxAndTzlxAndTzdxfl(String paramString1, String paramString2, String paramString3, String paramString4);
}