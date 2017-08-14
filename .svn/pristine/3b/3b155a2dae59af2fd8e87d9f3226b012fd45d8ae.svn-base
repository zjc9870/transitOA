package com.expect.admin.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.News;

public interface NewsRepository extends JpaRepository<News, String> {

	List<News> findByUser_id(String userId);
	List<News> findBySqsjBetween(Date start, Date end);
	Page<News> findBySqsjBetween(Date start, Date end, Pageable pageable);
	Page<News> findByCategory(String category, Pageable pageable);
	
}
