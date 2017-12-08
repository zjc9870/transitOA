package com.expect.admin.factory.impl;

import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Document;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.service.*;
import com.expect.admin.service.vo.*;
import com.expect.admin.utils.WordXmlUtil;
import freemarker.template.TemplateException;
import org.apache.poi.hssf.record.formula.MemAreaPtg;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("jtswFactory")


public class JtswFactory implements WordXmlFactory
{
//    private final String TEMPLATE_NAME = "sw1.ftl";
    @Autowired
    DraftSwService draftSwService;
    @Autowired
    DocumentService documentService;
    @Autowired
    FwtzService fwtzService;
    @Autowired
    LcrzbService lcrzbService;
    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;


    public byte[] create(String wjid) throws IOException, TemplateException {
        Map<String, Object> dataMap = new HashMap();
        DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(wjid);
        getDataMap(draftSwVo, dataMap);

        byte[] content = WordXmlUtil.create(dataMap, "sw3.ftl");
        return content;
    }

    public String getFileName(String wjid) {
        DraftSwVo draftSwVo = this.draftSwService.getDraftSwVoById(wjid);
        return draftSwVo.getBh() + ".doc";
    }

    public void getDataMap(DraftSwVo draftSwVo, Map<String,Object> dataMap){
        String CYandBL ="";
        List<LcrzbVo> ldRecordList = draftSwVo.getLdRecordList();
        LcrzbVo ld = ldRecordList.iterator().next();
        List<LcrzbVo> xgryRecordList = draftSwVo.getXgryRecordList();
        Iterator iterator = xgryRecordList.iterator();
        int i = 0;
        while(iterator.hasNext()){
            LcrzbVo lcrzbVo = (LcrzbVo) iterator.next();
//            CYandBL = CYandBL +draftSwService.getCyOrBlByLcrzId(lcrzbVo.getId())+"："
            CYandBL = CYandBL +lcrzbVo.getClnrfl()+"："
                    + lcrzbVo.getUserName()+" 处理时间："+(lcrzbVo.getClsj().split(" "))[0]+ "<w:br/>处理意见："+lcrzbVo.getMessage()
                    +"<w:br/>签名："
                    +"<w:pict><w:binData w:name=\"wordml://02000002"+i+".jpg\" xml:space=\"preserve\">"
                    + getImageStrByUserId(lcrzbVo.getUser().getId())
                    + "</w:binData>"
                    + "<v:shape id=\"_x0000_i1025"+i+"\" type=\"#_x0000_t75\" style=\"width:68.4pt;height:35.35pt\">"
                    + "<v:imagedata src=\"wordml://02000002"+i+".jpg\" o:title=\"ceshi23\"/></v:shape></w:pict>"+"<w:br/>";
            i++;
        }
        String imagePathld=getImageStrByUserId(ld.getUser().getId());
        dataMap.put("zgs", draftSwVo.getSbd());
        dataMap.put("bh", draftSwVo.getBh());
        dataMap.put("dyrq", draftSwVo.getDyrq());
        dataMap.put("lwdw", draftSwVo.getLwdw());
        dataMap.put("wh", draftSwVo.getWh());
        dataMap.put("wjbt", draftSwVo.getWjbt());
        dataMap.put("shr", ld.getUserName());
        dataMap.put("shyj",ld.getMessage());
        dataMap.put("sprq",(ld.getClsj().split(" "))[0]);
        dataMap.put("cyandbl",CYandBL);
        if (!imagePathld.equals("签名附件没有上传")){
            dataMap.put("image1",imagePathld);
        }

    }


    public String getImageStrByUserId(String id){
        User user = userRepository.findOne(id);
        List<AttachmentVo> attachmentVos = userService.getQmAttachmentByUser(user);
        String imgFile="";
        int size=attachmentVos.size();
        if (attachmentVos !=null && attachmentVos.size()>0){
            imgFile=attachmentVos.get(size-1).getPath()+"/"+attachmentVos.get(size-1).getId();
        }
        if(imgFile==""){
            return "签名附件没有上传";
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//将图片路径用Base64编码
    }
}


