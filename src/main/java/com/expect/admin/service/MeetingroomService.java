package com.expect.admin.service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.MeetingRepository;
import com.expect.admin.data.dao.MeetingroomRepository;

import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.*;


import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.DepartmentConvertor;
import com.expect.admin.service.convertor.MeetingConvertor;
import com.expect.admin.service.convertor.MeetingroomConvertor;

import com.expect.admin.service.convertor.UserConvertor;

import com.expect.admin.service.vo.*;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.StringUtil;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

import org.apache.commons.lang3.StringUtils;


/**
 * Created by twinkleStar on 2017/10/18.
 */

@Service
public class MeetingroomService {

    @Autowired
    private MeetingroomRepository meetingroomRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MeetingService meetingService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;


    @Transactional
    @Cacheable(cacheName = "MEETINGROOM_CACHE")
    public List<String> searchHydd(){
        List<String> hydd = meetingroomRepository.findHydd();
        return hydd;
    }

    @Cacheable(cacheName = "MEETINGROOM_CACHE")
    public List<String> searchHys(String hydd){
        List<String> hysname = meetingroomRepository.findHysnameByHydd(hydd);
        return hysname;
    }

    @Cacheable(cacheName = "MEETINGROOM_CACHE")
    public String retHydd(String user_departmentName){
        String str1 = "东山公交";
        String str2 = "集团";
        String retStr = "";
        if (user_departmentName.contains(str1) && user_departmentName.contains(str2)){
            retStr = str1 + "/" + str2;
        } else  if (user_departmentName.contains(str2)){
            retStr = str2;
        }else if (user_departmentName.contains(str1)){
            retStr = str1;
        }
        return retStr;
    }

    /**
     * 获取所有的会议室信息
     * @return
     */
    @Cacheable(cacheName = "MEETINGROOM_CACHE")
    public List<MeetingroomVo> getMeetingrooms() {
        List<Meetingroom> meetingrooms = meetingroomRepository.findAll();
        List<MeetingroomVo> meetingroomVos = MeetingroomConvertor.convert(meetingrooms);
        return meetingroomVos;
    }

    /**
     * 根据ID获取会议室
     * @param id
     * @return
     */
    public MeetingroomVo getMeetingroomById(String id) {
//        UserVo userVo = userService.getLoginUser();
        Meetingroom meetingroom = meetingroomRepository.findOne(id);
        MeetingroomVo meetingroomVo = MeetingroomConvertor.convert3(meetingroom);
        return meetingroomVo;
    }



//    public List<MeetingVo> getMeetingInfo2(String mid,String mdate){
//        Meetingroom meetingroom = meetingroomRepository.findOne(mid);
//        Set<Meeting> meetingList = meetingroom.getMeetings();
//
//        List<MeetingVo> meetingVoList = new ArrayList<>();
//        if(meetingList != null && !meetingList.isEmpty())
//            for (Meeting meeting : meetingList) {
//                MeetingVo meetingVo = new MeetingVo();
//                String meeting_date = meeting.getHyrq();
//                if (meeting_date.equals(mdate)){
//                    BeanUtils.copyProperties(meeting, meetingVo);
//                    meetingVoList.add(meetingVo);
//                }
//
//            }
//        return meetingVoList;
//    }


    /**
     * 根据ID和日期获取具体会议室的使用情况
     * @param mid
     * @param mdate
     * @return
     */
    public ResultVo getMeetingInfo(String mid,String mdate){
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("该天没有使用记录");
        Meetingroom meetingroom = meetingroomRepository.findOne(mid);
        Set<Meeting> meetingList = meetingroom.getMeetings();

        List<MeetingVo> meetingVoList = new ArrayList<>();
        if(meetingList != null && !meetingList.isEmpty())
            for (Meeting meeting : meetingList) {
                MeetingVo meetingVo = new MeetingVo();
                String meeting_date = meeting.getHyrq();
                String meeting_shzt = meeting.getHyshzt();
                if (meeting_date.equals(mdate) && !meeting_shzt.equals("T")) {
                    BeanUtils.copyProperties(meeting, meetingVo);
                    meetingVoList.add(meetingVo);
                }
            }

        if(meetingVoList == null || meetingVoList.isEmpty()){
            return resultVo;
        }
        resultVo.setResult(true);
        resultVo.setMessage("查询成功");
        resultVo.setObj(meetingVoList);
        return resultVo;

    }

    /**
     * 保存会议室
     * @param meetingroomVo
     * @return
     */
    @Transactional
    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
    public DataTableRowVo save(MeetingroomVo meetingroomVo) {
        DataTableRowVo mrrv = new DataTableRowVo();
        mrrv.setMessage("添加失败");

        String hydd_str = meetingroomVo.getHydd();
        String location_str = meetingroomVo.getLocation();
        String hysname_str = meetingroomRepository.findHysnameByHyddAndLocation(hydd_str,location_str);
        String hysname_up = meetingroomVo.getHysname();
        if(hysname_str != null &&!hysname_str.equals("")&&hysname_str.equals(hysname_up)){
            mrrv.setMessage("该会议室已经存在");
            return mrrv;

        }


        if (StringUtils.isEmpty(meetingroomVo.getHysname())) {
            mrrv.setMessage("会议室名不能为空");
            return mrrv;
        }
        if (StringUtils.isEmpty(meetingroomVo.getLocation())) {
            mrrv.setMessage("会议室地点不能为空");
            return mrrv;
        }
//        UserVo manager = userService.getLoginUser();
//        String department_user = manager.getDepartmentName();
//        String departmentName="";
//
//        if(manager.getRoleName()=="超级管理员"){
//            departmentName = "东山公交/集团部门";
//        }else{
//            if (department_user.contains("东山")){
//                departmentName = "东山公交";
//            }else if (department_user.contains("集团部门")){
//                departmentName = "集团部门";
//            }
//        }
//
//        meetingroomVo.setHydd(departmentName);
        Meetingroom meetingroom = MeetingroomConvertor.convert(meetingroomVo);


        Meetingroom result = meetingroomRepository.save(meetingroom);
        if (result != null) {
            mrrv.setMessage("增加成功");
            mrrv.setResult(true);
            MeetingroomConvertor.convertDtrv(mrrv, result);
        }
        return mrrv;
    }


    /**
     * 更新会议室
     * @param meetingroomVo
     * @return
     */
    @Transactional
    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
    public DataTableRowVo update(MeetingroomVo meetingroomVo) {
        DataTableRowVo mrrv = new DataTableRowVo();
        mrrv.setMessage("修改失败");

        Meetingroom checkMeetingroom = meetingroomRepository.findOne(meetingroomVo.getId());
        if (checkMeetingroom == null) {
            mrrv.setMessage("该会议室不存在");
            return mrrv;
        }

        if (StringUtils.isEmpty(meetingroomVo.getHysname())) {
            mrrv.setMessage("会议室名称不能为空");
            return mrrv;
        }

        if (StringUtils.isEmpty(meetingroomVo.getLocation())) {
            mrrv.setMessage("会议室地点不能为空");
            return mrrv;
        }


        String hydd_up = meetingroomVo.getHydd();
        String location_up = meetingroomVo.getLocation();
        String hysname_up = meetingroomVo.getHysname();


        String hydd_ck = checkMeetingroom.getHydd();
        String location_ck = checkMeetingroom.getLocation();
        String hysname_ck = checkMeetingroom.getHysname();

        String hysname_str = meetingroomRepository.findHysnameByHyddAndLocation(hydd_up,location_up);

        if (hydd_ck.equals(hydd_up)&&location_ck.equals(location_up)&&(hysname_ck.equals(hysname_up))){
            MeetingroomConvertor.convert(meetingroomVo, checkMeetingroom);

            mrrv.setMessage("修改成功");
            mrrv.setResult(true);
            MeetingroomConvertor.convertDtrv(mrrv, checkMeetingroom);
            return mrrv;
        }else if(hysname_str != null &&!hysname_str.equals("")&&hysname_str.equals(hysname_up)){
            mrrv.setMessage("该会议室已经存在，请重新修改");
            return mrrv;
        }else{
            MeetingroomConvertor.convert(meetingroomVo, checkMeetingroom);

            mrrv.setMessage("修改成功");
            mrrv.setResult(true);
            MeetingroomConvertor.convertDtrv(mrrv, checkMeetingroom);
            return mrrv;
        }

//        MeetingroomConvertor.convert(meetingroomVo, checkMeetingroom);
//
//        mrrv.setMessage("修改成功");
//        mrrv.setResult(true);
//        MeetingroomConvertor.convertDtrv(mrrv, checkMeetingroom);
//        return mrrv;
    }

    /**
     * 删除会议室
     * @param id
     * @return
     */
    @Transactional
    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
    public ResultVo delete(String id) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("删除失败");

        Meetingroom meetingroom = meetingroomRepository.findOne(id);
        if (meetingroom == null) {
            return resultVo;
        }
        meetingroomRepository.delete(meetingroom);
        resultVo.setResult(true);
        resultVo.setMessage("删除成功");
        return resultVo;
    }

    /**
     * 批量删除
     *
     * @param ids
     *            用,号隔开
     */
    @Transactional
    public ResultVo mRoomDelBatch(String ids) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("删除失败");
        if (StringUtils.isEmpty(ids)) {
            return resultVo;
        }
        String[] mIdArr = ids.split(",");
        for (String id : mIdArr) {
            meetingroomRepository.delete(id);
        }
        resultVo.setResult(true);
        resultVo.setMessage("删除成功");
        return resultVo;
    }

//    /**
//     * 查询会议室
//     */
//   @Cacheable(cacheName = "MEETINGROOM_CACHE")
//    public List<MeetingroomVo> getMeetingrooms() {
//        List<Meetingroom> meetingrooms = meetingroomRepository.findAll();
//        List<MeetingroomVo> meetingroomVos = MeetingroomConvertor.convert(meetingrooms);
//        return meetingroomVos;
//    }

//    public List<MeetingroomVo> getMeetingroomTest() {
////        Meetingroom meetingroom = meetingroomRepository.findOne(id);
////        MeetingroomVo meetingroomVo = new MeetingroomVo(meetingroom);
////        return meetingroomVo;
//
//
//        //测试2
//        List<Meetingroom> meetingroom = meetingroomRepository.findAll();
//        List<MeetingroomVo> meetingroomVos = new ArrayList<>();
//
//        if (!CollectionUtils.isEmpty(meetingroom)) {
//            for (Meetingroom meetingroom1 : meetingroom) {
//                MeetingroomVo meetingroomVo = MeetingroomConvertor.convert(meetingroom1);
//                MeetingroomVo newmeetingroomVo = new MeetingroomVo();
//                newmeetingroomVo.setHys(meetingroomVo.getHys());
//                newmeetingroomVo.setHysdd(meetingroomVo.getHysdd());
//                meetingroomVos.add(newmeetingroomVo);
//            }
//        }
//        return meetingroomVos;
//
//    }

//    public ResultVo update(String id,String hys,String hysdd,String fzr,String qt){
//        ResultVo resultVo = new ResultVo();
//        resultVo.setMessage("修改失败");
//        if (StringUtils.isEmpty(hys)) {
//            resultVo.setMessage("集团负责人不能为空");
//            return resultVo;
//        }
//        if (StringUtils.isEmpty(hysdd)) {
//            resultVo.setMessage("会议室地点不能为空");
//            return resultVo;
//        }
//        if (StringUtils.isEmpty(fzr)) {
//            resultVo.setMessage("负责人不能为空");
//            return resultVo;
//        }
//
//        Meetingroom meetingroom = meetingroomRepository.findOne(id);
//
//        if (meetingroom == null) {
//            return resultVo;
//        }
//        meetingroom.setFzr(fzr);
//        meetingroom.setHys(hys);
//        meetingroom.setHysdd(hysdd);
//        meetingroom.setQt(qt);
//
//
//        Meetingroom result=meetingroomRepository.save(meetingroom);
//
//        if (result != null) {
//            resultVo.setResult(true);
//            resultVo.setMessage("修改成功");
//            resultVo.setObj(result);
//        }
//        return resultVo;
//    }
    /**
     * 修改会议室
     */
//    @Transactional
//    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
//    public DataTableRowVo update(MeetingroomVo meetingroomVo) {
//        DataTableRowVo mrrv = new DataTableRowVo();
//        mrrv.setMessage("修改失败");
//
//        if (StringUtils.isEmpty(meetingroomVo.getHys())) {
//            mrrv.setMessage("部门所属单位不能为空");
//            return mrrv;
//        }
//        if (StringUtils.isEmpty(meetingroomVo.getHysdd())) {
//            mrrv.setMessage("部门地点不能为空");
//            return mrrv;
//        }
//
//        Meetingroom checkMeetingroom = meetingroomRepository.findOne(meetingroomVo.getId());
//        if (checkMeetingroom == null) {
//            mrrv.setMessage("该部门不存在");
//            return mrrv;
//        }
//
//        mrrv.setMessage("修改成功");
//        mrrv.setResult(true);
//        MeetingroomConvertor.convertMrrv(mrrv, checkMeetingroom);
//        return mrrv;
//    }


    /**
     * 删除会议室
     */
//    @Transactional
//    @TriggersRemove(cacheName = { "DEPARTMENT_CACHE" }, removeAll = true)
//    public ResultVo delete(String id) {
//        ResultVo resultVo = new ResultVo();
//        resultVo.setMessage("删除失败");
//
//        Meetingroom meetingroom = meetingroomRepository.findOne(id);
//        if (meetingroom == null) {
//            return resultVo;
//        }
//
//        meetingroomRepository.delete(meetingroom);
//        resultVo.setResult(true);
//        resultVo.setMessage("删除成功");
//        return resultVo;
//    }



//    public MeetingroomVo getMeetingroomsAll() {
//        List<Meetingroom> meetingrooms = meetingroomRepository.findAll();
//        MeetingroomVo meetingroomVo = new MeetingroomVo();
//        for (Meetingroom meetingroom : meetingrooms) {
//            meetingroomVo.addMeetingroomItem(meetingroom.getId(), meetingroom.getHysdd(), meetingroom.getHys(),meetingroom.getQt());
//        }
//        return meetingroomVo;
//    }



//
//    /**
//     * 更新
//     */
//    @Transactional
//    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
//    public DataTableRowVo update(MeetingroomVo meetingroomVo) {
//        DataTableRowVo mrrv = new DataTableRowVo();
//        mrrv.setMessage("修改失败");
//
//        if (StringUtils.isEmpty(meetingroomVo.getHys())) {
//            mrrv.setMessage("所属部门不能为空");
//            return mrrv;
//        }
//        if (StringUtils.isEmpty(meetingroomVo.getHysdd())) {
//            mrrv.setMessage("会议室不能为空");
//            return mrrv;
//        }
//
//        Meetingroom checkMeetingroom = meetingroomRepository.findOne(meetingroomVo.getId());
//        if (checkMeetingroom == null) {
//            mrrv.setMessage("该会议室不存在");
//            return mrrv;
//        }
//
//
//        // 把原来的name记录下来，如果和现在的不一样，并且修改的department是父部门，那就查询到所有的子部门
////        String name = checkDepartment.getName();
////        DepartmentConvertor.convert(departmentVo, checkDepartment);
////        checkDepartment.setParentDepartment(parentDepartment);
////        checkDepartment.setManager(manager);
//
//        meetingroomRepository.save(checkMeetingroom);
//        mrrv.setMessage("修改成功");
//        mrrv.setResult(true);
//        MeetingroomConvertor.convertMrrv(mrrv, checkMeetingroom);
//        return mrrv;
//    }
//
//    /**
//     * 删除
//     */
//    @Transactional
//    @TriggersRemove(cacheName = { "MEETINGROOM_CACHE" }, removeAll = true)
//    public ResultVo delete(String id) {
//        ResultVo resultVo = new ResultVo();
//        resultVo.setMessage("删除失败");
//
//        // 把所有所有的子功能id查询到，传给前台，一起删除
//
//        Meetingroom meetingroom = meetingroomRepository.findOne(id);
//        if (meetingroom == null) {
//            return resultVo;
//        }
//
//        meetingroomRepository.delete(meetingroom);
//        resultVo.setResult(true);
//        resultVo.setMessage("删除成功");
//        return resultVo;
//    }
}
