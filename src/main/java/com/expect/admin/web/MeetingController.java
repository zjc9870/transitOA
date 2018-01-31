package com.expect.admin.web;

import com.expect.admin.config.Settings;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.*;
import com.expect.admin.service.convertor.MeetingroomConvertor;
import com.expect.admin.service.vo.*;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    @Autowired
    private RoleService roleService;
    @Autowired
    private LcrzbService lcrzbService;
    @Autowired
    private LcService lcService;
    @Autowired
    private HytzService hytzService;
    @Autowired
    private NotifyObjectService notifyObjectService;
    @Autowired
    private MeetingroomService meetingroomService;
    @Autowired
    private FunctionService functionService;

    private final String viewName = "admin/meeting/";

    /**
     * 会议申请
     * @return
     */
    @RequestMapping(value = "/addMeeting", method = RequestMethod.GET)
    public ModelAndView addMeeting(){
        MeetingVo meetingVo = new MeetingVo();
        UserVo userVo =userService.getLoginUser();
        meetingVo.setUserName(userVo.getFullName());
        ModelAndView mv = new ModelAndView(viewName + "m_apply");
        mv.addObject("meetingVo", meetingVo);
        return mv;
    }

    /**
     * 申请记录
     * @param lx
     * @return
     */
    @RequestMapping(value = "/sqjl")
    public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx){
        if(StringUtil.isBlank(lx)) lx = "wtj";
        ModelAndView modelAndView = new ModelAndView(viewName + "m_apply_record");
        UserVo userVo = userService.getLoginUser();
        modelAndView.addObject("meetingVoList",
                meetingService.getMeetingByUserIdAndCondition(userVo.getId(), "1", lx));
        return modelAndView;
    }

    /**
     * 申请记录详情(可编辑)
     */
    @RequestMapping("/sqjlxqE")
    public ModelAndView sqjlxqE(@RequestParam(name = "id", required = true)String meetingId) {
        ModelAndView modelAndView = new ModelAndView(viewName + "m_apply_recordDetail");
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        modelAndView.addObject("meetingVo", meetingVo);
        return modelAndView;
    }

    /**
     * 申请记录详情(不可编辑)
     */
    @RequestMapping("/sqjlxqNE")
    public ModelAndView sqjlxqNE(@RequestParam(name = "id", required = true)String meetingId) {
        ModelAndView modelAndView = new ModelAndView(viewName + "m_apply_recordDetail_ne");
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        modelAndView.addObject("meetingVo", meetingVo);
        return modelAndView;
    }

    /**
     * 会议审批列表页面
     */
    @GetMapping(value = "/hysp")
    public ModelAndView hysp(@RequestParam(name = "lx", required = false)String lx) {
        UserVo userVo = userService.getLoginUser();
        if(StringUtil.isBlank(lx)) lx = "dsp";
        ModelAndView modelAndView = new ModelAndView(viewName + "m_approve");
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
//        meetingVoList = meetingService.getMeetingByUserIdAndCondition(userVo.getId(),"2",lx);
        meetingVoList = meetingService.getHyspByUserAndCondition(userVo,"2");
        modelAndView.addObject("meetingVoList", meetingVoList);
        modelAndView.addObject("userVo", userVo);
        return modelAndView;
    }

    /**
     * 会议审批查看详情
     * @return
     */
    @GetMapping(value = "/hyspckxq")
    public ModelAndView hyspckxq(@RequestParam(name = "id", required = true)String meetingId){
        ModelAndView modelAndView = new ModelAndView(viewName + "m_approveDetail");
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        //       UserVo userVo = userService.getLoginUser();
        modelAndView.addObject("meetingVo", meetingVo);
        //      modelAndView.addObject("userVo",userVo);
        return modelAndView;
    }

    /**
     * 会议通知
     * @param meetingId
     * @return
     */
    @GetMapping(value = "/notify")
    public ModelAndView notify(@RequestParam(name = "id", required = true) String meetingId){
        ModelAndView modelAndView = new ModelAndView(viewName + "m_notify");
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        List<NotifyObjectVo> notifyObjectVoList = notifyObjectService.getRelatedNotifyObject();
        modelAndView.addObject("notifyObjectVoList",notifyObjectVoList);
        modelAndView.addObject("meetingVo", meetingVo);
        return modelAndView;
    }

    @GetMapping(value = "/approveXq")
    public ModelAndView approveXq(@RequestParam(name = "id", required = true) String meetingId){
        ModelAndView modelAndView = new ModelAndView(viewName + "m_approve_recordDetail");
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        HytzVo hytzVo = hytzService.getHytzVoByMeetingId(meetingId);
        modelAndView.addObject("meetingVo",meetingVo);
        modelAndView.addObject("hytzVo", hytzVo);
        return modelAndView;
    }

    @RequestMapping(value = "/hytz", method = RequestMethod.GET)
    public ModelAndView hytz(){
        UserVo userVo = userService.getLoginUser();
        ModelAndView modelAndView = new ModelAndView(viewName + "m_hy_notify_all");
        String userName = userVo.getUsername();
        String role = userVo.getRoleName();
        List<HytzVo> hytzVoList = hytzService.getHytzByUserName(userName);
        List<HytzVo> wdHytzVoList = hytzService.getWdHytzList(hytzVoList);
        List<MeetingVo> meetingVoList = hytzService.getHyMeetingVo(wdHytzVoList,role);
        List<MeetingVo> meetingVoListByTzsj = hytzService.sortMeetingListByTzsj(meetingVoList);
        modelAndView.addObject("meetingVoListByTzsj",meetingVoListByTzsj);
        return modelAndView;
    }

    @RequestMapping("/hytzNE")
    public ModelAndView fwtzNE(@RequestParam(name = "id", required = true)String meetingId,
                               @RequestParam(name = "hytzid", required=true)String hytzid){
        ModelAndView modelAndView = new ModelAndView(viewName + "m_hy_notify_xq");
        UserVo userVo = userService.getLoginUser();
//        String roleName = userVo.getRoleName();
//        if(roleName.equals("公司高管") || roleName.equals("公司部门")){
//            modelAndView = new ModelAndView(viewName + "m_gshy_notify_xq");
//        }

//        MeetingVo meetingVo = hytzService.getMeetingById(meetingId);
//        meetingVo.setHytzid(hytzid);
//        HytzVo hytzVo= hytzService.getHytzVo(hytzid);

        MeetingVo meetingVo = hytzService.getMeetingVoById(meetingId);
        HytzVo hytzVo = hytzService.getHytzVoByMeetingId(meetingId);
        meetingVo.setHytzid(hytzid);
        modelAndView.addObject("meetingVo", meetingVo);
        modelAndView.addObject("hytzVo",hytzVo);
        return modelAndView;
    }

    /**
     * 保存，提交会议
     * @param meetingVo
     * @param bczl
     * @param attachmentId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/saveMeeting", method = RequestMethod.POST)
    public void saveMeeting(MeetingVo meetingVo,
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
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
        try {
            UserVo userVo = userService.getLoginUser();


            //申请记录的未提交
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "wtj")){
                meetingVoList = meetingService.getMeetingByUserIdAndCondition(userVo.getId(), "1", lx);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", meetingVoList).build());
                return;
            }


            //申请记录的待审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
                meetingVoList = meetingService.getSqjlWspList(userVo.getId(),"2");
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", meetingVoList).build());
                return;
            }

            //申请记录的已审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
                meetingVoList = meetingService.getSqjlYspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", meetingVoList).build());
                return;
            }

            //申请记录的已撤销
            if (StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ych")){
                meetingVoList = meetingService.getMeetingByUserIdAndCondition(userVo.getId(), "revocation", lx);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已撤销记录成功", meetingVoList).build());
                return;
            }

            //待审批记录
            if (StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "dsp")){
                meetingVoList = meetingService.getHyspByUserAndCondition(userVo,"2");
//                meetingVoList = meetingService.getHyspDspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取待审批记录成功", meetingVoList).build());
                return;
            }

            //已审批记录
            if (StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "ysp")){
                meetingVoList = meetingService.getHyspYspList(userVo);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", meetingVoList).build());
                return;
            }

        } catch (Exception e) {
            log.error("获取记录错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取记录出错").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "获取申请记录成功", meetingVoList).build());
    }

    /**
     * 以保存会议的提交
     * @throws IOException
     *
     */
    @PostMapping("/submitWtj")
    public void submitWtj(String id,
                          @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
                          HttpServletResponse response) throws IOException{
        try{
            MeetingVo meetingVo = meetingService.getMeetingById(id);
            if(meetingVo == null){
                MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该会议").build());
                return;
            }
            meetingVo.setHyshzt("2");
            meetingService.updateMeeting(meetingVo, attachmentId);
            meetingService.addXzLcrz(meetingVo.getId(), "2");
        }catch(Exception e) {
            log.error("以保存会议提交时报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "会议提交出错，请重试").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "会议提交完成").build());
    }

    /**
     * 以保存未提交的会议的删除
     * @param id
     * @param response
     * @throws IOException
     */
    @PostMapping("/deleteWjt")
    public void deleteWjt(String id, HttpServletResponse response) throws IOException {
        try{
            if(StringUtil.isBlank(id)){
                MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "会议删除失败， 要删除的会议id为空").build());
                return;
            }
            meetingService.delete(id);
        }catch(Exception e) {
            log.error("以保存合同提交时报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "会议删除失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "会议删除完成").build());
    }

    /**
     * 撤销会议（只有会议申请人可以撤销）
     * @param response
     * @param meetingId 要撤销的会议的id
     * @param revocationReason 撤销会议理由（最多为100个字）
     * @throws IOException
     */
    @PostMapping("/revocationMeeting")
    public void revocationMeeting(HttpServletResponse response,
                                  @RequestParam(name = "id", required = true)String meetingId,
                                  @RequestParam(name = "revocationReason", required = true)String revocationReason) throws IOException {
        try{
            if(revocationReason.length() > 100){
                MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "撤销理由过长，最多100个字").build());
            }
            else meetingService.revocationMeeting(meetingId, revocationReason);
        }catch(BaseAppException be) {
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, be.getMessage()).build());
            log.error("撤销会议是失败，会议id为" + meetingId, be);
        }catch(Exception e) {
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "撤销会议失败").build());
            log.error("撤销会议是失败，会议id为" + meetingId, e);
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "撤销会议成功").build());
    }

    /**
     * 流程审批
     * @param response
     * @param cljg
     * @throws IOException
     */
    @RequestMapping(value = "/addLcrz", method = RequestMethod.POST)
    public void addLcrz(MeetingVo meetingVo,
                        @RequestParam(name = "cljg", required = true)String cljg,
                        @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
                        HttpServletResponse response) throws IOException{
        //插入流程日志
        //判断审核是否通过 如果通过更新文件状态到下一个状态
        //如果不通过更新文件状态到退回状态，修改流程日志表中的审批记录以后不再显示退回状态之后的审批记录
//        if(!meetingCheck(meetingVo, response)) return;
//        if (StringUtil.equals(cljg,"tg"))
//            meetingService.ApproveMeetingSave(meetingVo,attachmentId);
        try{
            meetingService.saveMeetingLcrz(meetingVo, cljg, attachmentId);
        }catch(Exception e) {
            log.error("会议审核失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "会议审核失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "会议审核成功！").build());
    }

    @RequestMapping(value = "/passHysp", method = RequestMethod.POST)
    public void passHysp(HttpServletResponse response,
                         @RequestParam(name = "cljg", required = true)String cljg,
                         @RequestParam(name = "id",required = true)String clnrid)throws IOException{
        try{
            meetingService.saveHyspLcrz(cljg, clnrid);
        }catch(Exception e){
            log.error("会议审核失败",e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "会议审核失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true,"会议审核成功！").build());
    }

    @RequestMapping(value = "savenotify", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo save(HytzVo hytzVo,
                         @RequestParam(name = "id") String id,
                         @RequestParam(name = "fileId") String[] attachmentId){
        ResultVo resultVo = new ResultVo();
        String objectRepeated;
        try{
            objectRepeated = hytzService.saveHytz(hytzVo, id,attachmentId);
            meetingService.saveHyspLcrz("tz",id);
        }catch (Exception e){
            this.log.error("保存会议通知报错", e);
            resultVo.setResult(false);
            resultVo.setMessage("保存会议通知报错");
            return resultVo;
        }
        resultVo.setResult(true);
        resultVo.setMessage("保存会议通知成功");
        resultVo.setObj(objectRepeated);
        return  resultVo;
    }

    @GetMapping(value = "/hytzTab")
    public void hytzTab(@RequestParam(name = "lx", required = false)String lx,
                        HttpServletResponse response)throws  IOException{
        if (StringUtil.isBlank(lx)) lx = "wd";
        try {
            UserVo userVo = userService.getLoginUser();
            String userName = userVo.getUsername();
            String role = userVo.getRoleName();
            List<HytzVo> hytzVoList = hytzService.getHytzByUserName(userName);

            if (StringUtil.equals(lx,"wd")){
                List<HytzVo> wdHytzVoList = hytzService.getWdHytzList(hytzVoList);
                List<MeetingVo> meetingVoList = hytzService.getHyMeetingVo(wdHytzVoList, role);
                List<MeetingVo> meetingVoListByTzsj = hytzService.sortMeetingListByTzsj(meetingVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取未读会议通知成功", meetingVoListByTzsj).build());
                return;
            }

            if (StringUtil.equals(lx,"yd")){
                List<HytzVo> ydHytzVoList = hytzService.getYdHytzList(hytzVoList);
                List<MeetingVo> meetingVoList = hytzService.getHyMeetingVo(ydHytzVoList, role);
                List<MeetingVo> meetingVoListByYdsj = hytzService.sortMeetingListByYdsj(meetingVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已读会议通知成功", meetingVoListByYdsj).build());
                return;
            }
        }catch (Exception e){
            log.error("回去会议通知错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取会议通知出错").build());
        }
    }

    /**
     * 更新已读会议通知
     * @param meetingId
     * @param response
     * @throws IOException
     */
    @PostMapping("/updateHytz")
    public void updateHytz(@RequestParam(name = "meetingId", required=true)String meetingId,
                           @RequestParam(name = "hytzid", required = true)String hytzid,
                           HttpServletResponse response)throws IOException{
        HytzVo hytzVo = hytzService.getHytzVoById(hytzid);
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        if (hytzVo == null){
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该会议通知").build());
            return;
        }
        if (meetingVo == null){
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该会议记录").build());
            return;
        }
        hytzService.updateHytz(hytzVo);
        meetingService.addMemoItem(meetingVo);
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "会议已读成功").build());
    }

//    @PostMapping("/hyTab")
//    public void hyTab(
//                      @RequestParam(name = "lx", required = true)String lx,
//                      HttpServletResponse response)throws IOException{
//        if(StringUtil.isBlank(lx)) lx = "wd";
//        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
//        try {
//            UserVo userVo = userService.getLoginUser();
//            String username = userVo.getUsername();
//
//
//        }catch (Exception e){
//            log.error("获取会议记录出错"+ lx, e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取会议记录出错").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "获取会议记录成功", meetingVoList).build());
//    }




    /**
     * 会议附件上传
     * @return
     */
    @RequestMapping(value = "/uploadMeetingAttachment", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo upload(MultipartFile files, HttpServletRequest request) {
        String path = settings.getAttachmentPath();
        FileResultVo frv = attachmentService.save(files, path);
        return frv;
    }

    /**
     * @param attachmentId
     * @param meetingId
     * @param reponse
     * @return
     */
    @GetMapping("/meetingAttachmentDownload")
    public String meetingAttachmentDownload(
            @RequestParam(name = "attachmentId", required = true)String attachmentId,
            @RequestParam(name = "meetingId", required = true)String meetingId,
            HttpServletResponse reponse) {
        MeetingVo meetingVo = meetingService.getMeetingById(meetingId);
        if(meetingVo != null){
            return "forward:/admin/attachment/download?id=" + attachmentId;
        }
        return "forward:/admin/attachment/downloadAttachmentAsPdf?id=" + attachmentId;
    }



    @RequestMapping(value = "/selectHydd")
    public void selectHydd(HttpServletResponse response)throws IOException{
        List<String>  hydd=new ArrayList<>();
        try{
            hydd = meetingroomService.searchHydd();
        }catch(Exception e){
            log.error("查找会议地点失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "查找会议地点失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true,"获取会议地点成功", hydd).build());
    }

    @RequestMapping(value = "/selectHys")
    public void selectHys(
            @RequestParam(name = "hydd",required = true)String hydd,
            HttpServletResponse response)throws IOException{
        List<String>  hys=new ArrayList<>();
        try{
            hys = meetingroomService.searchHys(hydd);

        }catch(Exception e){
            log.error("查找会议室失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "查找会议室失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true,"获取会议室成功", hys).build());
    }

//    @RequestMapping(value = "/searchSyqk",method = RequestMethod.POST)
//    public void searchSyqk(
//            @RequestParam(name = "hydd", required = true)String hydd,
//            @RequestParam(name = "hys", required = true)String hys,
//            @RequestParam(name = "hyrq", required = true)String hyrq,
//            HttpServletResponse response) throws IOException{
////        List<String> syqk = new ArrayList<>();
//        List<MeetingVo> syqk = new ArrayList<>();
//        try{
//            syqk = meetingService.searchSyqk(hydd, hys, hyrq);
//        }catch(Exception e){
//            log.error("查看会议室使用情况失败", e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "查看会议室使用情况失败").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response,JsonResult.useDefault(true, "查看会议室使用情况成功", syqk).build());
//    }

    @RequestMapping(value = "/meetingRoomSyqk",method = RequestMethod.POST)
    public void meetingRoomSyqk(
            @RequestParam(name = "hydd", required = true)String hydd,
            @RequestParam(name = "hys", required = true)String hys,
            @RequestParam(name = "hyrq", required = true)String hyrq,
            HttpServletResponse response) throws IOException{
        List<String[]> syqk = new ArrayList<>();

        try{
            syqk = meetingService.meetingRoomSyqk(hydd, hys, hyrq);
        }catch(Exception e){
            log.error("查看会议室使用情况失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "查看会议室使用情况失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "查看会议室使用情况成功", syqk).build());
    }

    @RequestMapping(value = "/checkTime", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkTime(
            @RequestParam(name = "hydd", required = true)String hydd,
            @RequestParam(name = "hys", required = true)String hys,
            @RequestParam(name = "hyrq", required = true)String hyrq,
            @RequestParam(name = "kssj", required = true)String kssj,
            @RequestParam(name = "jssj", required = true)String jssj,
            @RequestParam(name = "meetingId", required = true)String meetingId,
            HttpServletResponse response) throws IOException{
        boolean isRightTime =false;

        isRightTime = meetingService.checkIsRightTime(hydd, hys, hyrq, kssj, jssj, meetingId);

        return isRightTime;
    }











    //    @RequestMapping(value = "/updateMeeting", method = RequestMethod.POST)
//    public void updateMeeting(MeetingVo meetingVo,
//    @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
//    HttpServletResponse response) throws IOException {
//        try{
//            meetingService.updateMeeting(meetingVo, attachmentId);
//        }catch(Exception e) {
//            log.error("更新会议内容报错", e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "更新会议内容失败！").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "更新会议内容成功！").build());
//    }







//    /**
//     * 删除已提交的会议（软删除）
//     * @param response
//     * @param id
//     * @throws IOException
//     */
//    @PostMapping("/deleteMeeting")
//    public void deleteMeeting(HttpServletResponse response,
//        @RequestParam(name = "id", required = true) String id) throws IOException{
//        try{
//            meetingService.deleteMeeting(id);
//        }catch(Exception e){
//            log.error("删除会议失败未找到id为" + id + "的会议", e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到要删除的会议！").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "删除的会议成功！").build());
//    }






//    /**
//     * 会议查询
//     */
//    @GetMapping("/hycx")
//    public ModelAndView hycx(){
//        ModelAndView modelAndView = new ModelAndView(viewName + "c_find");
//        return modelAndView;
//    }
//
//    /**
//     *
//     * @param response
//     * @param hyzt 会议主题
//     * @param hybh 会议编号
//     * @param startTime 开始时间（搜索的时间段，以申请时间为准）
//     * @param endTime 结束时间
//     * @param hyspzt 会议状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
//     * @param sqr 申请人姓名
//     * @throws IOException
//     */
//    @PostMapping("/searchMeeting")
//    public void contractSearchResult(HttpServletResponse response,
//        @RequestParam(name = "hyzt", required = false)String hyzt,
//        @RequestParam(name = "hybh", required = false)String hybh,
//        @RequestParam(name = "startTime", required = false)String startTime,
//        @RequestParam(name = "endTime", required = false)String endTime,
//        @RequestParam(name = "hyspzt", required = false)String hyspzt,
//        @RequestParam(name = "sqr", required = false)String sqr) throws IOException{
//        Date start = null,end = null;
//        List<MeetingVo> meetingVoList = null;
//        try{
//            if(!StringUtil.isBlank(startTime)) start = DateUtil.parse(startTime, DateUtil.fullFormat);
//            if(!StringUtil.isBlank(endTime)) end = DateUtil.parse(endTime, DateUtil.fullFormat);
//            meetingVoList = meetingService.searchMeeting(hyzt, hybh, start, end, hyspzt, sqr, "");
//        }catch(Exception e) {
//            log.error("会议查询失败", e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "搜索会议失败！").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", meetingVoList).build());
//    }
//
//    /**
//     * 个人使用的部分会议查询
//     * 只能查询与当前登录用户相关（当前用户参与了会议的申请或审批）的会议
//     * @param hyzt 会议主题
//     * @param hybh 会议编号
//     * @param startTime 开始时间（搜索的时间段，以申请时间为准）
//     * @param endTime 结束时间
//     * @param hyspzt 会议状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
//     * @param sqr 申请人姓名
//     * @throws IOException
//     */
//    @PostMapping("/searchContractOfUser")
//    public void meetingSearchResultOfUser(HttpServletResponse response,
//        @RequestParam(name = "hyzt", required = false)String hyzt,
//        @RequestParam(name = "hybh", required = false)String hybh,
//        @RequestParam(name = "startTime", required = false)String startTime,
//        @RequestParam(name = "endTime", required = false)String endTime,
//        @RequestParam(name = "hyspzt", required = false)String hyspzt,
//        @RequestParam(name = "sqr", required = false)String sqr) throws IOException {
//
//        UserVo userVo = userService.getLoginUser();//获取当前登录用户
//        Date start = null,end = null;
//        List<MeetingVo> meetingVoList = null;
//        try{
//            if(!StringUtil.isBlank(startTime)) start = DateUtil.parse(startTime, DateUtil.fullFormat);
//            if(!StringUtil.isBlank(endTime)) end = DateUtil.parse(endTime, DateUtil.fullFormat);
//            meetingVoList = meetingService.searchMeeting(hyzt, hybh, start, end, hyspzt, sqr, userVo.getId());
//        }catch(Exception e) {
//            log.error("合同查询失败", e);
//            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "搜索合同失败！").build());
//        }
//        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", meetingVoList).build());
//
//    }
//


    /**
     * 会议室-管理页面
     * @return
     */
    @RequestMapping(value = "/MeetingroomManagePage", method = RequestMethod.GET)
    public ModelAndView meetingroomManagePage() {
        UserVo userVo = userService.getLoginUser();
        String departmentName = userVo.getDepartmentName();
        if(userVo == null) return new ModelAndView("admin/login");
        List<MeetingroomVo> meetingroomVoList = meetingroomService.getMeetingrooms();
        List<DataTableRowVo> mrrvs = MeetingroomConvertor.convertDtrv(meetingroomVoList, departmentName);
        ModelAndView modelAndView = new ModelAndView(viewName + "m_meetingroom");
        modelAndView.addObject("meetingrooms", mrrvs);
        return modelAndView;
    }


    /**
     * 会议室-更新和新建-表单页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/meetingroomFormPage", method = RequestMethod.GET)
    public ModelAndView meetingroomFormPage(String id) {
        MeetingroomVo meetingroomVo = meetingroomService.getMeetingroomById(id);
        ModelAndView modelAndView = new ModelAndView(viewName + "m_meetingroom_form");
        modelAndView.addObject("meetingroom", meetingroomVo);
        return modelAndView;
    }

    /**
     *  会议室-查看会议室使用情况详情-表单页面
     * @param mid
     * @return
     */
    @RequestMapping(value = "/meetingrecordFormPage", method = RequestMethod.GET)
    public ModelAndView meetingrecordFormPage(String mid) {
        String meetingroom_id = mid;
        ModelAndView modelAndView = new ModelAndView(viewName + "m_meetingrecord_form");
        modelAndView.addObject("meetingroom_id", meetingroom_id);
        return modelAndView;
    }

    @RequestMapping(value = "/retHydd",method = RequestMethod.POST)
    public void retHydd(HttpServletResponse response) throws IOException{
        UserVo userVo = userService.getLoginUser();
        String departmentName = userVo.getDepartmentName();
        String retHydd = "";
        try{
            retHydd = meetingroomService.retHydd(departmentName);
        }catch(Exception e){
            log.error("获取所属部门失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取所属部门失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "获取所属部门失败", retHydd).build());
    }

    /**
     * 会议室-根据具体时间和地点查询会议室使用情况
     * @param mid
     * @param mdate
     * @return
     */
    @RequestMapping(value = "/pointedMrecord", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo pointedMrecord(String mid, String mdate) {
        return meetingroomService.getMeetingInfo(mid,mdate);
    }

    /**
     * 会议室-保存
     * @param meetingroomVo
     * @return
     */
    @RequestMapping(value = "/meetingroomSave", method = RequestMethod.POST)
    @ResponseBody
    public DataTableRowVo save(MeetingroomVo meetingroomVo) {
        return meetingroomService.save(meetingroomVo);
    }

    /**
     * 会议室-更新
     * @param meetingroomVo
     * @return
     */
    @RequestMapping(value = "/meetingroomUpdate", method = RequestMethod.POST)
    @ResponseBody
    public DataTableRowVo update(MeetingroomVo meetingroomVo) {
        return meetingroomService.update(meetingroomVo);
    }

    /**
     * 会议室-删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/meetingroomDelete", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo delete(String id) {
        return meetingroomService.delete(id);
    }

    /**
     * 会议室-批量删除
     */
    @RequestMapping(value = "/meetingroomDelBatch", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo meetingroomDelBatch(String ids) {
        return meetingroomService.mRoomDelBatch(ids);
    }
}

