package com.expect.admin.data.dataobject;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

@Entity
@Table(name = "s_contract_inf")
public class Contract {

	private String id;
	private String bh;//合同编号
	private User nhtr;//拟合同人
	private String htbt;//合同标题
	private String htnr;//合同能容
	private Date nqdrq;//拟签订日期
	private String qx;//期限
	private String htshzt;//合同审核状态
	private String htfl;//合同分类（东交合同    集团合同  其他公司合同）
	private String lcbs;//流程标识
	private Date sqsj;//申请时间
	private String sfsc;//是否已经删除（采用软删除，只标识，不删除）
	private String sfth;//是否退回（Y 是退回， N不是退回）
	private Set<Attachment> attachments;//附件
	private String sbd;//申办单名称
	private Set<Lcrzb> lcrzSet = new HashSet<>();//流程日志
	
	//电子序号，负责人审批后自定添加 序号格式是 日期+四位的序号（最后四位序号每年恢复到0001，否则递加） 如201701020001
	private String sequenceNumber;
//	private Department department;//合同所属部门
	
	public Contract() {
	}
	
	public Contract(ContractVo contractVo) {
		this.id = contractVo.getId();
		this.bh = contractVo.getBh();
		this.htbt = contractVo.getHtbt();
		this.htnr = contractVo.getHtnr();
		if(!StringUtil.isBlank(contractVo.getNqdrq()))
			this.nqdrq = DateUtil.parse(contractVo.getNqdrq(), DateUtil.zbFormat);
		this.qx = contractVo.getQx();
		this.htshzt = contractVo.getHtshzt();
		this.htfl = contractVo.getHtfl();
		this.lcbs = contractVo.getLcbs();
		if(StringUtil.isBlank(contractVo.getSqsj())){
			this.sqsj = new Date();
		}else {
			this.sqsj = DateUtil.parse(contractVo.getSqsj(), DateUtil.fullFormat);
	}
		if(!StringUtil.isBlank(contractVo.getSbd()))
			this.sbd = contractVo.getSbd();
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
	
	@Column(name = "bh", length = 20)
	public String getBh() {
		return bh;
	}
	public void setBh(String bh) {
		this.bh = bh;
	}

	@Column(name="nqdrq")
	public Date getNqdrq() {
		return nqdrq;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "nhtr_id")
	public User getNhtr() {
		return nhtr;
	}
	public void setNhtr(User nhtr) {
		this.nhtr = nhtr;
	}
	
	@Column(name = "lcbs", length = 32)
	public String getLcbs() {
		return lcbs;
	}
	public void setLcbs(String lcbs) {
		this.lcbs = lcbs;
	}
	public void setNqdrq(Date nqdrq) {
		this.nqdrq = nqdrq;
	}
	
	@Column(name = "htshzt", length = 200)
	public String getHtshzt() {
		return htshzt;
	}
	public void setHtshzt(String htshzt) {
		this.htshzt = htshzt;
	}
	
	@Column(name = "htfl", length = 20)
	public String getHtfl() {
		return htfl;
	}
	public void setHtfl(String htfl) {
		this.htfl = htfl;
	}
	
	@Column(name = "htbt", length = 100)
	public String getHtbt() {
		return htbt;
	}
	public void setHtbt(String htbt) {
		this.htbt = htbt;
	}
	
	@Column(name = "htnr")
	public String getHtnr() {
		return htnr;
	}
	public void setHtnr(String htnr) {
		this.htnr = htnr;
	}
	
	@Column(name = "qx_text")
	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

	@Column(name = "sqsj")
	public Date getSqsj() {
		return sqsj;
	}


	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}

	@Column(name = "sfsc", length = 2)
	public String getSfsc() {
		return sfsc;
	}

	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}

	@Column(name = "sfth", length = 2)
	public String getSfth() {
		return sfth;
	}

	public void setSfth(String sfth) {
		this.sfth = sfth;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "s_contract_attachment", joinColumns = @JoinColumn(name = "contract_id"), 
	inverseJoinColumns = @JoinColumn(name = "attachment_id"))
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Column(name = "sbd", length = 50)
	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
	}


	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "s_contract_lcrzb", joinColumns = @JoinColumn(name = "contract_id"),
	inverseJoinColumns = @JoinColumn(name = "lcrzb_id"))
	public Set<Lcrzb> getLcrzSet() {
		return lcrzSet;
	}

	public void setLcrzSet(Set<Lcrzb> lcrzSet) {
		this.lcrzSet = lcrzSet;
	}

	@Column(name = "sequenceNumber", length = 12)
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
	
}
