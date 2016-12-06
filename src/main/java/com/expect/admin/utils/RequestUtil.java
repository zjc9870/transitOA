package com.expect.admin.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

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
	public static void downloadFile(byte[] buffer, String filename, HttpServletResponse response) throws IOException {
		response.reset();
		response.setContentType("application/x-msdownload;");
		response.setCharacterEncoding("utf-8");

		int length = buffer.length;
		response.setHeader("Content-Length", String.valueOf(length));
		String header = "attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1");
		response.setHeader("Content-Disposition", header);
		ServletOutputStream sout = response.getOutputStream();

		sout.write(buffer, 0, length);
		sout.flush();
		sout.close();
	}

	/**
	 * 得到客户端ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
