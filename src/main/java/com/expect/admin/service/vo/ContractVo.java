package com.expect.admin.service.vo;

import java.util.List;

import com.expect.admin.data.dataobject.Contract;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

public class ContractVo {

	private String id;
	private String bh;//合同编号
	private String userName;//拟合同人
	private String htbt;//合同标题
	private String htnr;//合同能容
	private String nqdrq;//拟签订日期（年月日）
	private String qx;//期限（年月日）
	private String htshzt;//合同审核状态
	private List<LcrzbVo> lcrzList;//流程日志
	private String htfl;//合同分类（东交合同    集团合同  其他公司合同）
	private String lcbs;//流程标识
	private String sqsj;//申请时间
	private String date;//申请时间日期
	private String time;//申请时间 时间
	private String sfth;//是否退回
	private String sbd;//合同的大标题
	private String spyj;//审批意见
	private List<AttachmentVo> attachmentList;//附件Vo
	private String sequenceNumber;//电子序号
//	private User nhtr;
	
	public ContractVo() {
	}
	
	public ContractVo(Contract contract) {
		this.id = contract.getId();
		this.bh = contract.getBh();
//		this.userName = contract.getNhtr().getFullName();
		this.userName = contract.getNhtr().getUsername();
//		this.nhtr =contract.getNhtr();
		this.htbt = contract.getHtbt();
		this.htnr = contract.getHtnr();
		if(contract.getNqdrq() != null)
			this.nqdrq = DateUtil.format(contract.getNqdrq(), DateUtil.zbFormat);
		
		this.qx = contract.getQx();
		this.htshzt = contract.getHtshzt();
		this.htfl = contract.getHtfl();
		this.lcbs = contract.getLcbs();
		if(contract.getSqsj() != null){
			this.date = DateUtil.format(contract.getSqsj(), DateUtil.zbFormat);
			this.time = DateUtil.format(contract.getSqsj(), DateUtil.timeFormat);
			this.sqsj = DateUtil.format(contract.getSqsj(), DateUtil.fullFormat);
		}
		else {
			this.sqsj = "";
			this.date = "";
			this.time = "";
		}
		if(StringUtil.isBlank(contract.getSbd()))
			this.sbd = "";
		else this.sbd = contract.getSbd();
		
		if(StringUtil.isBlank(contract.getSequenceNumber())){
		    this.sequenceNumber = null;
		}else this.sequenceNumber = contract.getSequenceNumber();
	}
	public List<LcrzbVo> getLcrzList() {
		return lcrzList;
	}
	public void setLcrzList(List<LcrzbVo> lcrzList) {
		this.lcrzList = lcrzList;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHtbt() {
		return htbt;
	}
	public void setHtbt(String htbt) {
		this.htbt = htbt;
	}
	public String getHtnr() {
		return htnr;
	}
	public void setHtnr(String htnr) {
		this.htnr = htnr;
	}
	public String getNqdrq() {
		return nqdrq;
	}
	public void setNqdrq(String nqdrq) {
		this.nqdrq = nqdrq;
	}
	public String getQx() {
		return qx;
	}
	public void setQx(String qx) {
		this.qx = qx;
	}
	public String getHtshzt() {
		return htshzt;
	}
	public void setHtshzt(String htshzt) {
		this.htshzt = htshzt;
	}

	public String getHtfl() {
		return htfl;
	}

	public void setHtfl(String htfl) {
		this.htfl = htfl;
	}

	public String getLcbs() {
		return lcbs;
	}

	public void setLcbs(String lcbs) {
		this.lcbs = lcbs;
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

	public String getSfth() {
		return sfth;
	}

	public void setSfth(String sfth) {
		this.sfth = sfth;
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

	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
	}

	public String getSpyj() {
		return spyj;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

//	public User getNhtr() {
//		return nhtr;
//	}
//
//	public void setNhtr(User nhtr) {
//		this.nhtr = nhtr;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return  false;
		}
		ContractVo contractVo = (ContractVo) obj;
		return this.id == contractVo.id;
	}
}
