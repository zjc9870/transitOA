package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.utils.DateUtil;

public class DraftSwVo {
	private String id;
	private String bh;//编号
	private String rq;//日期(年月日)
	private String lwdw;//来文单位
	private String wh;//文号
	private String wjbt;//文件标题
	private String bz;//备注
	private String swr;//收文人
	private String zt;//状态
	private String ldps;//领导批示
	private String blqk;//办理情况
	
	public DraftSwVo() {
		
	}
	
	public DraftSwVo(DraftSw draftSw) {
		this.id = draftSw.getId();
		this.bh = draftSw.getBh();
		this.bz = draftSw.getBz();
		this.lwdw = draftSw.getLwdw();
		if(draftSw.getRq() != null)
			this.rq = DateUtil.format(draftSw.getRq(), DateUtil.zbFormat);
		this.swr = draftSw.getSwr().getFullName();
		this.wh = draftSw.getWh();
		this.wjbt = draftSw.getWjbt();
	}
	 
	
	
	public String getBlqk() {
		return blqk;
	}

	public void setBlqk(String blqk) {
		this.blqk = blqk;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
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
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	public String getLwdw() {
		return lwdw;
	}
	public void setLwdw(String lwdw) {
		this.lwdw = lwdw;
	}
	public String getWh() {
		return wh;
	}
	public void setWh(String wh) {
		this.wh = wh;
	}
	public String getWjbt() {
		return wjbt;
	}
	public void setWjbt(String wjbt) {
		this.wjbt = wjbt;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSwr() {
		return swr;
	}
	public void setSwr(String swr) {
		this.swr = swr;
	}

	public String getLdps() {
		return ldps;
	}

	public void setLdps(String ldps) {
		this.ldps = ldps;
	}
	
	
}
