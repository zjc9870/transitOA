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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.DocumentService;
import com.expect.admin.service.RoleJdgxbGxbService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.DocumentVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping("/weixin/document")
public class WeixinDocumentController {
	private final Logger log = LoggerFactory.getLogger(WeixinController.class);

	private final String viewName = "weixin/document/";

    @Autowired
    DocumentService documentService;
    

    @Autowired
    UserService userService;
    
	@Autowired
	RoleJdgxbGxbService roleJdgxbGxbService;
    

	@Autowired
	RoleService roleService;
	//合同审批列表
	@RequestMapping("/approve")
	public ModelAndView approve() {
		UserVo userVo = userService.getLoginUser();
        String lx = "dsp";
        ModelAndView modelAndView = new ModelAndView(viewName + "document_approve");
        List<DocumentVo> documentVoList = new ArrayList<DocumentVo>();
        documentVoList=documentService.getDocumentByUserIdAndCondition(userVo.getId(),"2",lx);
        modelAndView.addObject("documentVoList",documentVoList);
        return modelAndView;
	}
	
    /**
     * 申请记录tab 公文审批tab
     * @throws IOException
     */
    @GetMapping(value = "/sqjlTab")
    public void sqjlTab(@RequestParam(name = "lx", required = false)String lx,
                        @RequestParam(name = "bz", required = false)String bz,
                        HttpServletResponse response) throws IOException {
        if (StringUtil.isBlank(lx)) lx = "wtj";
        List<DocumentVo> documentVos = new ArrayList<DocumentVo>();
        try {
            UserVo userVo = userService.getLoginUser();


            //申请记录的待审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "dsp")){
                documentVos = documentService.getSqjldspList(userVo.getId(), "2");
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", documentVos).build());
                return;
            }

            //申请记录的已审批
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "ysp")){
                documentVos = documentService.getSqjlYspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", documentVos).build());
                return;
            }

            //申请记录的未提交
            if(StringUtil.equals(bz, "sq") && StringUtil.equals(lx, "wtj")){
                documentVos=documentService.getDocumentByUserIdAndCondition(userVo.getId(),"1",lx);
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取申请记录成功", documentVos).build());
                return;
            }
            //发文审批已审批记录
            if (StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "ysp")){
                documentVos = documentService.getGwspYspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取已审批记录成功", documentVos).build());
                return;
            }
            if(StringUtil.equals(bz, "sp") && StringUtil.equals(lx, "dsp")){
                documentVos = documentService.getGwspDspList(userVo.getId());
                MyResponseBuilder.writeJsonResponse(response,
                        JsonResult.useDefault(true, "获取待审批记录成功", documentVos).build());
                return;
            }

        } catch (Exception e) {
            log.error("获取记录错误" + lx, e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取记录出错").build());
        }
    }

	
	/**
     * 公文审批查看详情
     * @return
     */
    @RequestMapping(value = "/gwspckxq")
    public ModelAndView gwspckxq(@RequestParam(name = "id", required = true)String documentId,@RequestParam(name = "lx", required = true)String lx){
        ModelAndView modelAndView = new ModelAndView(viewName + "document_approve_detail");
        DocumentVo documentVo = documentService.getDocumentById(documentId);

        modelAndView.addObject("documentVo", documentVo);

		modelAndView.addObject("lx", lx);
        return modelAndView;
    }
    
    

	private String dateFormat(String sj) {
		Date date = DateUtil.parse(sj, "yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.zbFormat);
		return simpleDateFormat.format(date);
	}
	
	private String dateFormat_2(String sj) {
		Date date = DateUtil.parse(sj, DateUtil.zbFormat);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
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
            documentService.saveDocumentLcrz(cljg, message, clnrid, "gw");
        }catch(Exception e) {
            log.error("公文审核失败", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "公文审核失败！").build());
        }
        MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "公文审核成功！").build());

    }
	
}
