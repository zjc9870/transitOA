package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.NewsRepository;
import com.expect.admin.data.dataobject.News;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.convertor.NewsConvertor;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.service.vo.NewsVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.StringUtil;

@Service
public class NewsService {
	
	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private UserService userService;
	
	public String saveNews(NewsVo newsVo) {
		News news = NewsConvertor.convertToNews(newsVo);
		UserVo userVo = userService.getLoginUser();
		User user = UserConvertor.convert(userVo);
		news.setUser(user);
		return newsRepository.save(news).getId();
	}

	/**
	 * 获取新闻，包括新闻的内容，用于显示一个新闻的具体内容
	 * @param newsId
	 * @return
	 */
	public NewsVo getNewsById(String newsId) {
		if(StringUtil.isBlank(newsId)) throw new BaseAppException("获取新闻id为空");
		News news = newsRepository.findOne(newsId);
		return NewsConvertor.convertToNewsVoWithcontent(news);
	}
	
	public List<NewsVo> getNewsList(Date start, Date end, int page, int pageSize) {
		List<NewsVo> newsVoList = new ArrayList<>();
		Pageable pageable = new PageRequest(page, pageSize);
		Page<News> newspage = newsRepository.findBySqsjBetween(start, end, pageable);
		List<News> newsList = newspage.getContent();
		for (News news : newsList) {
			newsVoList.add(NewsConvertor.convertToNewsVoWithoutContent(news));
		}
		return newsVoList;
	}
	
	public List<NewsVo> getNewsList(String category, int page, int pageSize) {
		List<NewsVo> newsVoList = new ArrayList<>();
		Order order = new Order(Direction.DESC, "sqsj");
		Pageable pageable = new PageRequest(page, pageSize, new Sort(order));
		Page<News> newspage = newsRepository.findByCategory(category,pageable);
		List<News> newsList = newspage.getContent();
		for (News news : newsList) {
			newsVoList.add(NewsConvertor.convertToNewsVoWithoutContent(news));
		}
		return newsVoList;
	}
}
