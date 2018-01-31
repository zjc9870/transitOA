package com.expect.admin.service;

import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Document;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.*;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qifeng on 17/3/7.
 */
@Service
public class DocumentService {

    /**
     * 撤销状态
     */
    private static final String REVOCATION_CONDITION = "revocation";

    private static final String FWLC_ID = "4";//收文流程id
    @Autowired
    private LcrzbService lcrzbService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LcService lcService;
    @Autowired
    private RoleJdgxbGxbService roleJdgxbGxbService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LcjdbRepository lcjdbRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LcrzbRepository lcrzbRepository;
    @Autowired
    private  FwtzRepository fwtzRepository;
    @Autowired
    private FwtzService fwtzService;

    public String save(DocumentVo documentVo, String[] attachmentId){
        Document document = new Document(documentVo);
        UserVo userVo = userService.getLoginUser();
        User user= userRepository.findOne(userVo.getId());
        document.setNgwr(user);

        //处理公文和附件的对应关系
        if(attachmentId != null && attachmentId.length > 0) {
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if(attachmentList != null && attachmentList.size() > 0)
                document.setAttachments(new HashSet<>(attachmentList));
        }else document.setAttachments(new HashSet<Attachment>());

        documentRepository.save(document);
        return document.getId();
    }

    /**
     * 新公文的保存
     * @param documentVo
     * @param bczl  保存种类（提交：“tj”， 保存 ： “bc”）
     * @param attachmentId
     */
    @Transactional
    public void newDocumentSave(DocumentVo documentVo, String bczl, String[] attachmentId) {
        String fwfl = Document.FWFL_WTJ;
//        String condition = getCondition(bczl, FWLC_ID);
//        documentVo.setGwfl(fwfl);//发文分类(提交,未提交)
        String condition="0";//1:保存 2:提交
        documentVo.setSqsj(DateUtil.format(new Date(), DateUtil.fullFormat));
        if (StringUtil.equals(bczl, "tj")) {
            condition = "2";
        }
        else
            condition = "1";
        documentVo.setGwshzt(condition);//发文状态
        //设置发文申请的用户
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());
        Document document = new Document(documentVo);
        document.setNgwr(user);

        //设置审批人
        //从user找到公司，再从公司找到所有user，通过user找到user的所有role，通过对role的分析找到该公司的分管负责人
        UserVo gsfzr = userService.getZgsFzrByUser(userVo);
        document.setSpr(userRepository.findOne(gsfzr.getId()));



        //发文的附件信息
        if (attachmentId != null && attachmentId.length > 0) {
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if (!attachmentList.isEmpty()) document.setAttachments(new HashSet<>(attachmentList));
        }
        document = documentRepository.save(document);
        String id = document.getId();

        if (StringUtil.equals(bczl, "tj")) {
            addXzLcrz(id, condition);//如果是新增就增加一条日志记录
        }
    }

    @Cacheable(cacheName = "DOCUMENT_CACHE")
    public List<DocumentVo>  getDocumentByUserIdAndCondition(String userId, String condition, String lx) {
        List<Document> documentList = null;

        if(StringUtil.isBlank(condition)) return new ArrayList<>();
        if(StringUtil.equals(lx, "wtj")){//未提交
            documentList = getWtjDocuments(userId, condition);
        }
        if(StringUtil.equals(lx, "dsp") || StringUtil.equals(lx, "dht"))//待审批 || 待回填
//            documentList = documentRepository.findByGwshztOrderBySqsjDesc(condition);
            documentList = getGwspDspListCS(userId);
        if(StringUtil.equals(lx, "yht"))//已回填
            documentList = documentRepository.findYhtDocument(userId);
        if(StringUtil.equals(lx, "yth"))//已撤销的合同
            documentList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, REVOCATION_CONDITION);
        if(StringUtil.equals(lx, "ysp")){ //已审批（已审批就是根据个人取出的所以不需要再进行过滤）
        }


        List<DocumentVo> documentVos=new ArrayList<DocumentVo>();

        if(documentList ==null) return documentVos;
        for (Document document : documentList) {
            DocumentVo documentVo = new DocumentVo(document);
            documentVos.add(documentVo);
        }
        return documentVos;
    }

    private List<DocumentVo> filter(String userId, String condition, List<Document> documentList){
        List<DocumentVo> documentVoList = new ArrayList<DocumentVo>();
        return documentVoList;
    }

    /**
     * 发文审批已审批列表
     * @return
     */
    public List<DocumentVo> getGwspYspList(String userId) {
        List<DocumentVo> documentVoVoList=new ArrayList<DocumentVo>();
        List<Document> dhList = documentRepository.findByGwshztOrderBySqsjDesc("3");
        List<Document> ytgList = documentRepository.findByGwshztOrderBySqsjDesc("4");
        if(dhList != null && dhList.size() > 0) {
            for (Document document : dhList) {
                //判断登录用户是否是公文指定审批人
                if(document.getSpr()!=null) {
                    if (document.getSpr().getId().equals(userId)) {
                        Set<Lcrzb> lcrzbList = document.getLcrzSet();
                        DocumentVo documentVo = new DocumentVo(document);
                        setSpjg(userId, lcrzbList, documentVo);
                        documentVo.setGwshzt("打回");
                        documentVoVoList.add(documentVo);
                    }
                }
            }
        }
        if(ytgList != null && ytgList.size() > 0){
            for(Document document : ytgList){
                //判断登录用户是否是公文指定审批人
                if(document.getSpr()!=null){
                    if(document.getSpr().getId().equals(userId)) {
                    Set<Lcrzb> lcrzbList = document.getLcrzSet();
                    DocumentVo documentVo = new DocumentVo(document);
                    setSpjg(userId, lcrzbList, documentVo);
                    documentVo.setGwshzt("通过");
                    documentVoVoList.add(documentVo);
                    }
                }
            }
        }
        return documentVoVoList;
    }

    public List<DocumentVo> getGwspDspList(String userId) {
        List<DocumentVo> documentVoVoList=new ArrayList<DocumentVo>();
        List<Document> tjList = documentRepository.findByGwshztOrderBySqsjDesc("2");
        if(tjList != null && tjList.size() > 0) {
            for (Document document : tjList) {
                //判断登录用户是否是公文指定审批人
                if (document.getSpr() != null){
                    if (document.getSpr().getId().equals(userId)) {
                        Set<Lcrzb> lcrzbList = document.getLcrzSet();
                        DocumentVo documentVo = new DocumentVo(document);
                        setSpjg(userId, lcrzbList, documentVo);
                        documentVoVoList.add(documentVo);
                    }
             }
            }
        }
        return documentVoVoList;
    }
    //  为公文初始化时建立的，CS代表初始
    public List<Document> getGwspDspListCS(String userId) {
        List<Document> documentList=new ArrayList<Document>();
        List<Document> tjList = documentRepository.findByGwshztOrderBySqsjDesc("2");
        if(tjList != null && tjList.size() > 0) {
            for (Document document : tjList) {
                //判断登录用户是否是公文指定审批人
                if (document.getSpr() != null){
                    if (document.getSpr().getId().equals(userId)) {
                        documentList.add(document);
                    }
                }
            }
        }
        return documentList;
    }


    /**
     * 获取未提交的公文列表
     * @param userId
     * @param condition
     * @return
     */
    private List<Document> getWtjDocuments(String userId, String condition){
        List<Document> ythDocumentList = documentRepository.findYthDocument(userId, condition);//已退回合同
        List<Document> documentList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, condition);
        List<Document> result = new ArrayList<Document>();
        if(ythDocumentList == null || ythDocumentList.size() == 0) return documentList;
        Map<String, Document> ythDocumentMap = new HashMap<String, Document>();
        for (Document document2 : ythDocumentList) {
            ythDocumentMap.put(document2.getId(), document2);
        }
        for (Document document : documentList) {
            if(ythDocumentMap.get(document.getId()) == null)
                result.add(document);
        }

        return result;
    }


    /**
     * 公文审批流程日志
     * @param cljg
     * @param message
     * @param clnrid
     * @param clnrfl
     */
    @Transactional
    @TriggersRemove(cacheName = { "DOCUMENT_CACHE" }, removeAll = true)
    public void saveDocumentLcrz(String cljg, String message, String clnrid, String clnrfl) {
        DocumentVo documentVo = getDocumentById(clnrid);
        String curCondition = documentVo.getGwshzt();
        String nextCondition;
        if (StringUtil.equals(cljg,"不通过")){
            nextCondition="3";
        }else{
            nextCondition="4";
        }

        documentVo.setGwshzt(nextCondition);
        updateDocument(documentVo,null);

        String lcrzId = lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, nextCondition);
        bindDocumentWithLcrzSh(clnrid, lcrzId);
    }
    /**
     * 通过公文id获取公文详情
     * @param documentId
     * @return
     */
    public DocumentVo getDocumentById(String documentId) {
        Document document = documentRepository.findOne(documentId);
        if(document == null) throw new BaseAppException("id为 "+documentId+"的公文没有找到");
        DocumentVo documentVo = new DocumentVo(document);//公文的基本信息
        List<LcrzbVo> lcrzbVoList = lcrzbService.convert(document.getLcrzSet());
        Map<String, String> lcjdbMap = getAllLcjdMapping();
        for (LcrzbVo lcrzbVo : lcrzbVoList) {
            if(!StringUtil.isBlank(lcrzbVo.getLcjd())){
                String lcjdName = lcjdbMap.get(lcrzbVo.getLcjd());
                lcrzbVo.setLcjd(lcjdName);
            }
            if(StringUtil.equals(lcrzbVo.getCljg(),"xz")){
                lcrzbVo.setCljg("新增");


            }
       }
        documentVo.setLcrzbList(lcrzbVoList);
        List<AttachmentVo> attachmentVoList = getDocumentAttachment(document);
        documentVo.setAttachmentList(attachmentVoList);
        return documentVo;
    }

    public List<FwtzVo> getFwtzVosByDocumentId(String documentId) {
        List<String> tzids = fwtzRepository.findTzidByDocumentId(documentId);
        HashSet<String> tzidSet = new HashSet(tzids);
        Iterator tzidIterator = tzidSet.iterator();
        List<FwtzVo> fwtzVoList = new ArrayList();
        while (tzidIterator.hasNext()) {
            String tzid = tzidIterator.next().toString();
            FwtzVo fwtzVo = fwtzService.getFwtzVoByTzid(tzid);
            fwtzVoList.add(fwtzVo);
        }
        return fwtzVoList;
    }
    /**
     * 获取所有的合同的节点id和名字的map
     * @return
     */
    public Map<String, String> getAllLcjdMapping() {

        Map<String, String> resultMap = new HashMap<String, String>();

        resultMap.put("2", "发文申请");
        resultMap.put("3","发文审批");
        resultMap.put("4", "发文审批");
        resultMap.put("Y", "审核完成");
        return resultMap;
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

    private String getCondition(String bczl, String lcbs){
        String startCondition = lcService.getStartCondition(lcbs);
        if(!StringUtil.equals(bczl, "tj")) return startCondition;
        else return lcService.getNextCondition(lcbs, startCondition);
    }

    /**
     * 新增时增加流程日志
     * @param
     */
    @Transactional
    public void addXzLcrz(String id, String fwshzt) {
        LcrzbVo lcrzbVo = new LcrzbVo("xz", "");
        String lcrzId = lcrzbService.save(lcrzbVo, id, "gw", fwshzt);
        bindDocumentWithLcrz(id, lcrzId);
    }

    /**
     * 流程日志和公文的绑定
     * @param
     * @param lcrzId
     */
    public void bindDocumentWithLcrz(String documentId, String lcrzId) {
        Document document = documentRepository.findOne(documentId);
        Lcrzb lcrz = lcrzbRepository.findOne(lcrzId);
        document.getLcrzSet().add(lcrz);
        documentRepository.save(document);
    }

    /**
    * 发文审批的流程绑定
     * 此处给发文添加了审批时间和审批意见以及审批人id
     * 由于原来采用的流程节点来配置这个审批太复杂了，我就把信息尽量调出来放document表里了
    * */
    public void bindDocumentWithLcrzSh(String documentId, String lcrzId) {
        Document document = documentRepository.findOne(documentId);
        Lcrzb lcrz = lcrzbRepository.findOne(lcrzId);
        document.getLcrzSet().add(lcrz);
        String sdf = DateUtil.format(lcrz.getClsj(), "yyyy/MM/dd");
        document.setShrq(sdf);
        document.setSpyj(lcrz.getMessage());
        UserVo userVo = userService.getLoginUser();
        document.setSpr(UserConvertor.convert(userVo));
        documentRepository.save(document);
    }

    /**
     * 待审批公文队列
     * @param userId
     * @param condition
     * @return
     */
    @Cacheable(cacheName = "DOCUMENT_CACHE")
    public List<DocumentVo> getSqjldspList(String userId, String condition) {
        List<DocumentVo> documentVoVoList = new ArrayList<DocumentVo>();
        List<Document> dspList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, condition);
        if(dspList != null && dspList.size() > 0)
            for (Document document : dspList) {
                DocumentVo documentVo = new DocumentVo(document);
                documentVo.setGwshzt("待审批");
                documentVoVoList.add(documentVo);
            }
        return documentVoVoList;
    }
    @Cacheable(cacheName = "DOCUMENT_CACHE")
    public List<DocumentVo> getSqjlYspList(String userId) {
        List<DocumentVo> documentVoVoList = new ArrayList<DocumentVo>();
        List<Document> wtgList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, "3");
        List<Document> ytgList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, "4");
        if(wtgList != null && wtgList.size() > 0) {
            for (Document document : wtgList) {
                DocumentVo documentVo = new DocumentVo(document);
                documentVo.setGwshzt("打回");
                documentVoVoList.add(documentVo);
            }
        }
        if(ytgList != null && ytgList.size() > 0){
            for(Document document : ytgList){
                    DocumentVo documentVo = new DocumentVo(document);
                    documentVo.setGwshzt("通过");
                    documentVoVoList.add(documentVo);
            }
        }
        return documentVoVoList;
    }

    /**
     * 设置某人已审批公文的审批意见（某人对合同的最后一次的审批结果）
     * @param userId
     * @param lcrzbList
     * @param
     * @param
     */
    private void setSpjg(String userId, Set<Lcrzb> lcrzbList, DocumentVo documentVo) {
        List<Lcrzb> lcrzbListOfUser = new ArrayList<>();
        if(lcrzbList != null && !lcrzbList.isEmpty()){
            for (Lcrzb lcrzb : lcrzbList) {
                if(StringUtil.equals(lcrzb.getUser().getId(), userId))
                    lcrzbListOfUser.add(lcrzb);
            }
        }
        if(!lcrzbListOfUser.isEmpty()){
            Collections.sort(lcrzbListOfUser, new Comparator<Lcrzb>() {
                @Override
                public int compare(Lcrzb c1, Lcrzb c2) {
                    if(c1.getClsj() == null) return -1;
                    if(c2.getClsj() == null) return 1;
                    long dif = DateUtil.getDiffSeconds(c1.getClsj(), c2.getClsj());
                    return (dif > 0) ? -1 :
                            ((dif < 0) ? 1 : 0);
                }
            });
            Lcrzb lastLcrz = lcrzbListOfUser.get(0);
            documentVo.setSpyj(lastLcrz.getMessage());
        }else documentVo.setSpyj("");
    }
    @Transactional
    public void updateDocument(DocumentVo documentVo, String[] attachmentId) {
        if(documentVo == null || StringUtil.isBlank(documentVo.getId()))
            throw new BaseAppException("要更新的公文ID为空，无法更新");
        Document document = documentRepository.findOne(documentVo.getId());
        if(document == null) throw new BaseAppException("要更新的公文不存在！");
        //更新合同的附件列表
        if(attachmentId != null && attachmentId.length > 0) {
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if(attachmentList != null && attachmentList.size() > 0)
                document.setAttachments(new HashSet<>(attachmentList));
        }
        BeanUtils.copyProperties(documentVo, document);
        documentRepository.save(document);
    }

    /**
     * 从数据库中删除未提交的公文
     * @param id
     */
    @Transactional
    public void delete(String id) {
        Document document = documentRepository.findById(id);
        if(document == null) return;
        documentRepository.delete(document);
    }


}
