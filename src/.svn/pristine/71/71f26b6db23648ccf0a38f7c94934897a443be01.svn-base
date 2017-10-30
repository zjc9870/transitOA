package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 代码表 用于配置一些关键代码值，
 * 类别编号标识一个类别，
 * 代码编号标识某类别下的一个代码，代码描述用来存储改代码的值
 * @author zcz
 *
 */
@Entity
@Table(name = "s_dmb")
public class Dmb {

	@Id
	@Column(name = "id", length = 10)
	private String id;
	
	@Column(name = "lbbh", length = 20)
	private String lbbh;//类别编号
	
	@Column(name = "dmbh", length = 20)
	private String dmbh;//代码编号
	
	@Column(name = "dmms", length = 50)
	private String dmms;//代码描述，代码的值
	
	@Column(name = "bz", length = 100)
	private String bz;//备注
	
	@ManyToOne
	@JoinColumn(name = "ssgs_id")
	private Department ssgs;//所属公司

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLbbh() {
		return lbbh;
	}

	public void setLbbh(String lbbh) {
		this.lbbh = lbbh;
	}

	public String getDmbh() {
		return dmbh;
	}

	public void setDmbh(String dmbh) {
		this.dmbh = dmbh;
	}

	public String getDmms() {
		return dmms;
	}

	public void setDmms(String dmms) {
		this.dmms = dmms;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Department getSsgs() {
		return ssgs;
	}

	public void setSsgs(Department ssgs) {
		this.ssgs = ssgs;
	}
	
	
}
