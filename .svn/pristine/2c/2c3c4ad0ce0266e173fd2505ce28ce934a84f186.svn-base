package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "s_draft_inf")
public class Draft {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true)
	private String id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ngr_id")
	private User ngr;//拟稿人
	
	@Column(name = "ztc", length = 200)
	private String ztc;//主题词
	
	@Column(name = "mj")
	private int mj;//密级
	
	@Column(name = "sfmhwzfb", length = 2)
	private String sfmhwzfb;//是否门户网站发布（Y： 发布  N:不发布）
	
	@Column(name = "yfrq")
	private Date yfrq;//印发日期
	
	@Column(name = "fs")
	private int fs;//份数
	
	@Column(name = "dyr", length = 20)
	private String dyr;//打印人
	
	@Column(name = "jdr", length = 20)
	private String jdr;//校对人
	
	@Column(name = "bh", length = 20)
	private String bh;//编号
	
	@Column(name = "bz", length = 200)
	private String bz;//备注
	
	@Column(name = "ngfl", length = 2)
	private String ngfl;//拟稿分类
	
	@Column(name = "ngshzt", length = 100)
	private String ngshzt;//拟稿审核状态
	
	@Column(name = "sqsj")
	private Date sqsj;//申请时间
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getNgr() {
		return ngr;
	}

	public void setNgr(User ngr) {
		this.ngr = ngr;
	}

	public String getZtc() {
		return ztc;
	}

	public void setZtc(String ztc) {
		this.ztc = ztc;
	}

	public int getMj() {
		return mj;
	}

	public void setMj(int mj) {
		this.mj = mj;
	}

	public String getSfmhwzfb() {
		return sfmhwzfb;
	}

	public void setSfmhwzfb(String sfmhwzfb) {
		this.sfmhwzfb = sfmhwzfb;
	}


	public Date getYfrq() {
		return yfrq;
	}

	public void setYfrq(Date yfrq) {
		this.yfrq = yfrq;
	}

	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	public String getDyr() {
		return dyr;
	}

	public void setDyr(String dyr) {
		this.dyr = dyr;
	}

	public String getJdr() {
		return jdr;
	}

	public void setJdr(String jdr) {
		this.jdr = jdr;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getNgshzt() {
		return ngshzt;
	}

	public void setNgshzt(String ngshzt) {
		this.ngshzt = ngshzt;
	}

	public String getNgfl() {
		return ngfl;
	}

	public void setNgfl(String ngfl) {
		this.ngfl = ngfl;
	}
}
