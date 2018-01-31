package com.expect.admin.web;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expect.admin.data.dataobject.RoleJdgxbGxb;
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
import com.expect.admin.service.ContractService;
import com.expect.admin.service.LcService;
import com.expect.admin.service.LcrzbService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.FileResultVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping(value = "/admin/contract")
public class ContractController {
	private final Logger log = LoggerFactory.getLogger(ContractController.class);
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private LcrzbService lcrzbService;
	@Autowired
	private  LcService lcService;
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
	
	private final String viewName = "admin/contract/";

	/**
	 * 合同申请界面
	 * @return
	 */
	@RequestMapping(value = "/addContract", method = RequestMethod.GET)
		public ModelAndView addContract() {
			ContractVo contractVo = new ContractVo();
			UserVo userVo = userService.getLoginUser();
			contractVo.setUserName(userVo.getFullName());
			ModelAndView mv = new ModelAndView(viewName + "c_apply");
			mv.addObject("contractVo", contractVo);
			return mv;
	}
	
	/**
	 * 合同审批查看详情
	 * @return
	 */
	@GetMapping(value = "/htspckxq")
	public ModelAndView htspckxq(@RequestParam(name = "id", required = true)String contractId){
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approveDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		UserVo userVo = userService.getLoginUser();
		modelAndView.addObject("contractVo", contractVo);
		modelAndView.addObject("userVo",userVo);
		return modelAndView;
	}
	
	/**
	 * 申请记录
	 */
	@GetMapping(value = "/sqjl")
	public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx) {
		if(StringUtil.isBlank(lx)) lx = "wtj";
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_record");
		UserVo userVo = userService.getLoginUser();
		List<RoleJdgxbGxbVo> condition = roleJdgxbGxbService.getWjzt("sq", "ht");
		List<ContractVo> contractVoList = new ArrayList<>();
		for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
			List<ContractVo> contractVos = contractService.getContractByUserIdAndCondition(userVo.getId(), roleJdgxbGxbVo.getJdId(), lx);
			contractVoList.addAll(contractVos);
		}
		modelAndView.addObject("contractVoList", contractVoList);
		return modelAndView;
	}
	/**
	 * 申请记录tab 合同审批tab
	 * @throws IOException 
	 */
	@GetMapping(value = "/sqjlTab")
	public void sqjlTab(@RequestParam(name = "lx", required = false)String lx,
			            @RequestParam(name = "bz", required = false)String bz, 
			HttpServletResponse response) throws IOException {
		if(StringUtil.isBlank(lx)) lx = "wtj";
		List<ContractVo> contractVoList =  new ArrayList<ContractVo>();
		try{
			UserVo userVo = userService.getLoginUser();
			List<RoleJdgxbGxbVo> condition = roleJdgxbGxbService.getWjzt(bz, "ht");
			
			//申请记录的已审批
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
				contractVoList = contractService.getSqjlYspList(userVo.getId());
				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
			
			//申请记录的待审批
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
				for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
					List<ContractVo> contractVos = contractService.getSqjlWspList(userVo.getId(), roleJdgxbGxbVo.getJdId());
					contractVoList.addAll(contractVos);
				}

				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
			
			//已回填
			if(StringUtil.equals(lx, "yht")) {
				contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
						"T", lx);
			}else{
				for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
					List<ContractVo> contractVos = contractService.getContractByUserIdAndCondition(userVo.getId(),
							roleJdgxbGxbVo.getJdId(), lx);
					contractVoList.addAll(contractVos);

				}
				contractVoList = contractService.deleteRepeatedContract(contractVoList);
			}
		}catch(Exception e) {
//			e.printStackTrace();
			log.error("获取申请记录错误" + lx, e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取申请记录出错").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
		
	}
	
	/**
	 * 合同审批列表页面
	 */
	@GetMapping(value = "/htsp")
	public ModelAndView htsp(@RequestParam(name = "lx", required = false)String lx) {
		UserVo userVo = userService.getLoginUser();
		if(StringUtil.isBlank(lx)) lx = "dsp";
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve");
		List<RoleJdgxbGxbVo> condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		if(condition == null) return modelAndView;
		List<ContractVo> contractVos = new ArrayList<>();
		String roleName ="";
		for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
			List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
					roleJdgxbGxbVo.getJdId(), lx);
			contractVos.addAll(contractVoList);
			RoleVo roleVo = roleService.getRoleById(roleJdgxbGxbVo.getRoleId());
			roleName =roleName+" "+roleVo.getName();

		}
		modelAndView.addObject("xsth", sfxsTab(roleName, "yth"));
		modelAndView.addObject("roleName", roleName);

		modelAndView.addObject("contractVoList", contractVos);
		return modelAndView;
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
	 * 编号回填
	 */
	@RequestMapping("/getBhhtList")
	public ModelAndView getBhhtList(@RequestParam(name = "lx", required = false)String lx) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill");
		if(StringUtil.isBlank(lx)) lx = "dht";
		UserVo userVo = userService.getLoginUser();
//		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionName("编号回填");
		modelAndView.addObject("contractVoList", 
				contractService.getContractByUserIdAndCondition(userVo.getId(),
						"Y",lx));
		return modelAndView;
	}
	
	
	/**
	 * 申请记录详情(可编辑)
	 */
	@RequestMapping("/sqjlxqE")
	public ModelAndView sqjlxqE(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_recordDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 申请记录详情(不可编辑)
	 */
	@RequestMapping("/sqjlxqNE")
	public ModelAndView sqjlxqNE(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_recordDetail_ne");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 编号回填详情
	 */
	@GetMapping("/bhhtxq")
	public ModelAndView bhhtxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfillDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 保存，提交合同
	 * @param contractVo
	 * @param bczl
	 * @param attachmentId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
	public void saveContract(ContractVo contractVo, 
			                 @RequestParam(name = "bczl", required = true)   String bczl,
			                 @RequestParam(name = "fileId" ,required = false) String[] attachmentId, 
			                 HttpServletResponse response) throws IOException {
		if(!contractCheck(contractVo, response)) return;
		String message = StringUtil.equals(bczl, "tj") ? "合同申请" : "合同保存";
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
		Date nqdrq = DateUtil.parse(contractVo.getNqdrq(), DateUtil.zbFormat);
//		Date qx = DateUtil.parse(contractVo.getQx(), DateUtil.zbFormat);
		String qx = contractVo.getQx();
//		if(DateUtil.getDiffSeconds(qx, nqdrq) < 0) {
//			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "申请失败，合同期限必须在拟签订日期之后！").build());
//			return false;
//		}
		return true;
	}
	
	@RequestMapping(value = "/updateContract", method = RequestMethod.POST)
	public void updateContract(ContractVo contractVo, 
			@RequestParam(name = "fileId" ,required = false) String[] attachmentId,
			HttpServletResponse response) throws IOException {
		try{
			contractService.updateContract(contractVo, attachmentId);
		}catch(Exception e) {
			log.error("更新合同内容报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "更新合同内容失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "更新合同内容成功！").build());
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
			contractService.saveContractLcrz(cljg, message, clnrid, "ht");
		}catch(Exception e) {
			log.error("合同审核失败", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同审核失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同审核成功！").build());
		
	}
	
	/**
	 * 撤销合同（只有合同申请人可以撤销）
	 * @param response
	 * @param contractId 要撤销的合同的id
	 * @param revocationReason 撤销合同理由（最多为100个字）
	 * @throws IOException 
	 */
	@PostMapping("/revocationContract")
	public void revocationContract(HttpServletResponse response, 
            @RequestParam(name = "id", required = true)String contractId,
            @RequestParam(name = "revocationReason", required = true)String revocationReason) throws IOException {
	    try{
	        if(revocationReason.length() > 100){  
	            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "撤销理由过长，最多100个字").build());
	        }
	        else contractService.revocationContract(contractId, revocationReason);
	    }catch(BaseAppException be) {
	        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, be.getMessage()).build());
	        log.error("撤销合同是失败，合同id为" + contractId, be);
	    }catch(Exception e) {
	        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "撤销合同失败").build());
            log.error("撤销合同是失败，合同id为" + contractId, e);
	    }
	    MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "撤销合同成功").build());
	}
	
	/**
	 * 编号回填
	 * 1.合同编号最多20个字符，并且唯一
	 * 2.编号回填后修改合同状态为T（已回填）
	 * 3.增加编号回填流程日志
	 * @param response
	 * @param bh
	 * @param id
	 * @throws IOException 
	 */
	@RequestMapping(value = "/bhht", method = RequestMethod.POST)
	public void bhht(HttpServletResponse response,
			@RequestParam(name = "bh", required = true)String bh,
			@RequestParam(name = "id", required = true) String id) throws IOException{
		if(bh.length() > 20){ 
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同编号过长，最多20个字符！").build());
			return;
		}
		if(!contractService.isHtbhUnique(bh)) { 
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同编号已存在，请重新填写！").build());
			return;
		}
		ContractVo contractVo = contractService.getContractById(id);
//		if(StringUtil.equals(contractVo.getHtshzt(), "T"))
		contractVo.setBh(bh);
		contractVo.setHtshzt("T");
		contractService.updateContract(contractVo, null);
		String lcrzId = lcrzbService.save(new LcrzbVo("编号回填", bh), id, "ht", "T");
		contractService.bindContractWithLcrz(contractVo.getId(), lcrzId);
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同编号回填成功！").build());
	}
	
	/**
	 * 删除已提交的合同（软删除）
	 * @param response
	 * @param id
	 * @throws IOException
	 */
	@PostMapping("/deleteContract")
	public void deleteContract(HttpServletResponse response,
			                   @RequestParam(name = "id", required = true) String id) throws IOException{
		try{
			contractService.deleteContract(id);
		}catch(Exception e){
			log.error("删除合同失败未找到id为" + id + "的合同", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到要删除的合同！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "删除的合同成功！").build());
	}
	
	/**
	 * 合同附件上传
	 */
	@RequestMapping(value = "/uploadContractAttachment", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo upload(MultipartFile files, HttpServletRequest request) {
		String path = settings.getAttachmentPath();
//		path = Base64Util.decode(path);
		FileResultVo frv = attachmentService.save(files, path);
		return frv;
	}
	
	/**
	 * 以保存合同的提交
	 * @throws IOException 
	 * 
	 */
	@PostMapping("/submitWtj")
	public void submitWtj(String id, 
			@RequestParam(name = "fileId" ,required = false) String[] attachmentId,
			HttpServletResponse response) throws IOException{
		
		try{
			ContractVo contractVo = contractService.getContractById(id);
			if(contractVo == null){
				MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "未找到该合同").build());
				return;
			}
			String condition =  contractVo.getHtshzt();
			String nextCondition = lcService.getNextCondition(contractVo.getLcbs(), condition);
			contractVo.setHtshzt(nextCondition);
			contractService.updateContract(contractVo, attachmentId);
			contractService.addXzLcrz(contractVo.getId(), contractVo.getHtfl(), condition);
		}catch(Exception e) {
			log.error("以保存合同提交时报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同提交出错，请重试").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同提交完成").build());
	}
	
	/**
	 * 以保存未提交的合同的删除
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/deleteWjt")
	public void deleteWjt(String id, HttpServletResponse response) throws IOException {
		try{
			if(StringUtil.isBlank(id)){
				MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同删除失败， 要删除的合同id为空").build());
				return;
			}
			contractService.delete(id);
		}catch(Exception e) {
			log.error("以保存合同提交时报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同删除失败").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同删除完成").build());
	}
	
	/**
	 * 合同查询
	 */
	@GetMapping("/htcx")
	public ModelAndView htcx() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_find");
		
		return modelAndView;
	}
	
	/**
	 * 
	 * @param response
	 * @param htbt 合同标题
	 * @param htbh 合同编号
	 * @param startTime 开始时间（搜索的时间段，以申请时间为准）
	 * @param endTime 结束时间
	 * @param htzt 合同状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
	 * @param fqr 发起人（合同的申请人）姓名
	 * @throws IOException
	 */
	@PostMapping("/searchContract")
	public void contractSearchResult(HttpServletResponse response,
			@RequestParam(name = "htbt", required = false)String htbt,
			@RequestParam(name = "htbh", required = false)String htbh,
			@RequestParam(name = "startTime", required = false)String startTime,
			@RequestParam(name = "endTime", required = false)String endTime,
			@RequestParam(name = "htzt", required = false)String htzt,
			@RequestParam(name = "fqr", required = false)String fqr) throws IOException{
		Date start = null,end = null;
		List<ContractVo> contractVoList = null;
		try{
			if(!StringUtil.isBlank(startTime)) start = DateUtil.parse(startTime, DateUtil.zbFormat);
			if(!StringUtil.isBlank(endTime)) end = DateUtil.parse(endTime, DateUtil.zbFormat);
			contractVoList = contractService.searchContract(htbt, htbh, start, end, htzt, fqr, "");
		}catch(Exception e) {
			log.error("合同查询失败", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "搜索合同失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", contractVoList).build());
	}
	
	/**
	 * 个人使用的部分合同查询
	 * 只能查询与当前登录用户相关（当前用户参与了合同的申请或审批）的合同
	 * @param htbt 合同标题
	 * @param htbh 合同编号
	 * @param startTime 开始时间（搜索的时间段，以申请时间为准）
	 * @param endTime 结束时间
	 * @param htzt 合同状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
	 * @param fqr 发起人（合同的申请人）姓名
	 * @throws IOException
	 */
	@PostMapping("/searchContractOfUser")
	public void contractSearchResultOfUser(HttpServletResponse response,
				@RequestParam(name = "htbt", required = false)String htbt,
				@RequestParam(name = "htbh", required = false)String htbh,
				@RequestParam(name = "startTime", required = false)String startTime,
				@RequestParam(name = "endTime", required = false)String endTime,
				@RequestParam(name = "htzt", required = false)String htzt,
				@RequestParam(name = "fqr", required = false)String fqr) throws IOException {
			
			UserVo userVo = userService.getLoginUser();//获取当前登录用户
			Date start = null,end = null;
			List<ContractVo> contractVoList = null;
			try{
				if(!StringUtil.isBlank(startTime)) start = DateUtil.parse(startTime, DateUtil.zbFormat);
				if(!StringUtil.isBlank(endTime)) end = DateUtil.parse(endTime, DateUtil.zbFormat);
				contractVoList = contractService.searchContract(htbt, htbh, start, end, htzt, fqr, userVo.getId());
			}catch(Exception e) {
				log.error("合同查询失败", e);
				MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "搜索合同失败！").build());
			}
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", contractVoList).build());
		
	}
	
	/**
	 * 搜索页面查看详情
	 * @param id
	 * @param response
	 * @return
	 */
	@GetMapping("/ssxq")
	public ModelAndView getSsxq(String id, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView(viewName + "c_findDetail");
		ContractVo contractVo = contractService.getContractById(id);
		mv.addObject("contractVo", contractVo);
		return mv;
	}
	
	/**
	 * @param attachmentId
	 * @param contractId
	 * @param reponse
	 * @return
	 */
	@GetMapping("/contractAttachmentDownload")
	public String contractAttachmentDownload(
	        @RequestParam(name = "attachmentId", required = true)String attachmentId,
	        @RequestParam(name = "contractId", required = true)String contractId,
	        HttpServletResponse reponse) {
	    UserVo userVo = userService.getLoginUser();
	    if(contractService.attachmentDownloadAuthorityJudgement(contractId, userVo.getId())){
	        return "forward:/admin/attachment/download?id=" + attachmentId;
	    }
	    return "forward:/admin/attachment/downloadAttachmentAsPdf?id=" + attachmentId;
	}

	@RequestMapping(value = "/processConfiguration", method = RequestMethod.GET)
	public ModelAndView processConfiguration(){
		ModelAndView mv = new ModelAndView(viewName+"configuration");
		return mv;
	}
}
