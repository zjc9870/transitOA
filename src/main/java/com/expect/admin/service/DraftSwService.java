package com.expect.admin.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.DraftSwRepository;
import com.expect.admin.data.dao.DraftSwUserLcrzbGxbRepository;
import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcrzbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.data.dataobject.DraftSwUserLcrzbGxb;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.DmbVo;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

@Service
public class DraftSwService {

    private final String SWLC_ID = "4";// 收文流程id
    private final String ldcljg = "已阅";// 领导处理结果
    @Autowired
    private UserService userService;
    @Autowired
    private LcService lcService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DraftSwRepository draftSwRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DraftSwUserLcrzbGxbRepository draftSwUserLcrzbGxbRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private LcrzbRepository lcrzbRepository;
    @Autowired
    private DmbService dmbService;
    @Autowired
    private LcjdbRepository lcjdbRepository;

    /**
     * 保存一个新的收文 如果不是提交 {<br>
     * 1.将收文的状态设置为收文流程的开始状态<br>
     * 2.收文的分类设置为“未提交 W”<br>
     * } 如果提交{<br>
     * 1.ldId(领导Id)不能为空 <br>
     * 2.将收文的状态设置为 开始状态的下一个状态（领导审批）<br>
     * 3.收文分类设置为“第一次 提交：Y”<br>
     * 4.保存一条DraftSwUserLcrzbGxb记录<br>
     * }
     *
     * @param sftj
     *            是否提交（true：提交（提交到流程） false：不是提交(只保存，不提交的流程)）
     * @param draftSwVo（收文的基本信息）
     * @param ldId（领导的id）
     */
    @Transactional
    public void saveANewDraftSw(boolean sftj, DraftSwVo draftSwVo, String ldId, String[] attachmentIds) {
        String startCondition = lcService.getStartCondition(SWLC_ID);
        String condition = startCondition;
        System.out.println("startCondition"+condition);
        String swfl = DraftSw.SWFL_WTJ;// 默认收文为未提交
        if (sftj) {
            if (StringUtil.isBlank(ldId))
                throw new BaseAppException("没有选择批示的领导!");
            condition = lcService.getNextCondition(SWLC_ID, startCondition);
            swfl = DraftSw.SWFL_SLYTJ;
        }
        // 设置收文申请的用户
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());
        DraftSw draftSw = new DraftSw(draftSwVo);
        draftSw.setSwr(user);
        draftSw.setFqsj(new Date());

        draftSw.setSwzt(condition);// 收文当前所处的状态
        draftSw.setSwfl(swfl);// 收文分类

        // 收文的附件信息
        if (attachmentIds != null && attachmentIds.length > 0) {
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentIds);
            if (!attachmentList.isEmpty())
                draftSw.setAttachments(new HashSet<>(attachmentList));
        }

        // 保存收文
        DraftSw savedDraftSw = draftSwRepository.save(draftSw);

        DraftSwUserLcrzbGxb draftSwUserLcrzbGxb = new DraftSwUserLcrzbGxb(user, savedDraftSw, null);

        Lcrzb swLcrz = new Lcrzb("发起收文", "发起收文", user);
        swLcrz.setClsj(new Date());
        swLcrz.setDyjd("新增");
        Lcrzb savedLcrz = lcrzbRepository.save(swLcrz);

        draftSwUserLcrzbGxb.setLcrz(savedLcrz);
        draftSwUserLcrzbGxbRepository.save(draftSwUserLcrzbGxb);

        // 保存审批领导的信息
        if (!StringUtil.isBlank(ldId)) {
            User ldUser = userRepository.findOne(ldId);
            draftSwUserLcrzbGxb = new DraftSwUserLcrzbGxb(ldUser, savedDraftSw,
                    DraftSwUserLcrzbGxb.RYFL_LD);
            draftSwUserLcrzbGxbRepository.save(draftSwUserLcrzbGxb);
        }

        // 保存审批领导的信息
//        if (!StringUtil.isBlank(ldId)) {
//            User ldUser = userRepository.findOne(ldId);
//            draftSwUserLcrzbGxb = new DraftSwUserLcrzbGxb(ldUser, savedDraftSw,DraftSwUserLcrzbGxb.RYFL_LD);
//
//            swLcrz = new Lcrzb("未审批", "领导审批", ldUser);
//            swLcrz.setClsj(new Date());
//            swLcrz.setDyjd("领导审批");
//            savedLcrz = lcrzbRepository.save(swLcrz);
//
//            draftSwUserLcrzbGxb.setLcrz(savedLcrz);
//
//
//            draftSwUserLcrzbGxbRepository.save(draftSwUserLcrzbGxb);
//        }
    }

    /**
     * 保存收文
     *
     * @param draftSwVo
     *            永远不能为null
     * @return
     */
    @Transactional
    public String save(DraftSwVo draftSwVo) {
        DraftSw draftSw = new DraftSw(draftSwVo);
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());
        draftSw.setSwr(user);
        DraftSw savedDraftSw = draftSwRepository.save(draftSw);
        return savedDraftSw.getId();
    }

    @Transactional
    public void update(DraftSwVo draftSwVo) {
        if (draftSwVo == null || StringUtil.isBlank(draftSwVo.getId()))
            return;
        draftSwRepository.save(new DraftSw(draftSwVo));
    }

    @Transactional
    public void updateDyrqAndBh(DraftSwVo draftSwVo){
        DraftSw draftSw = draftSwRepository.findOne(draftSwVo.getId());
        draftSw.setDyrq(draftSwVo.getDyrq());
        draftSw.setBh(draftSwVo.getBh());
        draftSwRepository.save(draftSw);

    }

    public void terminate(String draftSwId) {
        if(StringUtil.isBlank(draftSwId)) throw new BaseAppException("要结束的收文ID为空");
        DraftSw swToTernimate = draftSwRepository.findOne(draftSwId);
        if(swToTernimate == null) throw new BaseAppException("没有找到要结束的收文");
        swToTernimate.setSwzt("Y");//设置收文状态为已完成
        draftSwRepository.save(swToTernimate);
    }

    /**
     * 未实现
     *
     * @param id
     */
    @Transactional
    public void delete(String id) {
        throw new UnsupportedOperationException();
        // 1.收文的当前流程标识，判断当前状态是不是开始状态
        // 2.当前用户是不是收文的收文人
        // 3.删除收文，删除相关用户，删除流程日志
    }

    /**
     * 获取某个收文的信息
     *
     * @param id
     * @return
     */
    public DraftSwVo getDraftSwVoById(String id) {
        // 基本信息
        if (StringUtil.isBlank(id))
            throw new BaseAppException("根据ID获取收文是ID为空");
        DraftSw draftSw = draftSwRepository.findOne(id);
        if (draftSw == null)
            throw new BaseAppException("id为 " + id + "的收文没有找到");
        DraftSwVo draftSwVo = new DraftSwVo(draftSw);

        draftSwVo.setAttachmentList(
                getDraftSwAttachment(draftSw.getAttachments()));
        // 流程信息
        processLcrz(id, draftSwVo);
        return draftSwVo;
    }

    private List<AttachmentVo> getDraftSwAttachment(Set<Attachment> attachmentSet) {
//        Set<Attachment> attachmentList = contract.getAttachments();
        List<AttachmentVo> attachmentVoList = new ArrayList<>();
        if(attachmentSet != null && !attachmentSet.isEmpty())
            for (Attachment attachment : attachmentSet) {
                AttachmentVo attachementVo = new AttachmentVo();
                BeanUtils.copyProperties(attachment, attachementVo);
                attachmentVoList.add(attachementVo);
            }
        return attachmentVoList;
    }

    /**
     * 设置收文的流程信息
     *
     * @param id
     * @param draftSwVo
     */
    private void processLcrz(String id, DraftSwVo draftSwVo) {
        List<DraftSwUserLcrzbGxb> draftSwRecordList = draftSwUserLcrzbGxbRepository.findByDraftSwId(id);
        // 机要专员日志信息
        draftSwVo.setJyzyRecordList(getLcrzList(draftSwRecordList, null));
        // 领导日志信息
        draftSwVo.setLdRecordList(getLcrzList(draftSwRecordList, DraftSwUserLcrzbGxb.RYFL_LD));
        // 传阅人日志信息
        draftSwVo.setCyrRecordList(getLcrzList(draftSwRecordList, DraftSwUserLcrzbGxb.RYFL_CYR));
        // 办理人日志信息
        draftSwVo.setBlrRecordList(getLcrzList(draftSwRecordList, DraftSwUserLcrzbGxb.RYFL_BLR));
        // 传阅人和办理人的日志信息
        draftSwVo.setXgryRecordList(getLcrzList(draftSwRecordList, "XGRY"));
    }

    /**
     * 获取某类人员的流程日志列表
     *
     * @param draftSwRecordList
     * @return
     */
    private List<LcrzbVo> getLcrzList(List<DraftSwUserLcrzbGxb> draftSwRecordList, String ryfl) {
        Set<Lcrzb> recordSet = new HashSet<Lcrzb>();
        for (DraftSwUserLcrzbGxb draftSwUserLcrzbGxb : draftSwRecordList) {
            Lcrzb lcrz = draftSwUserLcrzbGxb.getLcrz();
            if (lcrz == null) {
                lcrz = processUnfinishedLcrz(draftSwUserLcrzbGxb.getUser());
            }
            if (StringUtil.equals(draftSwUserLcrzbGxb.getRyfl(), ryfl)) {
                recordSet.add(lcrz);
            }else if(StringUtil.equals("XGRY", ryfl)){
                //说明获取所有相关人员的日志
                if(StringUtil.equals(draftSwUserLcrzbGxb.getRyfl(), DraftSwUserLcrzbGxb.RYFL_CYR)){
                    lcrz.setClnrfl("传阅人");
                    recordSet.add(lcrz);
                }else if(StringUtil.equals(draftSwUserLcrzbGxb.getRyfl(), DraftSwUserLcrzbGxb.RYFL_BLR)){
                    lcrz.setClnrfl("办理人");
                    recordSet.add(lcrz);
                }
            }
        }
        return LcrzbService.convert(recordSet);
    }

    /**
     * 处理没有完成操作的流程显示
     *
     * @param today
     * @param draftSwUserLcrzbGxb
     * @param lcrz
     * @return
     */
    private Lcrzb processUnfinishedLcrz(User user) {
        Date today = DateUtil.today();
        Lcrzb lcrz = new Lcrzb("未处理", "", user);
        lcrz.setClsj(today);
        return lcrz;
    }

    /**
     * 根据页面和tab标签获取各种收文数据（所有数据都会与当前用户相关的） 收文记录页面(ym = "swjl") 1.未提交("wtj") 2.
     * 待处理("dcl") 3.已处理(未完成)("ycl") 4.已完成("ywc") 收文批示页面(ym = "swps")
     * 5.待批示("dps") 6.已批示("yps") 收文办理页面(ym = "swbl") 7.未办理("wbl") 8.已办理("ybl")
     * 收文传阅页面(ym = "swcy")9.待传阅("dcy") 10.已传阅("ycy")
     *
     * @param ym
     *            请求来着的页面
     * @param tab
     *            请求的tab
     * @param userId
     *            当前登录的用户的id(不为空)
     * @return 请求数据的列表，如果没有数据返回空列表，不会返回NULL
     * @throws BaseAppException
     *             如果userId为空（空字符串，或者null）
     */
    public List<DraftSwVo> getDraftSwVoList(String ym, String tab, String userId) {
        if (StringUtil.isBlank(userId))
            throw new BaseAppException("获取用户相关收文记录时用户的ID为空");
        if (StringUtil.equals(ym, "swjl")) {// 收文记录页面tab
            return getSwjlTabList(tab, userId);
        } else if (StringUtil.equals(ym, "swps")) {// 收文批示页面tab

            if (StringUtil.equals(tab, "dps"))
                return getSwblOrSwcyOrLdps(userId, DraftSwUserLcrzbGxb.RYFL_LD, false);
            if (StringUtil.equals(tab, "yps"))
                return getSwblOrSwcyOrLdps(userId, DraftSwUserLcrzbGxb.RYFL_LD, true);
        } else {
            String ryfl = null;
            boolean sfcl = false;
            if (StringUtil.equals(ym, "swbl")) {// 收文办理页面tab
                ryfl = DraftSwUserLcrzbGxb.RYFL_BLR;
                if (StringUtil.equals(tab, "ybl"))
                    sfcl = true;
            }

            else if (StringUtil.equals(ym, "swcy")) {// 收文传阅页面tab
                ryfl = DraftSwUserLcrzbGxb.RYFL_CYR;
                if (StringUtil.equals(tab, "ycy"))
                    sfcl = true;
            }

            return getSwblOrSwcyOrLdps(userId, ryfl, sfcl);
        }
        return getDraftSwVoListFromDraftSwList(null);
    }

    /**
     * 收文记录页面teb的请求数据获取，获取某用户（用户的id是userId的）申请的收文的相关他tab的
     * （如果tab内容不是下面四个选项之一，返回空的列表）记录
     *
     * @param tab
     *            tab名称{"wtj"：未提交, "dcl"：待处理, "ycl"：已处理 , "ywc"：已完成 }
     * @param userId
     *            相关的用户id（某个申请人）
     * @return 返回相应的列表 如果没有内容返回空的列表 不会返回NULL
     */
    private List<DraftSwVo> getSwjlTabList(String tab, String userId) {
        if (StringUtil.equals(tab, "wtj"))
            return getWtjSw(userId);
        else if (StringUtil.equals(tab, "dcl"))
            return getDclSw(userId);
        else if (StringUtil.equals(tab, "ycl"))
            return getYclSw(userId);
        else if (StringUtil.equals(tab, "ywc"))
            return getYwcSw(userId);
        else
            return getDraftSwVoListFromDraftSwList(null);
    }

    /**
     * 修改加收文id判断某收文相关的数据 获取收文办理或者收文传阅的收文
     *
     * @param userId
     *            相关用户id
     * @param ryfl
     *            人员分类（"blr":办理人 "cyr"：传阅人）
     * @param sfcl（是否处理
     *            true：已经处理过的（已办理， 已传阅） false：（未办理， 未传阅））
     * @return
     */
    private List<DraftSwVo> getSwblOrSwcyOrLdps(final String userId, final String ryfl, final boolean sfcl) {
        if (StringUtil.isBlank(ryfl))
            return getDraftSwVoListFromDraftSwList(null);
        List<DraftSw> wclDraftSwList = draftSwRepository.findAll(new Specification<DraftSw>() {
            @Override
            public Predicate toPredicate(Root<DraftSw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<DraftSw, DraftSwUserLcrzbGxb> join = root.joinSet("draftSwUserLcrzbGxbs", JoinType.LEFT);
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(cb.notEqual(root.get("swfl").as(String.class), DraftSw.SWFL_WTJ));// 不是未提交
                if (!StringUtil.isBlank(userId)) {
                    predicateList.add(cb.equal(join.get("user").get("id").as(String.class), userId));// 限定相关用户的Id
                }
                predicateList.add(cb.equal(join.get("ryfl").as(String.class), ryfl));// 人员分类

                // 未处理的相关联的lcrzb记录为null，已处理的不是null
                if (sfcl)
                    predicateList.add(cb.isNotNull(join.get("lcrz").as(Lcrzb.class)));
                else
                    predicateList.add(cb.isNull(join.get("lcrz").as(Lcrzb.class)));
//                Predicate[] predicateA = (Predicate[]) predicateList.toArray();
                Predicate[] predicate = new Predicate[predicateList.size()];
                return cb.and(predicateList.toArray(predicate));
            }
        });
        return getDraftSwVoListFromDraftSwList(wclDraftSwList);
    }

    // /**
    // * 领导待批示
    // * @return
    // */
    // public List<DraftSwVo> getDps(){
    // return
    // getDraftSwVoListFromDraftSwList(draftSwRepository.findBySwzt("16"));
    // }
    /**
     * 获取某用户未提交的收文（收文人id是userid 收文分类是‘W’）
     *
     * @param userId
     * @return
     */
    public List<DraftSwVo> getWtjSw(String userId) {
        List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwfl(userId, DraftSw.SWFL_WTJ);
        return getDraftSwVoListFromDraftSwList(draftSwList);
    }

    /**
     * 获取申请人的待处理收文
     *
     * @param userId
     * @return
     */
    public List<DraftSwVo> getDclSw(final String userId) {
        // String startCondition = lcService.getStartCondition(SWLC_ID);
        // lcService.getNextCondition(SWLC_ID, startCondition);
        String condition = "15";
        List<DraftSw> dclDraftSwList = null;
        // 第一轮的待处理收文
        List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwztAndSwfl(userId, condition, DraftSw.SWFL_SLYTJ);
        // 不是第一轮的待处理收文
        List<DraftSw> draftSwList2 = draftSwRepository.findBySwr_idAndSwztAndSwfl(userId, condition,
                DraftSw.SWFL_DLYTJ);
        if (draftSwList == null)
            dclDraftSwList = draftSwList2;
        else if (draftSwList2 == null)
            dclDraftSwList = draftSwList;
        else {
            draftSwList.addAll(draftSwList2);
            dclDraftSwList = draftSwList;
        }
        return getDraftSwVoListFromDraftSwList(dclDraftSwList);
    }

    /**
     * 收文发起人已处理但是还没有完成的收文
     *
     * @param userId
     * @return
     */
    public List<DraftSwVo> getYclSw(final String userId) {
        final String condition = lcService.getStartCondition(SWLC_ID);
        List<DraftSw> draftSwList = draftSwRepository.findAll(new Specification<DraftSw>() {

            @Override
            public Predicate toPredicate(Root<DraftSw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                list.add(cb.equal(root.get("swr").get("id").as(String.class), userId));
                list.add(cb.notEqual(root.get("swzt").as(String.class), condition));
                list.add(cb.notEqual(root.get("swzt").as(String.class), "Y"));

                Predicate[] predicate = new Predicate[list.size()];
                Predicate psssredicate = cb.and(list.toArray(predicate));
                return psssredicate;
            }
        });
        return getDraftSwVoListFromDraftSwList(draftSwList);
    }

    /**
     * 发起人发起的已完成的收文（收文状态为Y）
     *
     * @param userId
     * @return
     */
    public List<DraftSwVo> getYwcSw(final String userId) {
        List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwztOrderByTjsjDesc(userId, "Y");
        return getDraftSwVoListFromDraftSwList(draftSwList);
    }

    /**
     * 将DoList转化为VoList
     *
     * @param draftSwList
     * @return
     */
    private List<DraftSwVo> getDraftSwVoListFromDraftSwList(List<DraftSw> draftSwList) {
        if (draftSwList == null || draftSwList.isEmpty())
            return new ArrayList<>(0);
        List<DraftSwVo> draftSwVoList = new ArrayList<>(draftSwList.size());
        Map<String, String> swZtMap = getAllSwLcjdMapping();
        for (DraftSw draftSw : draftSwList) {
            DraftSwVo draftSwVo = getDraftSwVoFromDraftSw(draftSw);
            if(!StringUtil.isBlank(draftSw.getSwzt())) {
                draftSwVo.setZt(swZtMap.get(draftSw.getSwzt()));
            }
            draftSwVoList.add(draftSwVo);
        }
        return draftSwVoList;
    }

    public Map<String, String> getAllSwLcjdMapping() {
        List<Lcjdb> lcjdbList = lcjdbRepository.findBySslc("4");
        Map<String, String> resultMap = new HashMap<String, String>();
        for (Lcjdb lcjdb : lcjdbList) {
            resultMap.put(lcjdb.getId(), lcjdb.getName());
        }
        resultMap.put("T", "已回填");
        resultMap.put("Y", "审核完成");
        return resultMap;
    }

    /**
     * 将Do转化为Vo
     *
     * @param draftSw
     * @return
     */
    private DraftSwVo getDraftSwVoFromDraftSw(DraftSw draftSw) {
        DraftSwVo draftSwVo = new DraftSwVo(draftSw);
//        BeanUtils.copyProperties(draftSw, draftSwVo);
        return draftSwVo;
    }

    /**
     * 收文领导审批处理 1.保存领导审批流程 2.修改收文的状态为发起人处理的状态
     *
     * @param swId
     * @param ldps
     * @param userId
     */
    public void ldpscl(String swId, String ldps, String userId) {
        swcl(swId, ldps, userId, DraftSwUserLcrzbGxb.RYFL_LD);
    }

    /**
     * 收文传阅人意见的保存
     *
     * @param swId
     * @param cyyj
     * @param userId
     */
    public void swcy(String swId, String cyyj, String userId) {
        swcl(swId, cyyj, userId, DraftSwUserLcrzbGxb.RYFL_CYR);
    }

    /**
     * 收文办理意见的保存
     *
     * @param swId
     * @param blqk
     *            办理情况
     * @param userId
     */
    public void swbl(String swId, String blqk, String userId) {
        swcl(swId, blqk, userId, DraftSwUserLcrzbGxb.RYFL_BLR);
    }

    /**
     * 保存处理记录到流程日志表并且与收文关联 如果是领导批示 就讲收文状态修改为待机要专员处理的状态 否则是传阅或者办理
     * 只有所有的传阅人或办理人都处理完了才改变收文的装态到机要专员处理 否则就不该变收文的状态
     *
     * @param swId
     * @param cyyj
     * @param userId
     * @param ryfl
     */
    @Transactional
    private void swcl(String swId, String clyj, String userId, String ryfl) {
        saveSwsp(swId, clyj, userId, ryfl);
        if (StringUtil.equals(ryfl, DraftSwUserLcrzbGxb.RYFL_LD) || swclqkpd(swId, ryfl)) {
            DraftSw draftSw = draftSwRepository.findOne(swId);
            String swzt = "15";
            draftSw.setSwzt(swzt);
            draftSwRepository.save(draftSw);
        }
    }

    /**
     * 判断某个收文是否已经完成处理（所有的传阅人或办理人都已经处理完毕了）
     *
     * @param swId
     *            收文id
     * @param ryfl
     *            人员分类（传阅人或者办理人）
     * @return true：已经完成处理 false:未完成完成办理
     */
    private boolean swclqkpd(String swId, String ryfl) {
        List<DraftSwUserLcrzbGxb> wclSwjlList = draftSwUserLcrzbGxbRepository.findByDraftSwIdAndRyflAndLcrzIsNull(swId,
                ryfl);
        return (wclSwjlList == null || wclSwjlList.size() == 0);
    }

    /**
     * 保存人对收文的处理结果 1查找关系表 找到该用户该条收文的未审批的记录 2保存用户审批到流程日志 3关联关系表和流程日志
     *
     * @param swId
     *            收文的Id
     * @param ldps
     *            领导批示的内容
     * @param userId
     *            领导的ID
     */
    @Transactional
    public void saveSwsp(String swId, String ldps, String userId, String ryfl) {
        DraftSwUserLcrzbGxb draftSwUserLcrzbGxb = draftSwUserLcrzbGxbRepository
                .findByUserIdAndDraftSwIdAndRyflAndLcrzIsNull(userId, swId, ryfl);
        if (draftSwUserLcrzbGxb == null)
            throw new BaseAppException("该条记录已经处理，不能重复处理");

        User user = userRepository.findOne(userId);
        Lcrzb ldpsLcrz = new Lcrzb(ldcljg, ldps, user);
        ldpsLcrz.setClsj(new Date());
        Lcrzb savedLcrz = lcrzbRepository.save(ldpsLcrz);

        draftSwUserLcrzbGxb.setLcrz(savedLcrz);
        draftSwUserLcrzbGxbRepository.save(draftSwUserLcrzbGxb);
    }

    /**
     * 添加传阅人
     *
     * @param userIdList
     *            传阅人的idList(不会为空)
     * @param swVo
     *            相应收文id（不会为空）
     */
    @Transactional
    public void addCyr(List<String> userIdList, final String draftSwId) {
        // 获取相应的收文数据
        addXgry(userIdList, draftSwId, DraftSwUserLcrzbGxb.RYFL_CYR, "18");
    }

    /**
     * 添加办理人
     *
     * @param userIdList
     * @param draftSwId
     */
    @Transactional
    public void addBlr(List<String> userIdList, final String draftSwId) {
        addXgry(userIdList, draftSwId, DraftSwUserLcrzbGxb.RYFL_BLR, "20");
    }

    /**
     * 增加相关人员 拿到收文的Do数据（如果收文的数据为空抛出异常） 获取所有相关用户的user对象 从代码表中获取传阅人角色的id，拿到角色的对象
     * 循环为相关用户增加想应角色 生成收文相关人员对象列表并绑定到相应收文 修改收文的状态
     *
     * @param userIdList
     * @param draftSwId
     */
    @Transactional
    private void addXgry(List<String> userIdList, final String draftSwId, String ryfl, String nextCondition) {
        DraftSw draftSw = draftSwRepository.getOne(draftSwId);
        if (draftSw == null)
            throw new BaseAppException("没有找到想应的收文记录");
        // 获取相应的人员列表
        List<User> xgryList = userRepository.findAll(userIdList);
        if(xgryList == null || xgryList.size() == 0) throw new BaseAppException("未选择要添加的人员");
        // 从代码表中获取传阅人角色的id
        String xgjsDmbh = getXgjsDmbh(ryfl);
        DmbVo cyrJsId = dmbService.getDmbVoByLbbhAndDmbh("draftSw", xgjsDmbh);
        Role cyrRole = roleRepository.getOne(cyrJsId.getDmms());// 相关角色

        List<DraftSwUserLcrzbGxb> draftSwCyrGxList = new ArrayList<>(xgryList.size());
        for (User user : xgryList) {
            // 给相应用户增加角色
            Set<Role> roleSet = user.getRoles();
            roleSet.add(cyrRole);
            user.setRoles(roleSet);

            // 生成关系表中新的相关人员数据
            DraftSwUserLcrzbGxb draftSwCyr = new DraftSwUserLcrzbGxb(user, draftSw, ryfl);
            draftSwCyrGxList.add(draftSwCyr);
        }

        userRepository.save(xgryList);

        draftSwUserLcrzbGxbRepository.save(draftSwCyrGxList);
        draftSw.setSwzt(nextCondition);
        draftSwRepository.save(draftSw);
    }

    /**
     * 根据人员分类获取相关角色的代码编号
     *
     * @param ryfl
     * @return
     */
    private String getXgjsDmbh(String ryfl) {
        if (StringUtil.equals(ryfl, DraftSwUserLcrzbGxb.RYFL_BLR))
            return "blrJsId";
        if (StringUtil.equals(ryfl, DraftSwUserLcrzbGxb.RYFL_CYR))
            return "cyrJsId";
        if (StringUtil.equals(ryfl, DraftSwUserLcrzbGxb.RYFL_LD))
            return "ldjsId";
        throw new BaseAppException("没有相关角色");
    }

    public List<DraftSwVo> getHomePageContent(String ym, String id) {
        List<DraftSwVo> draftSwVoList = new ArrayList<DraftSwVo>();
        String sfcl = "dcl";
        if(StringUtil.contains(ym, "ycl")){
            sfcl = "ycl";
        }
        List<String> tabList = new ArrayList<String>();
        if(StringUtil.contains(ym, "dsz")){
            if(StringUtil.equals("ycl", sfcl)){
                tabList.add("yps");
            }else{
                tabList.add("dps");
            }
        }

        if(StringUtil.contains(ym, "jyzy")){
            if(StringUtil.equals("ycl", sfcl)){
                tabList.add("ycl");
            }else{
                tabList.add("dcl");
            }
        }
        if(StringUtil.equals("ycl", sfcl)){
            tabList.add("ycy");
            tabList.add("ybl");
        }else{
            tabList.add("dcy");
            tabList.add("dbl");
        }
        for(String tab : tabList){
            String returnym = "";
            switch(tab){
                case "ycy":
                case "dcy":
                    returnym = "swcy";
                    break;
                case "ybl":
                case "dbl":
                    returnym = "swbl";
                    break;
                case "yps":
                case "dps":
                    returnym = "swps";
                    break;
                case "ycl":
                case "dcl":
                    returnym = "swjl";
                    break;
            }
            List<DraftSwVo> subDraftSwVoList = getDraftSwVoList(returnym,tab,id);
            if(StringUtil.equals(tab, "ycl")||StringUtil.equals(tab, "ycy")||StringUtil.equals(tab, "yps")||StringUtil.equals(tab, "ybl")){
                for(DraftSwVo vo : subDraftSwVoList){
                    boolean cf = false;
                    vo.setTab(tab);
                    for(DraftSwVo draftSwVo: draftSwVoList){
                        if(StringUtil.equals(draftSwVo.getId(), vo.getId())){
                            cf = true;
                            break;
                        }
                    }if(!cf){
                        draftSwVoList.add(vo);
                    }
                }
            }else{
                for(DraftSwVo vo : subDraftSwVoList){
                    vo.setTab(tab);
                    draftSwVoList.add(vo);
                }
            }
        }
        //进行时间排序显示最近的
        draftSwVoList.sort(new SortByFqsj());
        return draftSwVoList;

    }

    class SortByFqsj implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            DraftSwVo vo1=(DraftSwVo) o1;
            DraftSwVo vo2=(DraftSwVo) o2;
            Date date1 = new Date(vo1.getFqsj());
            Date date2 = new Date(vo2.getFqsj());
            if(date1.after(date2)){
                return -1;
            }
            else
                return 1;
        }
    }

    // 获取发文是否是穿越还是办理人的信息
    public String getCyAndBl(DraftSwVo draftSwVo) {
        String CYandBL = "";
        List<LcrzbVo> xgryRecordList = draftSwVo.getXgryRecordList();
        Iterator iterator = xgryRecordList.iterator();
        while (iterator.hasNext()) {
            LcrzbVo lcrzbVo = (LcrzbVo) iterator.next();
            CYandBL = CYandBL + lcrzbVo.getClnrfl() + "："
                    + lcrzbVo.getUserName() + " 处理时间：" + (lcrzbVo.getClsj().split(" "))[0] + "<br/>处理意见："
                    + lcrzbVo.getMessage() + "<br/>"
            ;
        }
        return CYandBL;
    }
}