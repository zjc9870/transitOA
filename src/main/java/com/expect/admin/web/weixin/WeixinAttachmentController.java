package com.expect.admin.web.weixin;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.Base64Util;
import com.expect.admin.utils.IOUtil;
import com.expect.admin.utils.PdfWatermark;
import com.expect.admin.utils.RequestUtil;
import com.expect.admin.utils.Word2Pdf;

@Controller
@RequestMapping("/weixin/attachment")
public class WeixinAttachmentController {
    private static final Logger log = LoggerFactory.getLogger(WeixinAttachmentController.class);

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 附件上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo upload(MultipartFile files, String path, HttpServletRequest request) {
		if (StringUtils.isEmpty(path)) {
			return new FileResultVo(false, "路径错误");
		}
		path = Base64Util.decode(path);
		FileResultVo frv = attachmentService.save(files, path);
		return frv;
	}

	/**
	 * 附件下载
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(String id, HttpServletResponse response, HttpServletRequest request) {
		if (StringUtils.isEmpty(id)) {
		    log.error("下载附件id为空");
			return;
		}
		AttachmentVo attachment = attachmentService.getAttachmentById(id);
		if (attachment != null) {
			String path = attachment.getPath() + File.separator + attachment.getId();
			byte[] buffer = IOUtil.inputDataFromFile(path);
			try {
				RequestUtil.downloadFile(buffer, attachment.getName(), response, request);
			} catch (IOException e) {
			    log.error("下载附件出错", e);
			}
		}
	}
	
	/**
	 * 将上传的word文件转换为PDF进行下载
	 * 对于合同的附件：上传的合同附件都是word文件
	 * 下载时：
	 * 1.合同所有审批没有完成之前所有用户下载的时候只能下载PDF版本
	 * 2.合同的审批完成后只有资产管理部可以下载word版本的附件，其他角色还是只能下载PDF
	 * @param id
	 * @param response
	 */
	@GetMapping("/downloadAttachmentAsPdf")
	public void downloadAttachmentAsPdf(String id, HttpServletResponse response, HttpServletRequest request) {
	    if (StringUtils.isEmpty(id)) {
	        log.error("下载附件id为空");
            return;
        }
	    AttachmentVo attachment = attachmentService.getAttachmentById(id);
	    if(attachment != null) {
	        //pdf文件的路径
	        String toFileNameWithoutSuffix = attachment.getPath() + File.separator + "convertToPdf" + File.separator + id;
	        //源文件的路径
	        String path = attachment.getPath() + File.separator + id;
//	        File pdfFile = new File(toFileName + ".pdf");//word转PDF后会自动加上.pdf的后缀
	        //如果不存在转换过的PDF文件就转换，否则就直接下载
	        Word2Pdf.wordToPDF(path, toFileNameWithoutSuffix + ".pdf");
	        try {
                PdfWatermark.setWartermark(toFileNameWithoutSuffix + "-withWatermark.pdf", toFileNameWithoutSuffix + ".pdf", "保密");
            } catch (Exception e) {
                log.error("pdf文件加水印出错 文件id为 ： " + id, e);
            }
	        byte[] buffer = IOUtil.inputDataFromFile(toFileNameWithoutSuffix + "-withWatermark.pdf");
	        try {
	            int lastDot = attachment.getName().lastIndexOf('.');
	            String pdfFileName = attachment.getName().substring(0, lastDot) + ".pdf";
	            RequestUtil.downloadFile(buffer, pdfFileName, response, request);
	        } catch (IOException e) {
	            log.error("下载附件出错", e);
	        }
	    }else {
	        log.error("没有找到id为 " + id + " 的附件");
	    }
	}

	/**
	 * 显示图片/视频
	 */
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public void show(String id, HttpServletResponse response) {
		if (StringUtils.isEmpty(id)) {
			return;
		}
		AttachmentVo attachment = attachmentService.getAttachmentById(id);
		if (attachment != null) {
			String path = attachment.getPath() + File.separator + attachment.getId();
			byte[] buffer = IOUtil.inputDataFromFile(path);
			try {
				response.getOutputStream().write(buffer);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
