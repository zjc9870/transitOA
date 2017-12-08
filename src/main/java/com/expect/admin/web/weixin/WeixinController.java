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
import com.expect.admin.data.dataobject.WxUser;
import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.FwtzService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.WxCpService;
import com.expect.admin.service.WxUserService;
import com.expect.admin.service.vo.AttachmentVo;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.FwtzVo;
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
	WeixinContractController weixinContractController;

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

    @Autowired
    private FwtzService fwtzService;
	
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
//		   String deviceId = res[1];
		   return login(wxId,request,response);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	//从数据库中获取用户名密码并登陆
	private ModelAndView login(String wxId,HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println(wxId);
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
	    	response.addCookie(wxIdCookie);
	    	String state = request.getParameter("state");
	    	return home(state);
	    }else{
	    	ModelAndView mv = new ModelAndView(viewName+"homepage");
			String state = request.getParameter("state");
			String url = wxService.oauth2buildAuthorizationUrl(state);
			mv.addObject("url", url);
			mv.addObject("fail", "true");
			return mv;
	    }
	}
	
	//服务定位
	private ModelAndView home(String state) {
		if(state.equals("submit")){
    		ContractVo contractVo = new ContractVo();
    		UserVo userVo = userService.getLoginUser();
    		contractVo.setUserName(userVo.getFullName());
    		ModelAndView mv = new ModelAndView("redirect:/weixin/contract/submit");
    		mv.addObject("contractVo", contractVo);
    		return mv;
    	}else if(state.equals("submit_record")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/contract/submit_record");
    		return mv;
    	}else if(state.equals("approve")){
//    		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
//    		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
    		ModelAndView mv = new ModelAndView("redirect:/weixin/contract/approve");
//    		mv.addObject("roleName", roleVo.getName());
    		return mv;
    	}else if(state.equals("documentApprove")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/document/approve");
    		return mv;
    	}else if(state.equals("backfill")){
    		ModelAndView mv = new ModelAndView(viewName + "contract_backfill");
    		return mv;
    	}else if(state.equals("documentApply")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/document/document_apply");
    		return mv;
    	}else if(state.equals("draftSwInstruction")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/draftSw/draftSw_list");
    		return mv;
    	}else if(state.equals("draftSwIncomming")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/draftSw/draftSw_incomming");
    		return mv;
    	}else if(state.equals("documentNotifyRecord")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/document/document_notify_record");
    		return mv;
    	}else if(state.equals("draftSwInstructions")){
    		ModelAndView mv = new ModelAndView("redirect:/weixin/draftSw/draftSw_instructions");
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
//		    System.out.println("msg_signature:"+msgSignature);
//		    System.out.println("nonce:"+nonce);
//		    System.out.println("timestamp:"+timestamp);
//		    System.out.println("echostr:"+echostr);
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
	
	@RequestMapping("/contract/submit_record")
	public ModelAndView submit_record() {
		ModelAndView mv = new ModelAndView(viewName + "contract/contract_submit_record");
		return mv;
	}

	//无cookies则访问微信服务器拿用户信息
	@RequestMapping("/authorize")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			if(cookies.length>=1){
				return login(cookies[0].getValue(),request, response);
			}
		}
		ModelAndView mv = new ModelAndView(viewName+"homepage");
		String state = request.getParameter("state");
		String url = wxService.oauth2buildAuthorizationUrl(state);
		mv.addObject("url", url);
		mv.addObject("fail", "false");
		return mv;
	}
	
	@RequestMapping("/homepage")
	public ModelAndView homepage(){
		ModelAndView mv = new ModelAndView(viewName+"homepage");
		mv.addObject("url", wxService.oauth2buildAuthorizationUrl("draftSw"));
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
	

	
	@RequestMapping("/document/document_apply")
	public ModelAndView document_apply() {
		ModelAndView mv = new ModelAndView(viewName + "document/document_apply");
		return mv;
	}
	
	
	@RequestMapping("/contract/submit")
	public ModelAndView submit(){
		ContractVo contractVo = new ContractVo();
		UserVo userVo = userService.getLoginUser();
		contractVo.setUserName(userVo.getFullName());
		ModelAndView mv = new ModelAndView(viewName+"contract/contract_submit");
		mv.addObject("contractVo", contractVo);
		return mv;
	}
	
	@RequestMapping("/document/document_notify_record")
	public ModelAndView document_notify_record() {
        UserVo userVo = userService.getLoginUser();
        String role=userVo.getRoleName();
        ModelAndView modelAndView=new ModelAndView(viewName+"document/document_notify_record");
        List<FwtzVo> fwtzVoList=fwtzService.getAllFwtz();
        List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
        List<DocumentVo> documentVoList=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
        List<DocumentVo> documentVoListBytzsj = fwtzService.sortDocumentListByTzsj(documentVoList);
        modelAndView.addObject("documentVoListBytzsj",documentVoListBytzsj);
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

}
