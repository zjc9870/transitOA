package com.expect.admin.web.weixin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.config.WebSecurityConfig;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.WxCpService;
import com.expect.admin.service.WxUserService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;
import com.expect.admin.weixin.cp.api.WxCpMessageRouter;


@Controller
@RequestMapping("/weixin/contract")
public class WeixinContractController {
	private final Logger log = LoggerFactory.getLogger(WeixinController.class);

	private final String viewName = "weixin/contract/";
	@Autowired
	WxCpService wxService;


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
	
	//合同
	//保存合同
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
					contractVoList = contractService.deleteRepeatedContract(contractVoList);
				}
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
	
	//合同审批列表
	@RequestMapping("/approve")
	public ModelAndView approve() {

		List<RoleJdgxbGxbVo> condition = roleJdgxbGxbService.getWjzt("sp", "ht");
		String roleName ="";
		for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
			RoleVo roleVo = roleService.getRoleById(roleJdgxbGxbVo.getRoleId());
			roleName = roleName+ " "+roleVo.getName();
		}

		ModelAndView mv = new ModelAndView(viewName + "contract_approve");
		mv.addObject("roleName", roleName);
		return mv;
	}
	
}
