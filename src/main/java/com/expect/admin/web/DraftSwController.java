package com.expect.admin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expect.admin.service.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.config.Settings;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.DmbService;
import com.expect.admin.service.DraftSwService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping(value = "/admin/draftSw")
public class DraftSwController {
private final Logger log = LoggerFactory.getLogger(DraftSwController.class);
	
	@Autowired
	private DraftSwService draftSwService;
//	@Autowired
//	private  LcService lcService;
	@Autowired
	private UserService userService;
	@Autowired
	private DmbService dmbService;
	@Autowired
	private RoleService roleService;
	@Autowired
    private Settings settings;
	@Autowired
	private AttachmentService attachmentService;
//	@Autowired
//	private DraftSwUserLcrzbGxbRepository draftSwUserLcrzbGxbRepository;
	
	private final String viewName = "admin/draftSw/";
	
	/**
	 * 上级来文 显示收文信息填写界面
	 * 设置收文时间（当前时间） 收文人（当前登录用户）
	 * 还没有做
	 * @return
	 */
	@RequestMapping(value = "/addSw", method = RequestMethod.GET)
	public ModelAndView addSw() {
		UserVo user = userService.getLoginUser();
		//从代码表中找到收文中“领导” 角色的id
		DmbVo dmbVo = dmbService.getDmbVoByLbbhAndDmbh("draftSw", "ldjsId");
		//获取领导的列表
		List<UserVo> ldList = roleService.getUserOfRole(dmbVo.getDmms());
		ModelAndView mv = new ModelAndView(viewName + "s_incoming");
		UserVo resultLd = null;
		for(UserVo ld:ldList){
			if(ld.getSsgsId().equals(user.getSsgsId())){
				resultLd = ld;
			}
		}
		mv.addObject("ld", resultLd);
		return mv;
	}
	//保存收文
	/**
	 * 如果收文的内容为空 就返回错误代码 “保存空的收文失败“ 并记录日志
	 * 如果是保存 {
	 * 	1.将收文的状态设置为收文流程的开始状态
	 * 	2.收文的分类设置为“未提交 W”
	 * }
	 * 如果提交{
	 * 	1.ldId(领导Id)不能为空 
	 * 	2.将收文的状态设置为 开始状态的下一个状态（领导审批）
	 * 	3.收文分类设置为“第一次 提交：Y”
	 * 	4.保存一条DraftSwUserLcrzbGxb记录
	 * } 
	 * 保存收文
	 * 返回保存结果
	 * @param draftSwVo
	 * @param bczl
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveSw", method = RequestMethod.POST)
	public void saveSw(DraftSwVo draftSwVo,
			@RequestParam(name = "ldId", required = false) String ldId,
			@RequestParam(name = "bczl", required = true)String bczl,
			 @RequestParam(name = "fileId" ,required = false) String[] attachmentIds, 
			HttpServletResponse response) throws IOException{
		if(draftSwVo==null){
			log.error("试图保存空收文");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的收文失败！").build());
			return;
		}
		String message = StringUtil.equals(bczl, "tj") ? "收文提交":"收文保存";
		try{
			boolean sftj = StringUtil.equals(bczl, "tj");//是否是提交
			draftSwService.saveANewDraftSw(sftj, draftSwVo, ldId, attachmentIds);
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false,  message + "失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true,  message + "成功！").build());
	}
	
	/**
     * 收文附件上传
     */
    @RequestMapping(value = "/uploadDraftSwAttachment", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo upload(MultipartFile files, HttpServletRequest request) {
        String path = settings.getAttachmentPath();
//      path = Base64Util.decode(path);
        FileResultVo frv = attachmentService.save(files, path);
        return frv;
    }
	
	/**
	 * 收文记录 直接返回页面
	 * @return
	 */
	@RequestMapping(value = "/swRecord", method = RequestMethod.GET)
	public ModelAndView swRecord() {
		ModelAndView mv = new ModelAndView(viewName + "s_records");
		UserVo userVo = userService.getLoginUser();
		List<DraftSwVo> draftSwList = draftSwService.getDraftSwVoList("swjl","dcl", userVo.getId());
		mv.addObject("draftSwVoList",draftSwList);
		return mv;
	}
	/**
     * 收文传阅列表界面  直接返回页面 不做数据请求（暂定）
     * @return
     */
    @RequestMapping(value = "/swCyRecord", method = RequestMethod.GET)
    public ModelAndView swCyRecord() {
        ModelAndView mv = new ModelAndView(viewName + "s_circulate");
        return mv;
    }
    /**
     * 收文领导批示列表页面 直接返回页面 不做数据请求（暂定）
     * @return
     */
    @RequestMapping(value = "/swPsRecord", method = RequestMethod.GET)
    public ModelAndView swPsRecord() {
        ModelAndView mv = new ModelAndView(viewName + "s_instructions");
        return mv;
    }
    /**
     * 收文办理列表页面 直接返回页面 不做数据请求（暂定）
     * @return
     */
    @RequestMapping(value = "/swBlRecord", method = RequestMethod.GET)
    public ModelAndView swblRecord() {
        ModelAndView mv = new ModelAndView(viewName + "s_handle");
        return mv;
    }
	
	
	/**
	 * 根据页面和tab标签获取各种收文数据（所有数据都会与当前用户相关的）
	 * 收文记录页面(ym = "swjl") 1.未提交("wtj") 2. 待处理("dcl") 3.已处理("ycl") 4.已完成("ywc")
	 * 收文批示页面(ym = "swps") 5.待批示("dps") 6.已批示("yps")
	 * 收文办理页面(ym = "swbl") 7.未办理("wbl") 8.已办理("ybl")
	 * 收文传阅页面(ym = "swcy")9.待传阅("dcy") 10.已传阅("ycy")
	 * @param ym 请求来自的页面
	 * @param tab 请求的tab
	 * @param response
	 */
	@PostMapping("/tabRequest")
	public void getDraftSwList(HttpServletResponse response,
			@RequestParam(name = "ym", required = true)String ym,
            @RequestParam(name = "tab", required = true)String tab) throws IOException {
		UserVo userVo = userService.getLoginUser();
		List<DraftSwVo> draftSwVoList = null;
		try{
			if(StringUtil.contains(tab, "homepage")){
				//获取首页需要拿到的快捷导航数据
				if(StringUtil.equals(tab,"dclhomepage")){
					tab="dcl";
				}else{
					tab="ycl";
				}
				draftSwVoList = draftSwService.getHomePageContent(ym,userVo.getId());
			}else{
				draftSwVoList = draftSwService.getDraftSwVoList(ym, tab, userVo.getId());
			}
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取列表内容出错").build());
			log.error("获取收文列表是出现错误 ym = " + ym + "  tab = " + tab + " userId = " + userVo.getId(), e);
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", draftSwVoList).build());
	}
	
	/**
	 * 添加参与人员
	 * @param draftSwId 相关收文的id（不能为空）
	 * @param cyrIdList 参与人的id的列表（不能为空）
	 * @param response
	 * @throws IOException 
	 */
	@PostMapping(value = "/addCyr")
	public void addCyr(
			@RequestParam(name = "draftSwId", required = true) String draftSwId,
			@RequestParam(name = "userIdList", required = true)List<String> cyrList,
			@RequestParam(name = "ryfl", required = true)String ryfl,
			HttpServletResponse response) throws IOException{
	    String message = "添加传阅人";
	    ArrayList<String> cyrIdList = new ArrayList<String>();
	    for(String cyrInfo:cyrList){
	    	cyrIdList.add(cyrInfo.split(",")[0]);
	    }
		try{
		    if(StringUtil.equals(ryfl, "chyr")){//传阅人
		        draftSwService.addCyr(cyrIdList, draftSwId);
		    }else if(StringUtil.equals(ryfl, "blr")) {//办理人
		        draftSwService.addBlr(cyrIdList, draftSwId);
		        message = "添加办理人";
		    }
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, message + "出错！").build());
			log.error(message + "出错收文id： " + draftSwId + "人员id " + cyrIdList.toString(), e);
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, message + "成功！").build());
	}
	
	
	/**
	 * 收文传阅
	 * 应该有一个错误页面
	 * @return
	 */
	@RequestMapping(value = "/swCy", method = RequestMethod.GET)
	public ModelAndView swCy(
			@RequestParam(name = "draftSwId", required = true)String draftSwId) {
		DraftSwVo draftSwVo = null;
		try{
			draftSwVo = draftSwService.getDraftSwVoById(draftSwId);
		}catch(Exception e) {
			log.error("收文传阅是获取收文记录失败 收文id" + draftSwId, e);
		}
		ModelAndView mv = new ModelAndView(viewName + "s_circulateDetail");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	
	/**
	 * 批阅 批阅要先对VO进行处理再根据是否所有批阅人都批阅完决定是否进入下一状态
	 * @param draftSwId 相应收文id
	 * @param cyrYj 传阅人id
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "/py" , method = RequestMethod.POST)
	public void py(@RequestParam(name = "draftSwId", required = true)String draftSwId,
			@RequestParam(name = "cyrYj" , required = false)String cyrYj, 
			HttpServletResponse response) throws IOException{
		UserVo loginUser = userService.getLoginUser();
		if(loginUser == null){
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "用户未登录，请先登录！").build());
			log.error("传阅审批时用户未登录收文id " + draftSwId);
			return;
		}
		try{
			draftSwService.swcy(draftSwId, cyrYj, loginUser.getId());
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "收文传阅审批失败！").build());
			log.error("收文传阅审批失败收文id： " + draftSwId, e);
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "收文传阅审批成功！").build());
	}
	
	/**
	 * 收文办理
	 * @return
	 */
	@RequestMapping(value = "/swBl", method = RequestMethod.GET)
	public ModelAndView swBl(
			@RequestParam(name = "draftSwId", required = true)String draftSwId) {
		DraftSwVo draftSwVo = null;
		try{
			draftSwVo = draftSwService.getDraftSwVoById(draftSwId);
		}catch(Exception e) {
			log.error("收文传阅是获取收文记录失败 收文id" + draftSwId, e);
		}
		ModelAndView mv = new ModelAndView(viewName + "s_handleDetail");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	
	//办理
	@RequestMapping(value = "/bl", method = RequestMethod.POST)
	public void bl(@RequestParam(name = "draftSwId", required = true)String draftSwId,
				   @RequestParam(name = "blqk", required = true)     String blqk, 
			       HttpServletResponse response) throws IOException{
		UserVo loginUser = userService.getLoginUser();
		if(loginUser == null){
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "用户未登录，请先登录！").build());
			log.error("收文办理审批时用户未登录收文id " + draftSwId);
			return;
		}
		try{
			draftSwService.swbl(draftSwId, blqk, loginUser.getId());
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "收文办理保存失败！").build());
			log.error("收文办理保存失败 收文id： " + draftSwId, e);
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "收文办理保存成功！").build());
	}
	
	
	/**
	 * 收文批示
	 * @return
	 */
	@RequestMapping(value = "/swPs", method = RequestMethod.GET)
	public ModelAndView swPs(
			@RequestParam(name = "draftSwId", required = true)String draftSwId) {
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(draftSwId);
		ModelAndView mv = new ModelAndView(viewName + "s_instructionsDetail");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	
	//领导批示
	/**
	 * @param ldps
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/ldps", method = RequestMethod.POST)
	public void saveSw(@RequestParam(name = "ldps", required = true)String ldps,
					   @RequestParam(name = "id", required = true)  String swId, 
			           HttpServletResponse response) throws IOException{
		if(StringUtil.isEmpty(ldps)){
			log.error("试图保存空领导批示");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的领导意见失败！").build());
			return;
		}
		String userId = userService.getLoginUser().getId();
		try{
			draftSwService.ldpscl(swId, ldps, userId);
		}catch(BaseAppException baseAppE) {
			log.error("保存领导批示出错", baseAppE);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, baseAppE.getMessage()).build());
		}catch(Exception e) {
			log.error("保存领导批示出错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存领导意见失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "保存批示成功！").build());
	}
	
	//收文结束
	@RequestMapping(value = "/terminate" , method = RequestMethod.POST)
	public void terminate(@RequestParam(name = "draftSwId", required = true)String draftSwId, HttpServletResponse response) throws IOException{
	    try{
	        draftSwService.terminate(draftSwId);
	    }catch(BaseAppException baseAppE) {
	        log.error("结束收文出错", baseAppE);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, baseAppE.getMessage()).build());
	    }
	    MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "结束收文成功！").build());
	}
	
	/**
	 * 收文查询
	 * @return
	 */
	@RequestMapping(value = "/swCx", method = RequestMethod.GET)
	public ModelAndView swCx() {
		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_find");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	//查询id
	@RequestMapping(value = "/cx", method = RequestMethod.GET)
	public ModelAndView findDraftSwById(@RequestParam(name = "id", required = true)String id){
		ModelAndView modelAndView = new ModelAndView();
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(id);
		modelAndView.addObject(draftSwVo);
		return modelAndView;
	}
	
	/**
	 * 收文记录详情
	 */
	@RequestMapping(value = "/swjlxq")
	public ModelAndView swjlxq(@RequestParam(name = "id", required = true)String swId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "s_recordsDetail");
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(swId);
		modelAndView.addObject("draftSwVo", draftSwVo);
		return modelAndView;
	}
	
	/**
	 * 收文记录详情(不可编辑)
	 */
	@RequestMapping(value = "/swjlxqNE")
	public ModelAndView swjlxqNE(@RequestParam(name = "id", required = true)String swId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "s_recordsDetail_ne");
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(swId);
		modelAndView.addObject("draftSwVo", draftSwVo);
		return modelAndView;
	}
	
	/**
	 * 收文记录未提交
	 */
	@RequestMapping(value = "/swjlWtj")
	public ModelAndView swjlWtj(@RequestParam(name = "id", required = true)String swId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "s_recordsWtj");
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(swId);
		DmbVo dmbVo = dmbService.getDmbVoByLbbhAndDmbh("draftSw", "ldjsId");
		List<UserVo> ldList = roleService.getUserOfRole(dmbVo.getDmms());
		UserVo resultLd = null;
		for(UserVo ld:ldList){
			if(ld.getSsgsId().equals(userService.getLoginUser().getSsgsId())){
				resultLd = ld;
			}
		}
		modelAndView.addObject("draftSwVo", draftSwVo);
		modelAndView.addObject("ld", resultLd);
		return modelAndView;
	}

	/**
	 * 打印收文表单
	 * @param
	 * @return
	 */
	@RequestMapping("/printTg")
	public ModelAndView printTg(@RequestParam(name = "id", required = true)String draftSwId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "s_print");
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(draftSwId);
		String CyAngBl = draftSwService.getCyAndBl(draftSwVo);
		List<LcrzbVo> ldRecordList = draftSwVo.getLdRecordList();
		LcrzbVo ld = ldRecordList.iterator().next();
		String LdName = ld.getUserName();
		String Ldps = ld.getMessage();
		modelAndView.addObject("draftSwVo",draftSwVo);
		modelAndView.addObject("CyAndBl",CyAngBl);
		modelAndView.addObject("ldname",LdName);
		modelAndView.addObject("Ldps",Ldps);
		return modelAndView;
	}

	/**
	 * 打印文号回填
	 */
	@RequestMapping("/dybh")
	public void bhht(@RequestParam(name = "id", required = true) String documentId,
					 @RequestParam(name = "gwbh" ,required = true) String gwbh,
					 @RequestParam(name = "yfrq", required=true) String yfrq,
					 HttpServletResponse response) throws IOException{
		try{
			DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(documentId);
			if(draftSwVo == null){
				MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该收文").build());
				return;
			}
			draftSwVo.setBh(gwbh);
			draftSwVo.setDyrq(yfrq);
			draftSwService.updateDyrqAndBh(draftSwVo);
		}
		catch(Exception e){
			this.log.error("打印公文报错");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "打印收文报错").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "打印收文成功").build());

	}



}
