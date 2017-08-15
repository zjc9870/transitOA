package com.expect.admin.service.vo.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件Vo
 */
public class FileResultVo extends ResultVo {

	private String id;// 如果使用附件表，且只有一个文件，传回一个id
	private String ids;// 如果使用附件表，且有多个文件，传回一个ids(多个id用,隔开)
	// 如果不使用附件表，则传回文件名和路径
	private String name;
	private String path;
	private List<String> errorNames;// 图片上传失败，则记录

	public FileResultVo() {

	}

	public FileResultVo(boolean result, String message, String id, boolean isMany) {
		this.result = result;
		this.message = message;
		if (isMany) {
			this.ids = id;
		} else {
			this.id = id;
		}
	}

	public FileResultVo(boolean result, String message) {
		this.result = result;
		this.message = message;
	}

	public FileResultVo(boolean result, String message, String name, String path) {
		this.result = result;
		this.message = message;
		this.name = name;
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void addIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			this.ids = ids;
		} else {
			this.ids += "," + ids;
		}
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

	public List<String> getErrorNames() {
		return errorNames;
	}

	public void setErrorNames(List<String> errorNames) {
		this.errorNames = errorNames;
	}

	public void addErrorName(String name) {
		if (this.errorNames == null) {
			this.errorNames = new ArrayList<>();
		}
	}

}
