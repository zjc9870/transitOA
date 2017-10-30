package com.expect.admin.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * http请求的相应处理
 */
public class ResponseBuilder {

	private static final String DEFAULT_CHARSET = "utf-8";

	/**
	 * 返回json请求的数据，传入参数content，必须满足json格式
	 */
	public static void writeJsonResponse(HttpServletResponse response, String content) throws IOException {
		response.addHeader("Content-Type", "application/json;charset=" + DEFAULT_CHARSET);
		response.setCharacterEncoding(DEFAULT_CHARSET);

		PrintWriter writer = response.getWriter();
		writer.write(content);
		writer.flush();
		writer.close();
	}

	/**
	 * 返回json请求的数据，传入参数Object
	 */
	public static void writeJsonResponse(HttpServletResponse response, Object o) throws IOException {
		String content = JacksonJsonUtil.getInstance().write(o);
		writeJsonResponse(response, content);
	}

	/**
	 * ajaxupload的json返回参数的设置
	 */
	public static void writeJsonResponseForAjaxUpload(HttpServletResponse response, Object o) throws Exception {
		String content = JacksonJsonUtil.getInstance().write(o);

		response.addHeader("Content-Type", "text/html;charset=" + DEFAULT_CHARSET);
		response.setCharacterEncoding(DEFAULT_CHARSET);

		PrintWriter writer = response.getWriter();
		writer.write(content);
		writer.flush();
		writer.close();
	}

}