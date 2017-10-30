package com.expect.admin.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.ResponseBuilder;

@Controller
@RequestMapping("/admin/personalCon")
public class ContractPageController {

	private final String viewName = "admin/personalCon/";


	/**
	 * 合同审批查看详情
	 */
	@RequestMapping("/test1")
	public ModelAndView test1() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approveDetail");
		return modelAndView;
	}
	
	/**
	 * 合同申请
	 */
	@RequestMapping("/test2")
	public ModelAndView test2() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply");
		return modelAndView;
	}
	
	/**
	 * 申请记录
	 */
	@RequestMapping("/test3")
	public ModelAndView test3() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_record");
		return modelAndView;
	}
	
	/**
	 * 合同审批
	 */
	@RequestMapping("/test4")
	public ModelAndView test4() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve");
		List<ContractVo> contractVoList = new ArrayList<>();
		ContractVo con = new ContractVo();
		con.setId("0001");
		con.setBh("江宁公交0001号合同");
		con.setUserName("张三");
		con.setHtbt("劳动法务合同");
		con.setHtnr("发打发第三方第三方");
		con.setNqdrq("2016/12/10");
		con.setQx("30天");
		con.setHtshzt("待审核");
		con.setHtfl("集团合同");
		con.setLcbs("1");
		con.setSqsj("2016/12/10");
		contractVoList.add(con);
		modelAndView.addObject("contractVoList", contractVoList);
		System.out.println(contractVoList);
		return modelAndView;
	}
	
	/**
	 * 合同审批
	 */
	@RequestMapping("/test41")
	public void test41(@RequestParam(name = "lx", required = false)String lx,HttpServletResponse response) {
//		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve");
		List<ContractVo> contractVoList = new ArrayList<>();
		ContractVo con = new ContractVo();
		switch (lx) {
		case "dsp":
			con.setHtbt("劳动法务合同");
			break;
		case "yth":
			con.setHtbt("纠纷处理合同");
			break;
		case "ysp":
			con.setHtbt("工资发放合同");
			break;
		default:
			break;
		}
		contractVoList.add(con);
//		modelAndView.addObject("contractVoList", contractVoList);
		System.out.println(lx);
		try {
			ResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "success",contractVoList));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 审批记录
	 */
	@RequestMapping("/test5")
	public ModelAndView test5() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_approve_record");
		return modelAndView;
	}
	
	/**
	 * 编号回填
	 */
	@RequestMapping("/test6")
	public ModelAndView test6() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill");
		return modelAndView;
	}
	
	/**
	 * 回填记录
	 */
	@RequestMapping("/test7")
	public ModelAndView test7() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill_record");
		return modelAndView;
	}
	
	/**
	 * 申请记录详情
	 */
	@RequestMapping("/test8")
	public ModelAndView test8() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_apply_recordDetail");
		return modelAndView;
	}
	
	/**
	 * 编号回填详情
	 */
	@RequestMapping("/test9")
	public ModelAndView test9() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfillDetail");
		return modelAndView;
	}
	
	/**
	 * 回填记录详情
	 */
	@RequestMapping("/test10")
	public ModelAndView test10() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_backfill_recordDetail");
		return modelAndView;
	}
	
	/**
	 * 合同查询
	 */
	@RequestMapping("/test11")
	public ModelAndView test11() {
		ModelAndView modelAndView = new ModelAndView(viewName + "c_find");
		return modelAndView;
	}
	
	/**
	 * 个人设置
	 */
	@RequestMapping("/test")
	public ModelAndView test() {
		ModelAndView modelAndView = new ModelAndView(viewName + "personalCon");
		return modelAndView;
	}

}
