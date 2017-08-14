package com.expect.admin.utils;

import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import me.chanjar.weixin.common.util.StringUtils;

/**
 * 
 * HttpRequest的工具类
 *
 */
public class RequestUtil {

	/**
	 * 获得一个请求的全路径，包括参数
	 */
	public static String getFullUrlByReqeust(HttpServletRequest request) {
		String doName = request.getServletPath();

		String argus = "";
		Enumeration<String> names = request.getParameterNames();
		Map<String, String[]> m = request.getParameterMap();

		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String[] value = m.get(name);
			argus += name + "=" + value[0] + "&";
		}

		if (Strings.isNullOrEmpty(argus)) {
			return doName;
		} else {
			argus = argus.substring(0, argus.length() - 1);
			return doName + "?" + argus;
		}
	}

	/**
	 * 判断一个请求是否为ajax
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String type = request.getHeader("X-Requested-With");
		if (Strings.isNullOrEmpty(type)) {
			return false;
		}
		return type.equals("XMLHttpRequest");
	}

	/**
	 * 如果是word或是ppt文档，就直接打开，如果是其他的，就下载
	 */
	public static void openFile(byte[] buffer, String filename, String postfix, HttpServletResponse response)
			throws IOException {
		response.reset();
		String header;
		String fileType;
		filename = new String(filename.getBytes("utf-8"), "ISO8859-1");
		if (Strings.isNullOrEmpty(postfix) || "doc".equals(postfix) || "docx".equals(postfix)) {
			fileType = "msword";
			header = "inline;filename=" + filename;
			response.setContentType("application/" + fileType);
		} else if ("ppt".equals(postfix) || "pptx".equals(postfix)) {
			fileType = "x-ppt";
			header = "inline;filename=" + filename;
			response.setContentType("application/" + fileType);
		} else {
			header = "attachment;filename=" + filename;
		}
		response.setContentType("application/x-msdownload;");
		response.setCharacterEncoding("utf-8");
		int length = buffer.length;
		response.setHeader("Content-Length", String.valueOf(length));
		response.setHeader("Content-Disposition", header);
		ServletOutputStream sout = response.getOutputStream();
		sout.write(buffer, 0, length);
		sout.flush();
		sout.close();
	}

	/**
	 * 下载文件
	 */
	public static void downloadFile(byte[] buffer, String fileName,
	        HttpServletResponse response, HttpServletRequest request) throws IOException {
		response.reset();
//		response.setContentType("application/x-msdownload;");
		response.setContentType("application/octet-stream;");//如果用x-msdownload Safari浏览器会给下载后的文件加.exe后缀
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("UTF-8");

		int length = buffer.length;
		response.setHeader("Content-Length", String.valueOf(length));
//		boolean isIE = request.getHeader("User-Agent").toUpperCase().contains("MSIE");//判断用户使用的浏览器是否是IE浏览器
//		String header = "attachment;filename=" + fileNameEncode(isIE, fileName);

		String header = request.getHeader("User-Agent").toUpperCase();
		if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
			fileName = URLEncoder.encode(fileName, "utf-8");
			fileName = fileName.replace("+", "%20");    //IE下载文件名空格变+号问题
		} else {
			fileName = new String(fileName.getBytes(), "ISO8859-1");
		}
		String head =  "attachment; filename=\"" + fileName + "\"";
		response.setHeader("Content-Disposition", head);
		ServletOutputStream sout = response.getOutputStream();

		sout.write(buffer, 0, length);
		sout.flush();
		sout.close();
	}


	/**
     * 如果是IE浏览器就用utf-8编码文件名称，否则就用ISO8859-1编码
     * @param isIE true是IE浏览器  false不是IE浏览器
     * @param fileName 未编码的文件名称
     * @return 编码后的文件名称
     * @throws UnsupportedEncodingException
     */
    private static String fileNameEncode(boolean isIE, String fileName)
            throws UnsupportedEncodingException {
	    if(isIE){ 
	        return URLEncoder.encode(fileName, "UTF-8");
	    } else{ 
	        return new String(fileName.getBytes("utf-8"), "ISO8859-1");
	    }
    }

	/**
	 * 得到客户端ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
			if(index != -1){
				return ip.substring(0,index);
			}else{
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
		return request.getRemoteAddr();
	}
}
