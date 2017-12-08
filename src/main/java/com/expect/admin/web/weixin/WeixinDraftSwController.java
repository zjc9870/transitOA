package com.expect.admin.web.weixin;

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

import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.DraftSwService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;


@Controller
@RequestMapping("/weixin/draftSw")
public class WeixinDraftSwController {
	private final Logger log = LoggerFactory.getLogger(WeixinController.class);

	private final String viewName = "weixin/draftSw/";
	
	//收文批示
	@Autowired
	UserService userService;
	
	@Autowired
	DraftSwService draftSwService;
	//领导批示
	/**
	 * 收文批示
	 * @return
	 */
	@RequestMapping(value = "/swPs", method = RequestMethod.GET)
	public ModelAndView swPs(@RequestParam(name = "id", required = true)String draftSwId,@RequestParam(name = "lx", required = true)String lx) {
		DraftSwVo draftSwVo = draftSwService.getDraftSwVoById(draftSwId);
		ModelAndView mv = new ModelAndView(viewName + "draftSw_instructions_detail");
		mv.addObject("draftSwVo", draftSwVo);

		mv.addObject("lx", lx);
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
			if(StringUtil.contains(ym, "cl")){
				//获取首页需要拿到的快捷导航数据
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
	
	
	@RequestMapping("/draftSw_instructions")
	public ModelAndView draftSw_list() {
		ModelAndView mv = new ModelAndView(viewName + "draftSw_instructions");
		return mv;
	}
	
	@RequestMapping("/draftSw_incomming")
	public ModelAndView draftSw_incomming() {
		ModelAndView mv = new ModelAndView(viewName + "draftSw_incomming");
		return mv;
	}
	
	@RequestMapping("/draftSw_detail")
	public ModelAndView draftSw_detail() {
		ModelAndView mv = new ModelAndView(viewName + "draftSw_detail");
		return mv;
	}
}
