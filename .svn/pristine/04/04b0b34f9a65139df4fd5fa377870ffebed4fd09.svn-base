package com.expect.admin.utils;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;

/**
 * Http响应的构造器
 * 
 * @author zym
 * 
 */
public class MyResponseBuilder {
	
	/**
	 * 默认的字符集
	 */
	private static final String DEFAULT_CHARSET = "utf-8";
	
	
	/**
	 * 返回Json格式的结果
	 * 
	 * @param response http响应对象
	 * @param content 响应内容
	 * @throws IOException 
	 */
	public static void writeJsonResponse(HttpServletResponse response, String content) throws IOException {
		response.addHeader("Content-Type", "application/json;charset=" + DEFAULT_CHARSET);
		//response.set
        response.setCharacterEncoding(DEFAULT_CHARSET);
        
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.flush();
        writer.close();
	}
	
	public static  void writeAjaxJsonResponse(HttpServletResponse response, String content) throws IOException {
		response.addHeader("Content-Type", "text/html;charset=" + DEFAULT_CHARSET);
		//response.set
        response.setCharacterEncoding(DEFAULT_CHARSET);
        
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 
	 * @param response
	 * @param o
	 * @throws Exception
	 */
	public static  void writeAjaxJsonResponse(HttpServletResponse response, Object o) throws IOException{
		Gson gson = new Gson();
		String content = gson.toJson(o);
		writeAjaxJsonResponse(response, content);
	}
	
	/**
	 * 
	 * @param response
	 * @param o
	 * @throws Exception
	 */
	public static void writeJsonResponse(HttpServletResponse response, Object o) throws IOException{
		Gson gson = new Gson();
		String content = gson.toJson(o);
		writeJsonResponse(response, content);
	}
	
	/**
	 * 
	 * @param response
	 * @param o
	 * @throws Exception
	 */
	public  static void writeJsonResponseAH(HttpServletResponse response, String o) throws IOException{
		Gson gson = new Gson();
		String content = gson.toJson(o);
		writeJsonResponse(response, content);
	}
}
