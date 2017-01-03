package com.expect.admin.data.dataobject;

import java.util.Date;
import java.util.List;

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

import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

/**
 * 收文表 办理人 传阅人存在Xgryb（相关人员表）中 处理结果，意见存在Lcrzb（流程日志表）中
 * 
 * @author zcz
 *
 */
@Entity
@Table(name = "draft_sw")
public class DraftSw {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;

	@Column(name = "bh", length = 20)
	private String bh;// 编号

	@Column(name = "rq")
	private Date rq;// 日期

	@Column(name = "lwdw", length = 50)
	private String lwdw;// 来文单位

	@Column(name = "wh", length = 20)
	private String wh;// 文号

	@Column(name = "nbwh", length = 40)
	private String nbwh;//内部文号
	
	@Column(name = "tjsj")
	private Date tjsj;//提交时间
	
	@Column(name = "wjbt", length = 100)
	private String wjbt;// 文件标题

	@Column(name = "bz", length = 200)
	private String bz;// 备注

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "swr_id")
	private User swr;// 收文人

	@Column(name = "swzt", length = 32)
	private String swzt;// 收文状态
	
	@Column(name = "blr", length = 32)
	private String blr;//办理人

	@Column(name = "blqk",length = 100)
	private String blqk;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "c_draftSw_user", joinColumns = @JoinColumn(name = "draftSw_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> pyrs;
	
	public DraftSw() {

	}

	public DraftSw(DraftSwVo draftSwVo) {
		this.id = draftSwVo.getId();
		this.bh = draftSwVo.getBh();
		this.bz = draftSwVo.getBz();
		this.lwdw = draftSwVo.getLwdw();
		if (!StringUtil.isBlank(draftSwVo.getRq()))
			this.rq = DateUtil.parse(draftSwVo.getRq(), DateUtil.zbFormat);
		this.wh = draftSwVo.getWh();
		this.wjbt = draftSwVo.getWjbt();
	}

	public String getBlqk() {
		return blqk;
	}

	public void setBlqk(String blqk) {
		this.blqk = blqk;
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

	
	

	public List<User> getPyrs() {
		return pyrs;
	}

	public void setPyrs(List<User> pyrs) {
		this.pyrs = pyrs;
	}

	public String getBlr() {
		return blr;
	}

	public void setBlr(String blr) {
		this.blr = blr;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public Date getRq() {
		return rq;
	}

	public void setRq(Date rq) {
		this.rq = rq;
	}

	public String getLwdw() {
		return lwdw;
	}

	public String getNbwh() {
		return nbwh;
	}

	public void setNbwh(String nbwh) {
		this.nbwh = nbwh;
	}

	public Date getTjsj() {
		return tjsj;
	}

	public void setTjsj(Date tjsj) {
		this.tjsj = tjsj;
	}

	public String getSwzt() {
		return swzt;
	}

	public void setSwzt(String swzt) {
		this.swzt = swzt;
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

	public User getSwr() {
		return swr;
	}

	public void setSwr(User swr) {
		this.swr = swr;
	}

}
