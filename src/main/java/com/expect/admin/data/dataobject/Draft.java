package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "s_draft_inf")
public class Draft {
	
	private String id;
	private String ngr;//拟稿人
	private String ztc;//主题词
	private int mj;//密级
	private String sfmhwzfb;//是否门户网站发布（Y： 发布  N:不发布）
	private String zs;//主送
	private String cb;//抄报
	private String cs;//抄送
	private String bmhgyj;//部门核稿意见
	private String bmfzrhgyj;//部门负责人核稿意见
	private String hqyj;//会签意见
	private String hgyj;//核稿意见
	private Date yfrq;//印发日期
	private int fs;//份数
	private String dyr;//打印人
	private String jdr;//校对人
	private String bh;//编号
	private String bz;//备注
	private String sfjtng;//是否集团拟稿
	private String ngshzt;//拟稿审核状态
	

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ngr", length = 20)
	public String getNgr() {
		return ngr;
	}

	public void setNgr(String ngr) {
		this.ngr = ngr;
	}

	@Column(name = "ztc", length = 200)
	public String getZtc() {
		return ztc;
	}

	public void setZtc(String ztc) {
		this.ztc = ztc;
	}

	@Column(name = "mj")
	public int getMj() {
		return mj;
	}

	public void setMj(int mj) {
		this.mj = mj;
	}

	@Column(name = "sfmhwzfb", length = 2)
	public String getSfmhwzfb() {
		return sfmhwzfb;
	}

	public void setSfmhwzfb(String sfmhwzfb) {
		this.sfmhwzfb = sfmhwzfb;
	}

	@Column(name = "zs", length = 20)
	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	@Column(name = "cb", length = 20)
	public String getCb() {
		return cb;
	}

	public void setCb(String cb) {
		this.cb = cb;
	}

	@Column(name = "cs", length = 20)
	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	@Column(name = "bmhgyj", length = 200)
	public String getBmhgyj() {
		return bmhgyj;
	}

	public void setBmhgyj(String bmhgyj) {
		this.bmhgyj = bmhgyj;
	}

	@Column(name = "bmfzrhgyj", length = 200)
	public String getBmfzrhgyj() {
		return bmfzrhgyj;
	}

	public void setBmfzrhgyj(String bmfzrhgyj) {
		this.bmfzrhgyj = bmfzrhgyj;
	}

	@Column(name = "hqyj", length = 200)
	public String getHqyj() {
		return hqyj;
	}

	public void setHqyj(String hqyj) {
		this.hqyj = hqyj;
	}

	@Column(name = "hgyj", length = 200)
	public String getHgyj() {
		return hgyj;
	}

	public void setHgyj(String hgyj) {
		this.hgyj = hgyj;
	}

	@Column(name = "yfrq")
	public Date getYfrq() {
		return yfrq;
	}

	public void setYfrq(Date yfrq) {
		this.yfrq = yfrq;
	}

	@Column(name = "fs")
	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	@Column(name = "dyr", length = 20)
	public String getDyr() {
		return dyr;
	}

	public void setDyr(String dyr) {
		this.dyr = dyr;
	}

	@Column(name = "jdr", length = 20)
	public String getJdr() {
		return jdr;
	}

	public void setJdr(String jdr) {
		this.jdr = jdr;
	}

	@Column(name = "bh", length = 20)
	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	@Column(name = "bz", length = 200)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "sfjtng", length = 2)
	public String getSfjtng() {
		return sfjtng;
	}

	public void setSfjtng(String sfjtng) {
		this.sfjtng = sfjtng;
	}

	@Column(name = "ngshzt", length = 100)
	public String getNgshzt() {
		return ngshzt;
	}

	public void setNgshzt(String ngshzt) {
		this.ngshzt = ngshzt;
	}
	
	
}
