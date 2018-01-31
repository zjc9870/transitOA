package com.expect.admin.service.vo;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.service.vo.component.html.SelectOptionVo;

public class FunctionVo implements Comparable<FunctionVo> {

	private String id;
	private String name;
	private String icon;
	private String url;
	private String encodeUrl;
	private int sequence;
	private String sequenceStr;
	private String description;
	private FunctionVo parentFunctionVo;
	private List<FunctionVo> childFunctionVos = new ArrayList<>();
	private String parentId;
	private String parentName;
	private SelectOptionVo parentFunctionSov = new SelectOptionVo();
	private String count; //功能栏信息条目

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncodeUrl() {
		return encodeUrl;
	}

	public void setEncodeUrl(String encodeUrl) {
		this.encodeUrl = encodeUrl;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getSequenceStr() {
		return sequenceStr;
	}

	public void setSequenceStr(String sequenceStr) {
		this.sequenceStr = sequenceStr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FunctionVo getParentFunctionVo() {
		return parentFunctionVo;
	}

	public void setParentFunctionVo(FunctionVo parentFunctionVo) {
		this.parentFunctionVo = parentFunctionVo;
	}

	public List<FunctionVo> getChildFunctionVos() {
		return childFunctionVos;
	}

	public void setChildFunctionVos(List<FunctionVo> childFunctionVos) {
		this.childFunctionVos = childFunctionVos;
	}

	public void addChildFunction(FunctionVo childFunctionVo) {
		if (childFunctionVos == null) {
			childFunctionVos = new ArrayList<>();
		}
		childFunctionVos.add(childFunctionVo);
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public SelectOptionVo getParentFunctionSov() {
		return parentFunctionSov;
	}

	public void setParentFunctionSov(SelectOptionVo parentFunctionSov) {
		this.parentFunctionSov = parentFunctionSov;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public int compareTo(FunctionVo o) {
		if (this.getSequence() > o.getSequence()) {
			return 1;
		} else if (this.getSequence() < o.getSequence()) {
			return -1;
		}
		return 0;
	}

}
