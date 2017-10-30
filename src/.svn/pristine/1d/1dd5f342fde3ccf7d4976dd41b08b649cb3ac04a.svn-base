package com.expect.admin.factory;

import java.io.IOException;

import freemarker.template.TemplateException;

/**
 * 生成word
 * @author DuWenjun
 *
 */
public interface  WordXmlFactory {
	
	/**直接接在字符串后面*/
	static final String BR_TYPE_LINE = "</w:t></w:r></w:p><w:p><w:pPr><w:jc w:val=\"left\"/></w:pPr><w:r><w:t>" ;
	
	/**对于word2003xml放在body下的sect节点后:在模板中使用占位符*/
	static final String BR_TYPE_PAGE = "<w:p><w:r><w:br w:type=\"page\"/></w:r></w:p>" ;
	
	/**
	 * 在段落中添加换行符
	 */
	static final String BR_P_PAGE = "<w:r><w:br w:type=\"page\"/></w:r>" ;
	/**
	 * 创建word
	 * @param wjid 文件id
	 * @return
	 * @throws IOException
	 * @throws TemplateException 未找到模板文件时抛出
	 */
	byte[] create(String wjid) throws IOException, TemplateException ;
	
	/**
	 * 创建的word文件名
	 * @param wjid
	 * @return
	 */
	String getFileName(String wjid) ;
}

