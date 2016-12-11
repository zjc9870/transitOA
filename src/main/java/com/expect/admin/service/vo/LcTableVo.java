package com.expect.admin.service.vo;

import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.LcTable;

public class LcTableVo {
	private String id;
	private String lcbs;//流程标识
	private String dqjd;//当前阶段
	private String syjd;//上一阶段
	private String xyjd;//下一阶段
	private String sfbmnbsh;//是否部门内部审核
	private String sfzgsnbsh;//是否子公司内部审核
	
	

	public LcTableVo() {
	}
	
	public LcTableVo(LcTable lc) {
		BeanUtils.copyProperties(lc, this);
	}

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
