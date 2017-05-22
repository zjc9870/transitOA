package com.expect.admin.service;

import com.expect.admin.data.dao.DocumentRepository;
import com.expect.admin.data.dao.FwtzRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Document;
import com.expect.admin.data.dataobject.Fwtz;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.FwtzVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by qifeng on 17/3/28.
 */
@Service
public class FwtzService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FwtzRepository fwtzRepository;
    @Autowired
    private DocumentRepository documentRepository;
    /**
     * 保存发文通知
     *
     * @param fwtzVo
     * @param documentId
     */
    @Transactional
    public void saveFwtz(FwtzVo fwtzVo, String documentId) {
        if (fwtzVo == null) return;
        if (fwtzVo.getZsjtgg() != null) {
            String[] zsjtgg = fwtzVo.getZsjtgg().split(",");
            save(zsjtgg, documentId, "1","集团高管");
        }
        if (fwtzVo.getZsjtbm() != null) {
            String[] zsjtbm = fwtzVo.getZsjtbm().split(",");
            save(zsjtbm, documentId, "1","集团部门");

        }
        if (fwtzVo.getZsgsgg() != null) {
            String[] zsgsgg = fwtzVo.getZsgsgg().split(",");
            save(zsgsgg, documentId, "1","公司高管");

        }
        if (fwtzVo.getZsgsbm() != null) {
            String[] zsgsbm = fwtzVo.getZsgsbm().split(",");
            save(zsgsbm, documentId, "1","公司部门");

        }
        if (fwtzVo.getZsqtgsbgs() != null) {
            String[] zsqtgsbgs = fwtzVo.getZsqtgsbgs().split(",");
            save(zsqtgsbgs, documentId, "1","其他公司办公室");
        }
        if (fwtzVo.getZswbdw() != "") {
            String[] zswbdw = fwtzVo.getZswbdw().split(",");
            save(zswbdw, documentId, "1","外部单位");
        }

        if (fwtzVo.getCsjtgg() != null) {
            String[] csjtgg = fwtzVo.getCsjtgg().split(",");
            save(csjtgg, documentId, "3","集团高管");

        }
        if (fwtzVo.getCsjtbm() != null) {
            String[] csjtbm = fwtzVo.getCsjtbm().split(",");
            save(csjtbm, documentId, "3","集团部门");
        }
        if (fwtzVo.getCsqtgsbgs() != null) {
            String[] csjtbgs = fwtzVo.getCsqtgsbgs().split(",");
            save(csjtbgs, documentId, "3","其他公司办公室");
        }
        if (fwtzVo.getCsgsgg() != null) {
            String[] cssgsgg = fwtzVo.getCsgsgg().split(",");
            save(cssgsgg, documentId, "3","公司高管");
        }
        if (fwtzVo.getCsgsbm() != null) {
            String[] csgsbm = fwtzVo.getCsgsbm().split(",");
            save(csgsbm, documentId, "3","公司部门");
        }
        if (fwtzVo.getCswbdw() != "") {
            String[] cswbdw = fwtzVo.getCswbdw().split(",");
            save(cswbdw, documentId, "3","外部单位");
        }

        if (fwtzVo.getCbjtgg() != null) {
            String[] cbjtgg = fwtzVo.getCbjtgg().split(",");
            save(cbjtgg, documentId, "2","集团高管");
        }
        if (fwtzVo.getCbjtbm() != null) {
            String[] cbjtbm = fwtzVo.getCbjtbm().split(",");
            save(cbjtbm, documentId, "2","集团部门");

        }
        if (fwtzVo.getCbqtgsbgs() != null) {
            String[] cbjtbgs = fwtzVo.getCbqtgsbgs().split(",");
            save(cbjtbgs, documentId, "2","其他公司办公室");
        }

        if (fwtzVo.getCbgsgg() != null) {
            String[] cbgsgg = fwtzVo.getCbgsgg().split(",");
            save(cbgsgg, documentId, "2","公司高管");

        }
        if (fwtzVo.getCbgsbm() != null) {
            String[] cbgsbm = fwtzVo.getCbgsbm().split(",");
            save(cbgsbm, documentId, "2","公司部门");
        }
        if (fwtzVo.getCbwbdw() != "") {
            String[] cbwbdw = fwtzVo.getCbwbdw().split(",");
            save(cbwbdw, documentId, "2","外部单位");
        }



    }

    private void save(String[] tzdx, String documentId, String tzlx,String tzdxfl) {
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());

        for (String dx : tzdx) {
            FwtzVo fwtzVo = new FwtzVo();
            fwtzVo.setFwid(documentId);
            fwtzVo.setTzlx(tzlx);
            fwtzVo.setTzdx(dx);
            fwtzVo.setTzdxfl(tzdxfl);
            fwtzVo.setIsread("0");
            Fwtz fwtz = new Fwtz(fwtzVo);
            fwtz.setTzfqr(user);
            fwtz.setTzid(randomString());
            fwtzRepository.save(fwtz);
            String id = fwtz.getId();
        }
    }

    public String randomString(){
        Date date = new Date();
        String random= DateUtil.format(date, DateUtil.longFormat).substring(0,14);
        return random;

    }
    public List<FwtzVo> getFwtz(String username) {
        List<Fwtz> fwtzList = fwtzRepository.findByTzdx(username);
        List<FwtzVo> fwtzVos = new ArrayList<FwtzVo>();
        if (fwtzList != null) {
            for (Fwtz fwtz : fwtzList) {
                FwtzVo fwtzVo = new FwtzVo(fwtz);
                fwtzVos.add(fwtzVo);
            }
        }
        return fwtzVos;
    }

    @Cacheable(cacheName = "FWTZ_CACHE")
    public List<FwtzVo> getAllFwtz(){
        List<Fwtz> fwtzList=fwtzRepository.findAll();
        List<FwtzVo> fwtzVos = new ArrayList<FwtzVo>();
        if (fwtzList != null) {
            for (Fwtz fwtz : fwtzList) {
                FwtzVo fwtzVo = new FwtzVo(fwtz);
                fwtzVos.add(fwtzVo);
            }
        }
        return fwtzVos;
    }
    public List<DocumentVo> getFwDocumentVo(List<FwtzVo> fwtzVos,String loginUserRole){
        List<DocumentVo> documentVos=new ArrayList<>();
        if(fwtzVos !=null ){
            for (FwtzVo fwtzVo:fwtzVos){
                String fwid = fwtzVo.getFwid();
                String id = fwtzVo.getId();
                String tzlx=fwtzVo.getTzlx();
                DocumentVo documentVo=getDocumentById(fwid);
                documentVo.setFwtzid(id);
                documentVo.setLoginUserRole(loginUserRole);
                if (StringUtil.equals(tzlx,"1")){
                    documentVo.setTzlx("主送");
                }else if(StringUtil.equals(tzlx,"2")){
                    documentVo.setTzlx("抄报");
                }else if(StringUtil.equals(tzlx,"3")){
                    documentVo.setTzlx("抄送");
                }
                documentVos.add(documentVo);
            }
        }
        return documentVos;
    }

    /**
     * 得到未读发文通知记录
     * @param fwtzVos
     * @return
     */
    public List<DocumentVo> getRecordDocumentVo(List<FwtzVo> fwtzVos){
        List<DocumentVo> documentVos=new ArrayList<>();
        if(fwtzVos !=null ){
            for (FwtzVo fwtzVo:fwtzVos){
                String fwid = fwtzVo.getFwid();
                String id = fwtzVo.getId();
                String tzlx = fwtzVo.getTzlx();
                String tzdx = fwtzVo.getTzdx();
                DocumentVo documentVo=getDocumentById(fwid);
                documentVo.setFwtzid(id);
                documentVo.setTzdx(tzdx);
                if (StringUtil.equals(tzlx,"1")){
                    documentVo.setTzlx("主送");
                }else if(StringUtil.equals(tzlx,"2")){
                    documentVo.setTzlx("抄报");
                }else if(StringUtil.equals(tzlx,"3")){
                    documentVo.setTzlx("抄送");
                }
                documentVos.add(documentVo);
            }
        }
        return documentVos;
    }
    public DocumentVo getDocumentById(String id){
        Document document = documentRepository.findOne(id);
        if(document == null) throw new BaseAppException("id为 "+id+"的公文没有找到");
        DocumentVo documentVo = new DocumentVo(document);//公文的基本信息
        List<AttachmentVo> attachmentVoList = getDocumentAttachment(document);
        documentVo.setAttachmentList(attachmentVoList);
        return documentVo;

    }

    /**
     *
     * @param id
     * @return
     */
    public FwtzVo getFwtzVoById(String id){
        Fwtz fwtz = fwtzRepository.findOne(id);
        if (fwtz == null) throw new BaseAppException("id为 "+id+"的发文通知没有找到");
        FwtzVo fwtzVo = new FwtzVo(fwtz);
        return fwtzVo;
    }
    /**
     * 获取一个公文相关的附件信息
     * @param document
     * @return
     */
    private List<AttachmentVo> getDocumentAttachment(Document document) {
        Set<Attachment> attachmentList = document.getAttachments();
        List<AttachmentVo> attachmentVoList = new ArrayList<>();
        if(attachmentList != null && !attachmentList.isEmpty())
            for (Attachment attachment : attachmentList) {
                AttachmentVo attachementVo = new AttachmentVo();
                BeanUtils.copyProperties(attachment, attachementVo);
                attachmentVoList.add(attachementVo);
            }
        return attachmentVoList;
    }

    /**
     * 未读fwtz列表
     * @param fwtzVos
     * @return
     */
    @Cacheable(cacheName = "FWTZ_CACHE")
    public List<FwtzVo> getWdFwtzList(List<FwtzVo> fwtzVos){
        List<FwtzVo> wdList=new ArrayList<>();
        if(fwtzVos !=null ){
            for (FwtzVo fwtzVo:fwtzVos){
                if(StringUtil.equals(fwtzVo.getIsread(),"0")){
                    wdList.add(fwtzVo);
                }
            }
        }
        return wdList;
    }


    @Cacheable(cacheName = "FWTZ_CACHE")
    public List<FwtzVo> getYdFwtzList(List<FwtzVo> fwtzVos){
        List<FwtzVo> wdList=new ArrayList<>();
        if(fwtzVos !=null ){
            for (FwtzVo fwtzVo:fwtzVos){
                if(StringUtil.equals(fwtzVo.getIsread(),"1")){
                    wdList.add(fwtzVo);
                }
            }
        }
        return wdList;
    }

    public void updateFwtz(FwtzVo fwtzVo){
        Fwtz fwtz = fwtzRepository.findOne(fwtzVo.getId());
        if (fwtz == null) throw new BaseAppException("id为 "+fwtzVo.getId()+"的发文通知没有找到");
        fwtz.setIsread("1");
        fwtzRepository.save(fwtz);
    }
    public FwtzVo getFwtzVo(String id){
        Fwtz fwtz1=fwtzRepository.findOne(id);
        List<Fwtz> fwtzList=fwtzRepository.findByTzid(fwtz1.getTzid());
        FwtzVo fwtzVo=new FwtzVo();
        String zsjtgg="";
        String zsjtbm="";
        String zsqtgsbgs="";
        String zswbdw="";
        String zsgsgg="";
        String zsgsbm="";

        String csjtgg="";
        String csjtbm="";
        String csqtgsbgs="";
        String cswbdw="";
        String csgsgg="";
        String csgsbm="";

        String cbjtgg="";
        String cbjtbm="";
        String cbqtgsbgs="";
        String cbwbdw="";
        String cbgsgg="";
        String cbgsbm="";
        Map<String,String> map=getAllTzdxMap();

        if (fwtzList !=null){
            for (Fwtz fwtz : fwtzList) {
                if (fwtz.getTzlx() ==null) continue;
                if (fwtz.getTzlx().equals("1")){
                    if (fwtz.getTzdxfl() ==null) continue;
                    if (fwtz.getTzdxfl().equals("集团高管")){
                        zsjtgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("集团部门")){
                        zsjtbm+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("外部单位")){
                        zswbdw+=fwtz.getTzdx()+" ";
                    }
                    if (fwtz.getTzdxfl().equals("其他公司办公室")){
                        zsqtgsbgs+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司高管")){
                        zsgsgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司部门")){
                        zsgsbm+=map.get(fwtz.getTzdx())+" ";
                    }
                }

                if (fwtz.getTzlx().equals("2")){
                    if (fwtz.getTzdxfl() ==null) continue;
                    if (fwtz.getTzdxfl().equals("集团高管")){
                        cbjtgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("集团部门")){
                        cbjtbm+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("外部单位")){
                        cbwbdw+=fwtz.getTzdx()+" ";
                    }
                    if (fwtz.getTzdxfl().equals("其他公司办公室")){
                        cbqtgsbgs+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司高管")){
                        cbgsgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司部门")){
                        cbgsbm+=map.get(fwtz.getTzdx())+" ";
                    }
                }

                if (fwtz.getTzlx().equals("3")){
                    if (fwtz.getTzdxfl() ==null) continue;
                    if (fwtz.getTzdxfl().equals("集团高管")){
                        csjtgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("集团部门")){
                        csjtbm+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("外部单位")){
                        cswbdw+=fwtz.getTzdx()+" ";
                    }
                    if (fwtz.getTzdxfl().equals("其他公司办公室")){
                        csqtgsbgs+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司高管")){
                        csgsgg+=map.get(fwtz.getTzdx())+" ";
                    }
                    if (fwtz.getTzdxfl().equals("公司部门")){
                        csgsbm+=map.get(fwtz.getTzdx())+" ";
                    }
                }
            }
        }
        fwtzVo.setZsjtgg(zsjtgg);
        fwtzVo.setZsjtbm(zsjtbm);
        fwtzVo.setZsqtgsbgs(zsqtgsbgs);
        fwtzVo.setZswbdw(zswbdw);
        fwtzVo.setZsgsgg(zsgsgg);
        fwtzVo.setZsgsbm(zsgsbm);

        fwtzVo.setCbjtgg(cbjtgg);
        fwtzVo.setCbjtbm(cbjtbm);
        fwtzVo.setCbqtgsbgs(cbqtgsbgs);
        fwtzVo.setCbwbdw(cbwbdw);
        fwtzVo.setCbgsgg(cbgsgg);
        fwtzVo.setCbgsbm(cbgsbm);

        fwtzVo.setCsjtgg(csjtgg);
        fwtzVo.setCsjtbm(csjtbm);
        fwtzVo.setCsqtgsbgs(csqtgsbgs);
        fwtzVo.setCswbdw(cswbdw);
        fwtzVo.setCsgsgg(csgsgg);
        fwtzVo.setCsgsbm(csgsbm);

        if (fwtzVo ==null){
            return null;
        }
        return fwtzVo;
    }
    public Map<String,String> getAllTzdxMap(){
        Map<String,String> tzdxMap = new HashMap<>();
        tzdxMap.put("jsh","监事会");
        tzdxMap.put("dsgjky","南京东山公交客运有限公司办公室");
        tzdxMap.put("dw","党委");
        tzdxMap.put("zjl","总经理");
        tzdxMap.put("cwb","财务部");
        tzdxMap.put("rlzyb","人力资源部");
        tzdxMap.put("dwgzb","党委工作部");
        tzdxMap.put("njggjtczjs","南京江宁公共交通场站建设有限公司办公室");
        tzdxMap.put("gsjsh","公司监事会");
        tzdxMap.put("gsdw","公司党委");
        tzdxMap.put("gszjl","公司总经理");
        tzdxMap.put("gscwb","公司财务部");
        tzdxMap.put("gsrlzyb","公司人力资源部");
        tzdxMap.put("gsdwgzb","公司党委工作部");


        return  tzdxMap;
    }



}