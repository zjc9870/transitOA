package com.expect.admin.factory.impl;

import com.expect.admin.factory.WordXmlFactory;
/*  4:   */import com.expect.admin.service.DocumentService;
/*  5:   */import com.expect.admin.service.FwtzService;
/*  6:   */import com.expect.admin.service.vo.DocumentVo;
/*  7:   */import com.expect.admin.service.vo.FwtzVo;
/*  8:   */import com.expect.admin.utils.WordXmlUtil;
/*  9:   */import freemarker.template.TemplateException;
/* 10:   */import java.io.IOException;
/* 11:   */import java.util.HashMap;
/* 12:   */import java.util.Map;
/* 13:   */import org.springframework.beans.factory.annotation.Autowired;
/* 14:   */import org.springframework.stereotype.Service;

@Service("jtgwFactory")
public class JtgwFactory implements WordXmlFactory
{
    private final String TEMPLATE_NAME = "jtfw.ftl";
    @Autowired
    DocumentService documentService;
    @Autowired
    FwtzService fwtzService;

    public byte[] create(String wjid) throws IOException, TemplateException {
        Map<String, Object> dataMap = new HashMap();
        DocumentVo documentVo = this.documentService.getDocumentById(wjid);
        FwtzVo fwtzVo = this.fwtzService.getNewestFwtzVoByDocumentId(wjid);
        getDataMap(documentVo, fwtzVo, dataMap);

        byte[] content = WordXmlUtil.create(dataMap, "jtfw.ftl");
        return content;
    }

    public String getFileName(String wjid) {
        DocumentVo documentVo = this.documentService.getDocumentById(wjid);
        return documentVo.getHtbh() + "号.doc";
    }

    public void getDataMap(DocumentVo documentVo, FwtzVo fwtzVo, Map<String, Object> dataMap) {
        Map<String, String> mjMap = getMjMap();
        String mj = (String)mjMap.get(documentVo.getMj());
        String sffb = "";
        if (documentVo.getSffb().equals("1")) {
            sffb = "是";
        }
        if (documentVo.getSffb().equals("2")) {
            sffb = "否";
        }
        dataMap.put("ngwr", documentVo.getUserName());
        dataMap.put("bh", documentVo.getHtbh());
        dataMap.put("bt", documentVo.getBt());
        dataMap.put("ztc", documentVo.getZtc());
        dataMap.put("mj", mj);
        dataMap.put("sffb", sffb);
        dataMap.put("yfrq", documentVo.getYfrq());
    }
    public Map<String, String> getMjMap() {
        Map<String, String> map = new HashMap();
        map.put("1", "绝密");
        map.put("2", "机密");
        map.put("3", "秘密");
        return map;
    }
}


