package com.expect.admin.web;

import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.DraftService;
import com.expect.admin.service.vo.DraftVO;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.ResponseBuilder;
import com.expect.admin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/draft")
public class DraftController {
	
	private static Logger logger = LoggerFactory.getLogger(DraftController.class);
	
	@Autowired
	private DraftService draftService;
	
	@RequestMapping(value = "/getDrafts", method = RequestMethod.POST)
	public String getDrafts(HttpServletRequest request, ModelMap model) {
//		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		return "";
	}
	
	/**
	 * 新增拟稿
	 * @param response
	 * @param draftVO
	 * @throws IOException
	 */
	@RequestMapping(value = "/addDraft", method = RequestMethod.POST)
	public void addDraft(HttpServletResponse response, @ModelAttribute() DraftVO draftVO) throws IOException {
		if(draftVO == null) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "拟稿内容为空！"));
			logger.error("新增拟稿失败");
		}
		draftVO.setNgshzt("1");//新申请的拟稿
		String message = "新增拟稿成功！";
		boolean isSuccess = false;
		try{
			isSuccess = draftService.add(draftVO);
		}catch(IllegalArgumentException ie) {
			message = "新增拟稿失败！";
			logger.error("新增拟稿失败！", ie);
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(isSuccess, message));
	}
	
	@RequestMapping(value = "/updateDraft", method = RequestMethod.POST)
	public void updateDraft(HttpServletResponse response, @ModelAttribute() DraftVO draftVO) throws IOException {
		if(draftVO == null || StringUtil.isBlank(draftVO.getId())) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "修改拟稿失败，没有找到要修改的拟稿！"));
			if(draftVO != null) {
				String ngr = draftVO.getNgr();
				String ztc = draftVO.getZtc();
				logger.error("修改拟稿失败，没有找到要修改的拟稿！ draftID = "+ ngr + "主题词  ：" + ztc);
			}else logger.error("修改拟稿失败，没有找到要修改的拟稿！ ");
			return;
		}
			
		if(!updateCheck(draftVO.getNgshzt())) {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "没有修改拟稿的权限！"));
			return;
		}
		String message = "修改拟稿成功！";
		boolean isSuccess = false;
		try{
			isSuccess = draftService.add(draftVO);
		}catch(IllegalArgumentException ie) {
			message = "新增拟稿失败！";
			logger.error("新增拟稿失败！", ie);
		}
		ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(isSuccess, message));	
	}
	
	/**
	 *  这个是不对的
	 * 判断用户现在是否有权限修改拟稿
	 * 根据用户的授权码  和拟稿的审核状态相等
	 * @param ngshzt
	 * @return
	 */
	private boolean updateCheck(String ngshzt){
		if(StringUtil.isBlank(ngshzt)) return false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles = user.getRoles();
		for(Role r : roles) {
//			Integer auth = Integer.parseInt(r.getAuthority());
//			if(auth == Integer.parseInt(ngshzt)) return true;
		}
		return false;
	}

}
