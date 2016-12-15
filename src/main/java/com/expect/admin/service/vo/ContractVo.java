package com.expect.admin.service.vo;

import java.util.List;

import com.expect.admin.data.dataobject.Contract;
import com.expect.admin.utils.DateUtil;

public class ContractVo {

	private String id;
	private String bh;//合同编号
	private String userName;//拟合同人
	private String htbt;//合同标题
	private String htnr;//合同能容
	private String nqdrq;//拟签订日期
	private String qx;//期限
	private String htshzt;//合同审核状态
	private List<LcrzbVo> lcrzList;//流程日志
	private String htfl;//合同分类（东交合同    集团合同  其他公司合同）
	private String lcbs;//流程标识
	private String sqsj;//申请时间
	
	public ContractVo() {
	}
	
	public ContractVo(Contract contract) {
		this.id = contract.getId();
		this.bh = contract.getBh();
		this.userName = contract.getNhtr().getFullName();
		this.htbt = contract.getHtbt();
		this.htnr = contract.getHtnr();
		if(contract.getNqdrq() != null)
			this.nqdrq = DateUtil.format(contract.getNqdrq(), DateUtil.webFormat);
		this.qx = contract.getQx();
		this.htshzt = contract.getHtshzt();
		this.htfl = contract.getHtfl();
		this.lcbs = contract.getLcbs();
		if(contract.getSqsj() != null)
			this.sqsj = DateUtil.format(contract.getSqsj(), DateUtil.webFormat);
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
	
}
