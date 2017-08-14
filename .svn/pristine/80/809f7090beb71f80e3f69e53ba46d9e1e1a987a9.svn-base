package com.expect.admin.web;

import com.expect.admin.config.Settings;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.MeetingService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.MeetingVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qifeng on 17/5/15.
 */
@Controller
@RequestMapping(value = "admin/meeting")
public class MeetingController {
    private final Logger log = LoggerFactory.getLogger(MeetingController.class);
    @Autowired
    private Settings settings;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private UserService userService;

    private final String viewName = "admin/meeting/";

    /**
     * 发文申请
     * @return
     */
    @RequestMapping(value = "/addMeeting", method = RequestMethod.GET)
    public ModelAndView addMeeting(){
        ModelAndView mv = new ModelAndView(viewName + "m_apply");
        return mv;
    }

    @RequestMapping(value = "/uploadMeetingAttachment", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo upload(MultipartFile files, HttpServletRequest request) {
        String path = settings.getAttachmentPath();
        FileResultVo frv = attachmentService.save(files, path);
        return frv;
    }

    @RequestMapping(value = "/saveMeeting", method = RequestMethod.POST)
    public void saveDocument(MeetingVo meetingVo,
                             @RequestParam(name = "bczl", required = true)   String bczl,
                             @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
                             HttpServletResponse response) throws IOException {
        if(!meetingCheck(meetingVo, response)) return;
        String message = StringUtil.equals(bczl, "tj") ? "会议提交" : "会议保存";
        try{
            meetingService.newMeetingSave(meetingVo, bczl, attachmentId);

        }catch(Exception e) {
            log.error("保存会议报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, message + "失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, message + "成功！").build());
    }

    @RequestMapping(value = "/sqjl")
    public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx){
        if(StringUtil.isBlank(lx)) lx = "wtj";
        ModelAndView modelAndView = new ModelAndView(viewName + "m_apply_record");
        UserVo userVo = userService.getLoginUser();
        List<MeetingVo> meetingVoList = meetingService.getMeetingByUserIdAndCondition(userVo.getId(), "1", lx);
        modelAndView.addObject("meetingVoList", meetingVoList);
        return modelAndView;
    }

    private boolean  meetingCheck(MeetingVo meetngVo, HttpServletResponse response) throws IOException {
        if(meetngVo == null) {
            log.error("试图保存空的会议单");
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "申请失败，会议内容为空！").build());
            return false;
        }

        return true;
    }

    /**
     * 申请记录tab会议审批tab
     * @throws IOException
     */
    @GetMapping(value = "/sqjlTab")
    public void sqjlTab(@RequestParam(name = "lx", required = false)String lx,
                        @RequestParam(name = "bz", required = false)String bz,
                        HttpServletResponse response) throws IOException {
        if (StringUtil.isBlank(lx)) lx = "wtj";
        List<MeetingVo> meetingVos = new ArrayList<MeetingVo>();
        try {
            UserVo userVo = userService.getLoginUser();


            //申请记录的待审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
                meetingVos = meetingService.getSqjldspList(userVo.getId(), "2");
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", meetingVos).build());
                return;
            }

//            //申请记录的已审批
//            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
//                meetingVos = meetingService.getSqjlYspList(userVo.getId());
//                MyResponseBuilder.writeJsonResponse(response,
//                        JsonResult.useDefault(true, "获取已审批记录成功", meetingVos).build());
//                return;
//            }

            //申请记录的未提交
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "wtj")){
                meetingVos=meetingService.getMeetingByUserIdAndCondition(userVo.getId(),"1",lx);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", meetingVos).build());
                return;
            }
//            //发文审批已审批记录
//            if (StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "ysp")){
//                documentVos = documentService.getGwspYspList(userVo.getId());
//                MyResponseBuilder.writeJsonResponse(response,
//                        JsonResult.useDefault(true, "获取已审批记录成功", documentVos).build());
//                return;
//            }
//            if(StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "dsp")){
//                documentVos = documentService.getGwspDspList(userVo.getId());
//                MyResponseBuilder.writeJsonResponse(response,
//                        JsonResult.useDefault(true, "获取待审批记录成功", documentVos).build());
//                return;
//            }

        } catch (Exception e) {
            log.error("获取记录错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取记录出错").build());
        }
    }
}
