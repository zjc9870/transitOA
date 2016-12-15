package com.expect.admin.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/contract")
public class ContractPageController {

	private final String viewName = "admin/contract/";


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
		return modelAndView;
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
	
	

}
