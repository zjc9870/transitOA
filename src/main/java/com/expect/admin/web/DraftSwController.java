package com.expect.admin.web;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;
import com.expect.admin.service.DraftSwService;
import com.expect.admin.service.LcService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping(value = "/admin/draftSw")
public class DraftSwController {
private final Logger log = LoggerFactory.getLogger(DraftSwController.class);
	
	@Autowired
	private DraftSwService draftSwService;
	@Autowired
	private  LcService lcService;
	@Autowired
	private UserService userService;
	
	private final String viewName = "admin/draftSw/";
	
	/**
	 * 上级来文 显示收文信息填写界面
	 * 设置收文时间（当前时间） 收文人（当前登录用户）
	 * 还没有做
	 * @return
	 */
	@RequestMapping(value = "/addSw", method = RequestMethod.GET)
	public ModelAndView addSw() {
		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_incoming");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	//保存收文
	/**
	 * 如果收文的内容为空 就返回错误代码 “保存空的收文失败“ 并记录日志
	 * 如果是保存 {
	 * 1.将收文的状态设置为收文流程的开始状态
	 * 2. 收文的分类设置为“未提交 W”
	 * }
	 * 如果提交{
	 * 1.将收文的状态设置为 开始状态的下一个状态（领导审批）
	 * 2.收文分类设置为“第一次 提交：Y”
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
			@RequestParam(name = "bczl", required = true)String bczl,
			HttpServletResponse response) throws IOException{
		if(draftSwVo==null){
			log.error("试图保存空收文");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的收文失败！").build());
			return;
		}
		String message = StringUtil.equals(bczl, "tj") ? "收文提交":"收文保存";
		try{
			String startCondition = lcService.getStartCondition("4");
			String condition;
			if(StringUtil.equals(bczl, "tj")){
				condition = lcService.getNextCondition("4", startCondition);
			}else condition = startCondition;
			draftSwVo.setZt(condition);
			draftSwService.save(draftSwVo);
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false,  message + "失败！").build());
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true,  message + "成功！").build());
	}
	
	/**
	 * 收文记录 直接返回页面 不做数据请求（暂定）
	 * @return
	 */
	@RequestMapping(value = "/swRecord", method = RequestMethod.GET)
	public ModelAndView swRecord() {
//		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_records");
//		UserVo userVo = userService.getLoginUser();
//		List<DraftSwVo> draftSwVoList = draftSwService.getDraftSwVoByUserAndCondition(userVo.getId(), condition);
//		mv.addObject("draftSwVoList", draftSwVoList);
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
			draftSwVoList = draftSwService.getDraftSwVoList(ym, tab, userVo.getId());
		}catch(Exception e) {
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取列表内容出错").build());
			log.error("获取收文列表是出现错误 ym = " + ym + "  tab = " + tab + " userId = " + userVo.getId(), e);
		}
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", draftSwVoList).build());
	}
	
	//领导批示
	@RequestMapping(value = "/ldps", method = RequestMethod.POST)
	public void saveSw(@RequestParam(name = "ldps", required = true)String ldps,
					   @RequestParam(name = "id", required = true)  String id, 
			           HttpServletResponse response) throws IOException{
		if(StringUtil.isEmpty(ldps)){
			log.error("试图保存空领导批示");
			MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "保存空的领导意见失败！").build());
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
	public void addPyr(DraftSwVo draftSwVo,
			@RequestParam(name = "userIdList", required = true)List<String> pyrIdList,
			HttpServletResponse response){
		
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwService.addPyr(pyrIdList, draftSwVo);
	}
	
	//添加办理人
	@RequestMapping(value = "/addBlr" , method = RequestMethod.POST)
	public void addBlr(DraftSwVo draftSwVo,
			@RequestParam(name = "userId", required = true)String blrId ,
			HttpServletResponse response){
		String currentCondition = draftSwVo.getZt();
		String condition = lcService.getNextCondition("4", currentCondition);
		draftSwVo.setZt(condition);
		draftSwService.addBlr(blrId, draftSwVo);
	}
	
	//批阅 批阅要先对VO进行处理再根据是否所有批阅人都批阅完决定是否进入下一状态
	@RequestMapping(value = "/py" , method = RequestMethod.POST)
	public void py(DraftSwVo draftSwVo,
			@RequestParam(name = "userId" , required = true)String pyrId , 
			HttpServletResponse response){
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
	
	
	/**
	 * 申请记录
	 */
//	@GetMapping(value = "/sqjl")
//	public ModelAndView sqjl(@RequestParam(name = "lx", required = false)String lx) {
//		if(StringUtil.isBlank(lx)) lx = "wtj";
//		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_record");
//		UserVo userVo = userService.getLoginUser();
//		RoleJdgx bGxbVo condition = roleJdgxbGxbService.getWjzt("sq", "ht");
//		modelAndView.addObject("contractVoList", 
//				contractService.getContractByUserIdAndCondition(userVo.getId(), condition.getJdId(), lx));
//		return modelAndView;
//	}
	
	/**
	 * 收文批示
	 * @return
	 */
	@RequestMapping(value = "/swPs", method = RequestMethod.GET)
	public ModelAndView swPs() {
		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_instructions");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	
	/**
	 * 收文传阅
	 * @return
	 */
	@RequestMapping(value = "/swCy", method = RequestMethod.GET)
	public ModelAndView swCy() {
		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_circulate");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
	}
	
	/**
	 * 收文办理
	 * @return
	 */
	@RequestMapping(value = "/swBl", method = RequestMethod.GET)
	public ModelAndView swBl() {
		DraftSwVo draftSwVo = new DraftSwVo();
		ModelAndView mv = new ModelAndView(viewName + "s_handle");
		mv.addObject("draftSwVo", draftSwVo);
		return mv;
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

}
