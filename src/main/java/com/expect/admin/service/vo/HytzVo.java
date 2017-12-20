package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Hytz;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.utils.DateUtil;

import java.util.List;

public class HytzVo {
    private String id;
    private String hyid; //会议id
    private String hytzr;//会议通知人
    private String tzdxfl;//通知对象分类(集团高管，集团部门,东山公交高管，东山公交部门，公司办公室，外部单位)
    private String tzdx;//通知对象
    private String isread;//会议通知是否已读
    private String jtgg;
    private String jtbm;
    private String dsgjgg;
    private String dsgjbm;
    private String gsbgs;
    private String wbdw;
    private String tzid;
    private String tzsj;
    private String ydsj;
    private User hytzrId; //会议通知人id
    private List<AttachmentVo> attachmentList; //附件

    public HytzVo(){

    }

    public HytzVo(Hytz hytz){
        this.id=hytz.getId();
        this.hyid=hytz.getHyid();
        this.hytzr=hytz.getHytzr().getFullName();
        this.hytzrId=hytz.getHytzr();
        this.tzdxfl=hytz.getTzdxfl();
        this.tzdx=hytz.getTzdx();
        this.isread=hytz.getIsread();
        this.tzid=hytz.getTzid();
        if (hytz.getTzsj() == null) {
            this.tzsj = "";
        }
        else {
            this.tzsj = DateUtil.format(hytz.getTzsj(), "yyyy/MM/dd HH:mm:ss");
        }
        if (hytz.getYdsj() == null) {
            this.ydsj = "";
        }
        else
            this.ydsj = DateUtil.format(hytz.getYdsj(), "yyyy/MM/dd HH:mm:ss");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHyid() {
        return hyid;
    }

    public void setHyid(String hyid) {
        this.hyid = hyid;
    }

    public String getHytzr() {
        return hytzr;
    }

    public void setHytzr(String hytzr) {
        this.hytzr = hytzr;
    }

    public String getTzdxfl() {
        return tzdxfl;
    }

    public void setTzdxfl(String tzdxfl) {
        this.tzdxfl = tzdxfl;
    }

    public String getTzdx() {
        return tzdx;
    }

    public void setTzdx(String tzdx) {
        this.tzdx = tzdx;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getJtgg() {
        return jtgg;
    }

    public void setJtgg(String jtgg) {
        this.jtgg = jtgg;
    }

    public String getJtbm() {
        return jtbm;
    }

    public void setJtbm(String jtbm) {
        this.jtbm = jtbm;
    }

    public String getDsgjgg() {
        return dsgjgg;
    }

    public void setDsgjgg(String dsgjgg) {
        this.dsgjgg = dsgjgg;
    }

    public String getDsgjbm() {
        return dsgjbm;
    }

    public void setDsgjbm(String dsgjbm) {
        this.dsgjbm = dsgjbm;
    }

    public String getGsbgs() {
        return gsbgs;
    }

    public void setGsbgs(String gsbgs) {
        this.gsbgs = gsbgs;
    }

    public String getWbdw() {
        return wbdw;
    }

    public void setWbdw(String wbdw) {
        this.wbdw = wbdw;
    }

    public String getTzid() {
        return tzid;
    }

    public void setTzid(String tzid) {
        this.tzid = tzid;
    }

    public String getTzsj() {
        return tzsj;
    }

    public void setTzsj(String tzsj) {
        this.tzsj = tzsj;
    }

    public String getYdsj() {
        return ydsj;
    }

    public void setYdsj(String ydsj) {
        this.ydsj = ydsj;
    }

    public User getHytzrId() {
        return hytzrId;
    }

    public void setHytzrId(User hytzrId) {
        this.hytzrId = hytzrId;
    }

    public List<AttachmentVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentVoList(List<AttachmentVo> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
