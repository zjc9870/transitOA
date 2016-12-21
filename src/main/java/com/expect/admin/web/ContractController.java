package com.expect.admin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.expect.admin.utils.Base64Util;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.ResponseBuilder;
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
	@PostMapping(value = "/htspckxq")
	public ModelAndView htspckxq(@RequestParam(name = "id", required = true)String contractId){
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approveDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
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
		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sq", "ht");
		List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
				condition.getJdId(), lx);
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
			RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt(bz, "ht");
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
				contractVoList = contractService.getSqjlYspList(userVo.getId());
				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
			if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
				contractVoList = contractService.getSqjlWspList(userVo.getId(), condition.getJdId());
				MyResponseBuilder.writeJsonResponse(response, 
						JsonResult.useDefault(true, "获取申请记录成功", contractVoList).build());
				return;
			}
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
	 * 合同审批
	 */
	@GetMapping(value = "/htsp")
	public ModelAndView htsp(@RequestParam(name = "lx", required = false)String lx) {
		UserVo userVo = userService.getLoginUser();
		if(StringUtil.isBlank(lx)) lx = "dsp";
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve");
		RoleJdgxbGxbVo condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		if(condition == null) return modelAndView;
		List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
				condition.getJdId(), lx);
		//
		RoleVo roleVo = roleService.getRoleById(condition.getRoleId());
		modelAndView.addObject("role", roleVo.getName());
//		List<ContractVo> contractVoList = new ArrayList<>();
		modelAndView.addObject("contractVoList", contractVoList);
		return modelAndView;
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
		List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
			"Y",lx);
		modelAndView.addObject("contractVoList", contractVoList);
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
	@PostMapping("/bhhtxq")
	public ModelAndView bhhtxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfillDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
//	/**
//	 * 回填记录详情
//	 */
//	@PostMapping("/htjlxq")
//	public ModelAndView htjlxq(@RequestParam(name = "id", required = true)String contractId) {
//		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill_recordDetail");
//		ContractVo contractVo = contractService.getContractById(contractId);
//		modelAndView.addObject("contractVo", contractVo);
//		return modelAndView;
//	}
	
	/**
	 * 合同查询
	 */
	@PostMapping("/htcx")
	public ModelAndView htcx() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_find");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
	public void saveContract(ContractVo contractVo, 
			                 @RequestParam(name = "bczl", required = true)   String bczl,
			                 @RequestParam(name = "fileId" ,required = false) String[] attachmentId, 
			                 HttpServletResponse response) throws IOException {
		if(contractVo == null) {
			log.error("试图保存空的合同");
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的合同失败！").build());
			return;
		}
		try{
			String htfl = contractService.getHtfl();
			contractVo.setHtfl(htfl);
			String lcbs = lcService.getDefaultLc(htfl);
			String condition;
			String startCondition = lcService.getStartCondition(lcbs);
			if(StringUtil.equals(bczl, "tj")){
				condition = lcService.getNextCondition(lcbs, startCondition);
			}else condition = startCondition;
			contractVo.setHtshzt(condition);//合同审核状态
			contractVo.setLcbs(lcbs);//流程标识
			contractService.save(contractVo, attachmentId);
		}catch(Exception e) {
//			e.printStackTrace();
			log.error("保存合同报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存合同失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "保存合同内容成功！").build());
	}
	
	@RequestMapping(value = "/updateContract", method = RequestMethod.POST)
	public void updateContract(ContractVo contractVo, HttpServletResponse response) throws IOException {
		try{
			contractService.updateContract(contractVo);
		}catch(Exception e) {
			log.error("更新合同内容报错", e);
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "更新合同内容失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "更新合同内容成功！").build());
	}
	
	@RequestMapping(value = "/getContract", method = RequestMethod.POST)
	public ModelAndView getContract(@RequestParam(name = "id", required = true)String contractId){
		ModelAndView mv = new ModelAndView(viewName + "contractForm");
		ContractVo contractVo = contractService.getContractById(contractId);
		mv.addObject("contractVo", contractVo);
		return mv;
	}
	
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
	 * 编号回填
	 * @param response
	 * @param bh
	 * @param id
	 * @throws IOException 
	 */
	@RequestMapping(value = "/bhht", method = RequestMethod.POST)
	public void bhht(HttpServletResponse response,
			@RequestParam(name = "bh", required = false)String bh,
			@RequestParam(name = "id", required = true) String id) throws IOException{
		ContractVo contractVo = contractService.getContractById(id);
		contractVo.setBh(bh);
		contractVo.setHtshzt("T");
		contractService.updateContract(contractVo);
		lcrzbService.save(new LcrzbVo("编号回填", bh), id, contractVo.getHtfl(), "T");
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同编号回填成功！").build());
	}
	
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
	
	@PostMapping("/searchContract")
	public void contractSearchResult(HttpServletResponse response,
			@RequestParam(name = "htbt", required = false)String htbt,
			@RequestParam(name = "htbh", required = false)String htbh,
			@RequestParam(name = "startTime", required = false)Date startTime,
			@RequestParam(name = "endTime", required = false)Date endTime,
			@RequestParam(name = "htzt", required = false)String htzt,
			@RequestParam(name = "fqr", required = false)String fqr){
		
	}
}
