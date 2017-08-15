package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.Document;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by qifeng on 17/3/7.
 */

public interface DocumentRepository extends JpaRepository<Document,String> {
    public Document findById(String id);
    public Document findByBh(String bh);
    public List<Document> findAll(Specification<Document> spec);
    List<Document> findByGwshztOrderBySqsjDesc(String condition);
    List<Document> findByGwflOrderBySqsjDesc(String contion);


    @Query("select distinct d from Document d, Lcrzb l where d.gwshzt =  ?2 and d.sfth = 'Y' and d.id = l.clnrid "
            + "and l.user.id = ?1 order by d.sqsj desc")
    public List<Document> findYthDocument(String userId, String curCondition);

    List<Document> findByNgwr_idAndGwshztOrderBySqsjDesc(String userId, String condition);

    @Query("select distinct d from Document d, Lcrzb l where d.id = l.clnrid and l.user.id = ?1 and l.dyjd = 'T' order by d.sqsj desc")
    List<Document> findYhtDocument(String userId);


    /**
     * 某用户已审批的合同记录
     * 对于一些退回的合同，可能会存在在待审批，已审批中出现同一条记录的情况，
     * 现在通过“and c.htshzt <> ?2” 去除这种情况
     * @param userId
     *
     * @return
     */
//	@Query("select distinct c from Contract c, Lcrzb l where c.id = l.clnrid and l.user.id = ?1 and l.cljg = '通过'")
    @Query("select distinct d from Document d, Lcrzb l where d.id = l.clnrid and l.user.id = ?1 and d.gwshzt <> ?2 order by d.sqsj desc")
    List<Document> findYspDocument(String userId, String condition);

}
