package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by qifeng on 17/3/6.
 */
@Entity
@Table(name = "s_document_inf")
public class Document {
    /**
     * 密级 绝密
     */
    public static final String MJ_JM = "1";
    /**
     * 密级 机密
     */
    public static final String MJ_JIM = "2";
    /**
     * 密级 秘密
     */
    public static final String MJ_MM = "3";

    /**
     * 发文分类 未提交
     */
    public static final String FWFL_WTJ = "W";
    /**
     * 发文分类 首轮 已提交
     */
    public static final String FWFL_SLYTJ = "Y";
    /**
     * 发文分类 多轮 已提交
     */
    public static final String FWFL_DLYTJ = "N";

    private String id;
    private String bh;//公文编号
    private String bt;//公文标题
    private String ztc;//主题词
    private User ngwr;//拟公文人
    private String mj;//密级
    private String sffb;//Y是发布,N是不发布
    private Set<Attachment> attachments;//附件
    private String lcbs;//流程标识
    private String gwshzt;//公文审核状态
    private String gwfl;//公文分类(提交 未提交)
    private Date sqsj;//申请时间
    private String sfsc;//是否已经删除（采用软删除，只标识，不删除）
    private String sfth;//是否退回（Y 是退回， N不是退回）
    private Set<Lcrzb> lcrzSet = new HashSet<>();//流程日志
    private String sbd;//申办单名称
    private String htbh;//打印回填编号
    private String yfrq;
    private String shrq;//审核日期
    private String spyj;//审批意见
    private String zgs;//子公司名称
    private User spr;//审批人

    public Document(){

    }

    public Document(DocumentVo documentVo) {
        this.id=documentVo.getId();
        this.bh=documentVo.getBh();
        this.bt=documentVo.getBt();
        this.ztc=documentVo.getZtc();
        this.mj=documentVo.getMj();
        this.sffb=documentVo.getSffb();
        this.lcbs=documentVo.getLcbs();
        this.gwshzt=documentVo.getGwshzt();
        this.gwfl=documentVo.getGwfl();
        if(StringUtil.isBlank(documentVo.getSqsj())){
            this.sqsj = new Date();
        }else {
            this.sqsj = DateUtil.parse(documentVo.getSqsj(), DateUtil.fullFormat);
        }
        this.sfsc=documentVo.getSfsc();
        this.sfth=documentVo.getSfth();
        if(!StringUtil.isBlank(documentVo.getSbd()))
            this.sbd = documentVo.getSbd();
        this.htbh=documentVo.getHtbh();
        this.zgs=documentVo.getZgs();
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

    @Column(name = "bh", length = 20)
    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    @Column(name = "bt", length = 20)
    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    @Column(name = "ztc", columnDefinition="TEXT")
    public String getZtc() {
        return ztc;
    }

    public void setZtc(String ztc) {
        this.ztc = ztc;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ngwr_id")
    public User getNgwr() {
        return ngwr;
    }

    public void setNgwr(User ngwr) {
        this.ngwr = ngwr;
    }

    @Column(name = "mj", length=10)
    public String getMj() {
        return mj;
    }

    public void setMj(String mj) {
        this.mj = mj;
    }

    @Column(name = "gwfl", length = 20)
    public String getGwfl() {
        return gwfl;
    }

    public void setGwfl(String gwfl) {
        this.gwfl = gwfl;
    }

    @Column(name = "sqsj")
    public Date getSqsj() {
        return sqsj;
    }

    public void setSqsj(Date sqsj) {
        this.sqsj = sqsj;
    }

    @Column(name ="sfsc" , length = 2)
    public String getSfsc() {
        return sfsc;
    }

    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }

    @Column(name = "sfth" , length=2 )
    public String getSfth() {
        return sfth;
    }

    public void setSfth(String sfth) {
        this.sfth = sfth;
    }

    @Column(name = "sbd" , length =50)
    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }

    @Column(name = "sffb", length=2)
    public String getSffb() {
        return sffb;
    }

    public void setSffb(String sffb) {
        this.sffb = sffb;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "s_document_attachment", joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Column(name = "lcbs", length=32)
    public String getLcbs() {
        return lcbs;
    }

    public void setLcbs(String lcbs) {
        this.lcbs = lcbs;
    }

    @Column(name = "gwshzt", length=10)
    public String getGwshzt() {
        return gwshzt;
    }

    public void setGwshzt(String gwshzt) {
        this.gwshzt = gwshzt;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "s_document_lcrzb", joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "lcrzb_id"))
    public Set<Lcrzb> getLcrzSet() {
        return lcrzSet;
    }

    public void setLcrzSet(Set<Lcrzb> lcrzSet) {
        this.lcrzSet = lcrzSet;
    }

    @Column(name = "htbh" , length=20)
    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    @Column(name="yfrq", length=10)
    public String getYfrq() {
        return yfrq;
    }

    public void setYfrq(String yfrq) {
        this.yfrq = yfrq;
    }

    @Column(name="shrq",length=10)
    public String getShrq() {
        return shrq;
    }

    public void setShrq(String shrq) {
        this.shrq = shrq;
    }

    @Column(name="spyj",length=300)
    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    @OneToOne
    @JoinColumn(name = "spr_id")
    public User getSpr() {
        return spr;
    }

    public void setSpr(User spr) {
        this.spr = spr;
    }

    @Column(name = "zgs", length = 20)
    public String getZgs() {
        return zgs;
    }

    public void setZgs(String zgs) {
        this.zgs = zgs;
    }

}
