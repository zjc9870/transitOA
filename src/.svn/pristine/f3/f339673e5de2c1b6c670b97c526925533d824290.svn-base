package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.FwtzVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qifeng on 17/3/24.
 */
@Entity
@Table(name = "s_fwtz_inf")
public class Fwtz {
    private String id;
    private User tzfqr;//通知发起人
    private String fwid;//公文id
    private String tzdxfl;//通知对象分类(集团高管,集团部门,外部单位,其他公司办公室,公司高管,公司部门,公司外部单位)
    private String tzdx;//通知对象
    private String tzlx;//通知类型(主送1,抄报2,抄送3)
    private String isread;//发文是否已读
    private String tzid;//一次通知id
    private Date tzsj;//通知时间
    private Date ydsj;//已读时间


    public Fwtz(){

    }

    public Fwtz(FwtzVo fwtzVo){
        this.id=fwtzVo.getId();
        this.fwid=fwtzVo.getFwid();
        this.tzdxfl=fwtzVo.getTzdxfl();
        this.tzdx=fwtzVo.getTzdx();
        this.tzlx=fwtzVo.getTzlx();
        this.isread=fwtzVo.getIsread();
        this.tzid=fwtzVo.getTzid();
    }
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "tzfqr_id")
    public User getTzfqr() {
        return tzfqr;
    }

    public void setTzfqr(User tzfqr) {
        this.tzfqr = tzfqr;
    }

    @Column(name = "fwid",length = 50)
    public String getFwid() {
        return fwid;
    }

    public void setFwid(String fwid) {
        this.fwid = fwid;
    }

    @Column(name = "tzdxfl",length = 30)
    public String getTzdxfl() {
        return tzdxfl;
    }

    public void setTzdxfl(String tzdxfl) {
        this.tzdxfl = tzdxfl;
    }

    @Column(name="tzdx",length=30)
    public String getTzdx() {
        return tzdx;
    }

    public void setTzdx(String tzdx) {
        this.tzdx = tzdx;
    }

    @Column(name="tzlx",length=10)
    public String getTzlx() {
        return tzlx;
    }

    public void setTzlx(String tzlx) {
        this.tzlx = tzlx;
    }

    @Column(name="isread",length=10)
    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    @Column(name="tzid",length=20)
    public String getTzid() {
        return tzid;
    }

    public void setTzid(String tzid) {
        this.tzid = tzid;
    }

    public Date getTzsj() {
        return tzsj;
    }

    @Column(name="tzsj")
    public void setTzsj(Date tzsj) {
        this.tzsj = tzsj;
    }

    @Column(name="ydsj")
    public Date getYdsj() {
        return ydsj;
    }

    public void setYdsj(Date ydsj) {
        this.ydsj = ydsj;
    }
}

