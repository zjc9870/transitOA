package com.expect.admin.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.utils.WordXmlUtil;

import org.springframework.web.context.ContextLoaderListener;

@Controller
public class WordXmlController {
	Logger logger = LoggerFactory.getLogger(WordXmlController.class);
	
//	private final String beanClassPath = "com.expect.admin.factory.impl";

	@GetMapping("/wjsc")
	public void bgsc(HttpServletRequest request, String xgdm, String xgid, HttpServletResponse response) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		WordXmlFactory factory = null;
		String factoryBeanName = getFactoryBeanName(xgdm);
		try{
			factory = (WordXmlFactory) context.getBean(factoryBeanName);//获取本次转换的factory，目前只有两个可以选
		}catch(Exception e) {
			logger.error("未找到word生成factory："+factoryBeanName, e);
		}
		
		byte[] content = null;
		
		try {
			String fileName = factory.getFileName(xgid);//设置最后生成的word文件名
			content = factory.create(xgid);//利用wordxml的create函数创建word
			WordXmlUtil.sendToResponse(response, content, fileName);//将转换后的word发送给用户
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("表格生成失败,xgdm " + xgdm +"xgid "+ xgid, e);
		}
		
		
		
	}
	
	private String getFactoryBeanName(String xgdm){
//		String firChar = xgdm.substring(0, 1).toUpperCase();
//		String theLast = xgdm.substring(1, xgdm.length());
		return  xgdm+ "Factory";
	}
}
