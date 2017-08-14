package com.expect.admin.plugins.ueditor.upload;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.expect.admin.config.Settings;
import com.expect.admin.plugins.ueditor.define.AppInfo;
import com.expect.admin.plugins.ueditor.define.BaseState;
import com.expect.admin.plugins.ueditor.define.State;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.utils.DateUtil;

@Component
public class BinaryUploader {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private Settings settings;

	public State save(HttpServletRequest request, Map<String, Object> conf, MultipartFile upfile) {
		try {
			String path = settings.getUeditorPath();
			String url = null;
			String fileName = null;
			String fileExt = null;

			if (upfile != null) {
				fileName = upfile.getOriginalFilename();
				fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				if (!validType(fileExt, (String[]) conf.get("allowFiles"))) {
					return new BaseState(false, 8);
				}
				String currentDate = DateUtil.format(new Date(System.currentTimeMillis()), DateUtil.newFormat);
				path = path + File.separator + currentDate;
				FileResultVo frv = attachmentService.save(upfile, path);
				if (!frv.isResult()) {
					return new BaseState(false, AppInfo.IO_ERROR);
				}
				String id = frv.getId();
				url = "attachment/show?id=" + id;
			}else{
				return new BaseState(false, AppInfo.IO_ERROR);
			}

			State storageState = new BaseState(Boolean.TRUE);
			if (storageState.isSuccess()) {
				storageState.putInfo("url", url);
				storageState.putInfo("type", fileExt);
				storageState.putInfo("original", fileName);
			}
			return storageState;
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseState(false, AppInfo.IO_ERROR);
		}
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
