package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.Meeting;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,String> {
    public Meeting findById(String id);
    
    public List<Meeting> findAll(Specification<Meeting> spec);

    public List<Meeting> findByHyflAndHyshzt(String hyfl, String hyshzt);

    List<Meeting> findByHyshztOrderBySqsjDesc(String condition);

    List<Meeting> findByHyshztAndHyflOrderBySqsjDesc(String condition, String hyfl);

    public List<Meeting> findByNhyr_idAndHyshzt(String userId, String condition);

    @Query("select m.kssj, m.jssj from Meeting as m where m.hydd = ?1 and m.hys = ?2 and m.hyrq = ?3 and m.hyshzt <> 'revocation' and m.hyshzt <> 'N'")
    public List<Object> findHysSyqk(String paramString1, String paramString2, String paramString3);

    @Query("select m.kssj, m.jssj from Meeting as m where m.hydd = ?1 and m.hys = ?2 and m.hyrq = ?3 and m.id <> ?4 and m.hyshzt <> 'revocation' and m.hyshzt <> 'N'")
    public List<Object> findHysSyqk2(String paramString1, String paramString2, String paramString3, String paramString4);

    @Query("select count(m) from Meeting as m where m.hyshzt = ?1 and m.hyfl =?2")
    public int findCountMeeting(String hyshzt, String hyfl);

}
