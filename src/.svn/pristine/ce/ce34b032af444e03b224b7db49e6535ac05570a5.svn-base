package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.NotifyObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by qifeng on 17/5/10.
 */

public interface NotifyObjectRepository extends JpaRepository<NotifyObject,String> {
    List<NotifyObject> findByTzdxfl(String tzdxfl);
    NotifyObject findByTzdx(String tzdx);
    @Query("select distinct n from NotifyObject n where n.tzdx.user.ssgs.id = ?1")
    List<NotifyObject> findBySsgsid(String ssgsid);
}
