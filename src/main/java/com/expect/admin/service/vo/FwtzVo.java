package com.expect.admin.service.vo;

/**
 * Created by qifeng on 17/3/26.
 */
public class FwtzVo {
    private String id;
    private String tzfqr;//通知发起人
    private String fwid;//发文id
    private String tzdxfl;//通知对象分类(集团高管,集团部门,外部单位,其他公司办公室)
    private String tzdx;//通知对象
    private String tzlx;//通知类型(主送1,抄报2,抄送3)
    private String isread;//发文是否已读
    private String zsjtgg;
    private String zsjtbm;
    private String zsqtgsbgs;
    private String zswbdw;
    private String cbjtgg;
    private String cbjtbm;
    private String cbqtgsbgs;
    private String cbwbdw;
    private String csjtgg;
    private String csjtbm;
    private String csqtgsbgs;
    private String cswbdw;
    private String zsgsgg;
    private String zsgsbm;
    private String cbgsgg;
    private String cbgsbm;
    private String csgsgg;
    private String csgsbm;
    private String tzid;

    public FwtzVo(){

    }
    public FwtzVo(Fwtz fwtz){
        this.id=fwtz.getId();
        this.tzfqr=fwtz.getTzfqr().getFullName();
        this.fwid=fwtz.getFwid();
        this.tzdxfl=fwtz.getTzdxfl();
        this.tzdx=fwtz.getTzdx();
        this.tzlx=fwtz.getTzlx();
        this.isread=fwtz.getIsread();
        this.tzid=fwtz.getTzid();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTzfqr() {
        return tzfqr;
    }

    public void setTzfqr(String tzfqr) {
        this.tzfqr = tzfqr;
    }

    public String getFwid() {
        return fwid;
    }

    public void setFwid(String fwid) {
        this.fwid = fwid;
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

    public String getTzlx() {
        return tzlx;
    }

    public void setTzlx(String tzlx) {
        this.tzlx = tzlx;
    }

    public String getZsjtgg() {
        return zsjtgg;
    }

    public void setZsjtgg(String zsjtgg) {
        this.zsjtgg = zsjtgg;
    }

    public String getZsjtbm() {
        return zsjtbm;
    }

    public void setZsjtbm(String zsjtbm) {
        this.zsjtbm = zsjtbm;
    }

    public String getZsqtgsbgs() {
        return zsqtgsbgs;
    }

    public void setZsqtgsbgs(String zsqtgsbgs) {
        this.zsqtgsbgs = zsqtgsbgs;
    }

    public String getZswbdw() {
        return zswbdw;
    }

    public void setZswbdw(String zswbdw) {
        this.zswbdw = zswbdw;
    }

    public String getCbjtgg() {
        return cbjtgg;
    }

    public void setCbjtgg(String cbjtgg) {
        this.cbjtgg = cbjtgg;
    }

    public String getCbjtbm() {
        return cbjtbm;
    }

    public void setCbjtbm(String cbjtbm) {
        this.cbjtbm = cbjtbm;
    }

    public String getCbqtgsbgs() {
        return cbqtgsbgs;
    }

    public void setCbqtgsbgs(String cbqtgsbgs) {
        this.cbqtgsbgs = cbqtgsbgs;
    }

    public String getCbwbdw() {
        return cbwbdw;
    }

    public void setCbwbdw(String cbwbdw) {
        this.cbwbdw = cbwbdw;
    }

    public String getCsjtgg() {
        return csjtgg;
    }

    public void setCsjtgg(String csjtgg) {
        this.csjtgg = csjtgg;
    }

    public String getCsjtbm() {
        return csjtbm;
    }

    public void setCsjtbm(String csjtbm) {
        this.csjtbm = csjtbm;
    }

    public String getCsqtgsbgs() {
        return csqtgsbgs;
    }

    public void setCsqtgsbgs(String csjtbgs) {
        this.csqtgsbgs = csqtgsbgs;
    }

    public String getCswbdw() {
        return cswbdw;
    }

    public void setCswbdw(String cswbdw) {
        this.cswbdw = cswbdw;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getZsgsgg() {
        return zsgsgg;
    }

    public void setZsgsgg(String zsgsgg) {
        this.zsgsgg = zsgsgg;
    }

    public String getZsgsbm() {
        return zsgsbm;
    }

    public void setZsgsbm(String zsgsbm) {
        this.zsgsbm = zsgsbm;
    }

    public String getCbgsgg() {
        return cbgsgg;
    }

    public void setCbgsgg(String cbgsgg) {
        this.cbgsgg = cbgsgg;
    }

    public String getCbgsbm() {
        return cbgsbm;
    }

    public void setCbgsbm(String cbgsbm) {
        this.cbgsbm = cbgsbm;
    }

    public String getCsgsgg() {
        return csgsgg;
    }

    public void setCsgsgg(String csgsgg) {
        this.csgsgg = csgsgg;
    }

    public String getCsgsbm() {
        return csgsbm;
    }

    public void setCsgsbm(String csgsbm) {
        this.csgsbm = csgsbm;
    }

    public String getTzid() {
        return tzid;
    }

    public void setTzid(String tzid) {
        this.tzid = tzid;
    }
}
