package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "lc_table")
public class LcTable {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "lcbs", length = 4)
	private String lcbs;//流程标识
	
	@Column(name = "dqjd", length = 2)
	private String dqjd;//当前阶段
	
	@Column(name = "syjd", length = 2)
	private String syjd;//上一阶段
	
	@Column(name = "xyjd", length = 2)
	private String xyjd;//下一阶段
	
	@Column(name = "sfbmnbsh", length = 2)
	private String sfbmnbsh;//是否部门内部审核
	
	@Column(name = "sfzgznbsh", length = 2)
	private String sfzgsnbsh;//是否子公司内部审核

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLcbs() {
		return lcbs;
	}

	public void setLcbs(String lcbs) {
		this.lcbs = lcbs;
	}

	public String getDqjd() {
		return dqjd;
	}

	public void setDqjd(String dqjd) {
		this.dqjd = dqjd;
	}

	public String getSyjd() {
		return syjd;
	}

	public void setSyjd(String syjd) {
		this.syjd = syjd;
	}

	public String getXyjd() {
		return xyjd;
	}

	public void setXyjd(String xyjd) {
		this.xyjd = xyjd;
	}

	public String getSfbmnbsh() {
		return sfbmnbsh;
	}

	public void setSfbmnbsh(String sfbmnbsh) {
		this.sfbmnbsh = sfbmnbsh;
	}

	public String getSfzgsnbsh() {
		return sfzgsnbsh;
	}

	public void setSfzgsnbsh(String sfzgsnbsh) {
		this.sfzgsnbsh = sfzgsnbsh;
	}
	
}
