package com.expect.admin.data.dataobject;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

/**
 * @seeDraftSwUserLcrzbGxb
 * 将收文表 办理人 传阅人 处理意见（Lcrzb）（流程日志表）关联起来
 * 附件信息用c_draftSw_attachment关联附件表
 * 收文分类（，'Y':首轮 已提交，'N' : 不是首轮 已提交， 'W': 未提交）
 * 密级（1：绝密  2.机密 3.秘密）
 * 首轮 发起人发起——领导审批——发起人选择传阅人（每次最多50人）——传阅人完成——发起人选择办理人（每次最多50人）——办理人完成——
 * 发起人四个选择（1.完成 2.领导批阅， 3.传阅人 4.办理人）（最后一步可以多次循环知道发起人选择完成，每次处理完成后都回到发起人）
 * @author zcz
 *
 */
@Entity
@Table(name = "draft_sw")
public class DraftSw {
	/**
	 * 密级 绝密
	 */
	public static final String MJ_JM = "1";
	/**
	 * 密级 机密
	 */
	public static final String MJ_JIM = "2";
	/**
	 * 密级 秘密
	 */
	public static final String MJ_MM = "3";
	
	/**
	 * 收文分类 未提交
	 */
	public static final String SWFL_WTJ = "W";
	/**
	 * 收文分类 首轮 已提交
	 */
	public static final String SWFL_SLYTJ = "Y";
	/**
	 * 收文分类 多轮 已提交
	 */
	public static final String SWFL_DLYTJ = "N";
	
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

	@Column(name = "sbd", length = 100)
	private String sbd;//副标题
	
	@Column(name = "bz", length = 200)
	private String bz;// 备注

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "swr_id")
	private User swr;// 收文人

	@Column(name = "swzt", length = 32)
	private String swzt;// 收文状态
	
	@Column(name = "swfl", length = 10)
	private String swfl;//收文分类（，'Y':首轮 已提交，'N' : 不是首轮 已提交， 'W': 未提交）
	
	@Column(name = "mj", length = 2)
	private String mj;//密级（1：绝密  2.机密 3.秘密）
	@Column(name = "sqsj")
	private Date fqsj;//发起时间（收文的申请时间）

	@Column
	private String dyrq;//打印日期



	public String getDyrq() {
		return dyrq;
	}

	public void setDyrq(String dyrq) {
		this.dyrq = dyrq;
	}



	@OneToMany(mappedBy = "draftSw" , fetch = FetchType.EAGER)
	private Set<DraftSwUserLcrzbGxb> draftSwUserLcrzbGxbs = new HashSet<>();
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "s_draftSw_attachment", joinColumns = @JoinColumn(name = "draftSw_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"))
	private Set<Attachment> attachments = new HashSet<>();//附件
	
	
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
		this.swzt = draftSwVo.getZt();
		this.mj = draftSwVo.getMj();
		this.sbd = draftSwVo.getSbd();
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

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
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

	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
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

	public String getSwfl() {
		return swfl;
	}

	public void setSwfl(String swfl) {
		this.swfl = swfl;
	}

	public Set<DraftSwUserLcrzbGxb> getDraftSwUserLcrzbGxbs() {
		return draftSwUserLcrzbGxbs;
	}

	public void setDraftSwUserLcrzbGxbs(Set<DraftSwUserLcrzbGxb> draftSwUserLcrzbGxbs) {
		this.draftSwUserLcrzbGxbs = draftSwUserLcrzbGxbs;
	}

	public String getMj() {
		return mj;
	}

	public void setMj(String mj) {
		this.mj = mj;
	}

	public Date getFqsj() {
		return fqsj;
	}

	public void setFqsj(Date fqsj) {
		this.fqsj = fqsj;
	}

}