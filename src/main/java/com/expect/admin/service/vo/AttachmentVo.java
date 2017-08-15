package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.service.vo.component.BaseVo;
import com.expect.admin.utils.JacksonJsonUtil;

public class AttachmentVo extends BaseVo {

	private String id;
	private String name;
	private String path;
	private String timeStr;

	public AttachmentVo(){

	}
	public AttachmentVo(Attachment attachment){
		this.id = attachment.getId();
		this.name = attachment.getName();
		this.path = attachment.getPath();
	}
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	@Override
	public String toString() {
		String result = JacksonJsonUtil.getInstance().write(this);
		return result;
	}


}
