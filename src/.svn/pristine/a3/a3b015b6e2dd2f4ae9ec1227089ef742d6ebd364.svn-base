package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Document;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

import java.util.List;

/**
 * Created by qifeng on 17/3/6.
 */
public class DocumentVo {
    private String id;
    private String bh;//公文编号
    private String bt;//公文标题
    private String ztc;//主题词
    private String userName;//拟公文人
    private String mj;//密级
    private String sffb;//Y是发布,N是不发布
    private List<AttachmentVo> attachmentList;//附件
    private String lcbs;//流程标识
    private String gwshzt;//公文审核状态
    private List<LcrzbVo> lcrzbList;//流程日志
    private String gwfl;//公文分类(提交 未提交)
    private String sqsj;//申请时间
    private String sfsc;//是否已经删除（采用软删除，只标识，不删除）
    private String sfth;//是否退回（Y 是退回， N不是退回）
    private String sbd;//申办单名称
    private String date;//申请时间日期
    private String time;//申请时间 时间
    private String spyj;//审批意见
    private String htbh;//打印回填编号
    private String fwtzid;//Fwtz表数据的id
    private String loginUserRole;//登录用户角色
    private String tzdx;//发文通知对象
    private String tzlx;//发文通知类型(主送,抄送,抄报)
    private String yfrq;//印发日期
    private String tzsj;
    private String ydsj;
    private String shrq;//审核日期
    private UserVo spr;//审批人 指董事长
    private String zgs;//子公司名称



    public DocumentVo(){
    }

    public DocumentVo(Document document) {
        this.id=document.getId();
        this.bh=document.getBh();
        this.bt=document.getBt();
        this.ztc=document.getZtc();
        this.userName=document.getNgwr().getFullName();
        this.mj=document.getMj();
        this.sffb=document.getSffb();
        this.lcbs=document.getLcbs();
        this.gwshzt=document.getGwshzt();
        this.gwfl=document.getGwfl();
        if(document.getSqsj() != null){
            this.date = DateUtil.format(document.getSqsj(), DateUtil.zbFormat);
            this.time = DateUtil.format(document.getSqsj(), DateUtil.timeFormat);
            this.sqsj = DateUtil.format(document.getSqsj(), DateUtil.fullFormat);
        }
        else {
            this.sqsj = "";
            this.date = "";
            this.time = "";
        }
        this.sfsc=document.getSfsc();
        this.sfth=document.getSfth();
        this.sbd=document.getSbd();
        if(StringUtil.isBlank(document.getSbd()))
            this.sbd = "";
        else this.sbd = document.getSbd();
        this.htbh=document.getHtbh();
        this.yfrq = document.getYfrq();
        this.shrq = document.getShrq();
        this.spyj = document.getSpyj();
        this.spr = UserConvertor.convert(document.getSpr());
        this.zgs = document.getZgs();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getZtc() {
        return ztc;
    }

    public void setZtc(String ztc) {
        this.ztc = ztc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMj() {
        return mj;
    }

    public void setMj(String mj) {
        this.mj = mj;
    }

    public String getSffb() {
        return sffb;
    }

    public void setSffb(String sffb) {
        this.sffb = sffb;
    }

    public List<AttachmentVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentVo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getLcbs() {
        return lcbs;
    }

    public void setLcbs(String lcbs) {
        this.lcbs = lcbs;
    }

    public String getGwshzt() {
        return gwshzt;
    }

    public void setGwshzt(String gwshzt) {
        this.gwshzt = gwshzt;
    }

    public List<LcrzbVo> getLcrzbList() {
        return lcrzbList;
    }

    public void setLcrzbList(List<LcrzbVo> lcrzbList) {
        this.lcrzbList = lcrzbList;
    }

    public String getGwfl() {
        return gwfl;
    }

    public void setGwfl(String gwfl) {
        this.gwfl = gwfl;
    }

    public String getSqsj() {
        return sqsj;
    }

    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }

    public String getSfsc() {
        return sfsc;
    }

    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }

    public String getSfth() {
        return sfth;
    }

    public void setSfth(String sfth) {
        this.sfth = sfth;
    }

    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getFwtzid() {
        return fwtzid;
    }

    public void setFwtzid(String fwtzid) {
        this.fwtzid = fwtzid;
    }

    public String getLoginUserRole() {
        return loginUserRole;
    }

    public void setLoginUserRole(String loginUserRole) {
        this.loginUserRole = loginUserRole;
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

    public String getYfrq() {
        return yfrq;
    }

    public void setYfrq(String yfrq) {
        this.yfrq = yfrq;
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

    public String getShrq() {
        return shrq;
    }

    public void setShrq(String shrq) {
        this.shrq = shrq;
    }


    public UserVo getSpr() {
        return spr;
    }

    public void setSpr(UserVo spr) {
        this.spr = spr;
    }

    public String getZgs() {
        return zgs;
    }

    public void setZgs(String zgs) {
        this.zgs = zgs;
    }
}
