package com.expect.admin.service;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.LcrzbRepository;
import com.expect.admin.data.dao.MeetingRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.*;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.MeetingVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by qifeng on 17/5/17.
 */
@Service
public class MeetingService {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    LcrzbService lcrzbService;
    @Autowired
    LcrzbRepository lcrzbRepository;

    public void newMeetingSave(MeetingVo meetingVo,String bczl,String[] attachmentId){
        Meeting meeting = new Meeting(meetingVo);
        String condition="0";
        if (bczl.equals("bc")){
            condition = "1";
        }
        else if (bczl.equals("tj")){
            condition = "2";
        }
        meeting.setHyshzt(condition);

        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());
        meeting.setNhyr(user);

        Date hysj =  DateUtil.parse(meetingVo.getSj(), DateUtil.noSecondFormat);
        meeting.setHysj(hysj);

        if (attachmentId != null && attachmentId.length > 0) {
            List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
            if (!attachmentList.isEmpty()) meeting.setAttachments(new HashSet<>(attachmentList));
        }

        Meeting meeting1 = meetingRepository.save(meeting);


        if (StringUtil.equals(bczl, "tj")) {
            addXzLcrz(meeting1.getId(), condition);//如果是新增就增加一条日志记录
        }

    }

    /**
     * 新增时增加流程日志
     * @param
     */
    @Transactional
    public void addXzLcrz(String id, String hyshzt) {
        LcrzbVo lcrzbVo = new LcrzbVo("xz", "");
        String lcrzId = lcrzbService.save(lcrzbVo, id, "hy", hyshzt);
        bindDocumentWithLcrz(id, lcrzId);
    }


    /**
     * 流程日志和会议的绑定
     * @param
     * @param lcrzId
     */
    public void bindDocumentWithLcrz(String documentId, String lcrzId) {
        Meeting meeting = meetingRepository.findOne(documentId);
        Lcrzb lcrz = lcrzbRepository.findOne(lcrzId);
        meeting.getLcrzSet().add(lcrz);
        meetingRepository.save(meeting);
    }

    public List<MeetingVo> getMeetingByUserIdAndCondition(String userId, String condition, String lx){
        List<Meeting> meetingList = null;

        if(StringUtil.isBlank(condition)) return new ArrayList<>();
        if(StringUtil.equals(lx, "wtj")){//未提交
            meetingList = getWtjMeetings(userId, condition);
        }

        List<MeetingVo> meetingVoList=new ArrayList<MeetingVo>();

        if(meetingList ==null) return meetingVoList;
        for (Meeting meeting : meetingList) {
            MeetingVo meetingVo = new MeetingVo(meeting);
            meetingVoList.add(meetingVo);
        }
        return meetingVoList;
    }

    public List<Meeting> getWtjMeetings(String userId,String condition){
        List<Meeting> meetingList = meetingRepository.findByNhyr_idAndHyshztOrderByHysjDesc(userId,condition);
        return meetingList;

    }

    @Cacheable(cacheName = "MEETING_CACHE")
    public List<MeetingVo> getSqjldspList(String userId, String condition) {
        List<MeetingVo> meetingVoList = new ArrayList<>();
        List<Meeting> dspList = meetingRepository.findByNhyr_idAndHyshztOrderByHysjDesc(userId, condition);
        if(dspList != null && dspList.size() > 0)
            for (Meeting meeting : dspList) {
                MeetingVo meetingVo = new MeetingVo(meeting);
                meetingVo.setHyshzt("待审批");
                meetingVoList.add(meetingVo);
            }
        return meetingVoList;
    }
//    @Cacheable(cacheName = "MEETING_CACHE")
//    public List<DocumentVo> getSqjlYspList(String userId) {
//        List<DocumentVo> documentVoVoList = new ArrayList<>();
//        List<Document> wtgList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, "3");
//        List<Document> ytgList = documentRepository.findByNgwr_idAndGwshztOrderBySqsjDesc(userId, "4");
//        if(wtgList != null && wtgList.size() > 0) {
//            for (Document document : wtgList) {
//                DocumentVo documentVo = new DocumentVo(document);
//                documentVo.setGwshzt("打回");
//                documentVoVoList.add(documentVo);
//            }
//        }
//        if(ytgList != null && ytgList.size() > 0){
//            for(Document document : ytgList){
//                DocumentVo documentVo = new DocumentVo(document);
//                documentVo.setGwshzt("通过");
//                documentVoVoList.add(documentVo);
//            }
//        }
//        return documentVoVoList;
//    }

}
