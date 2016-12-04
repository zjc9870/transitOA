package com.expect.admin.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.FunctionService;
import com.expect.admin.service.convertor.FunctionConvertor;
import com.expect.admin.service.vo.FunctionVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;

@Controller
@RequestMapping("/admin/function")
public class FunctionController {

	private final String viewName = "admin/system/function/";

	@Autowired
	private FunctionService functionService;

	@RequestMapping("/functionManagePage")
	public ModelAndView userManagePage() {
		List<FunctionVo> functions = functionService.getFunctions();
		List<DataTableRowVo> dtrvs = FunctionConvertor.convertDtrv(functions);
		ModelAndView modelAndView = new ModelAndView(viewName + "manage");
		modelAndView.addObject("functions", dtrvs);
		return modelAndView;
	}

	/**
	 * 获取functionSelect的html
	 */
	@RequestMapping("/getFunctionSelectHtml")
	@ResponseBody
	public SelectOptionVo getFunctionSelect(String id) {
		List<FunctionVo> functions = functionService.getFunctions();
		FunctionVo checkedFunction = null;
		if (!StringUtils.isEmpty(id)) {
			for (int i = functions.size() - 1; i >= 0; i--) {
				if (id.equals((functions.get(i).getId()))) {
					checkedFunction = functions.remove(i);
					break;
				}
			}
		}
		SelectOptionVo sov = FunctionConvertor.convertSov(functions, checkedFunction);
		return sov;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo save(FunctionVo functionVo) {
		return functionService.save(functionVo);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataTableRowVo update(FunctionVo functionVo) {
		return functionService.update(functionVo);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo delete(String id) {
		return functionService.delete(id);
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo batchDelete(String ids) {
		return functionService.batchDelete(ids);
	}
}
