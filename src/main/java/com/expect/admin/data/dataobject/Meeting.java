package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.MeetingVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by qifeng on 17/5/17.
 */
@Entity
@Table(name = "m_meeting_info")
public class Meeting {
    private String id;
    private String bt;
    private String nr;
    private String sqbgs;
    private Date hysj;
    private String rs;
    private String rymd;
    private String gg;
    private Set<Attachment> attachments;//附件
    private User nhyr;
    private String hyshzt;
    private Set<Lcrzb> lcrzSet = new HashSet<>();//流程日志

    public Meeting(){}

    public Meeting(MeetingVo meetingVo){
        this.bt=meetingVo.getBt();
        this.nr=meetingVo.getNr();
        this.sqbgs=meetingVo.getSqbgs();
        this.rs=meetingVo.getRs();
        this.rymd=meetingVo.getRymd();
        this.gg=meetingVo.getGg();
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

    @Column(name = "bt", length = 50)
    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    @Column(name = "nr", length = 300)
    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    @Column(name = "sqbgs", length = 20)
    public String getSqbgs() {
        return sqbgs;
    }

    public void setSqbgs(String sqbgs) {
        this.sqbgs = sqbgs;
    }

    @Column(name = "hysj")
    public Date getHysj() {
        return hysj;
    }

    public void setHysj(Date hysj) {
        this.hysj = hysj;
    }

    @Column(name = "rs", length = 10)
    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    @Column(name = "rymd", length = 100)
    public String getRymd() {
        return rymd;
    }

    public void setRymd(String rymd) {
        this.rymd = rymd;
    }

    @Column(name = "gg", length = 10)
    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "nhyr_id")
    public User getNhyr() {
        return nhyr;
    }

    public void setNhyr(User nhyr) {
        this.nhyr = nhyr;
    }

    @Column(name = "hyshzt", length=5)
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
}
