package com.expect.admin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.AttachmentService;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.LcService;
import com.expect.admin.service.LcrzbService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.JsonResult;
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
	
	private final String viewName = "admin/contract/";
	
	/**
	 * 合同申请界面
	 * @return
	 */
	@RequestMapping(value = "/addContract", method = RequestMethod.GET)
	public ModelAndView addContract() {
//		ContractVo contractVo = new ContractVo();
//		UserVo userVo = userService.getLoginUser();
//		contractVo.setUserName(userVo.getFullName());
//		contractService.save(contractVo);
		ModelAndView mv = new ModelAndView(viewName + "c_apply");
//		mv.addObject("contractVo", contractVo);
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
	@PostMapping("/sqjl")
	public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx,
			@RequestParam(name = "startTime", required = false)Date start,
			@RequestParam(name = "endTime", required = false)Date end) {
		if(StringUtil.isBlank(lx)) lx = "wtj";
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_record");
		UserVo userVo = userService.getLoginUser();
//		String condition = roleJdgxbGxbService.getWjzt("", "");
//		String condition = lcService.getStartCondition(lcCategory);
//		List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
//				condition, start, end, lx);
		List<ContractVo> contractVoList = new ArrayList<>();
		modelAndView.addObject("contractVoList", contractVoList);
		return modelAndView;
	}
	/**
	 * 合同审批
	 */
	@RequestMapping("/htsp")
	public ModelAndView htsp(@RequestParam(name = "lx", required = false)String lx,
			@RequestParam(name = "startTime", required = false)Date start,
			@RequestParam(name = "endTime", required = false)Date end) {
		UserVo userVo = userService.getLoginUser();
		
		
		if(StringUtil.isBlank(lx)) lx = "dsp";
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve");
		//TODO
//		String condition = roleJdgxbGxbService.getWjzt("", "");
//		List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
//				condition, start, end, lx);
		List<ContractVo> contractVoList = new ArrayList<>();
		modelAndView.addObject("contractVoList", contractVoList);
		return modelAndView;
	}
	
	/**
	 * 审批记录  已审批的记录
	 */
//	@RequestMapping("/spjl")
//	public ModelAndView spjl(@RequestParam(name = "lx", required = false)String lx,
//			@RequestParam(name = "startTime", required = false)Date start,
//			@RequestParam(name = "endTime", required = false)Date end) {
//		if(StringUtil.isBlank(lx)) lx = "ysp";
//		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve_record");
//		UserVo userVo = userService.getLoginUser();
//		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionName("审批记录");
//		 List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
//				functionJdgxbGxbVo.getJdgxbId(), start, end, lx);
//		 modelAndView.addObject("contractVoList", contractVoList);
//		return modelAndView;
//	}
	
	/**
	 * 编号回填
	 */
	@RequestMapping("/bhht")
	public ModelAndView bhht(@RequestParam(name = "lx", required = false)String lx,
			@RequestParam(name = "startTime", required = false)Date start,
			@RequestParam(name = "endTime", required = false)Date end) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill");
		if(StringUtil.isBlank(lx)) lx = "dht";
		UserVo userVo = userService.getLoginUser();
//		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionName("编号回填");
		 List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
				"Y", start, end, lx);
		 modelAndView.addObject("contractVoList", contractVoList);
		return modelAndView;
	}
	
	/**
	 * 回填记录
	 */
//	@RequestMapping("/htjl")
//	public ModelAndView htjl(@RequestParam(name = "lx", required = false)String lx,
//			@RequestParam(name = "startTime", required = false)Date start,
//			@RequestParam(name = "endTime", required = false)Date end) {
//		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill_record");
//		if(StringUtil.isBlank(lx)) lx = "yht";
//		UserVo userVo = userService.getLoginUser();
//		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionName("回填记录");
//		 List<ContractVo> contractVoList = contractService.getContractByUserIdAndCondition(userVo.getId(),
//				functionJdgxbGxbVo.getJdgxbId(), start, end, lx);
//		 modelAndView.addObject("contractVoList", contractVoList);
//		return modelAndView;
//	}
	
	/**
	 * 申请记录详情
	 */
	@RequestMapping("/sqjlxq")
	public ModelAndView sqjlxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_recordDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 编号回填详情
	 */
	@RequestMapping("/bhhtxq")
	public ModelAndView bhhtxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfillDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 回填记录详情
	 */
	@PostMapping("/htjlxq")
	public ModelAndView htjlxq(@RequestParam(name = "id", required = true)String contractId) {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill_recordDetail");
		ContractVo contractVo = contractService.getContractById(contractId);
		modelAndView.addObject("contractVo", contractVo);
		return modelAndView;
	}
	
	/**
	 * 合同查询
	 */
	@PostMapping("/htcx")
	public ModelAndView htcx() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_find");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
	public void saveContract(ContractVo contractVo, @RequestParam(name = "bczl", required = true)String bczl,
			@RequestParam MultipartFile[] files, HttpServletResponse response) throws IOException {
		if(contractVo == null) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的合同失败！").build());
			return;
		}
		try{
			String lcbs = lcService.getDefaultLc(contractVo.getHtfl());
			String condition;
			if(StringUtil.equals(bczl, "tj")){
				String startCondition = lcService.getStartCondition(contractVo.getHtfl());
				condition = lcService.getNextCondition(lcbs, startCondition);
				
			}else condition = lcService.getStartCondition(contractVo.getHtfl());
			contractVo.setHtshzt(condition);//合同审核状态
			contractVo.setLcbs(lcbs);//流程标识
			String contractId = contractService.save(contractVo);
			//附件保存
			if(files != null){
				attachmentService.save(files, null, contractId);
			}
		}catch(Exception e) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存合同失败！").build());
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "保存合同成功！").build());
	}
	
//	@PostMapping(value = "/fileUpload")
//	public void uploadFiles(@RequestParam("file") MultipartFile file,
//							@RequestParam("id") String id, HttpServletResponse response) throws IOException {
//		String fileName = file.getName();
//		if(!fileName.endsWith("ceb") || !fileName.endsWith("doc") || !fileName.endsWith("pdf")){
//			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "文件上传失败，只能是ceb,pdf,doc个时的文件！").build());
//			return;
//		}
//		
//	}
	
	@RequestMapping(value = "/updateContract", method = RequestMethod.POST)
	public void updateContract(ContractVo contractVo, HttpServletResponse response) throws IOException {
		try{
			contractService.updateContract(contractVo);
		}catch(Exception e) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "更新合同内容失败！").build());
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "更新合同内容成功！").build());
	}
	
//	@RequestMapping(value = "/getContractList", method = RequestMethod.POST)
//	@ResponseBody
//	public List<ContractVo> getContractList(
//			@RequestParam(name = "fucntionId", required = true)String functionId,
//			@RequestParam(name = "lx", required = true)String lx,
//			@RequestParam(name = "startTime", required = false)Date start,
//			@RequestParam(name = "endTime", required = false)Date end) {
//		UserVo userVo = userService.getLoginUser();
//		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionId(functionId);
//		return contractService.getContractByUserIdAndCondition(userVo.getId(),
//				functionJdgxbGxbVo.getJdgxbId(), start, end, lx);
//		
//	}
	
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
			@RequestParam(name = "yj", required = false)String message,
			@RequestParam(name = "id", required = true)String clnrid,
			@RequestParam(name = "clnrfl", required = true)String clnrfl) throws IOException{
		//插入流程日志
		//判断审核是否通过 如果通过更新文件状态到下一个状态
		//如果不通过更新文件状态到退回状态，修改流程日志表中的审批记录以后不再显示退回状态之后的审批记录
		if(message == null) message  ="";
		try{
			contractService.saveContractLcrz(cljg, message, clnrid, clnrfl);
		}catch(Exception e) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "合同审核失败！").build());
			log.error("合同审核失败", e);
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同审核成功！").build());
		
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
			@RequestParam(name = "id", required = true)String id) throws IOException{
		ContractVo contractVo = contractService.getContractById(id);
		contractVo.setBh(bh);
		contractVo.setHtshzt("T");
		contractService.updateContract(contractVo);
		lcrzbService.save(new LcrzbVo(), id, contractVo.getHtfl(), "T");
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "合同编号回填成功！").build());
	}
}
