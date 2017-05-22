package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.Meeting;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.utils.DateUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by qifeng on 17/5/17.
 */
public class MeetingVo {
    private String id;
    private String bt;
    private String nr;
    private String sqbgs;
    private String sj;
    private String rs;
    private String rymd;
    private String gg;
    private List<AttachmentVo> attachmentList;//附件
    private String userName;
    private String hyshzt;
    private List<LcrzbVo> lcrzbList;//流程日志
    private String date;
    private String time;

    public MeetingVo(){}
    public MeetingVo(Meeting meeting){
        this.id=meeting.getId();
        this.bt=meeting.getBt();
        this.nr=meeting.getNr();
        this.sqbgs=meeting.getSqbgs();
        this.sj= DateUtil.format(meeting.getHysj(), DateUtil.noSecondFormat);
        this.rs=meeting.getRs();
        this.rymd=meeting.getRymd();
        this.gg=meeting.getGg();
        this.hyshzt=meeting.getHyshzt();
        this.userName=meeting.getNhyr().getFullName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getSqbgs() {
        return sqbgs;
    }

    public void setSqbgs(String sqbgs) {
        this.sqbgs = sqbgs;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getRymd() {
        return rymd;
    }

    public void setRymd(String rymd) {
        this.rymd = rymd;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public List<AttachmentVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentVo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHyshzt() {
        return hyshzt;
    }

    public void setHyshzt(String hyshzt) {
        this.hyshzt = hyshzt;
    }

    public List<LcrzbVo> getLcrzbList() {
        return lcrzbList;
    }

    public void setLcrzbList(List<LcrzbVo> lcrzbList) {
        this.lcrzbList = lcrzbList;
    }
}
