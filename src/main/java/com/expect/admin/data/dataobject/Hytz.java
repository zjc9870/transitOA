package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.HytzVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "m_hytz_inf")
public class Hytz {
    private String id;
    private String hyid; //会议id
    private User hytzr;//会议通知人
    private String tzdxfl;//通知对象分类(集团高管，集团部门,东山公交高管，东山公交部门，公司办公室，外部单位)
    private String tzdx;//通知对象
    private String isread;//会议通知是否已读
    private String tzid;//一次通知id
    private Date tzsj;
    private Date ydsj;
    private Set<Attachment> attachments; //附件

    public Hytz(){

    }

    public Hytz(HytzVo hytzVo){
        this.id=hytzVo.getId();
        this.hyid=hytzVo.getHyid();
        this.tzdxfl=hytzVo.getTzdxfl();
        this.tzdx=hytzVo.getTzdx();
        this.isread=hytzVo.getIsread();
        this.tzid=hytzVo.getTzid();
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

    @Column(name = "hyid",length = 50)
    public String getHyid() {
        return hyid;
    }

    public void setHyid(String hyid) {
        this.hyid = hyid;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "hytzr_id")
    public User getHytzr() {
        return hytzr;
    }

    public void setHytzr(User hytzr) {
        this.hytzr = hytzr;
    }

    @Column(name = "tzdxfl",length = 30)
    public String getTzdxfl() {
        return tzdxfl;
    }

    public void setTzdxfl(String tzdxfl) {
        this.tzdxfl = tzdxfl;
    }

    @Column(name = "tzdx",length = 150)
    public String getTzdx() {
        return tzdx;
    }

    public void setTzdx(String tzdx) {
        this.tzdx = tzdx;
    }

    @Column(name = "isread",length = 10)
    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    @Column(name = "tzid",length = 20)
    public String getTzid() {
        return tzid;
    }

    public void setTzid(String tzid) {
        this.tzid = tzid;
    }

    @Column(name = "tzsj")
    public Date getTzsj() {
        return tzsj;
    }

    public void setTzsj(Date tzsj) {
        this.tzsj = tzsj;
    }

    @Column(name = "ydsj")
    public Date getYdsj() {
        return ydsj;
    }

    public void setYdsj(Date ydsj) {
        this.ydsj = ydsj;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "m_hytz_attachment", joinColumns = @JoinColumn(name = "hytz_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
}
