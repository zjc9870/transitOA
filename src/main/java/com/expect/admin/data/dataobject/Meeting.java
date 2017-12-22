package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.MeetingVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="m_meeting_info")
public class Meeting {
    private String id;
    private String hyzt; //会议主题
    private String hynr; //会议内容
    private String hydd; //会议地点（集团 东山公交）
    private String hys; //会议室
    private String hyrq; //会议日期
    private String kssj; //会议开始时间
    private String jssj; //会议结束时间
    private String chry; //参会人员
    private String djrxm; //对接人姓名
    private String lxfs; //联系方式
    private String hygg; //会议规格
    private String qt; //其他
    private Set<Attachment> attachments;//附件
    private Date sqsj; //申请时间
    private String hyshzt; //会议审核状态
    private Set<Lcrzb> lcrzSet = new HashSet<>();//流程日志
    private User nhyr; //拟会议人
    private String hyfl; //会议分类（集团会议 东山公交会议 其他公司会议）
    private String sqd; //申请单名称


    public Meeting() {}

    public Meeting(MeetingVo meetingVo)
    {
        this.id=meetingVo.getId();
        this.hyzt=meetingVo.getHyzt();
        this.hynr=meetingVo.getHynr();
        this.hydd=meetingVo.getHydd();
        this.hys=meetingVo.getHys();
        this.hyrq=meetingVo.getHyrq();
        this.kssj=meetingVo.getKssj();
        this.jssj=meetingVo.getJssj();
        this.chry=meetingVo.getChry();
        this.djrxm=meetingVo.getDjrxm();
        this.lxfs=meetingVo.getLxfs();
        this.hygg=meetingVo.getHygg();
        this.qt=meetingVo.getQt();
        if(StringUtil.isBlank(meetingVo.getSqsj())){
            this.sqsj = new Date();
        } else {
            this.sqsj = DateUtil.parse(meetingVo.getSqsj(), DateUtil.fullFormat);
        }
        this.hyshzt=meetingVo.getHyshzt();
        this.hyfl=meetingVo.getHyfl();
        this.sqd = meetingVo.getSqd();
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

    @Column(name = "hyzt", length = 50)
    public String getHyzt() {
        return hyzt;
    }

    public void setHyzt(String hyzt) {
        this.hyzt = hyzt;
    }

    @Column(name = "hynr",length = 350)
    public String getHynr() {
        return hynr;
    }

    public void setHynr(String hynr) {
        this.hynr = hynr;
    }

    @Column(name = "hydd", length = 50)
    public String getHydd() {
        return hydd;
    }

    public void setHydd(String hydd) {
        this.hydd = hydd;
    }

    @Column(name = "hys", length = 50)
    public String getHys() {
        return hys;
    }

    public void setHys(String hys) {
        this.hys = hys;
    }

    @Column(name = "hyrq")
    public String getHyrq() {
        return hyrq;
    }

    public void setHyrq(String hyrq) {
        this.hyrq = hyrq;
    }

    @Column(name = "kssj")
    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    @Column(name = "jssj")
    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    @Column(name = "chry",length=250)
    public String getChry() {
        return chry;
    }

    public void setChry(String chry) {
        this.chry = chry;
    }

    @Column(name ="djrxm",length=10)
    public String getDjrxm() {
        return djrxm;
    }

    public void setDjrxm(String djrxm) {
        this.djrxm = djrxm;
    }

    @Column(name = "lxfs",length=20)
    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }


    @Column(name = "hygg",length=50)
    public String getHygg() {
        return hygg;
    }

    public void setHygg(String hygg) {
        this.hygg = hygg;
    }

    @Column(name = "qt",length = 250)
    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "m_meeting_attachment", joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Column(name = "hyshzt", length=20)
    public String getHyshzt() {
        return hyshzt;
    }

    public void setHyshzt(String hyshzt) {
        this.hyshzt = hyshzt;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "m_meeting_lcrzb", joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "lcrzb_id"))
    public Set<Lcrzb> getLcrzSet() {
        return lcrzSet;
    }

    public void setLcrzSet(Set<Lcrzb> lcrzSet) {
        this.lcrzSet = lcrzSet;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "nhyr_id")
    public User getNhyr() {
        return nhyr;
    }

    public void setNhyr(User nhyr) {
        this.nhyr = nhyr;
    }

    @Column(name = "hyfl",length = 20)
    public String getHyfl() {
        return hyfl;
    }

    public void setHyfl(String hyfl) {
        this.hyfl = hyfl;
    }

    @Column(name = "sqd",length = 50)
    public String getSqd() {
        return sqd;
    }

    public void setSqd(String sqd) {
        this.sqd = sqd;
    }

    @Column(name = "sqsj")
    public Date getSqsj() {
        return sqsj;
    }

    public void setSqsj(Date sqsj) {
        this.sqsj = sqsj;
    }

}
