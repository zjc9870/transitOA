package com.expect.admin.service.vo;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.data.dataobject.DraftSwUserLcrzbGxb;
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
	private String mj;//密级
	private String fqsj;//发起时间
    private List<LcrzbVo> jyzyRecordList = new ArrayList<LcrzbVo>();
    private List<LcrzbVo> ldRecordList = new ArrayList<LcrzbVo>();
    private List<LcrzbVo> cyrRecordList = new ArrayList<LcrzbVo>();
    private List<LcrzbVo> blrRecordList = new ArrayList<LcrzbVo>();
//	private String fqsjDate;//发起时间的年月日（YYYY/mm/dd）
//	private String fasjTime;//
	
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
		if(draftSw.getFqsj() != null) {
			this.fqsj = DateUtil.format(draftSw.getFqsj(), DateUtil.fullFormat);
		}else this.fqsj = "";
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

	public String getMj() {
		return mj;
	}

	public void setMj(String mj) {
		this.mj = mj;
	}

	public String getFqsj() {
		return fqsj;
	}

	public void setFqsj(String fqsj) {
		this.fqsj = fqsj;
	}
	public List<LcrzbVo> getJyzyRecordList() {
        return jyzyRecordList;
    }

    public void setJyzyRecordList(List<LcrzbVo> jyzyRecordList) {
        this.jyzyRecordList = jyzyRecordList;
    }

    public List<LcrzbVo> getLdRecordList() {
        return ldRecordList;
    }

    public void setLdRecordList(List<LcrzbVo> ldRecordList) {
        this.ldRecordList = ldRecordList;
    }

    public List<LcrzbVo> getCyrRecordList() {
        return cyrRecordList;
    }

    public void setCyrRecordList(List<LcrzbVo> cyrRecordList) {
        this.cyrRecordList = cyrRecordList;
    }

    public List<LcrzbVo> getBlrRecordList() {
        return blrRecordList;
    }

    public void setBlrRecordList(List<LcrzbVo> blrRecordList) {
        this.blrRecordList = blrRecordList;
    }
}
