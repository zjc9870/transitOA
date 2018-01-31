package com.expect.admin.data.dao;


import com.expect.admin.data.dataobject.Hytz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HytzRepository extends JpaRepository<Hytz, String>{
    public List<Hytz> findByTzdx(String tzdx);
    public List<Hytz> findByTzid(String tzid);

//    @Query("select f from Hytz as f where f.fwid = ?1 and f.tzid = (select max (b.tzid) from Fwtz as b where b.fwid =?1)")
//    public List<Hytz> findFwtz(String paramString);
//
//    @Query("select f.tzid from Hytz as f where f.fwid = ?1")
//    public List<String> findTzidByDocumentId(String paramString);

    public List<Hytz> findByHyid(String paramString);

    public List<Hytz> findByHyidAndTzdxAndTzdxfl(String paramString1, String paramString2, String paramString3);

    @Query("select count(h) from Hytz as h where h.tzdx = ?1 and h.isread = ?2")
    public int findCountHytz(String tzdx, String tzzt);
}
