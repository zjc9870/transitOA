package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 日志表
 * @author zcz
 *
 */
@Entity
@Table(name = "c_log")
public class LogSys {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true)
	private String id;
	
	@Column(name = "czsj")
	private Date czsj;//操作时间
	
	@Column(name = "yhmc", length = 20)
	private String yhmc;//用户名称
	
	@Column(name = "czlx", length = 45)
	private String czlx;//操作类型
	
	@Column(name = "ywlx", length = 45)
	private String ywlx;//业务类型

	@Column(name = "cznrid", length = 32)
	private String cznrid;//操作内容id
	
	@Column(name = "cznrmc", length = 45)
	private String cznrmc;//操作内容名称(比如文稿的名称)
	
	@Column(name = "yhip", length = 45)
	private String yhip;//用户IP
	
	@Column(name = "dwid", length = 32)
	private String dwid;//用户单位id
	
	@Column(name = "czjg", length = 20)
	private String czjg;//操作结果
	
	@Column(name = "rzxq", length = 200)
	private String rzxq;//日志详情
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCzsj() {
		return czsj;
	}
	public void setCzsj(Date czsj) {
		this.czsj = czsj;
	}
	public String getYhmc() {
		return yhmc;
	}
	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}
	public String getCzlx() {
		return czlx;
	}
	public void setCzlx(String czlx) {
		this.czlx = czlx;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getCznrid() {
		return cznrid;
	}
	public void setCznrid(String cznrid) {
		this.cznrid = cznrid;
	}
	public String getYhip() {
		return yhip;
	}
	public void setYhip(String yhip) {
		this.yhip = yhip;
	}
	public String getDwid() {
		return dwid;
	}
	public void setDwid(String dwid) {
		this.dwid = dwid;
	}
	public String getCzjg() {
		return czjg;
	}
	public void setCzjg(String czjg) {
		this.czjg = czjg;
	}
	public String getRzxq() {
		return rzxq;
	}
	public void setRzxq(String rzxq) {
		this.rzxq = rzxq;
	}
}
