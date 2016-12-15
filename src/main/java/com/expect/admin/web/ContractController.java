package com.expect.admin.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.ContractService;
import com.expect.admin.service.FunctionJdgxbGxbService;
import com.expect.admin.service.LcService;
import com.expect.admin.service.LcrzbService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.FunctionJdgxbGxbVo;
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
	private FunctionJdgxbGxbService functionJdgxbGxbService;
	
	private final String viewName = "admin/system/contract/";
	
	@RequestMapping(value = "/addContract", method = RequestMethod.GET)
	public ModelAndView addContract() {
		return new ModelAndView(viewName + "contractForm");
	}
	
	@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
	public void saveContract(ContractVo contractVo, HttpServletResponse response) throws IOException {
		if(contractVo == null) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的合同失败！").build());
			return;
		}
		try{
			String startCondition = lcService.getStartCondition(contractVo.getHtfl());
			String lcbs = lcService.getDefaultLc(contractVo.getHtfl());
			contractVo.setHtshzt(startCondition);//合同审核状态
			contractVo.setLcbs(lcbs);//流程标识
			contractService.save(contractVo);
		}catch(Exception e) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存合同失败！").build());
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "保存合同成功！").build());
	}
	
	@RequestMapping(value = "/updateContract", method = RequestMethod.POST)
	public void updateContract(ContractVo contractVo, HttpServletResponse response) throws IOException {
		try{
			contractService.updateContract(contractVo);
		}catch(Exception e) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "更新合同内容失败！").build());
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "更新合同内容成功！").build());
	}
	
	@RequestMapping(value = "/getContractList", method = RequestMethod.POST)
	@ResponseBody
	public List<ContractVo> getContractList(
			@RequestParam(name = "fucntionId", required = true)String functionId,
			@RequestParam(name = "lx", required = true)String lx,
			@RequestParam(name = "startTime", required = false)Date start,
			@RequestParam(name = "endTime", required = false)Date end) {
		UserVo userVo = userService.getLoginUser();
		FunctionJdgxbGxbVo functionJdgxbGxbVo = functionJdgxbGxbService.getByFunctionId(functionId);
		return contractService.getContractByUserIdAndCondition(userVo.getId(),
				functionJdgxbGxbVo.getJdgxbId(), start, end, lx);
		
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
