package com.expect.admin.utils;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * word-xml文件工具
 * <pre>本类涉及的word文件都是另存为word-xml格式的文件，不是普通的word文件</pre>
 * @author DuWenjun
 *
 */
public class WordXmlUtil {

    private static Logger log =LoggerFactory.getLogger(WordXmlUtil.class);

    /**
     * 创建word
     * @param dataMap 数据Map：key为占位符，Object为真实数据
     * @param templateName 模板文件名称
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static byte[] create(Map<String,Object> dataMap,String templateName) throws IOException, TemplateException{
        //FreeMarker 配置
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0) ;
        configuration.setDefaultEncoding("UTF-8");
        //设置模板文件路径
       
//            configuration.setDirectoryForTemplateLoading(
//                    new File(WordXmlUtil.getFtlTemplateDirPath()));
        configuration.setClassForTemplateLoading(WordXmlUtil.class, "/ftl");
        //获取模板文件
        Template t = null ;
        try {
            t = configuration.getTemplate(templateName) ;
        } catch (TemplateNotFoundException e) {
            log.error("未找到模板文件:"+templateName, e);
            throw e ;
        }
        //输出到内存流
        ByteArrayOutputStream resOs = null ;
        Writer out = null ;
        byte[] res = null ;
        try {
            resOs = new ByteArrayOutputStream() ;
            out = new BufferedWriter(new OutputStreamWriter(resOs,Charset.forName("UTF-8"))) ; 
            t.process(dataMap, out);
            out.close();
            //获取byte数组
            res = resOs.toByteArray() ;
            resOs.close() ;
        } catch (IOException e) {
            log.error("生成文书失败！"+templateName, e);
            throw e ;
        }finally{
            if(resOs!=null) resOs.close();
            if(out!=null) out.close();
        }
        return res ;
    }

    /**
     * 合并word-xml文档（注意：只能用于word-xml格式，即word文档另存为word-xml格式）
     * 2007版本（目前都是2003版本的wordxml）
     * @param list 存放word-xml文件内容的byte数组
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public static byte[] merge2007(List<byte[]> list) throws IOException{
        Document all = null ;	//总篇
        for (int i = 0; i < list.size(); i++) {
            byte[] ws = list.get(i) ;
            ByteArrayInputStream in = null ;
            try {
                SAXReader reader = new SAXReader() ;
                in = new ByteArrayInputStream(ws) ;
                Document document = reader.read(in) ;
                Element root = document.getRootElement() ; 
                List<Element> elements = root.elements() ;
                Element part = elements.get(2) ;	//取第三个pkg:part，里面包含正文内容
                Element xmlData = part.element("xmlData");
                Element doc = xmlData.element("document");
                Element body = doc.element("body");
                if(i==0){
                    //当前是第一篇文档，设置总篇为第一篇
                    all = document ;
                }else{
                    //不是第一篇，则将该篇追加到总篇下面
                    //获取总篇的body
                    List<Element> eles = all.getRootElement().elements() ;
                    Element bd = eles.get(2).element("xmlData").element("document").element("body") ;
                    Element p = body.element("p") ;
                    Element pPr = p.element("pPr") ;
                    try {
                        appendNewPage(p, pPr);
                    } catch (Exception e) {
                        log.error("追加页失败，换备用方案", e);
                        //=====这种方法换页会多一个空行=====//
                        appendNewPage(bd);						
                    }
                    //追加当前篇的body里的内容
                    List<Element> contents = body.elements() ;
                    for (Element c : contents) {
                        Node node = c.detach() ;
                        bd.add(node);
                    }
                }
            } catch (DocumentException e) {
                log.error("合并文书失败！", e);
            } finally{
                if(in!=null) in.close(); 
            }
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        OutputFormat format = new OutputFormat() ;
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(byteOut, format) ;
        writer.write(all);
        writer.close();
        byte[] data = byteOut.toByteArray() ;
        byteOut.close() ;
        return data ;
    }

    /**
     * 合并word-xml文档（注意：只能用于word-xml格式，即word文档另存为word-xml格式）
     * 2003版本
     * @param list 存放word-xml文件内容的byte数组
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public static byte[] merge2003(List<byte[]> list) throws IOException {
        Document all = null ;	//总篇
        Element sectPr = null ;	//第一篇的页边距信息
        for (int i = 0; i < list.size(); i++) {
            byte[] ws = list.get(i) ;
            ByteArrayInputStream in = null ;
            try {
                SAXReader reader = new SAXReader() ;
                in = new ByteArrayInputStream(ws) ;
                Document document = reader.read(in) ;
                Element root = document.getRootElement() ; 
                Element body = root.element("body");
                if(i==0){
                    //当前是第一篇文档，设置总篇为第一篇
                    all = document ;
                    //第一篇的页边距信息
                    Element sect =  all.getRootElement().element("body").element("sect") ;
                    sectPr = sect.element("sectPr").createCopy() ;
                    //sect.remove(sectPr) ;
                }else{
                    //不是第一篇，则将该篇追加到总篇下面
                    //获取总篇的body(bd)
                    Element bd = all.getRootElement().element("body") ;
                    appendNewPage(bd);						
                    //追加当前篇的body里的内容
                    List<Element> contents = body.elements() ;
                    Element currentSect = body.element("sect") ;
                    Element currentSectPr = currentSect.element("sectPr") ;
                    currentSect.remove(currentSectPr) ;
                    currentSect.add(sectPr.createCopy());
                    for (Element c : contents) {
                        Node node = c.detach() ;
                        bd.add(node);
                    }
                }
            } catch (DocumentException e) {
                log.error("合并文书失败！", e);
            } finally{
                if(in!=null) in.close(); 
            }
        }


        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        OutputFormat format = new OutputFormat() ;
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(byteOut, format) ;
        writer.write(all);
        writer.close();
        byte[] data = byteOut.toByteArray() ;
        byteOut.close() ;
        return data ;
    }

    /**
     * 新加一页，不会多一个空行
     * @param parent child的父元素
     * @parem child 在此元素后加分页
     */
    @SuppressWarnings("unchecked")
    private static void appendNewPage(Element parent,Element child){
        List<Element> elements = parent.elements() ;
        int index = elements.indexOf(child) ;
        Element page = DocumentHelper.createElement("w:r") ;
        page.add(DocumentHelper.createElement("w:br").addAttribute("w:type", "page")) ;
        elements.add((index+1), page);	//在child节点后追加分页
    }

    /**
     * 新加一页，会多一个空行
     * @param body wordxml的body元素节点
     */
    private static void appendNewPage(Element body){
        body.addElement("w:p").addElement("w:r").addElement("w:br").addAttribute("w:type", "page") ;
    }

    /**
     * 获取Ftl模板路径
     * @return
     */
    public static String getFtlTemplateDirPath() {
        return "\\resources\\ftl\\";
    }

    /**
     * 发送回用户（包括将xml格式的word转为普通word）
     * @param response
     * @param content word内容
     * @param filename	文件名
     * @throws IOException
     */
    public static void sendToResponse(HttpServletResponse response,byte[] content,String filename) throws IOException{
        String wjm = URLEncoder.encode(filename, "UTF-8") 	//中文名转码
                .replace("%28", "(").replace("%29", ")") ;
        // 清空response
        response.reset();
        // 设置response的Header
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + wjm);
        OutputStream out = response.getOutputStream();

        out.write(content);
        out.flush();
        out.close();

    }

    /**
     * 将wordXml格式的doc转为普通的doc
     * @param wordXml
     * @return
     */
    public static byte[] convertWordXmlToDoc(byte[] wordXml){
        byte[] doc = null ;
        ByteArrayOutputStream baos = null ;
        ByteArrayInputStream bais = null ;
        try {
            baos = new ByteArrayOutputStream() ;
            bais = new ByteArrayInputStream(wordXml) ;
            POIFSFileSystem fs = new POIFSFileSystem();  
            DirectoryEntry directory = fs.getRoot();  
            directory.createDocument("WordDocument", bais);
            fs.writeFilesystem(baos);
            doc = baos.toByteArray() ;
            baos.flush();  
        } catch (IOException e) {
            log.error("保存wordxml为普通doc失败",e);
        }  finally {
            try {
                bais.close();
                baos.close();
            } catch (IOException e) {
                log.error("保存wordxml为普通doc失败",e);
            }  
        }
        return doc==null?wordXml:doc ;
    }

}

