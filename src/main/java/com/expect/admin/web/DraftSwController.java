package com.expect.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.DraftSwService;
import com.expect.admin.service.LcService;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.ResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping(value = "/admin/draftSw")
public class DraftSwController {
private final Logger log = LoggerFactory.getLogger(ContractController.class);
	
	@Autowired
	private DraftSwService draftSwService;
	@Autowired
	private  LcService lcService;
	
	private final String viewName = "admin/draftSw/";
	
	//保存收文
	@RequestMapping(value = "/saveSw", method = RequestMethod.POST)
	public void saveSw(DraftSwVo draftSwVo,@RequestParam(name = "bczl", required = true)String bczl,HttpServletResponse response) throws IOException{
		if(draftSwVo==null){
			log.error("试图保存空收文");
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的合同失败！").build());
			return;
		}
		String startCondition = lcService.getStartCondition("收文流程");
		String condition;
		if(StringUtil.equals(bczl, "tj")){
			condition = lcService.getNextCondition("4", startCondition);
		}else condition = startCondition;
		draftSwVo.setZt(condition);
		draftSwService.save(draftSwVo);
		return;
	}
	
	//领导批示
	@RequestMapping(value = "/ldps", method = RequestMethod.POST)
	public void saveSw(@RequestParam(name = "ldps", required = true)String ldps,@RequestParam(name = "id", required = true)String id, HttpServletResponse response) throws IOException{
		if(StringUtil.isEmpty(ldps)){
			log.error("试图保存空领导批示");
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的领导意见失败！").build());
			return;
		}
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(id);
		draftSwVo.setLdps(ldps);
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwService.update(draftSwVo);
		return;
	}
	
	//查询id
	@RequestMapping(value = "/cx", method = RequestMethod.POST)
	public ModelAndView findDraftSwById(@RequestParam(name = "id", required = true)String id){
		ModelAndView modelAndView = new ModelAndView();
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(id);
		modelAndView.addObject(draftSwVo);
		return modelAndView;
	}
	
	//添加批阅人
	@RequestMapping(value = "/addPyr" , method = RequestMethod.POST)
	public void addPyr(DraftSwVo draftSwVo,@RequestParam(name = "userIdList", required = true)List<String> pyrIdList,HttpServletResponse response){
		
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwService.addPyr(pyrIdList, draftSwVo);
	}
	
	//添加办理人
	@RequestMapping(value = "/addBlr" , method = RequestMethod.POST)
	public void addBlr(DraftSwVo draftSwVo,@RequestParam(name = "userId", required = true)String blrId ,HttpServletResponse response){
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwService.addBlr(blrId, draftSwVo);
	}
	
	//批阅 批阅要先对VO进行处理再根据是否所有批阅人都批阅完决定是否进入下一状态
	@RequestMapping(value = "/py" , method = RequestMethod.POST)
	public void py(DraftSwVo draftSwVo,@RequestParam(name = "userId" , required = true)String pyrId , HttpServletResponse response){
		if(draftSwService.py(draftSwVo.getId(), pyrId)){
			String currentCondition = draftSwVo.getZt();
			String condition = lcService.getNextCondition("4", currentCondition);
			draftSwVo.setZt(condition);
			draftSwService.update(draftSwVo);
		}
	}
	
	//办理
	@RequestMapping(value = "/bl", method = RequestMethod.POST)
	public void bl(DraftSwVo draftSwVo,@RequestParam(name = "blqk", required = true)String blqk, HttpServletResponse response){
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwVo.setBlqk(blqk);
		draftSwService.update(draftSwVo);
	}
	
	//返回或结束,设置一下流程节点
	@RequestMapping(value = "/fh" , method = RequestMethod.POST)
	public void fh(DraftSwVo draftSwVo,@RequestParam(name = "nextStep", required = true)String nextStep, HttpServletResponse response){
		if(nextStep.equals("fh")){
			String condition = lcService.getJd("4", "选择传阅人");
			draftSwVo.setZt(condition);
		}else if(nextStep.equals("js")){
			String condition = "Y";
			draftSwVo.setZt(condition);
		}
		
		draftSwService.save(draftSwVo);
	}

}
