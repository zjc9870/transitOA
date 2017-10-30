package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.LogRepository;
import com.expect.admin.data.dataobject.LogSys;
import com.expect.admin.service.convertor.LogConvertor;
import com.expect.admin.service.vo.LogVo;

@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;
	
	@Transactional
	public void save(LogVo logVo) {
		LogSys log = LogConvertor.convertor(logVo);
		logRepository.save(log);
	}
	
	public List<LogVo> getLogs(){
		List<LogVo> logList = new ArrayList<LogVo>();
		return logList;
	}
	
}
