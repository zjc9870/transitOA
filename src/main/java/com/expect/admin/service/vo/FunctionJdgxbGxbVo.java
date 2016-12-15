package com.expect.admin.service.vo;

import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.FunctionJdgxbGxb;

public class FunctionJdgxbGxbVo {
	private String id;
	private String functionId;
	private String jdgxbId;
	
	public FunctionJdgxbGxbVo() {
	}
	
	public FunctionJdgxbGxbVo(FunctionJdgxbGxb gxb) {
		BeanUtils.copyProperties(gxb, this);
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
