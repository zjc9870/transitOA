package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流程节点关系表
 * @author zcz
 *
 */
@Entity
@Table(name = "lcjdgxb")
public class Lcjdgxb {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "start_lcjd_id", length = 32)
	private String ksjd;//开始节点（当前节点）
	
	@Column(name = "end_lcjd_id", length = 32)
	private String jsjd;//结束节点（下一节点）
	
	@Column(name = "back_lcjd_id", length = 32)
	private  String thjd;//退回节点（退回是回到的节点）
	
	@Column(name = "lc_id", length = 32)
	private String lcbs;//流程标识
	
	@Column(name = "qygz", length = 2)
	private String qygz;//启用规则
	
	@Column(name = "sftb", length = 2)
	private String sftb;//是否同步
	
	@Column(name = "xssx")
	private Integer xssx;//显示顺序(用来标识该节点在界面上的显示顺序)

	public Lcjdgxb(){

	}
	public Lcjdgxb(String startLcId,String endLcId,String backLcId,String qygz,String sftb,String lcid){
		this.ksjd = startLcId;
		this.jsjd = endLcId;
		this.thjd = backLcId;
		this.qygz = qygz;
		this.sftb = sftb;
		this.lcbs = lcid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQygz() {
		return qygz;
	}

	public void setQygz(String qygz) {
		this.qygz = qygz;
	}

	public String getSftb() {
		return sftb;
	}

	public void setSftb(String sftb) {
		this.sftb = sftb;
	}

	public String getKsjd() {
		return ksjd;
	}

	public void setKsjd(String ksjd) {
		this.ksjd = ksjd;
	}

	public String getJsjd() {
		return jsjd;
	}

	public void setJsjd(String jsjd) {
		this.jsjd = jsjd;
	}

	public String getThjd() {
		return thjd;
	}

	public void setThjd(String thjd) {
		this.thjd = thjd;
	}

	public String getLcbs() {
		return lcbs;
	}

	public void setLcbs(String lcbs) {
		this.lcbs = lcbs;
	}

	public Integer getXssx() {
		return xssx;
	}

	public void setXssx(Integer xssx) {
		this.xssx = xssx;
	}
	
}
