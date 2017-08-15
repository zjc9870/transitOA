package com.expect.admin.service.convertor;

import java.util.Date;

import com.expect.admin.data.dataobject.News;
import com.expect.admin.service.vo.NewsVo;
import com.expect.admin.utils.DateUtil;


public class NewsConvertor {

	public static NewsVo convertToNewsVoWithcontent(News news){
		NewsVo newsVo = new NewsVo();
		newsVo.setId(news.getId());
		newsVo.setTittle(news.getTittle());
		if(news.getUser() != null)
			newsVo.setUserName(news.getUser().getFullName());
		else newsVo.setUserName(" ");
		if(news.getSqsj() != null)
			newsVo.setSqsj(DateUtil.format(news.getSqsj(), DateUtil.fullFormat));
		else newsVo.setSqsj("");
		newsVo.setContent(news.getContent());
		return newsVo;
	}
	
	public static NewsVo convertToNewsVoWithoutContent(News news) {
		NewsVo newsVo = new NewsVo();
		newsVo.setId(news.getId());
		newsVo.setTittle(news.getTittle());
		if(news.getUser() != null)
			newsVo.setUserName(news.getUser().getFullName());
		else newsVo.setUserName("");
		if(news.getSqsj() != null)
			newsVo.setSqsj(DateUtil.format(news.getSqsj(), DateUtil.fullFormat));
		else newsVo.setSqsj("");
		return newsVo;
	}
	
	/**
	 * 没有设置用户。申请时间为当前时间
	 * @param newsVo
	 * @return
	 */
	public static News convertToNews(NewsVo newsVo) {
		News news = new News();
		news.setId(newsVo.getId());
		news.setTittle(newsVo.getTittle());
		news.setSqsj(new Date());
		news.setContent(newsVo.getContent());
		news.setCategory(newsVo.getCategory());
		return news;
	}
}
