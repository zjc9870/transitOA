package com.expect.admin.web;

import com.expect.admin.config.Settings;
import com.expect.admin.data.dataobject.Fwtz;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.*;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.FwtzVo;
import com.expect.admin.service.vo.NotifyObjectVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.DateUtil;
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
import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qifeng on 17/3/6.
 */
@Controller
@RequestMapping(value = "admin/document")
public class DocumentController {
    private final Logger log = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private LcrzbService lcrzbService;
    @Autowired
    private LcService lcService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleJdgxbGxbService roleJdgxbGxbService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private Settings settings;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FwtzService fwtzService;
    @Autowired
    private NotifyObjectService notifyObjectService;


    private final String viewName = "admin/document/";

    /**
     * 发文申请
     * @return
     */
    @RequestMapping(value = "/addDocument", method = RequestMethod.GET)
    public ModelAndView addDocument(){
        DocumentVo documentVo = new DocumentVo();
        UserVo userVo = userService.getLoginUser();
        documentVo.setUserName(userVo.getFullName());
        ModelAndView mv = new ModelAndView(viewName + "d_apply");
        mv.addObject("documentVo",documentVo);
        return mv;
    }

    @RequestMapping(value = "/sqjl")
    public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx){
        if(StringUtil.isBlank(lx)) lx = "wtj";
        ModelAndView modelAndView = new ModelAndView(viewName + "d_apply_record");
        UserVo userVo = userService.getLoginUser();
        List<DocumentVo> documentVoList = documentService.getDocumentByUserIdAndCondition(userVo.getId(), "1", lx);
        modelAndView.addObject("documentVoList", documentVoList);
        return modelAndView;
    }

    /**
     * 公文审批列表页面
     */
    @GetMapping(value = "/fwsp")
    public ModelAndView fwsp(@RequestParam(name = "lx", required = false)String lx) {
        UserVo userVo = userService.getLoginUser();
        if(StringUtil.isBlank(lx)) lx = "dsp";
        ModelAndView modelAndView = new ModelAndView(viewName + "d_approve");
        List<DocumentVo> documentVoList = new ArrayList<DocumentVo>();
        documentVoList=documentService.getDocumentByUserIdAndCondition(userVo.getId(),"2",lx);
        modelAndView.addObject("documentVoList",documentVoList);
        return modelAndView;
    }

    /**
     * 公文审批查看详情
     * @return
     */
    @RequestMapping(value = "/gwspckxq")
    public ModelAndView gwspckxq(@RequestParam(name = "id", required = true)String documentId){
        ModelAndView modelAndView;
        UserVo userVo = userService.getLoginUser();
        if(userVo.getDepartmentName().contains("集团部门")) {
            modelAndView = new ModelAndView(viewName + "d_approveDetail");
        }else{
            modelAndView = new ModelAndView(viewName + "d_approveDetail_zgs");
        }
//        ModelAndView modelAndView = new ModelAndView(viewName + "d_approveDetail");
        FwtzVo fwtzVo = fwtzService.getFwtzVoByDocumentId(documentId);
        DocumentVo documentVo = documentService.getDocumentById(documentId);
        modelAndView.addObject("documentVo", documentVo);
        modelAndView.addObject("fwtzVo", fwtzVo);
        return modelAndView;
    }
    
    /**
     * 流程审批
     * @param response
     * @param cljg
     * @param message
     * @param clnrid
     * @throws IOException
     */
    @RequestMapping(value = "/addLcrz", method = RequestMethod.POST)
    public void addLcrz(HttpServletResponse response,
                        @RequestParam(name = "cljg", required = true)String cljg,
                        @RequestParam(name = "yj", required = false) String message,
                        @RequestParam(name = "id", required = true)  String clnrid) throws IOException{
        //插入流程日志
        //判断审核是否通过 如果通过更新文件状态到下一个状态
        //如果不通过更新文件状态到退回状态，修改流程日志表中的审批记录以后不再显示退回状态之后的审批记录
        if(message == null) message  ="";
        try{
            documentService.saveDocumentLcrz(cljg, message, clnrid, "gw");
        }catch(Exception e) {
            log.error("公文审核失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "公文审核失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "公文审核成功！").build());

    }
    /**
     * 判断某些tab是否显示
     * @param tj  判断条件
     * @param tabName 要判断的tab
     * @return true tab显示 <br>
     * false tab不显示
     */
    //TODO
    private boolean sfxsTab(String tj, String tabName) {
        //已退回tab 用户角色是
        if(StringUtil.equals(tabName, "yth"))
            return !StringUtil.isBlank(tj) &&
                    (tj.endsWith("文员") ||
                            StringUtil.equals(tj, "部门负责人"));
        return false;
    }

    /**
     * 公文附件上传
     */
    @RequestMapping(value = "/uploadDocumentAttachment", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo upload(MultipartFile files, HttpServletRequest request) {
        String path = settings.getAttachmentPath();
//		path = Base64Util.decode(path);
        FileResultVo frv = attachmentService.save(files, path);
        return frv;
    }

    /**
     * 保存，提交公文
     * @param documentVo
     * @param bczl
     * @param attachmentId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/saveDocument", method = RequestMethod.POST)
    public void saveDocument(DocumentVo documentVo,
                             @RequestParam(name = "bczl", required = true)   String bczl,
                             @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
                             HttpServletResponse response) throws IOException {
        if(!documentCheck(documentVo, response)) return;
        String message = StringUtil.equals(bczl, "tj") ? "发文提交" : "发文保存";
        try{
            documentService.newDocumentSave(documentVo, bczl, attachmentId);

        }catch(Exception e) {
            log.error("保存公文报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, message + "失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, message + "成功！").build());
    }

    /**
     * 判断新申请的公文是不是满足要求
     * 1.公文不能为空
     *
     * @param documentVo
     * @param response
     * @return 满足全部要求返回true， 否则返回false
     * @throws IOException
     */
    private boolean  documentCheck(DocumentVo documentVo, HttpServletResponse response) throws IOException {
        if(documentVo == null) {
            log.error("试图保存空的发文");
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "申请失败，发文内容为空！").build());
            return false;
        }

        return true;
    }

    /**
     * 申请记录tab 公文审批tab
     * @throws IOException
     */
    @GetMapping(value = "/sqjlTab")
    public void sqjlTab(@RequestParam(name = "lx", required = false)String lx,
                        @RequestParam(name = "bz", required = false)String bz,
                        HttpServletResponse response) throws IOException {
        if (StringUtil.isBlank(lx)) lx = "wtj";
        List<DocumentVo> documentVos = new ArrayList<DocumentVo>();
        try {
            UserVo userVo = userService.getLoginUser();


            //申请记录的待审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
                documentVos = documentService.getSqjldspList(userVo.getId(), "2");
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", documentVos).build());
                return;
            }

            //申请记录的已审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
                documentVos = documentService.getSqjlYspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", documentVos).build());
                return;
            }

            //申请记录的未提交
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "wtj")){
                documentVos=documentService.getDocumentByUserIdAndCondition(userVo.getId(),"1",lx);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", documentVos).build());
                return;
            }
            //发文审批已审批记录
            if (StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "ysp")){
                documentVos = documentService.getGwspYspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", documentVos).build());
                return;
            }
            if(StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "dsp")){
                documentVos = documentService.getGwspDspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取待审批记录成功", documentVos).build());
                return;
            }

        } catch (Exception e) {
            log.error("获取记录错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取记录出错").build());
        }
    }

    @GetMapping(value = "/fwtzTab")
    public void fwtzTab(@RequestParam(name = "lx", required = false)String lx,
                        HttpServletResponse response) throws IOException {
        if (StringUtil.isBlank(lx)) lx = "wd";
        try{
            UserVo userVo = userService.getLoginUser();
            String userName= userVo.getUsername();
            String role=userVo.getRoleName();
            List<FwtzVo> fwtzVoList=fwtzService.getFwtzByUserName(userName);

            if(StringUtil.equals(lx,"wd")){
                List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
                List<DocumentVo> documentVoList1=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
                // 合并了对同一个人的通知类型
                List<DocumentVo> documentVoList=fwtzService.getFwDocumentVoMerge(documentVoList1);
                List<DocumentVo> documentVoListBytzsj = fwtzService.sortDocumentListByTzsj(documentVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取未读发文通知成功", documentVoListBytzsj).build());
                return;
            }

            if(StringUtil.equals(lx,"yd")){
                List<FwtzVo> ydFwtzVoList = fwtzService.getYdFwtzList(fwtzVoList);
                List<DocumentVo> documentVoList1=fwtzService.getFwDocumentVo(ydFwtzVoList,role);
                List<DocumentVo> documentVoList=fwtzService.getFwDocumentVoMerge(documentVoList1);
                List<DocumentVo> documentVoListByydsj = fwtzService.sortDocumentListByYdsj(documentVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已读发文通知成功", documentVoListByydsj).build());
                return;
            }

        }catch(Exception e){
            log.error("获取发文通知错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取发文通知出错").build());
        }
    }

    @GetMapping(value = "/fwtzRecordTab")
    public void fwtzRecordTab(@RequestParam(name = "lx", required = false)String lx,
                        HttpServletResponse response) throws IOException {
        if (StringUtil.isBlank(lx)) lx = "wd";
        try{
            UserVo userVo = userService.getLoginUser();
            String fullName = userVo.getFullName();
            String userName= userVo.getUsername();
            String role=userVo.getRoleName();
            List<FwtzVo> fwtzVoList=fwtzService.getAllFwtz();
            if(StringUtil.equals(lx,"wd")){
                List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
                List<DocumentVo> documentVoList=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
                List<DocumentVo> documentVoListBytzsj = this.fwtzService.sortDocumentListByTzsj(documentVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取未读发文通知成功", documentVoListBytzsj).build());
                return;
            }

            if(StringUtil.equals(lx,"yd")){
                List<FwtzVo> ydFwtzVoList = fwtzService.getYdFwtzList(fwtzVoList);
                List<DocumentVo> documentVoList=fwtzService.getFwDocumentVo(ydFwtzVoList,role);
                List<DocumentVo> documentVoListByydsj = fwtzService.sortDocumentListByYdsj(documentVoList);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已读发文通知成功", documentVoListByydsj).build());
                return;
            }

        }catch(Exception e){
            log.error("获取发文通知记录错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取发文通知记录出错").build());
        }
    }

    /**
     * 申请记录详情(可编辑)
     */
    @RequestMapping("/sqjlxqE")
    public ModelAndView sqjlxqE(@RequestParam(name = "id", required = true)String documentId) {
        ModelAndView modelAndView = new ModelAndView(viewName + "d_apply_recordDetail");
        DocumentVo documentVo = documentService.getDocumentById(documentId);
        modelAndView.addObject("documentVo", documentVo);
        return modelAndView;
    }

    /**
     * 申请记录详情(不可编辑)
     */
    @RequestMapping("/sqjlxqNE")
    public ModelAndView sqjlxqNE(@RequestParam(name = "id", required = true)String documentId) {
        ModelAndView modelAndView;
        UserVo userVo = userService.getLoginUser();
        if(userVo.getDepartmentName().contains("集团部门")) {
            modelAndView = new ModelAndView(viewName + "d_apply_recordDetail_ne");
        }else{
            modelAndView = new ModelAndView(viewName + "d_apply_recordDetail_ne_zgs");
        }
//        ModelAndView modelAndView = new ModelAndView(viewName + "d_apply_recordDetail_ne");
        DocumentVo documentVo = documentService.getDocumentById(documentId);
        FwtzVo fwtzVo = fwtzService.getFwtzVoByDocumentId(documentId);
        modelAndView.addObject("documentVo", documentVo);
        modelAndView.addObject("fwtzVo", fwtzVo);
        return modelAndView;
    }

    /**
     * 打印发文表单
     * @param documentId
     * @return
     */
    @RequestMapping("/printTg")
    public ModelAndView printTg(@RequestParam(name = "id", required = true)String documentId) {
        ModelAndView modelAndView = new ModelAndView(viewName + "d_apply_print");
        DocumentVo documentVo = documentService.getDocumentById(documentId);
        String sprName = userService.getUsernameById(documentVo.getSpr().getId());
        FwtzVo fwtzVo = fwtzService.getFwtzVoByDocumentIdDy(documentId);
        String zs=fwtzService.getZsNameByFwtzVo(fwtzVo);
        String cs=fwtzService.getCsNameByFwtzVo(fwtzVo);
        String cb=fwtzService.getCbNameByFwtzVo(fwtzVo);
        modelAndView.addObject("documentVo", documentVo);
        modelAndView.addObject("fwtzVo", fwtzVo);
        modelAndView.addObject("zs",zs);
        modelAndView.addObject("cb",cb);
        modelAndView.addObject("cs",cs);
        modelAndView.addObject("sprName",sprName);
        return modelAndView;
    }

    /**
     *
     *  documentId是fwtz的fwid
     * fwtzid是fwtz对象的id
     * @return
     */
    @RequestMapping("/fwtzNE")
    public ModelAndView fwtzNE(@RequestParam(name = "id", required = true)String documentId,
                               @RequestParam(name = "fwtzid", required=true)String fwtzid){
        ModelAndView modelAndView = new ModelAndView(viewName + "d_fw_notify_xq");
        UserVo userVo = userService.getLoginUser();
        String roleName = userVo.getRoleName();
//        if(roleName.contains("公司高管") || roleName.contains("公司部门")){
//            modelAndView = new ModelAndView(viewName + "d_gsfw_notify_xq");
//        }

        DocumentVo documentVo = fwtzService.getDocumentById(documentId);
        documentVo.setFwtzid(fwtzid);
        FwtzVo fwtzVo= fwtzService.getFwtzVo(fwtzid);

        boolean isJtJyzy = fwtzService.isJtJyzy(fwtzVo);

        if (isJtJyzy == false){
            modelAndView = new ModelAndView(viewName + "d_gsfw_notify_xq");
        }

        modelAndView.addObject("documentVo", documentVo);
        modelAndView.addObject("fwtzVo",fwtzVo);
        return modelAndView;
    }
    /**
     *发文通知
     * @param documentId
     * @return
     */
    @RequestMapping("/notify")
    public ModelAndView notify(@RequestParam(name = "id", required = true) String documentId) {
        ModelAndView modelAndView;
        UserVo userVo = userService.getLoginUser();
        if(userVo.getDepartmentName().contains("集团部门")) {
            modelAndView = new ModelAndView(viewName + "d_apply_notify");
        }else{
            modelAndView = new ModelAndView(viewName + "d_gs_notify");
        }
        DocumentVo documentVo = documentService.getDocumentById(documentId);
        List<NotifyObjectVo> notifyObjectVoList = notifyObjectService.getAllNotifyObject();
        modelAndView.addObject("notifyObjectVoList",notifyObjectVoList);
        modelAndView.addObject("documentVo",documentVo);
        return modelAndView;
    }

//    @RequestMapping("/gsnotify")
//    public ModelAndView gsnotify(@RequestParam(name = "id", required = true) String documentId) {
//        ModelAndView modelAndView = new ModelAndView(viewName + "d_gs_notify");
//        DocumentVo documentVo = documentService.getDocumentById(documentId);
//        List<NotifyObjectVo> notifyObjectVoList = notifyObjectService.getAllNotifyObject();
//        modelAndView.addObject("documentVo",documentVo);
//        modelAndView.addObject("notifyObjectVoList",notifyObjectVoList);
//        return modelAndView;
//    }
    /**
     * 公文的提交
     * @throws IOException
     *
     */
    @PostMapping("/submitWtj")
    public void submitWtj(String id,
                          @RequestParam(name = "fileId" ,required = false) String[] attachmentId,
                          HttpServletResponse response) throws IOException{

        try{
            DocumentVo documentVo = documentService.getDocumentById(id);
            if(documentVo == null){
                MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该公文").build());
                return;
            }
//            String nextCondition = lcService.getNextCondition(contractVo.getLcbs(), contractVo.getHtshzt());
            documentVo.setGwshzt("2");
            documentService.updateDocument(documentVo, attachmentId);
            documentService.addXzLcrz(documentVo.getId(), "2");
        }catch(Exception e) {
            log.error("以保存公文提交时报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "公文提交出错，请重试").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "公文提交完成").build());
    }

    /**
     * 以保存未提交的公文的删除
     * @param id
     * @param response
     * @throws IOException
     */
    @PostMapping("/deleteWjt")
    public void deleteWjt(String id, HttpServletResponse response) throws IOException {
        try{
            if(StringUtil.isBlank(id)){
                MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "公文删除失败， 要删除的公文id为空").build());
                return;
            }
            documentService.delete(id);
        }catch(Exception e) {
            log.error("以保存公文删除时报错", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "公文删除失败").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "公文删除完成").build());
    }

    /**
     * 编号回填
     * @param documentId
     * @param gwbh
     * @param response
     * @throws IOException
     */
    @PostMapping("/bhht")
    public void bhht(@RequestParam(name = "id", required = true) String documentId,
                     @RequestParam(name = "gwbh" ,required = true) String gwbh,
                     @RequestParam(name = "yfrq", required=true) String yfrq,
                     HttpServletResponse response) throws IOException{
                         try{
                             DocumentVo documentVo = documentService.getDocumentById(documentId);
                             if(documentVo == null){
                                 MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该公文").build());
                                 return;
                             }
                             documentVo.setHtbh(gwbh);
                             documentVo.setYfrq(yfrq);
                             documentService.updateDocument(documentVo, null);
                         }
                         catch(Exception e){
                             this.log.error("打印公文报错");
                             MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "打印公文报错").build());
                         }
                         MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "打印公文成功").build());

    }


    /**
     * 保存发文通知
     * @param fwtzVo
     * @param
     * @param
     * @throws IOException
     */
    @RequestMapping(value = "savenotify",method = RequestMethod.POST)
    @ResponseBody
    public ResultVo save(FwtzVo fwtzVo, String id) {
//        System.out.println(fwtzVo.getZsqtgsbgs());
        ResultVo resultVo = new ResultVo();
        String objectRepeated;
        try {
            objectRepeated = fwtzService.saveFwtz(fwtzVo, id);
        } catch (Exception e) {
            this.log.error("保存发文通知报错", e);
            resultVo.setResult(false);
            resultVo.setMessage("保存发文通知报错");
            return resultVo;
        }
        resultVo.setResult(true);
        resultVo.setMessage("保存发文通知成功");
        resultVo.setObj(objectRepeated);
        return resultVo;
  }

    @RequestMapping(value ="/fwtz",method = RequestMethod.GET)
    public ModelAndView fwtz(){
        UserVo userVo = userService.getLoginUser();
        ModelAndView modelAndView=new ModelAndView(viewName+"d_fw_notify_all");
        String userName= userVo.getUsername();
        String fullName = userVo.getFullName();
        String role = userVo.getRoleName();
        List<FwtzVo> fwtzVoList=fwtzService.getFwtzByUserName(userName);
        List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
        List<DocumentVo> documentVoList1=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
        List<DocumentVo> documentVoList=fwtzService.getFwDocumentVoMerge(documentVoList1);
        List<DocumentVo> documentVoListBytzsj = fwtzService.sortDocumentListByTzsj(documentVoList);
        modelAndView.addObject("documentVoListBytzsj",documentVoListBytzsj);
        return modelAndView;
    }

    /**
     * 更新已读发文通知
     * @param
     * @param response
     * @throws IOException
     */
    @PostMapping("/updateFwtz")
    public void updateFwtz(@RequestParam(name="fwtzid", required=true) String fwtzid,
                           HttpServletResponse response) throws IOException{
        FwtzVo fwtzVo = fwtzService.getFwtzVoById(fwtzid);
        List<FwtzVo> fwtzVos = fwtzService.getFwtzsByFwidAndTzdx(fwtzVo.getFwid(),fwtzVo.getTzdx());
        if(fwtzVo == null){
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该发文通知").build());
            return;
        }
        for (FwtzVo fwtzVo1: fwtzVos) {
            fwtzService.updateFwtz(fwtzVo1);
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "发文已读成功").build());


    }
    @RequestMapping(value="/fwtzjl",method=RequestMethod.GET)
    public ModelAndView fwtzjl(){
        UserVo userVo = userService.getLoginUser();
        String role=userVo.getRoleName();
        ModelAndView modelAndView=new ModelAndView(viewName+"d_notify_record");
        List<FwtzVo> fwtzVoList=fwtzService.getAllFwtz();
        List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
        List<DocumentVo> documentVoList=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
        List<DocumentVo> documentVoListBytzsj = fwtzService.sortDocumentListByTzsj(documentVoList);
        modelAndView.addObject("documentVoListBytzsj",documentVoListBytzsj);
        return modelAndView;

    }
}

