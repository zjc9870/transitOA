package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Meeting;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

import java.util.List;


public class MeetingVo {
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
    private List<AttachmentVo> attachmentList;//附件
    private String sqsj; //申请时间
    private String hyshzt; //会议审核状态
    private String spyj; //审批意见
    private List<LcrzbVo> lcrzList;//流程日志
    private String userName; //拟会议人
    private String hyrid; //拟会议人id
    private String hyfl; //会议分类（集团会议 东山公交会议 其他公司会议）
    private String sqd; //申请单名称
    private String hytzid; //Hytz表数据的id
    private String loginUserRole; //登录用户角色
    private String tzdx; //会议通知对象
    private String tzsj;
    private String ydsj;


    public MeetingVo(){}
    public MeetingVo(Meeting meeting){
        this.id=meeting.getId();
        this.hyzt=meeting.getHyzt();
        this.hynr=meeting.getHynr();
        this.hydd=meeting.getHydd();
        this.hys=meeting.getHys();
        this.hyrq=meeting.getHyrq();
        this.kssj=meeting.getKssj();
        this.jssj=meeting.getJssj();
        this.chry=meeting.getChry();
        this.djrxm=meeting.getDjrxm();
        this.lxfs=meeting.getLxfs();
        this.hygg=meeting.getHygg();
        this.qt=meeting.getQt();
        if(meeting.getSqsj() != null)
            this.sqsj= DateUtil.format(meeting.getSqsj(), DateUtil.fullFormat);
        this.hyshzt=meeting.getHyshzt();
        this.userName=meeting.getNhyr().getUsername();
        this.hyrid=meeting.getNhyr().getId();
        this.hyfl=meeting.getHyfl();
        if(StringUtil.isBlank(meeting.getSqd()))
            this.sqd = "";
        else  this.sqd = meeting.getSqd();


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHyshzt() {
        return hyshzt;
    }

    public void setHyshzt(String hyshzt) {
        this.hyshzt = hyshzt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHyrid() {
        return hyrid;
    }

    public void setHyrid(String hyrid) {
        this.hyrid = hyrid;
    }

    public String getHyzt() {
        return hyzt;
    }

    public void setHyzt(String hyzt) {
        this.hyzt = hyzt;
    }

    public String getHynr() {
        return hynr;
    }

    public void setHynr(String hynr) {
        this.hynr = hynr;
    }

    public String getHydd() {
        return hydd;
    }

    public void setHydd(String hydd) {
        this.hydd = hydd;
    }

    public String getHys() {
        return hys;
    }

    public void setHys(String hys) {
        this.hys = hys;
    }

    public String getHyrq() {
        return hyrq;
    }

    public void setHyrq(String hyrq) {
        this.hyrq = hyrq;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getChry() {
        return chry;
    }

    public void setChry(String chry) {
        this.chry = chry;
    }

    public String getDjrxm() {
        return djrxm;
    }

    public void setDjrxm(String djrxm) {
        this.djrxm = djrxm;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    public String getHygg() {
        return hygg;
    }

    public void setHygg(String hygg) {
        this.hygg = hygg;
    }

    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    public String getSqsj() {
        return sqsj;
    }

    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }

    public List<AttachmentVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentVo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    public List<LcrzbVo> getLcrzList() {
        return lcrzList;
    }

    public void setLcrzList(List<LcrzbVo> lcrzList) {
        this.lcrzList = lcrzList;
    }

    public String getHyfl() {
        return hyfl;
    }

    public void setHyfl(String hyfl) {
        this.hyfl = hyfl;
    }

    public String getSqd() {
        return sqd;
    }

    public void setSqd(String sqd) {
        this.sqd = sqd;
    }

    public String getHytzid() {
        return hytzid;
    }

    public void setHytzid(String hytzid) {
        this.hytzid = hytzid;
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
}
