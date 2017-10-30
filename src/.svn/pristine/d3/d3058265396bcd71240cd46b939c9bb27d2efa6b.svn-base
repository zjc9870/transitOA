package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Fwtz;

import com.expect.admin.data.dataobject.User;
import com.expect.admin.utils.DateUtil;


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
    private String zsjtgg;//主送集团高管
    private String zsjtbm;//主送集团部门
    private String zsqtgsbgs;//主送其他公司办公室
    private String zswbdw;//主送外部单位
    private String cbjtgg;//抄报集团高管
    private String cbjtbm;//抄报集团部门
    private String cbqtgsbgs;//抄报其他公司办公室
    private String cbwbdw;//抄报外部单位
    private String csjtgg;//抄送集团高管
    private String csjtbm;//抄送集团部门
    private String csqtgsbgs;//抄送其他公司办公室
    private String cswbdw;//抄送外部单位
    private String zsgsgg;//主送公司高管
    private String zsgsbm;//主送公司部门
    private String cbgsgg;//抄送公司高管
    private String cbgsbm;//抄报公司部门
    private String csgsgg;//抄送公司高管
    private String csgsbm;//抄送公司部门
    private String tzid;//
    private String tzsj;//通知时间
    private String ydsj;//已读时间
    private User tzfqrId;//通知发起人id
   private int printNumber;//打印份数

    public FwtzVo(){

    }
    public FwtzVo(Fwtz fwtz){
        this.id=fwtz.getId();
        this.tzfqrId = fwtz.getTzfqr();
        this.tzfqr=fwtz.getTzfqr().getFullName();
        this.fwid=fwtz.getFwid();
        this.tzdxfl=fwtz.getTzdxfl();
        this.tzdx=fwtz.getTzdx();
        this.tzlx=fwtz.getTzlx();
        this.isread=fwtz.getIsread();
        this.tzid=fwtz.getTzid();
        if (fwtz.getTzsj() == null) {
            this.tzsj = "";
        }
        else {
            this.tzsj = DateUtil.format(fwtz.getTzsj(), "yyyy/MM/dd HH:mm:ss");
        }
        if (fwtz.getYdsj() == null) {
            this.ydsj = "";
        }
        else
            this.ydsj = DateUtil.format(fwtz.getYdsj(), "yyyy/MM/dd HH:mm:ss");
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

    public void setCsqtgsbgs(String csqtgsbgs) {
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

    public User getTzfqrId() {
        return tzfqrId;
    }

    public void setTzfqrId(User tzfqrId) {
        this.tzfqrId = tzfqrId;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }
}
