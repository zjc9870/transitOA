package com.expect.admin.plugins.ueditor.upload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.expect.admin.plugins.ueditor.define.State;

@Component
public class Uploader {

	@Autowired
	private BinaryUploader bu;

	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader() {
	}

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec(MultipartFile upfile) {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName), this.conf);
		} else {
			state = bu.save(this.request, this.conf, upfile);
		}

		return state;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map<String, Object> getConf() {
		return conf;
	}

	public void setConf(Map<String, Object> conf) {
		this.conf = conf;
	}

}
