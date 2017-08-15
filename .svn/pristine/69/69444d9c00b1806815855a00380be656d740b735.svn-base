package com.expect.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.expect.admin.service.NewsService;
import com.expect.admin.service.vo.NewsVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import com.expect.admin.utils.StringUtil;

@Controller
@RequestMapping("admin/news")
public class NewsController {

	@Autowired
	private NewsService newsService;
	
	private final String viewPath = "admin/news/";
	
	@GetMapping("/getNewsDetail")
	public ModelAndView getNewsDetail(@RequestParam(name = "id", required = true)String newsId) {
		ModelAndView mv = new ModelAndView(viewPath + "newDetail");
		NewsVo newsVo = newsService.getNewsById(newsId);
		mv.addObject("newsVo", newsVo);
		return mv;
	}
	
	@PostMapping("/getNewsList")
	public void getNewsList(@RequestParam(name = "xwlx", required = false)String lx,//新闻类型
							@RequestParam(name = "page", required = false)Integer page,
							@RequestParam(name = "pageSize", required = false)Integer pageSize,
							HttpServletResponse response) throws IOException{
		
		if(StringUtil.isBlank(lx)) lx = "dtgg";
		if(page == null) page = 1;
		if(pageSize == null) pageSize = 8;
//		Calendar calendar = new GregorianCalendar();
//		Date end = calendar.getTime();
//		calendar.add(Calendar.MONTH, -1);
//		Date start = calendar.getTime();
		List<NewsVo> newsVoList = newsService.getNewsList(lx, page - 1, pageSize);
		MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "", newsVoList).build());
	}
	
}
