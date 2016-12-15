package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import com.expect.admin.service.vo.FunctionJdgxbGxbVo;

/**
 * 功能和流程中的节点的关联表
 * @author zcz
 *
 */
@Entity
@Table(name = "function_jdgxb")
public class FunctionJdgxbGxb {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "function_id", length = 32)
	private String functionId;
	
	@Column(name = "jdgxb_id", length = 32)
	private String jdgxbId;
	
	public FunctionJdgxbGxb(FunctionJdgxbGxbVo gxbVo) {
		BeanUtils.copyProperties(gxbVo, this);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getJdgxbId() {
		return jdgxbId;
	}
	public void setJdgxbId(String jdgxbId) {
		this.jdgxbId = jdgxbId;
	}
	
	
}
