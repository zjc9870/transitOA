package com.expect.admin.service;

import com.expect.admin.data.dao.DocumentRepository;
import com.expect.admin.data.dao.FwtzRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Document;
import com.expect.admin.data.dataobject.Fwtz;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.*;
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
    public String saveFwtz(com.expect.admin.service.vo.FwtzVo fwtzVo, String documentId) {
        String isExistNotifyObject = "";
        if (fwtzVo == null) return isExistNotifyObject;
        if (fwtzVo.getZsjtgg() != null) {
            String[] zsjtgg = fwtzVo.getZsjtgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zsjtgg, documentId, "1", "集团高管");
        }
        if (fwtzVo.getZsjtbm() != null) {
            String[] zsjtbm = fwtzVo.getZsjtbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zsjtbm, documentId, "1", "集团部门");
        }
        if (fwtzVo.getZsgsgg() != null) {
            String[] zsgsgg = fwtzVo.getZsgsgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zsgsgg, documentId, "1", "公司高管");
        }
        if (fwtzVo.getZsgsbm() != null) {
            String[] zsgsbm = fwtzVo.getZsgsbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zsgsbm, documentId, "1", "公司部门");
        }
        if (fwtzVo.getZsqtgsbgs() != null) {
            String[] zsqtgsbgs = fwtzVo.getZsqtgsbgs().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zsqtgsbgs, documentId, "1", "其他公司办公室");
        }
        if (fwtzVo.getZswbdw() != "") {
            String[] zswbdw = fwtzVo.getZswbdw().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(zswbdw, documentId, "1", "外部单位");
        }
        if (fwtzVo.getCsjtgg() != null) {
            String[] csjtgg = fwtzVo.getCsjtgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(csjtgg, documentId, "3", "集团高管");
        }
        if (fwtzVo.getCsjtbm() != null) {
            String[] csjtbm = fwtzVo.getCsjtbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(csjtbm, documentId, "3", "集团部门");
        }
        if (fwtzVo.getCsqtgsbgs() != null) {
            String[] csjtbgs = fwtzVo.getCsqtgsbgs().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(csjtbgs, documentId, "3", "其他公司办公室");
        }
        if (fwtzVo.getCsgsgg() != null) {
            String[] cssgsgg = fwtzVo.getCsgsgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cssgsgg, documentId, "3", "公司高管");
        }
        if (fwtzVo.getCsgsbm() != null) {
            String[] csgsbm = fwtzVo.getCsgsbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(csgsbm, documentId, "3", "公司部门");
        }
        if (fwtzVo.getCswbdw() != "") {
            String[] cswbdw = fwtzVo.getCswbdw().split(",");
           isExistNotifyObject = isExistNotifyObject + " " + save(cswbdw, documentId, "3", "外部单位");
        }
        if (fwtzVo.getCbjtgg() != null) {
            String[] cbjtgg = fwtzVo.getCbjtgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbjtgg, documentId, "2", "集团高管");
        }
        if (fwtzVo.getCbjtbm() != null) {
            String[] cbjtbm = fwtzVo.getCbjtbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbjtbm, documentId, "2", "集团部门");
        }
        if (fwtzVo.getCbqtgsbgs() != null) {
            String[] cbjtbgs = fwtzVo.getCbqtgsbgs().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbjtbgs, documentId, "2", "其他公司办公室");
        }
        if (fwtzVo.getCbgsgg() != null) {
            String[] cbgsgg = fwtzVo.getCbgsgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbgsgg, documentId, "2", "公司高管");
        }
        if (fwtzVo.getCbgsbm() != null) {
            String[] cbgsbm = fwtzVo.getCbgsbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbgsbm, documentId, "2", "公司部门");
        }
        if (fwtzVo.getCbwbdw() != "") {
            String[] cbwbdw = fwtzVo.getCbwbdw().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(cbwbdw, documentId, "2", "外部单位");
        }
        return isExistNotifyObject;
 }
    private String save(String[] tzdx, String documentId, String tzlx,String tzdxfl) {
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());

        String isExistObject = "";
        for (String dx : tzdx) {
            Boolean isExist = Boolean.valueOf(isNotifyObjectExist(dx, tzlx, documentId, tzdxfl));
            if (isExist.equals(Boolean.valueOf(false))) {
                isExistObject = isExistObject + " " + dx;
            }
            else{
                FwtzVo fwtzVo = new FwtzVo();
                fwtzVo.setFwid(documentId);
                fwtzVo.setTzlx(tzlx);
                fwtzVo.setTzdx(dx);
                fwtzVo.setTzdxfl(tzdxfl);
                fwtzVo.setIsread("0");
                Fwtz fwtz = new Fwtz(fwtzVo);
                fwtz.setTzfqr(user);
                fwtz.setTzid(randomString());

                Date tzsj = new Date();
                fwtz.setTzsj(tzsj);
                fwtzRepository.save(fwtz);
                String id = fwtz.getId();
            }
        }
        return isExistObject;
    }

    public boolean isNotifyObjectExist(String notifyObject, String tzlx, String documentId, String tzdxfl) { List<Fwtz> fwtzList = this.fwtzRepository.findByFwidAndTzdxAndTzlxAndTzdxfl(documentId, notifyObject, tzlx, tzdxfl);
        if (fwtzList.size() == 0) {
            return true;
        }
        return false;
    }
    public String randomString(){
        Date date = new Date();
        String random= DateUtil.format(date, DateUtil.longFormat).substring(0,14);
        return random;

    }
    public List<FwtzVo> getFwtzByFullName(String username) {
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
                String tzsj = fwtzVo.getTzsj();
                String ydsj = fwtzVo.getYdsj();
                String tzdx = fwtzVo.getTzdx();
                DocumentVo documentVo=getDocumentById(fwid);
                documentVo.setFwtzid(id);
                documentVo.setLoginUserRole(loginUserRole);
                documentVo.setYdsj(ydsj);
                documentVo.setTzsj(tzsj);
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
        Date date = new Date();
        fwtz.setYdsj(date);
        fwtzRepository.save(fwtz);
    }
    public FwtzVo getFwtzVoByDocumentId(String documentId) {
        List<Fwtz> fwtzList = this.fwtzRepository.findByFwid(documentId);
        FwtzVo fwtzVo = new FwtzVo();
        getNewFwtzVo(fwtzVo, fwtzList);
        return fwtzVo;
    }
    public FwtzVo getNewestFwtzVoByDocumentId(String documentId) {
        List<Fwtz> fwtzList = this.fwtzRepository.findFwtz(documentId);
        FwtzVo fwtzVo = new FwtzVo();
        getNewFwtzVo(fwtzVo, fwtzList);
        return fwtzVo;
    }

    public FwtzVo getFwtzVo(String id)
    {
        Fwtz fwtz1 = this.fwtzRepository.findOne(id);
        List<Fwtz> fwtzList = this.fwtzRepository.findByTzid(fwtz1.getTzid());
        FwtzVo fwtzVo = new FwtzVo();
        getNewFwtzVo(fwtzVo, fwtzList);
        return fwtzVo;
    }
    public FwtzVo getFwtzVoByTzid(String tzid) {
        List<Fwtz> fwtzList = this.fwtzRepository.findByTzid(tzid);
        FwtzVo fwtzVo = new FwtzVo();
        getNewFwtzVo(fwtzVo, fwtzList);
        return fwtzVo;
    }
    public void getNewFwtzVo(FwtzVo fwtzVo, List<Fwtz> fwtzList){
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

        if (fwtzList != null) {
            for (Fwtz fwtz : fwtzList) {
                if (fwtz.getTzlx() != null) {
                    if (fwtz.getTzlx().equals("1")) {
                        if (fwtz.getTzdxfl() != null) {
                            if (fwtz.getTzdxfl().equals("集团高管")) {
                                zsjtgg = mergeNotifyObject(fwtz.getIsread(), zsjtgg, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("集团部门")) {
                                zsjtbm = mergeNotifyObject(fwtz.getIsread(), zsjtbm, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("外部单位")) {
                                zswbdw = mergeNotifyObject(fwtz.getIsread(), zswbdw, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("其他公司办公室")) {
                                zsqtgsbgs = mergeNotifyObject(fwtz.getIsread(), zsqtgsbgs, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司高管")) {
                                zsgsgg = mergeNotifyObject(fwtz.getIsread(), zsgsgg, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司部门")) {
                                zsgsbm = mergeNotifyObject(fwtz.getIsread(), zsgsbm, fwtz.getTzdx());
                            }
                        }
                    }
                    else if (fwtz.getTzlx().equals("2")) {
                        if (fwtz.getTzdxfl() != null) {
                            if (fwtz.getTzdxfl().equals("集团高管")) {
                                cbjtgg = mergeNotifyObject(fwtz.getIsread(), cbjtgg, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("集团部门")) {
                                cbjtbm = mergeNotifyObject(fwtz.getIsread(), cbjtbm, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("外部单位")) {
                                cbwbdw = mergeNotifyObject(fwtz.getIsread(), cbwbdw, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("其他公司办公室")) {
                                cbqtgsbgs = mergeNotifyObject(fwtz.getIsread(), cbqtgsbgs, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司高管")) {
                                cbgsgg = mergeNotifyObject(fwtz.getIsread(), cbgsgg, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司部门")) {
                                cbgsbm = mergeNotifyObject(fwtz.getIsread(), cbgsbm, fwtz.getTzdx());
                            }
                        }
                    }
                    else if (fwtz.getTzlx().equals("3")) {
                        if (fwtz.getTzdxfl() != null) {
                            if (fwtz.getTzdxfl().equals("集团高管")) {
                                csjtgg = mergeNotifyObject(fwtz.getIsread(), csjtgg, fwtz.getTzdx());}
                            if (fwtz.getTzdxfl().equals("集团部门")) {
                                csjtbm = mergeNotifyObject(fwtz.getIsread(), csjtbm, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("外部单位")) {
                                cswbdw = mergeNotifyObject(fwtz.getIsread(), cswbdw, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("其他公司办公室")) {
                                csqtgsbgs = mergeNotifyObject(fwtz.getIsread(), csqtgsbgs, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司高管")) {
                                csgsgg = mergeNotifyObject(fwtz.getIsread(), csgsgg, fwtz.getTzdx());
                            }
                            if (fwtz.getTzdxfl().equals("公司部门"))
                                csgsbm = mergeNotifyObject(fwtz.getIsread(), csgsbm, fwtz.getTzdx());
                        }
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
    }

    public String mergeNotifyObject(String isRead, String dxjh, String notifyObject) {
        if (isRead.equals("0")) {
            dxjh = dxjh + notifyObject + "(未读) ";
        }
        else if (isRead.equals("1")) {
            dxjh = dxjh + notifyObject + "(已读) ";
        }
        return dxjh;
    }
    public List<DocumentVo> sortDocumentListByTzsj(List<DocumentVo> documentVoList) {
        if (documentVoList.size() <= 1) {
            return documentVoList;
        }
        TzsjCompare tzsjCompare = new TzsjCompare();
        Collections.sort(documentVoList, tzsjCompare);
        return documentVoList;
    }

    public List<DocumentVo> sortDocumentListByYdsj(List<DocumentVo> documentVoList) {
        if (documentVoList.size() <= 1) {
            return documentVoList;
        }
        YdsjCompare ydsjCompare = new YdsjCompare();
        Collections.sort(documentVoList, ydsjCompare);
        return documentVoList;
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