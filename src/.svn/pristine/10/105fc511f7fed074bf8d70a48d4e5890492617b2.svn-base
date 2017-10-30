package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流程表
 * @author zcz
 *
 */
@Entity
@Table(name = "lcb")
public class Lcb {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;//流程标识
	
	@Column(name = "name", length = 30)
	private String name;//流程名称
	
	@Column(name = "cjz", length = 20)
	private String cjz;//创建者
	
	@Column(name = "cjsj")
	private Date cjsj;//创建时间
	
	@Column(name = "bbh", length = 20)
	private String bbh;//版本号
	
	@Column(name = "zt", length = 2)
	private String zt;//状态（Y 启用， N 不启用）

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCjz() {
		return cjz;
	}

	public void setCjz(String cjz) {
		this.cjz = cjz;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	
	
}
