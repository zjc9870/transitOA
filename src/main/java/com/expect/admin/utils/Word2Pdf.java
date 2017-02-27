package com.expect.admin.utils;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Word2Pdf {
    public static final String wdFormatPDF = "17";

    public static void wordToPDF(String sfileName, String toFileName) {

        // System.out.println("启动Word...");
        // long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.call(docs, "Open", sfileName).toDispatch();
            // System.out.println("打开文档..." + sfileName);
            // System.out.println("转换文档到PDF..." + toFileName);
            File tofile = new File(toFileName);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", "c.pdf", // FileName
                    wdFormatPDF);
            // long end = System.currentTimeMillis();
            // System.out.println("转换完成..用时：" + (end - start) + "ms.");

        } catch (Exception e) {
            // System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            if (app != null)
                app.invoke("Quit", new Variant[] {});
        }

        // 如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
    }
}
