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

import com.expect.admin.service.vo.LcrzbVo;

/**
 * 流程日志表
 * 用来记录用来记录用户在各种处理流程中所作的操作，和产生的数据
 * @author zcz
 *
 */
@Entity
@Table(name = "lcrzb")
public class Lcrzb {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "clsj")
	private Date clsj;//处理时间
	
	@Column(name = "message", length = 200)
	private String message;//处理意见
	
	@Column(name = "cljg", length = 20)
	private String cljg;//处理结果
	
	@Column(name = "clnrid", length = 32)
	private String clnrid;//处理内容的id（所处理的东西的id）
	
	@Column(name = "clnrfl", length = 10)
	private String clnrfl;//处理内容分类（所处理的东西的分类）
	
	@Column(name = "dyjd", length = 32)
	private String dyjd;//对应的流程节点
	
	@Column(name = "sfxs")
	private String sfxs;//是否显示(本条数据是否在流程中显示)

	public Lcrzb(){}
	public Lcrzb(LcrzbVo lczrbVo, User user, String clnrid, String clnrfl){
		this.user = user;
		this.cljg = lczrbVo.getCljg();
		this.clsj = new Date();
		this.message = lczrbVo.getMessage();
		this.clnrfl = clnrfl;
		this.clnrid = clnrid;
	}
	
	public Lcrzb(String cljg, String clyj, User clr) {
		this.cljg = cljg;
		this.message = clyj;
		this.user = clr;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getClsj() {
		return clsj;
	}

	public void setClsj(Date clsj) {
		this.clsj = clsj;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCljg() {
		return cljg;
	}

	public void setCljg(String cljg) {
		this.cljg = cljg;
	}

	public String getClnrid() {
		return clnrid;
	}

	public void setClnrid(String clnrid) {
		this.clnrid = clnrid;
	}

	public String getClnrfl() {
		return clnrfl;
	}

	public void setClnrfl(String clnrfl) {
		this.clnrfl = clnrfl;
	}
	public String getDyjd() {
		return dyjd;
	}
	public void setDyjd(String dyjd) {
		this.dyjd = dyjd;
	}
	public String getSfxs() {
		return sfxs;
	}
	public void setSfxs(String sfxs) {
		this.sfxs = sfxs;
	}
	

}
