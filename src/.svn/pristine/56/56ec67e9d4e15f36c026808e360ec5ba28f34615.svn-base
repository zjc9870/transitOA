package com.expect.admin.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.expect.admin.config.Settings;
import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.utils.IOUtil;
import sun.misc.BASE64Encoder;

/**
 * 附件Service
 */
@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private Settings settings;
	@Autowired
	private UserService userService;
	/**
	 * 保存单张图片
	 */
	public FileResultVo save(MultipartFile file, String path, String xgid) {
		FileResultVo frv = new FileResultVo(false, "上传失败");
		try {
			if (file == null || file.getBytes() == null) {
				return frv;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return frv;
		}
		if (StringUtils.isEmpty(path)) {
			path = settings.getAttachmentPath();
		}
		String originalFileName = file.getOriginalFilename();
		Attachment resultAttachment = saveFileInf(file, path, xgid);
		if (resultAttachment != null) {
			String fileName = resultAttachment.getId();
			try {
				IOUtil.outputDataToFile(file.getBytes(), path, fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return frv;
			}
			
			frv.setId(resultAttachment.getId());
			frv.setMessage("上传成功");
			frv.setResult(true);
		} else {
			frv.addErrorName(originalFileName);
		}
		return frv;
	}
	
	public FileResultVo save(MultipartFile file, String path) {
		FileResultVo frv = new FileResultVo(false, "上传失败");
		try {
			if (file == null || file.getBytes() == null) {
				return frv;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return frv;
		}
		if (StringUtils.isEmpty(path)) {
			path = settings.getAttachmentPath();
		}
		String originalFileName = file.getOriginalFilename();
		Attachment resultAttachment = saveFileInf(file, path);
		if (resultAttachment != null) {
			String fileName = resultAttachment.getId();
			try {
				IOUtil.outputDataToFile(file.getBytes(), path, fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return frv;
			}
			
			frv.setId(resultAttachment.getId());
			frv.setName(originalFileName);
			frv.setMessage("上传成功");
			frv.setResult(true);
		} else {
			frv.addErrorName(originalFileName);
		}
		return frv;
	}

	/**
	 * 保存文件的信息在数据库中
	 * @param file
	 * @param path
	 * @param xgid  文件相关联的id
	 * @return
	 */
	private Attachment saveFileInf(MultipartFile file, String path, String xgid) {
		Attachment attachment = new Attachment();
		attachment.setName(file.getOriginalFilename());
		attachment.setPath(path);
		attachment.setTime(new Date());
		Attachment resultAttachment = attachmentRepository.save(attachment);
		return resultAttachment;
	}

	private Attachment saveFileInf(MultipartFile file, String path) {
		Attachment attachment = new Attachment();
		attachment.setName(file.getOriginalFilename());
		attachment.setPath(path);
		attachment.setTime(new Date());
		Attachment resultAttachment = attachmentRepository.save(attachment);
		return resultAttachment;
	}
	/**
	 * 保存多张图片
	 */
	public FileResultVo save(MultipartFile[] files, String path, String xgid) {
		FileResultVo frv = new FileResultVo(false, "上传失败");
		if (files == null || files.length == 0) {
			return frv;
		}
		for (MultipartFile file : files) {
			FileResultVo resultFileRv = save(file, path, xgid);
			if (!resultFileRv.isResult()) {
				frv.addErrorName(resultFileRv.getName());
			} else {
				frv.addIds(resultFileRv.getId());
			}
		}
		frv.setMessage("上传成功");
		frv.setResult(true);
		return frv;
	}

	/**
	 * 根据id获取附件
	 */
	public AttachmentVo getAttachmentById(String id) {
		Attachment attachment = attachmentRepository.findOne(id);
		AttachmentVo attachmentVo = new AttachmentVo();
		BeanUtils.copyProperties(attachment, attachmentVo);
		return attachmentVo;
	}

	/**
	 * 根据ids获取多个附件
	 */
	public List<AttachmentVo> getAttachmentsByIds(String[] ids) {
		List<Attachment> attachments = attachmentRepository.findByIdIn(ids);
		if (CollectionUtils.isEmpty(attachments)) {
			return null;
		}
		List<AttachmentVo> attachmentVos = new ArrayList<>();
		for (Attachment attachment : attachments) {
			AttachmentVo attachmentVo = new AttachmentVo();
			BeanUtils.copyProperties(attachmentVo, attachment);
			attachmentVos.add(attachmentVo);
		}
		return attachmentVos;
	}
	
	public List<AttachmentVo> getAttachmentsByXgid(String xgid) {
		List<AttachmentVo> attachmentVoList = new ArrayList<AttachmentVo>();
		List<Attachment> attachmentList = attachmentRepository.findByXgId(xgid);
		if(attachmentList == null || attachmentList.size() == 0) return attachmentVoList;
		for (Attachment attachment : attachmentList) {
			AttachmentVo attachmentVo = new AttachmentVo();
			BeanUtils.copyProperties(attachmentVo, attachment);
			attachmentVoList.add(attachmentVo);
		}
		return attachmentVoList;
	}


}
