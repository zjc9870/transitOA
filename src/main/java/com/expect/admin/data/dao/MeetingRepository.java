package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by qifeng on 17/5/17.
 */
public interface MeetingRepository extends JpaRepository<Meeting,String> {
    List<Meeting> findByNhyr_idAndHyshztOrderByHysjDesc(String nhyr_id,String hyshzt);
}
