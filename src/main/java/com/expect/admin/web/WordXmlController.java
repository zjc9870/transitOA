package com.expect.admin.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.utils.WordXmlUtil;


@Controller
public class WordXmlController {
	Logger logger = LoggerFactory.getLogger(WordXmlController.class);

	@PostMapping("/wjsc")
	public void bgsc(HttpServletRequest request, String xgdm, String xgid, HttpServletResponse response) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		WordXmlFactory factory = null;
		
		String factoryBeanName = getFactoryBeanName(xgdm);
		try{
			factory = (WordXmlFactory) context.getBean(factoryBeanName);
		}catch(Exception e) {
			logger.error("未找到word生成factory："+factoryBeanName, e);
		}
		
		byte[] content = null;
		
		try {
			String fileName = factory.getFileName(xgid);
			content = factory.create(xgid);
			WordXmlUtil.sendToResponse(response, content, fileName);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("表格生成失败,xgdm " + xgdm +"xgid "+ xgid, e);
		}
		
		
		
	}
	
	private String getFactoryBeanName(String xgdm){
		String firChar = xgdm.substring(0, 1).toUpperCase();
		String theLast = xgdm.substring(1, xgdm.length());
		return  firChar + theLast+ "Factory";
	}
}
