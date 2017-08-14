package com.expect.admin.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


public class PdfWatermark {

    
    /**
     * 为地址是sourceFilePath的文件设置水印，水印的内容为watermark，设置水印后的文件输出为destFilePath
     * 如果文件地址为destFilePath的文件已经存在了就不在从新生成
     * @param destFilePath 生成水印之后的文件的地址（要有.pdf后缀）
     * @param sourceFilePath要生成水印的文件的地址（要有.pdf后缀）
     * @param watermark 水印的内容
     * @throws IOException
     * @throws DocumentException
     */
    public static void setWartermark(String destFilePath, String sourceFilePath, String watermark
            ) throws IOException, DocumentException{
//        long start = System.currentTimeMillis();
        File destFile = new File(destFilePath);
        if(destFile.exists()) return;
        OutputStream destOs = new BufferedOutputStream(
                new FileOutputStream(destFile));
        PdfReader pdfReader = new PdfReader(sourceFilePath);
        PdfStamper stamper = new PdfStamper(pdfReader, destOs);
        int totalPage = pdfReader.getNumberOfPages();
        PdfContentByte over;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.EMBEDDED);
        for (int i = 0; i < totalPage; i++) {
//            over = stamper.getUnderContent(i + 1);//生成的水印在文字图片下方
            over = stamper.getOverContent(i + 1);//生成的水印在文字图片下方
            over.beginText();
            over.setColorFill(BaseColor.LIGHT_GRAY);  
            over.setFontAndSize(bf, 150);  
            over.setTextMatrix(70, 200); 
            //设置水印文字透明度
            PdfGState ps = new PdfGState();
            ps.setFillOpacity(0.3F);
            over.setGState(ps);
            
            over.showTextAlignedKerned(Element.ALIGN_CENTER, watermark, 300,350, 30);  
//            over.showTextAlignedgned(Element.ALIGN_CENTER, watermark, 300,350, 30);  
            over.endText();
        }
        stamper.close();
        pdfReader.close();
//        System.out.println("加水印时间" + (System.currentTimeMillis() - start));
    }
}
