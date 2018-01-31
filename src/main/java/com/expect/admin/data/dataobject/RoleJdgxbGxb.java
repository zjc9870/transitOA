package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import com.expect.admin.service.vo.RoleJdgxbGxbVo;

/**
 * 功能和流程中的节点的关联表
 * @author zcz
 *
 */
@Entity
@Table(name = "role_jdgxb")
public class RoleJdgxbGxb {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "role_id", length = 32)
	private String roleId;
	
	@Column(name = "jd_id", length = 32)
	private String jdId;
	
	@Column(name = "wjzl", length = 10)
	private String wjzl;//文件种类（集团合同等）
	
	@Column(name = "bz", length = 20)
	private String bz;//用来标识这个关系用在哪个阶段(审批阶段)
	
	public RoleJdgxbGxb(RoleJdgxbGxbVo gxbVo) {
		BeanUtils.copyProperties(gxbVo, this);
	}

	public RoleJdgxbGxb(String bz,String jdId,String roleId,String wjzl){
		this.bz = bz;
		this.jdId= jdId;
		this.roleId = roleId;
		this.wjzl= wjzl;
	}
	public RoleJdgxbGxb() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getJdId() {
		return jdId;
	}

	public void setJdId(String jdId) {
		this.jdId = jdId;
	}

	public String getWjzl() {
		return wjzl;
	}

	public void setWjzl(String wjzl) {
		this.wjzl = wjzl;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
