package com.expect.admin.web.weixin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.config.WebSecurityConfig;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WxUser;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.FunctionService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.WxCpService;
import com.expect.admin.service.WxUserService;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.FunctionVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.IOUtil;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.RequestUtil;
import com.expect.admin.utils.StringUtil;
import com.expect.admin.weixin.common.exception.WxErrorException;
import com.expect.admin.weixin.cp.api.WxCpConfigStorage;
import com.expect.admin.weixin.cp.api.WxCpMessageRouter;
import com.expect.admin.weixin.cp.util.crypto.WxCpCryptUtil;


@Controller
@RequestMapping("/weixin")
public class WeixinController {
	private final Logger log = LoggerFactory.getLogger(WeixinController.class);

	private final String viewName = "weixin/";
	@Autowired
	WxCpService wxService;


	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	WxUserService wxUserService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	WebSecurityConfig webSecurityConfig;
	
	@Autowired
	RoleService roleService;
	
	WxCpMessageRouter wxCpMessageRouter = new WxCpMessageRouter(wxService);
	

	@Autowired
	RoleJdgxbGxbService roleJdgxbGxbService;
	
	//通过微信服务器的code来进行身份验证
	@RequestMapping("/login")
	protected ModelAndView loginByCode(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {

	    String code = request.getParameter("code");
	    try {
		   String[] res = wxService.oauth2getUserInfo(code);
		   String wxId = res[0];
		   String deviceId = res[1];
		   return login(wxId,deviceId,request,response);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	private ModelAndView login(String wxId,String deviceId,HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println(wxId+" "+deviceId);
	    if(wxUserService.isUserExisit(wxId)){
		    WxUser wxuser = wxUserService.findUserByWxid(wxId);
		    UserDetails detail = userService.loadUserByUsername(wxuser.getUserId());
		    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(detail,detail.getPassword(),detail.getAuthorities());
		    Authentication auth = webSecurityConfig.getDaoAuthenticationProvider().authenticate(authentication);
		    SecurityContextHolder.getContext().setAuthentication(auth);  
		    HttpSession session = request.getSession(true);  
		    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
		    	
	    	System.out.println(userService.getLoginUser().getId()+"登录成功");
	    	Cookie wxIdCookie = new Cookie("wxId",wxId);
	    	Cookie deviceIdCookie = new Cookie("deviceId",deviceId);
	    	response.addCookie(wxIdCookie);
	    	response.addCookie(deviceIdCookie);
	    	String state = request.getParameter("state");
	    	return home(state);
	    }else{
	    	response.getWriter().println("<h1>登录失败</h1>");
//	    	System.out.println("登录失败");
	    }
	    return null;
	}
	
	//服务定位
	private ModelAndView home(String state) {
		if(state.equals("submit")){
    		ContractVo contractVo = new ContractVo();
    		UserVo userVo = userService.getLoginUser();
    		contractVo.setUserName(userVo.getFullName());
    		ModelAndView mv = new ModelAndView(viewName+"contract_submit");
    		mv.addObject("contractVo", contractVo);
    		return mv;
    	}else if(state.equals("submit_record")){
    		ModelAndView mv = new ModelAndView(viewName+"contract_submit_record");
    		return mv;
    	}else if(state.equals("approve")){
    		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
    		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
    		ModelAndView mv = new ModelAndView(viewName + "contract_approve");
    		mv.addObject("roleName", roleVo.getName());
    		return mv;
    	}else if(state.equals("draftSw")){
    		ModelAndView mv = new ModelAndView(viewName+"draftSw_list");
    		return mv;
    	}else if(state.equals("backfill")){
    		ModelAndView mv = new ModelAndView(viewName + "contract_backfill");
    		return mv;
    	}
		return null;
	}
	
	//用于微信服务器验证
	@RequestMapping("/core")
	public void core(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
		 response.setContentType("text/html;charset=utf-8");
		    response.setStatus(HttpServletResponse.SC_OK);

		    String msgSignature = request.getParameter("msg_signature");
		    String nonce = request.getParameter("nonce");
		    String timestamp = request.getParameter("timestamp");
		    String echostr = request.getParameter("echostr");

		    if (StringUtils.isNotBlank(echostr)) {
		      if (!this.wxService.checkSignature(msgSignature, timestamp, nonce, echostr)) {
		        // 消息签名不正确，说明不是公众平台发过来的消息
		        response.getWriter().println("非法请求");
		        return;
		      }
		      WxCpCryptUtil cryptUtil = new WxCpCryptUtil(wxService.getWxCpConfig());
		      String plainText = cryptUtil.decrypt(echostr);
		      // 说明是一个仅仅用来验证的请求，回显echostr
		      response.getWriter().println(plainText);
		      return;
		    }

//		    WxCpXmlMessage inMessage = WxCpXmlMessage
//		            .fromEncryptedXml(request.getInputStream(), this.wxCpConfigStorage, timestamp, nonce, msgSignature);
//		    WxCpXmlOutMessage outMessage = this.wxCpMessageRouter.route(inMessage);
//		    if (outMessage != null) {
//		      response.getWriter().write(outMessage.toEncryptedXml(this.wxCpConfigStorage));
//		    }

		    return;
	}
	
	//查看收文细节
	@RequestMapping(value = "/draftSwDetail")
	public ModelAndView draftSwDetail(){
		ModelAndView mv = new ModelAndView(viewName+"draftSw_detail");
		return mv;
	}
	

	/**
	 * 附件下载
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(String id, HttpServletResponse response, HttpServletRequest request) {
		if (StringUtils.isEmpty(id)) {
			return;
		}
		AttachmentVo attachment = attachmentService.getAttachmentById(id);
		if (attachment != null) {
			String path = attachment.getPath() + File.separator + attachment.getId();
			byte[] buffer = IOUtil.inputDataFromFile(path);
			try {
				RequestUtil.downloadFile(buffer, attachment.getName(), response, request);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("/submit_record")
	public ModelAndView submit_record() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_record");
		return mv;
	}


	@RequestMapping("/authorize")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			if(cookies.length>1){
				return login(cookies[1].getValue(),cookies[0].getValue(),request, response);
			}
		}
		ModelAndView mv = new ModelAndView(viewName+"homepage");
		String state = request.getParameter("state");
		String url = wxService.oauth2buildAuthorizationUrl(state);
		mv.addObject("url", url);
		return mv;
	}
	
	//测试专用接口 部署时请删除
	
	@RequestMapping("/homepage")
	public ModelAndView homepage(){
		ModelAndView mv = new ModelAndView(viewName+"homepage");
		mv.addObject("url", wxService.oauth2buildAuthorizationUrl("draftSw"));
		return mv;
	}
	
	@RequestMapping("/approve")
	public ModelAndView approve() {

		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
		ModelAndView mv = new ModelAndView(viewName + "contract_approve");
		mv.addObject("roleName", roleVo.getName());
		return mv;
	}

	@RequestMapping("/submit_detail")
	public ModelAndView submit_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_detail");
		return mv;
	}

	@RequestMapping("/approve_detail")
	public ModelAndView approve_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_approve_detail");
		return mv;
	}
	
	@RequestMapping("/backfill")
	public ModelAndView backfill() {
		ModelAndView mv = new ModelAndView(viewName + "contract_backfill");
		return mv;
	}

	@RequestMapping("/success")
	public ModelAndView success() {
		ModelAndView mv = new ModelAndView(viewName + "success");
		return mv;
	}
	
	@RequestMapping("/draftSw_list")
	public ModelAndView draftSw_list() {
		ModelAndView mv = new ModelAndView(viewName + "draftSw_list");
		return mv;
	}
	
	@RequestMapping("/submit")
	public ModelAndView submit(){
		ContractVo contractVo = new ContractVo();
		UserVo userVo = userService.getLoginUser();
		contractVo.setUserName(userVo.getFullName());
		ModelAndView mv = new ModelAndView(viewName+"contract_submit");
		mv.addObject("contractVo", contractVo);
		return mv;
	}
	
	
	//合同
	//保存合同
	@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
	public void saveContract(ContractVo contractVo, 
			                 @RequestParam(name = "bczl", required = true)   String bczl,
			                 @RequestParam(name = "fileId" ,required = false) String[] attachmentId, 
			                 HttpServletResponse response) throws IOException {
		if(!contractCheck(contractVo, response)) return;
		String message = StringUtil.equals(bczl, "tj") ? "合同申请" : "合同保存";
		contractVo.setNqdrq(dateFormat(contractVo.getNqdrq()));
		contractVo.setQx(dateFormat(contractVo.getQx()));
		try{
			contractService.newContractSave(contractVo, bczl, attachmentId);
//			log.info("合同申请 sbd" + contractVo.getHtbt());
//			log.info("合同申请 sbd" + contractVo.getSbd());
		}catch(Exception e) {
			log.error("保存合同报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, message + "失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, message + "成功！").build());
	}
	
	private String dateFormat(String sj) {
		Date date = DateUtil.parse(sj, "yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.zbFormat);
		return simpleDateFormat.format(date);
	}
	
	private String dateFormat_2(String sj) {
		Date date = DateUtil.parse(sj, DateUtil.zbFormat);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 判断新申请的合同是不是满足要求
	 * 1.合同不能为空
	 * 2.合同的期限必须在拟签订日期之后
	 * @param contractVo
	 * @param response
	 * @return 满足全部要求返回true， 否则返回false
	 * @throws IOException
	 */
	private boolean  contractCheck(ContractVo contractVo, HttpServletResponse response) throws IOException {
		if(contractVo == null) {
			log.error("试图保存空的合同");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "申请失败，合同内容为空！").build());
			return false;
		}
		Date nqdrq = DateUtil.parse(contractVo.getNqdrq(), "yyyy-mm-dd");
		Date qx = DateUtil.parse(contractVo.getQx(), "yyyy-mm-dd");
		if(DateUtil.getDiffSeconds(qx, nqdrq) < 0) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "申请失败，合同期限必须在拟签订日期之后！").build());
			return false;
		}
		return true;
	}

	//获取合同申请记录
	@GetMapping(value = "/sqjlTab")
	public void sqjlTab(@RequestParam(name = "lx", required = false)String lx,
			            @RequestParam(name = "bz", required = false)String bz, 
			HttpServletResponse response) throws IOException {
		if(StringUtil.isBlank(lx)) lx = "wtj";
		List<ContractVo> contractVoList =  new ArrayList<ContractVo>();
		try{
			UserVo userVo = userService.getLoginUser();
			RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt(bz, "ht");
			
			//申请记录的已审批
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
				contractVoList = contractService.getSqjlYspList(userVo.getId());
				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
			
			//申请记录的待审批
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
				contractVoList = contractService.getSqjlWspList(userVo.getId(), condition.getJdId());
				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
			
			//已回填
			if(StringUtil.equals(lx, "yht")) {
				contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
						"T", lx);
			}else{
				contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
						condition.getJdId(), lx);
			}
		}catch(Exception e) {
//			e.printStackTrace();
			log.error("获取申请记录错误" + lx, e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取申请记录出错").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
		
	}
	
	
	/**
	 * 合同申请记录详情(可编辑)
	 */
	@RequestMapping("/sqjlxqE")
	public ModelAndView sqjlxqE(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "contract_submit_detail");
		ContractVo contractVo = contractService.getContractById(contractId);
		contractVo.setNqdrq(dateFormat_2(contractVo.getNqdrq()));
		contractVo.setQx(dateFormat_2(contractVo.getQx()));
		System.out.println(contractVo.getHtnr());
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 合同申请记录详情(不可编辑)
	 */
	@RequestMapping("/sqjlxqNE")
	public ModelAndView sqjlxqNE(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "contract_submit_detail");
		ContractVo contractVo = contractService.getContractById(contractId);
		contractVo.setNqdrq(dateFormat_2(contractVo.getNqdrq()));
		contractVo.setQx(dateFormat_2(contractVo.getQx()));
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 合同审批查看详情
	 * @return
	 */
	@RequestMapping("/htspckxq")
	public ModelAndView htspckxq(@RequestParam(name = "id", required = true)String contractId,@RequestParam(name = "lx", required = true)String lx){
		ModelAndView modelAndView = new ModelAndView(viewName + "contract_approve_detail");
		ContractVo contractVo = contractService.getContractById(contractId);
		contractVo.setNqdrq(dateFormat_2(contractVo.getNqdrq()));
		contractVo.setQx(dateFormat_2(contractVo.getQx()));
		modelAndView.addObject("contractVo", contractVo);
		modelAndView.addObject("lx", lx);
		return modelAndView;
	}
	
	/**
	 * 合同审批
	 */
	@GetMapping("/htsp")
	public ModelAndView htsp(@RequestParam(name = "lx", required = false)String lx) {
		UserVo userVo = userService.getLoginUser();
		if(StringUtil.isBlank(lx)) lx = "dsp";
		ModelAndView modelAndView = new ModelAndView(viewName + "contract_approve");
		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		if(condition == null) return modelAndView;
		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
		String roleName = roleVo.getName();
		modelAndView.addObject("xsth", sfxsTab(roleName, "yth"));
		modelAndView.addObject("roleName", roleVo.getName());
		System.out.println(roleVo.getName());
		modelAndView.addObject("contractVoList", 
				contractService.getContractByUserIdAndCondition(userVo.getId(),
						condition.getJdId(), lx));
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
		System.out.println(cljg);
		//插入流程日志
		//判断审核是否通过 如果通过更新文件状态到下一个状态
		//如果不通过更新文件状态到退回状态，修改流程日志表中的审批记录以后不再显示退回状态之后的审批记录
		if(message == null) message  ="";
		try{
			contractService.saveContractLcrz(cljg, message, clnrid, "ht");
		}catch(Exception e) {
			log.error("合同审核失败", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同审核失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同审核成功！").build());
		
	}
	
	/**
	 * 判断合同列表查看某些tab是否显示
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
	 * 合同编号回填详情
	 */
	@PostMapping("/bhhtxq")
	public ModelAndView bhhtxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "contract_backfill_detail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 附件下载
	 */
//	@RequestMapping(value = "/download", method = RequestMethod.GET)
//	public void download(String id, HttpServletResponse response) {
//		if (StringUtils.isEmpty(id)) {
//			return;
//		}
//		AttachmentVo attachment = attachmentService.getAttachmentById(id);
//		if (attachment != null) {
//			String path = attachment.getPath() + File.separator + attachment.getId();
//			byte[] buffer = IOUtil.inputDataFromFile(path);
//			try {
//				RequestUtil.downloadFile(buffer, attachment.getName(), response);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
	@RequestMapping("/submit_record")
	public ModelAndView submit_record() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_record");
		return mv;
	}

	@RequestMapping("/approve")
	public ModelAndView approve() {

		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
		ModelAndView mv = new ModelAndView(viewName + "contract_approve");
		mv.addObject("roleName", roleVo.getName());
		return mv;
	}

	@RequestMapping("/submit_detail")
	public ModelAndView submit_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_submit_detail");
		return mv;
	}

	@RequestMapping("/approve_detail")
	public ModelAndView approve_detail() {
		ModelAndView mv = new ModelAndView(viewName + "contract_approve_detail");
		return mv;
	}
	
	@RequestMapping("/backfill")
	public ModelAndView backfill() {
		ModelAndView mv = new ModelAndView(viewName + "contract_backfill");
		return mv;
	}

	@RequestMapping("/success")
	public ModelAndView success() {
		ModelAndView mv = new ModelAndView(viewName + "success");
		return mv;
	}
	
	@RequestMapping("/submit")
	public ModelAndView submit(){
		ContractVo contractVo = new ContractVo();
		UserVo userVo = userService.getLoginUser();
		contractVo.setUserName(userVo.getFullName());
		ModelAndView mv = new ModelAndView(viewName+"contract_submit");
		mv.addObject("contractVo", contractVo);
		return mv;
	}
	
	@RequestMapping("/authorize")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			if(cookies.length>1){
				return login(cookies[1].getValue(),cookies[0].getValue(),request, response);
			}
		}
		String htsp = wxService.oauth2buildAuthorizationUrl("approve");
		String htcx = wxService.oauth2buildAuthorizationUrl("search");
		ModelAndView mv = new ModelAndView(viewName+"homepage");
		mv.addObject("htsp", htsp);
		mv.addObject("htcx", htcx);
		return mv;
	}
}
