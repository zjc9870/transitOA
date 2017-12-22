package com.expect.admin.service;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.HytzRepository;
import com.expect.admin.data.dao.MeetingRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Hytz;
import com.expect.admin.data.dataobject.Meeting;
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


@Service
public class HytzService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HytzRepository hytzRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private MeetingService meetingService;

    /**
     * 保存会议通知
     * @param hytzVo 所有通知对象
     * @param meetingId 此次通知的会议ID
     * @return
     */
    @Transactional
    public String saveHytz(HytzVo hytzVo, String meetingId, String[] attachmentId){
        String isExistNotifyObject = "";
        if(hytzVo == null) return isExistNotifyObject;
        if (hytzVo.getJtgg() != null){
            String[] jtgg = hytzVo.getJtgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(jtgg, meetingId, "集团高管", attachmentId);
        }
        if (hytzVo.getJtbm() != null){
            String[] jtbm = hytzVo.getJtbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(jtbm, meetingId, "集团部门", attachmentId);
        }
        if (hytzVo.getDsgjgg() != null) {
            String[] dsgjgg = hytzVo.getDsgjgg().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(dsgjgg, meetingId, "东山公交高管", attachmentId);
        }
        if (hytzVo.getDsgjbm() != null){
            String[] dsgjbm = hytzVo.getDsgjbm().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(dsgjbm, meetingId, "东山公交部门", attachmentId);
        }
        if (hytzVo.getGsbgs() != null){
            String[] gsbgs = hytzVo.getGsbgs().split(",");
            isExistNotifyObject = isExistNotifyObject + " " + save(gsbgs, meetingId, "公司办公室", attachmentId);
        }
        if (hytzVo.getWbdw() != ""){
            String[] wbdw = new String[1];
            wbdw[0] = hytzVo.getWbdw();
            isExistNotifyObject = isExistNotifyObject + " " + save(wbdw, meetingId, "外部单位", attachmentId);
        }
        return isExistNotifyObject;
    }

    /**
     * 保存新的会议通知对象，筛选出通知过的对象保存到isExistObject中，保存会议的状态为已通知
     * @param tzdx
     * @param meetingId
     * @param tzdxfl
     * @return
     */
    private String save(String[] tzdx, String meetingId, String tzdxfl, String[] attachmentId){
        UserVo userVo = userService.getLoginUser();
        User user = userRepository.findOne(userVo.getId());

        String isExistObject = "";
        for (String dx : tzdx){
            Boolean isExist = isNotifyObjectExist(dx, meetingId, tzdxfl);
            if(isExist.equals(false)){
                isExistObject = isExistObject + " " + dx;
            }
            else{
                HytzVo hytzVo = new HytzVo();
                hytzVo.setHyid(meetingId);
                hytzVo.setTzdx(dx);
                hytzVo.setTzdxfl(tzdxfl);
                hytzVo.setIsread("0");
                Hytz hytz = new Hytz(hytzVo);
                hytz.setHytzr(user);
                hytz.setTzid(randomString());

                Date tzsj = new Date();
                hytz.setTzsj(tzsj);

                if (attachmentId != null && attachmentId.length > 0){
                    List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
                    if (!attachmentList.isEmpty()) hytz.setAttachments(new HashSet<>(attachmentList));
                }

                hytzRepository.save(hytz);
                String id = hytz.getId();
            }
        }
        return isExistObject;
    }

    /**
     * 根据通知对象，会议ID以及通知对象分类唯一的确定一条会议通知，判断这条通知之前是否存在 true(表示通知记录) false(不存在这条通知记录)
     * @param notifyObject 每一个通知对象
     * @param meetingId
     * @param tzdxfl
     * @return
     */
    public boolean isNotifyObjectExist(String notifyObject, String meetingId, String tzdxfl){
        List<Hytz> hytzList = hytzRepository.findByHyidAndTzdxAndTzdxfl(meetingId, notifyObject, tzdxfl);
        if (hytzList.size() == 0){
            return true;
        }
        return false;
    }

    public String randomString(){
        Date date = new Date();
        String random = DateUtil.format(date, DateUtil.longFormat).substring(0, 14);
        return  random;
    }


    public HytzVo getHytzVoByMeetingId(String meetingId){
        List<Hytz> hytzList = hytzRepository.findByHyid(meetingId);
        Hytz hytz1;
        HytzVo hytzVo = new HytzVo();
        getNewHytzVo(hytzVo, hytzList);
        if(hytzList.size() != 0){
            hytz1 = hytzList.get(0);
            List<AttachmentVo> attachmentVoList = getHytzAttachment(hytz1);
            hytzVo.setAttachmentVoList(attachmentVoList);
        }
        return  hytzVo;
    }

    private List<AttachmentVo> getHytzAttachment(Hytz hytz) {
        Set<Attachment> attachmentList = hytz.getAttachments();
        List<AttachmentVo> attachmentVoList = new ArrayList<>();
        if(attachmentList != null && !attachmentList.isEmpty()) {
            for (Attachment attachment : attachmentList) {
                AttachmentVo attachementVo = new AttachmentVo();
                BeanUtils.copyProperties(attachment, attachementVo);
                attachmentVoList.add(attachementVo);
            }
        }
        return attachmentVoList;
    }

    public void getNewHytzVo(HytzVo hytzVo, List<Hytz> hytzList){
        String jtgg="";
        String jtbm="";
        String dsgjgg="";
        String dsgjbm="";
        String gsbgs="";
        String wbdw="";

        if (hytzList != null){
            for (Hytz hytz : hytzList){
                if (hytz.getTzdxfl() != null){
                    if (hytz.getTzdxfl().equals("集团高管")){
                        jtgg = mergeNotifyObject(hytz.getIsread(), jtgg, hytz.getTzdx());
                    }
                    if (hytz.getTzdxfl().equals("集团部门")){
                        jtbm = mergeNotifyObject(hytz.getIsread(), jtbm, hytz.getTzdx());
                    }
                    if (hytz.getTzdxfl().equals("东山公交高管")){
                        dsgjgg = mergeNotifyObject(hytz.getIsread(), dsgjgg, hytz.getTzdx());
                    }
                    if (hytz.getTzdxfl().equals("东山公交部门")){
                        dsgjbm = mergeNotifyObject(hytz.getIsread(), dsgjbm, hytz.getTzdx());
                    }
                    if (hytz.getTzdxfl().equals("公司办公室")){
                        gsbgs = mergeNotifyObject(hytz.getIsread(), gsbgs, hytz.getTzdx());
                    }
                    if (hytz.getTzdxfl().equals("外部单位")){
//                        wbdw = mergeNotifyObject(hytz.getIsread(), wbdw, hytz.getTzdx());
                        wbdw = hytz.getTzdx();
                    }
                }
            }
        }
        hytzVo.setJtgg(jtgg);
        hytzVo.setJtbm(jtbm);
        hytzVo.setDsgjgg(dsgjgg);
        hytzVo.setDsgjbm(dsgjbm);
        hytzVo.setGsbgs(gsbgs);
        hytzVo.setWbdw(wbdw);
        if (hytzList.size() != 0){
            hytzVo.setHytzrId(hytzList.get(0).getHytzr());
        }
    }

    public String mergeNotifyObject(String isRead, String dxjh, String notifyObject){
        if (isRead.equals("0")){
            dxjh = dxjh + notifyObject + "(未读) ";
        }
        else if (isRead.equals("1")){
            dxjh = dxjh + notifyObject + "(已读) ";
        }
        return dxjh;
    }


    public List<HytzVo> getHytzByUserName(String username){
        List<Hytz> hytzList = hytzRepository.findByTzdx(username);
        List<HytzVo> hytzVos = new ArrayList<HytzVo>();
        if (hytzList != null){
            for (Hytz hytz : hytzList){
                HytzVo hytzVo = new HytzVo(hytz);
                hytzVos.add(hytzVo);
            }
        }
        return  hytzVos;
    }

    /**
     * 未读会议通知列表
     * @param hytzVos
     * @return
     */
    @Cacheable(cacheName = "HYTZ_CACHE")
    public List<HytzVo> getWdHytzList(List<HytzVo> hytzVos){
        List<HytzVo> wdList = new ArrayList<>();
        if (hytzVos != null){
            for (HytzVo hytzVo : hytzVos){
                if (StringUtil.equals(hytzVo.getIsread(),"0")){
                    wdList.add(hytzVo);
                }
            }
        }
        return wdList;
    }

    @Cacheable(cacheName = "HYTZ_CACHE")
    public List<HytzVo> getYdHytzList(List<HytzVo> hytzVos){
        List<HytzVo> ydList = new ArrayList<>();
        if (hytzVos != null){
            for (HytzVo hytzVo : hytzVos){
                if (StringUtil.equals(hytzVo.getIsread(),"1")){
                    ydList.add(hytzVo);
                }
            }
        }
        return ydList;
    }

    public List<MeetingVo> getHyMeetingVo(List<HytzVo> hytzVos, String loginUserRole){
        List<MeetingVo> meetingVos = new ArrayList<>();
        if (hytzVos != null){
            for (HytzVo hytzVo : hytzVos){
                String hyid = hytzVo.getHyid();
                String id = hytzVo.getId();
                String tzsj = hytzVo.getTzsj();
                String ydsj = hytzVo.getYdsj();
                String tzdx = hytzVo.getTzdx();
                MeetingVo meetingVo = getMeetingVoById(hyid);
                meetingVo.setHytzid(id);
                meetingVo.setLoginUserRole(loginUserRole);
                meetingVo.setTzsj(tzsj);
                meetingVo.setYdsj(ydsj);
                meetingVo.setTzdx(tzdx);
                meetingVos.add(meetingVo);
            }
        }
        return meetingVos;
    }

    public MeetingVo getMeetingVoById(String id){
        Meeting meeting = meetingRepository.findOne(id);
        if (meeting == null) throw new BaseAppException("id为"+id+"的会议没有找到");
        MeetingVo meetingVo = new MeetingVo(meeting); //会议的基本信息
        List<AttachmentVo> attachmentVoList = getDocumentAttachment(meeting);
        meetingVo.setAttachmentList(attachmentVoList);
        return meetingVo;
    }

    /**
     * 获取一个公文相关的附件信息
     * @param meeting
     * @return
     */
    private List<AttachmentVo> getDocumentAttachment(Meeting meeting) {
        Set<Attachment> attachmentList = meeting.getAttachments();
        List<AttachmentVo> attachmentVoList = new ArrayList<>();
        if(attachmentList != null && !attachmentList.isEmpty()) {
            for (Attachment attachment : attachmentList) {
                AttachmentVo attachementVo = new AttachmentVo();
                BeanUtils.copyProperties(attachment, attachementVo);
                attachmentVoList.add(attachementVo);
            }
        }
        return attachmentVoList;
    }

    public List<MeetingVo> sortMeetingListByTzsj(List<MeetingVo> meetingVoList){
        if (meetingVoList.size() <= 1){
            return meetingVoList;
        }
        TzsjCompare tzsjCompare = new TzsjCompare<MeetingVo>();
        Collections.sort(meetingVoList, tzsjCompare);
        return meetingVoList;
    }

    public List<MeetingVo> sortMeetingListByYdsj(List<MeetingVo> meetingVoList){
        if (meetingVoList.size() <= 1){
            return meetingVoList;
        }
        YdsjCompare ydsjCompare = new YdsjCompare<MeetingVo>();
        Collections.sort(meetingVoList, ydsjCompare);
        return meetingVoList;
    }

    public HytzVo getHytzVo(String id){
        Hytz hytz = hytzRepository.findOne(id);
        List<Hytz> hytzList = hytzRepository.findByTzid(hytz.getTzid());
        HytzVo hytzVo = new HytzVo();
        getNewHytzVo(hytzVo, hytzList);
        return hytzVo;
    }

    public HytzVo getHytzVoById(String id){
        Hytz hytz = hytzRepository.findOne(id);
        if (hytz == null) throw new BaseAppException("id为" +id+"的会议通知没有找到");
        HytzVo hytzVo = new HytzVo(hytz);
        return  hytzVo;
    }

//    public void updateHytz(HytzVo hytzVo){
//        Hytz hytz = hytzRepository.findOne(hytzVo.getId());
//        if (hytz == null) throw new BaseAppException("id为" +hytzVo.getId()+"的会议通知没有找到");
//        hytz.setIsread("1");
//        Date date = new Date();
//        hytz.setYdsj(date);
//        hytzRepository.save(hytz);
//    }

    public void updateHytz(HytzVo hytzVo){
        Hytz hytz = hytzRepository.findOne(hytzVo.getId());
        if (hytz == null) throw new BaseAppException("id为" +hytzVo.getId()+"的会议通知没有找到");
        hytz.setIsread("1");
        Date date = new Date();
        hytz.setYdsj(date);
        hytzRepository.save(hytz);
    }
}
