package com.expect.admin.service;

import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.*;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.MeetingVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class MeetingService {
    /**
     * 撤销状态
     */
    private static final String REVOCATION_CONDITION = "revocation";

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private LcrzbService lcrzbService;
    @Autowired
    private UserService userService;
    @Autowired
    private MeetingroomService meetingroomService;
    @Autowired
    private LcjdbRepository lcjdbRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private MeetingroomRepository meetingroomRepository;
    @Autowired
    private LcrzbRepository lcrzbRepository;
    @Autowired
    private MemoService memoService;
    @Transactional

    /**
     * 新会议申请的保存
     * @param meetingVo
     * @param bczl 保存种类（提交：”tj“，保存：”bc“）
     * @param attachmentId
     */
    public void newMeetingSave(MeetingVo meetingVo, String bczl, String[] attachmentId)
    {
        String hyfl = getHyfl(meetingVo);
        meetingVo.setHyfl(hyfl);
        String condition = "0"; //1:保存 2：提交
        meetingVo.setHyshzt(condition); //会议审核状态
        meetingVo.setSqsj(DateUtil.format(new Date(), DateUtil.fullFormat));
        if(StringUtil.equals(bczl, "tj"))
            condition = "2";
        else
            condition = "1";
        meetingVo.setHyshzt(condition); //会议状态

        //设置会议申请的用户
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());
        Meeting meeting = new Meeting(meetingVo);
        meeting.setNhyr(user);
        //会议的附件信息
        if (attachmentId != null && attachmentId.length > 0){
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if (!attachmentList.isEmpty()) meeting.setAttachments(new HashSet<>(attachmentList));
        }
        meeting = meetingRepository.save(meeting);
        String id = meeting.getId();

        //绑定会议室
        bindMeetingWithRoom(meeting);
        if (StringUtil.equals(bczl,"tj")){
            addXzLcrz(id,condition); //如果是新增就增加一条日志记录
        }
    }

    public void bindMeetingWithRoom(Meeting meeting){
        String hydd = meeting.getHydd();
        String hysname = meeting.getHys();
        String meetingroomId = meetingroomRepository.findMeetingroomId(hydd, hysname);
        Meetingroom meetingroom = meetingroomRepository.findById(meetingroomId);
        meetingroom.getMeetings().add(meeting);
        meetingroomRepository.save(meetingroom);
    }

    /**
     * 新申请会议时获取会议分类
     * @return
     */
    public String getHyfl(MeetingVo meetingVo){
        if(meetingVo.getHydd().indexOf("集团") != -1)
            return "1"; //集团会议
//        else
//            return "2";
        if(meetingVo.getHydd().indexOf("东山公交") != -1) return "2"; //东山公交会议
        return "3"; //非集团会议

    }

    /**
     * 新增时增加流程日志
     * @param
     */
    @Transactional
    public void addXzLcrz(String id, String hyshzt){
        LcrzbVo lcrzbVo = new LcrzbVo("新增","");
        String lcrzId = lcrzbService.save(lcrzbVo, id, "hy", hyshzt);
        bindMeetingWithLcrz(id, lcrzId);
    }

    /**
     * 流程日志和会议的绑定
     * @param
     * @param lcrzId
     */
    public void bindMeetingWithLcrz(String MeetingId, String lcrzId){
        Meeting meeting = meetingRepository.findOne(MeetingId);
        Lcrzb lcrz = lcrzbRepository.findOne(lcrzId);
        meeting.getLcrzSet().add(lcrz);
        meetingRepository.save(meeting);
    }

    /**
     * @param userId
     * @param condition
     * @param lx
     * @return
     */
    @Cacheable(cacheName = "MEETING_CACHE")
    public List<MeetingVo> getMeetingByUserIdAndCondition(String userId, String condition, String lx) {
        List<Meeting> meetingList = null;

        if(StringUtil.isBlank(condition)) return new ArrayList<>();
        if(StringUtil.equals(lx, "wtj")){//未提交
            meetingList = meetingRepository.findByNhyr_idAndHyshzt(userId, condition);
        }
        if(StringUtil.equals(lx, "dsp") ){//待审批
            meetingList = meetingRepository.findByNhyr_idAndHyshzt(userId,condition);
        }

        if(StringUtil.equals(lx, "ych"))//已撤回（在未审批之前撤回会议申请）
            meetingList = meetingRepository.findByNhyr_idAndHyshzt(userId, REVOCATION_CONDITION);
        if(StringUtil.equals(lx, "ysp")){ //已审批（已审批就是根据个人取出的所以不需要再进行过滤）

        }

        List<MeetingVo> meetingVos = new ArrayList<MeetingVo>();
        if(meetingList == null) return meetingVos;
        for(Meeting meeting : meetingList){
            MeetingVo meetingVo = new MeetingVo(meeting);
            meetingVos.add(meetingVo);
        }
        return meetingVos;
    }

    /**
     * 根据会议审批人筛选出不同的会议分类hyfl(1:集团会议；2：东山公交会议)
     * @param userVo
     * @param condition
     * @return
     */
    @Cacheable(cacheName = "MEETING_CACHE")
    public List<MeetingVo> getHyspByUserAndCondition(UserVo userVo, String condition){
        List<Meeting> meetingList = null;
        if(StringUtil.isBlank(condition)) return new ArrayList<>();
        if(userVo.getRoleName().indexOf("会议室管理员") != -1){
            if(userVo.getDepartmentName().indexOf("集团") != -1){
                meetingList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc(condition,"1");
            }else if(userVo.getDepartmentName().indexOf("东山公交") != -1){
                meetingList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc(condition,"2");
            }
        }

        List<MeetingVo> meetingVos = new ArrayList<MeetingVo>();
        if(meetingList == null) return meetingVos;
        for(Meeting meeting : meetingList){
            MeetingVo meetingVo = new MeetingVo(meeting);
            meetingVos.add(meetingVo);
        }
        return meetingVos;
    }

    @Transactional
    public void updateMeeting(MeetingVo meetingVo, String[] attachmentId){
        if(meetingVo == null || StringUtil.isBlank(meetingVo.getId()))
            throw new BaseAppException("要更新的申请会议ID为空，无法更新");
        Meeting meeting = meetingRepository.findOne(meetingVo.getId());
        if(meeting == null) throw new BaseAppException("要更新的会议不存在！");
        //更新会议的附件列表
        if(attachmentId != null && attachmentId.length > 0){
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if(attachmentList != null && attachmentList.size() > 0)
                meeting.setAttachments(new HashSet<>(attachmentList));
        }
        BeanUtils.copyProperties(meetingVo,meeting);
        meetingRepository.save(meeting);
    }


    /**
     * 1.根据id找到相关会议，并将数据写入到对应的vo中
     * 2.获取相关的流程日志
     * 3.设置个流程日志相关联的会议状态的名称
     * @param meetingId
     * @return
     */
    public MeetingVo getMeetingById(String meetingId) {
        Meeting meeting = meetingRepository.findOne(meetingId);
        if(meeting == null) throw new BaseAppException("id为 "+meetingId+"的会议没有找到");
        MeetingVo meetingVo = new MeetingVo(meeting);//会议的基本信息
        List<LcrzbVo> lcrzbVoList = LcrzbService.convert(meeting.getLcrzSet());
        Map<String, String> lcjdbMap = getAllLcjdMapping();
        for (LcrzbVo lcrzbVo : lcrzbVoList) {
            if(!StringUtil.isBlank(lcrzbVo.getLcjd())){
                String lcjdName = lcjdbMap.get(lcrzbVo.getLcjd());
//                lcrzbVo.setLcjd(lcjdName);
                lcrzbVo.setMessage(lcjdName);
            }
        }
        meetingVo.setLcrzList(lcrzbVoList);
        //如果会议的状态不是已撤销就显示会议的附件信息，否则就不显示附件信息
//        if(!StringUtil.equals(meeting.getHyshzt(), REVOCATION_CONDITION)){
            List<AttachmentVo> attachmentVoList = getMeetingAttachment(meeting);
            meetingVo.setAttachmentList(attachmentVoList);
//        }
        return meetingVo;
    }

    /**
     * 获取一个会议相关的附件信息
     * @param meeting
     * @return
     */
    private List<AttachmentVo> getMeetingAttachment(Meeting meeting){
        Set<Attachment> attachmentList = meeting.getAttachments();
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
     * 对数据进行过滤，并设置会议的状态
     * 1.过滤掉已经被标记为删除的会议
     * 2.如果是部门内部审批时过滤掉其他部门的会议
     * 3.如果是公司内部审批时过滤掉其他公司的会议
     * @param userId
     * @param condition
     * @param meetingList
     * @return
     */
    private List<MeetingVo> filter(String userId, String condition, List<Meeting> meetingList){
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
        if(meetingList == null) return meetingVoList;
        Map<String, String> lcjdbMap = getAllLcjdMapping();
        Lcjdb lcjd = lcjdbRepository.findOne(condition);
        User user = userRepository.findOne(userId);
        for(Meeting meeting : meetingList){
            //      if(StringUtil.equals(meeting.getSfsc(),"Y")) continue; //过滤掉已删除的会议
            MeetingVo meetingVo = new MeetingVo(meeting);

            convertHyshzt(lcjdbMap, meetingVo);
            meetingVoList.add(meetingVo);
        }
        return meetingVoList;
    }

    /**
     * 判断一个会议是不是属于一个部门
     * @return true(是同一个部门)
     * false （不是同一个部门）
     */
    private boolean sfsybm(Department meetingDepartment, Set<Department> userDepartments){
        if(meetingDepartment == null || userDepartments == null) return false;
        for(Department department : userDepartments){
            if(StringUtil.equals(meetingDepartment.getId(), department.getId())) return true;
        }
        return false;
    }


    /**
     *  会议审批已审批列表
     * @param userVo
     * @return
     */
    public List<MeetingVo> getHyspYspList(UserVo userVo){
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
        List<Meeting> wtgList = null;
        List<Meeting> ytgList = null;
        List<Meeting> ytzList = null;
        String userId = userVo.getId();
        if(userVo.getRoleName().indexOf("会议室管理员") != -1){
            if(userVo.getDepartmentName().indexOf("集团") != -1){
                wtgList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("N", "1");
                ytgList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("3", "1");
                ytzList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("T", "1");
            }else if(userVo.getDepartmentName().indexOf("东山公交") != -1){
                wtgList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("N", "2");
                ytgList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("3", "2");
                ytzList = meetingRepository.findByHyshztAndHyflOrderBySqsjDesc("T", "2");
            }
        }


        if(wtgList != null && wtgList.size() > 0) {
            for (Meeting meeting : wtgList) {
                Set<Lcrzb> lcrzbList = meeting.getLcrzSet();
                MeetingVo meetingVo = new MeetingVo(meeting);
//                setSpjg(userId, lcrzbList, meetingVo);
                meetingVo.setHyshzt("终止");
                meetingVoList.add(meetingVo);
            }
        }
        if(ytgList != null && ytgList.size() > 0){
            for(Meeting meeting : ytgList){
                //            Set<Lcrzb> lcrzbList = meeting.getLcrzSet();
                MeetingVo meetingVo = new MeetingVo(meeting);
//                setSpjg(userId, lcrzbList, meetingVo);
                meetingVo.setHyshzt("未通知");
                meetingVoList.add(meetingVo);
            }
        }
        if(ytzList != null && ytzList.size() > 0){
            for(Meeting meeting : ytzList){
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("已通知");
                meetingVoList.add(meetingVo);
            }
        }
        return meetingVoList;
    }

    /**
     * 会议审核状态由代码转换为对应汉字
     * @param lcjdbMap
     * @param meetingVo
     */
    private void convertHyshzt(Map<String, String> lcjdbMap, MeetingVo meetingVo) {
        if(!StringUtil.isBlank(meetingVo.getHyshzt())) {
            if (lcjdbMap.get(meetingVo.getHyshzt()).equals("审核完成")) {
                String id = meetingVo.getId();
                LcrzbVo lcrzbVo= lcrzbService.getLastLcrzByClnrid(id);
                if(lcrzbVo !=null){
                    if(lcrzbVo.getCljg().equals("通过")){
                        meetingVo.setHyshzt("通过");
                    }
                    else if(lcrzbVo.getCljg().equals("不通过")){
                        meetingVo.setHyshzt("终止");
                    }
                }
            }
            else {
                meetingVo.setHyshzt(lcjdbMap.get(meetingVo.getHyshzt()));
            }
        }
        else meetingVo.setHyshzt("");
    }

    /**
     * 申请记录界面已审批会议
     * @param userId
     * @return
     */
    @Cacheable(cacheName = "MEETING_CACHE")
    public List<MeetingVo> getSqjlYspList(String userId){
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
        List<Meeting> wtgList = meetingRepository.findByNhyr_idAndHyshzt(userId, "N");
        List<Meeting> ytgList = meetingRepository.findByNhyr_idAndHyshzt(userId, "3");
        List<Meeting> ytzList = meetingRepository.findByNhyr_idAndHyshzt(userId, "T");
        if(wtgList != null && wtgList.size() > 0) {
            for (Meeting meeting : wtgList) {
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("终止");
                meetingVoList.add(meetingVo);
            }
        }
        if (ytgList != null && ytgList.size() > 0){
            for (Meeting meeting : ytgList) {
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("通过");
                meetingVoList.add(meetingVo);
            }
        }
        if (ytzList != null && ytzList.size() > 0){
            for (Meeting meeting : ytzList) {
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("通过");
                meetingVoList.add(meetingVo);
            }
        }
        return meetingVoList;
    }

    /**
     * 申请记录界面未审批会议
     * 1.没有完成 状态不是Y
     * 2.没有回填 状态不是T
     * 3.没有撤销 状态不是revocation
     * @param userId
     * @return
     */
    @Cacheable(cacheName = "MEETING_CACHE")
    public List<MeetingVo> getSqjlWspList(String userId, String condition){
        List<MeetingVo> meetingVoList = new ArrayList<>();
        List<Meeting> wspList = meetingRepository.findByNhyr_idAndHyshzt(userId, condition);
        if(wspList != null && wspList.size() > 0)
            for(Meeting meeting : wspList){
//                if(StringUtil.equals(REVOCATION_CONDITION, meeting.getHyshzt())) continue;//过滤掉已撤销的会议
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("待审批");
                meetingVoList.add(meetingVo);
            }
        return meetingVoList;
    }


//    /**
//     * 获取未提交的会议列表
//     * @param userId
//     * @param condition
//     * @return
//     */
//    private List<Meeting> getWtjMeetings(String userId, String condition){
//
//        List<Meeting> meetingList = meetingRepository.findByNhyr_idAndHyshzt(userId, condition);
//        List<Meeting> result = new ArrayList<>();
//        if(ycxMeetingList == null || ycxMeetingList.size() == 0) return meetingList;
//        Map<String, Meeting> ycxMeetingMap = new HashMap<>();
//        for (Meeting meeting2 : ycxMeetingList) {
//            ycxMeetingMap.put(meeting2.getId(),meeting2);
//        }
//        for (Meeting meeting : meetingList){
//            if (ycxMeetingMap.get(meeting.getId()) == null)
//                result.add(meeting);
//        }
//        return result;
//    }

//    @Transactional
//    public void ApproveMeetingSave(MeetingVo meetingVo, String[] attachmentId){
//        String hyfl = getHyfl(meetingVo);
//        meetingVo.setHyfl(hyfl);
//        String condition = "3";
//        meetingVo.setHyshzt(condition); //会议审核状态
//        updateMeeting(meetingVo,attachmentId);
//    }

    /**
     * 会议审批，保存审批时对会议的修改，并更新流程日志
     * @param meetingVo 审批会议单时修改生成的表单内容
     * @param cljg  操作结果(通过，终止)
     */
    @Transactional
    @TriggersRemove(cacheName = {"MEETING_CACHE"}, removeAll = true)
    public void saveMeetingLcrz(MeetingVo meetingVo, String cljg, String[] attachmentId){
        String hyfl = getHyfl(meetingVo);
        meetingVo.setHyfl(hyfl);

        String condition = "2"; //1:保存 2：提交
        meetingVo.setHyshzt(condition); //会议审核状态

        if(StringUtil.equals(cljg, "tg"))
            condition = "3";
        else
            condition = "N";
        meetingVo.setHyshzt(condition); //会议状态
        //设置会议申请的用户
        User user = userRepository.findOne(meetingVo.getHyrid());
        Meeting meeting = new Meeting(meetingVo);
        meeting.setNhyr(user);
        //会议的附件信息
        if (attachmentId != null && attachmentId.length > 0){
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if (!attachmentList.isEmpty()) meeting.setAttachments(new HashSet<>(attachmentList));
        }
        meeting = meetingRepository.save(meeting);
        String clnrid = meeting.getId();


        String lcrzId = lcrzbService.save(new LcrzbVo(cljg),clnrid, "hy",condition);
        bindMeetingWithLcrz(clnrid, lcrzId);

    }

    /**
     * 会议审批流程日志
     * @param cljg
     * @param clnrid

     */
    @Transactional
    @TriggersRemove(cacheName = { "MEETING_CACHE" }, removeAll = true)
    public void saveHyspLcrz(String cljg, String clnrid) {
        MeetingVo meetingVo = getMeetingById(clnrid);
        String curCondition = meetingVo.getHyshzt();
        String nextCondition = curCondition;
        if (StringUtil.equals(cljg,"tg")){
            nextCondition="3";
        }else if(StringUtil.equals(cljg,"zz")){
            nextCondition="N";
        }else if(StringUtil.equals(cljg,"tz")){
            nextCondition="T";
        }
        meetingVo.setHyshzt(nextCondition);
        Meeting meeting = meetingRepository.findOne(meetingVo.getId());
        BeanUtils.copyProperties(meetingVo, meeting);
        meetingRepository.save(meeting);

        String lcrzId = lcrzbService.save(new LcrzbVo(cljg), clnrid, "hy", nextCondition);
        bindMeetingWithLcrz(clnrid, lcrzId);
    }


//    /**
//     * 删除合同（软删除）
//     * @param id
//     */
//    @Transactional
//    public void deleteMeeting(String id){
//        Meeting meeting = meetingRepository.findById(id);
//        if(meeting == null) throw new BaseAppException("未找到要删除的会议 会议id = " + id);
//        meeting.setSfsc("Y");
//        meetingRepository.save(meeting);
//    }

    /**
     * 从数据库中删除会议，用于未提交以保存的会议草稿
     * @param id
     */
    @Transactional
    public void delete(String id){
        Meeting meeting = meetingRepository.findById(id);
        if(meeting == null) return;
        meetingRepository.delete(meeting);
    }

    /**
     * 获取所有的会议的节点id和名字的map
     * @return
     */
    public Map<String, String> getAllLcjdMapping(){

        Map<String, String> resultMap = new HashMap<String, String>();

        resultMap.put("2", "提交会议申请");
        resultMap.put("3", "通过会议审批");
        resultMap.put("T", "发布会议通知");
        resultMap.put("N", "终止会议审批");
        resultMap.put(REVOCATION_CONDITION, "撤销会议申请");
        return resultMap;
    }
//
//    /**
//     * @param hyzt 会议主题
//     * @param hybh 会议编号
//     * @param startTime 开始时间（申请时间）
//     * @param endTime 会议时间
//     * @param hyspzt 会议审批状态（0:全部， 1：待审批 2：已审批 3：已回填）
//     * @param sqr 会议申请人姓名
//     * @param djrId 会议对接人的ID（用于查询涉及对接人的会议）
//     * @return
//     */
//    @Cacheable(cacheName = "MEETING_CACHE")
//    public List<MeetingVo> searchMeeting(final String hyzt, final String hybh, final Date startTime,
//           final Date endTime, final String hyspzt, final String sqr, final String djrId){
//        List<MeetingVo> meetingVoList = new ArrayList<>();
//        List<Meeting> meetingList = meetingRepository.findAll(new Specification<Meeting>() {
//            @Override
//            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> list = new ArrayList<Predicate>();
//                //会议主题
//                if(!StringUtil.isBlank(hyzt)) list.add(cb.like(root.get("hyzt").as(String.class),"%" + hyzt + "%"));
//                //会议编号
//                if(!StringUtil.isBlank(hybh)) list.add(cb.like(root.get("hybh").as(String.class),"%" + hybh + "%"));
//
//                //会议状态
//                if(StringUtil.equals(hyspzt, "1")){
//                    list.add(cb.notEqual(root.get("hyshzt").as(String.class), "Y"));
//                    list.add(cb.notEqual(root.get("hyshzt").as(String.class), "T"));
//                }
//                if(StringUtil.equals(hyspzt, "2"))
//                    list.add(cb.equal(root.get("hyshzt").as(String.class), "Y"));
//                if(StringUtil.equals(hyspzt, "3"))
//                    list.add(cb.equal(root.get("hyshzt").as(String.class), "T"));
//
//                //申请时间
//                if(startTime != null && endTime != null) list.add(cb.between(root.get("sqsj").as(Date.class),startTime,endTime));
//
//                //会议申请人
//                if(!StringUtil.isBlank(sqr)){
//                    Join<Meeting, User> meetingUserLeftJoin = root.join(root.getModel().getSingularAttribute("nhyr",User.class),JoinType.LEFT);
//                    list.add(cb.equal(meetingUserLeftJoin.get("fullName").as(String.class),sqr)); //申请人
//                }
//
//                //会议对接人的ID（用于查询对接人相关的会议）
//                if(!StringUtil.isBlank(djrId)){
//                    Join<Meeting, Lcrzb> meetingLcrzbLeftJoin = root.joinSet("lcrzSet", JoinType.LEFT);
//                    list.add(cb.equal(meetingLcrzbLeftJoin.get("user").get("id").as(String.class),djrId));
//                }
//
//                Predicate[] predicate = new Predicate[list.size()];
//                Predicate psssredicate = cb.and(list.toArray(predicate));
//                return psssredicate;
//            }
//        });
//        if(meetingList == null) return meetingVoList;
//        for(Meeting meeting : meetingList){
//            MeetingVo meetingVo = new MeetingVo(meeting);
//            if(StringUtil.equals(meeting.getHyshzt(),"Y")) meetingVo.setHyshzt("已审批");
//            else if(StringUtil.equals(meeting.getHyshzt(),"T")) meetingVo.setHyshzt("已回填");
//            else meetingVo.setHyshzt("待审批");
//            meetingVoList.add(meetingVo);
//        }
//        return meetingVoList;
//    }


    /**
     * 撤销会议
     * 会议申请人有权利撤销会议 撤销后的会议只能查看基本属性，不能查看和下载已上传的附件
     * 1.在数据库中查找相应id的会议（未找到抛出异常，指示会议没有找到）
     * 2.在流程日志表中插入一条会议撤销的记录
     * 3.修改会议的状态为已撤销，并保存
     * @param meetingId 要撤销的会议的id
     * @param reason (1.提交人发现有误 2.其他（理由自填）)
     */
    @Transactional
    @TriggersRemove(cacheName = {"MEETING_CACHE"}, removeAll = true)
    public void revocationMeeting(String meetingId,String reason){
        if(StringUtil.isBlank(meetingId)) throw new BaseAppException("要撤销的会议id为空");
        Meeting meeting = meetingRepository.getOne(meetingId);
        if(meeting == null) throw new BaseAppException("没有找到要撤销的会议！");

        //插入撤销会议的记录
        String lcrzId = lcrzbService.save(new LcrzbVo("撤销",reason), meetingId, "hy", REVOCATION_CONDITION);
        bindMeetingWithLcrz(meetingId,lcrzId);
        //记录与会议绑定
        meeting.setHyshzt(REVOCATION_CONDITION);
        //保存会议
        meetingRepository.save(meeting);
    }

    public List<String[]> meetingRoomSyqk(String hydd, String hys, String hyrq){
        List<Object> syqk = meetingRepository.findHysSyqk(hydd, hys, hyrq);
        List<String[]> syqk2 = convertString(syqk);
        return syqk2;
    }

    public List<String[]> meetingRoomSyqk2(String hydd, String hys, String hyrq, String meetingId){
        List<Object> syqk = meetingRepository.findHysSyqk2(hydd, hys, hyrq, meetingId);
        List<String[]> syqk2 = convertString(syqk);
        return syqk2;
    }

    /**
     * 将jpa查询的结果集(object类型)循环遍历进行强制类型转换成String类型，并进行封装
     * @param syqk
     * @return
     */
    public List<String[]> convertString(List<Object> syqk){
        List<String[]> syqk2 = new ArrayList<>();
        for (int i = 0; i < syqk.size(); i++){
            Object[] obj = (Object[])syqk.get(i);
            String kssj = (String)obj[0];
            String jssj = (String)obj[1];
            String[] str = {kssj,jssj};
            syqk2.add(str);
        }
        return syqk2;
    }

    /**
     * 检测所选的时间段是否与当天的会议室使用情况冲突
     * @param hydd
     * @param hys
     * @param hyrq
     * @param kssj
     * @param jssj
     * @param meetingId
     * @return
     */

    public boolean checkIsRightTime(String hydd, String hys, String hyrq, String kssj, String jssj, String meetingId){
        int count = 0;
        List<String[]> syqk = new ArrayList<>();
        String[] ksStr = kssj.split(":");
        int ksHour = Integer.parseInt(ksStr[0]);
        int ksMin = Integer.parseInt(ksStr[1]);
        String[] jsStr = jssj.split(":");
        int jsHour = Integer.parseInt(jsStr[0]);
        int jsMin = Integer.parseInt(jsStr[1]);
        if (meetingId != null){
            syqk = meetingRoomSyqk2(hydd, hys, hyrq, meetingId);
        }else{
            syqk = meetingRoomSyqk(hydd, hys, hyrq);
        }
        if (syqk != null){
            for(int i = 0; i < syqk.size(); i++){
                String[] str = syqk.get(i);
                String kssj1 = str[0];
                String[] ksStr2 = kssj1.split(":");
                int ksHour2 = Integer.parseInt(ksStr2[0]);
                int ksMin2 = Integer.parseInt(ksStr2[1]);
                String jssj2 = str[1];
                String[] jsStr2 = jssj2.split(":");
                int jsHour2 = Integer.parseInt(jsStr2[0]);
                int jsMin2 = Integer.parseInt(jsStr2[1]);
                if(jsHour < ksHour2 || ksHour > jsHour2){
                    count++;
                }else if(jsHour == ksHour2){
                    if(jsMin < ksMin2)
                        count++;
                }else if(ksHour == jsHour2){
                    if(ksMin > jsMin2)
                        count++;
                }
            }
        }
        if (count == syqk.size()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 对于已读的会议事件记录到备忘录中
     * @param meetingVo
     */
    public void addMemoItem(MeetingVo meetingVo){
        UserVo userVo = userService.getLoginUser();
        String userId = userVo.getId();
        String hyrq = meetingVo.getHyrq();
        String[] str = hyrq.split("/");
        String year = str[0];
        String month = str[1].replaceFirst("^0*", "");
        String day = str[2];
        String time = hyrq + " " + meetingVo.getKssj();
        String descript = meetingVo.getHyzt();
        memoService.save(userId, year, month, day, time, descript);
    }

}
