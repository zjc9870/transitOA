package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.NotifyObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qifeng on 17/5/10.
 */

public interface NotifyObjectRepository extends JpaRepository<NotifyObject,String> {
    List<NotifyObject> findByTzdxfl(String tzdxfl);
    NotifyObject findByTzdx(String tzdx);
}
