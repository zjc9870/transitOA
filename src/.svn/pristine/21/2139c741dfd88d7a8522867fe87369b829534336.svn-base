package com.expect.admin.factory.impl;

import com.expect.admin.data.dao.LcrzbRepository;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.service.DocumentService;
import com.expect.admin.service.FwtzService;
import com.expect.admin.service.LcrzbService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.FwtzVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.utils.WordXmlUtil;
import freemarker.template.TemplateException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.*;

import javax.swing.text.html.HTMLDocument;

@Service("jtgwFactory")


public class JtgwFactory implements WordXmlFactory
{
    private final String TEMPLATE_NAME = "jtfw.ftl";
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
        DocumentVo documentVo = documentService.getDocumentById(wjid);
        FwtzVo fwtzVo = fwtzService.getFwtzVoByDocumentIdDy(wjid);
        getDataMap(documentVo, fwtzVo, dataMap);

        byte[] content = WordXmlUtil.create(dataMap, "jtgwcs3.ftl");
//        byte[] content = WordXmlUtil.create(dataMap, "jtfw2.ftl");
        return content;
    }

    public String getFileName(String wjid) {
        DocumentVo documentVo = this.documentService.getDocumentById(wjid);
        return documentVo.getHtbh() + "号.doc";
    }

    public void getDataMap(DocumentVo documentVo, FwtzVo fwtzVo, Map<String, Object> dataMap) {

        String imagePathDy=getDyImageStr();
        String imagePathSh=getImageStrByUserId(documentVo.getSpr().getId());
        Map<String, String> mjMap = getMjMap();//mj是密级
        String mj = mjMap.get(documentVo.getMj());
        String sffb = "";
        String yj="";
        String shr;
        String zs;
        String cb;
        String cs;

        zs=fwtzService.getZsNameByFwtzVo(fwtzVo);
        cs=fwtzService.getCsNameByFwtzVo(fwtzVo);
        cb=fwtzService.getCbNameByFwtzVo(fwtzVo);
//        if (documentVo.getSffb().equals("1")) {
//            sffb = "是";
//        }
//        if (documentVo.getSffb().equals("2")) {
//            sffb = "否";
//        }
        if (documentVo.getGwshzt().equals("4")){
            yj=documentVo.getSpyj();//gwshzt=公文审核状态
        }
        User shUser = lcrzbService.getUser(documentVo.getId(),"gw");
        shr = shUser.getUsername();
        dataMap.put("ngwr", documentVo.getUserName());
        dataMap.put("bh", documentVo.getHtbh());
        dataMap.put("bt", documentVo.getBt());
        dataMap.put("ztc", documentVo.getZtc());
        dataMap.put("mj", mj);
        dataMap.put("sffb", sffb);
        dataMap.put("yfrq", documentVo.getYfrq());
        dataMap.put("zs",zs);
        dataMap.put("cs",cs);
        dataMap.put("cb",cb);
        dataMap.put("yj",yj);
        dataMap.put("shr",shr);
        dataMap.put("number",fwtzVo.getPrintNumber());
        dataMap.put("shrq",documentVo.getShrq());
        dataMap.put("qtgsm",documentVo.getZgs());
        if (!imagePathDy.equals("签名附件没有上传")){
            dataMap.put("imagedy",imagePathDy);
        }
        if (!imagePathSh.equals("签名附件没有上传")){
            dataMap.put("imagesh",imagePathSh);
        }

    }
    public Map<String, String> getMjMap() {
        Map<String, String> map = new HashMap();
        map.put("1", "绝密");
        map.put("2", "机密");
        map.put("3", "秘密");
        return map;
    }

    private String getDyImageStr() {
        List<AttachmentVo> attachmentVos = userService.getQmAttachment();
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


