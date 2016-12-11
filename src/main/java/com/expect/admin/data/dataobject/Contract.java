package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "s_contract_inf")
public class Contract {

	private String id;
	private String bh;//合同编号
	private String nhtr;//拟合同人
	private String htbt;//合同标题
	private String htnr;//合同能容
	private Date nqdrq;//拟签订日期
	private String qx;//期限
//	private String fwshyj;//法务审核意见
//	private String zcglbyj;//资产管理部意见
//	private String fgfzryj;//分管负责人意见
	private String htshzt;//合同审核状态
	private String htfl;//合同分类（1：东交合同    2：集团合同  3：其他公司合同）
	
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
	
	@Column(name = "nhtr", length = 20)
	public String getNhtr() {
		return nhtr;
	}
	public void setNhtr(String nhtr) {
		this.nhtr = nhtr;
	}
	
	@Column(name="nqdrq")
	public Date getNqdrq() {
		return nqdrq;
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
	
	@Column(name = "htfl", length = 200)
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
	
	@Column(name = "qx", length = 20)
	public String getQx() {
		return qx;
	}
	public void setQx(String qx) {
		this.qx = qx;
	}
	
	
}
