package com.expect.admin.service.convertor;

import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.LogSys;
import com.expect.admin.service.vo.LogVo;

public class LogConvertor {

	public static LogVo convertor(LogSys log) {
		LogVo logVo = new LogVo();
		BeanUtils.copyProperties(log, logVo);
		return logVo;
	}
	
	public static LogSys convertor(LogVo logVo) {
		LogSys log = new LogSys();
		BeanUtils.copyProperties(logVo, log);
		return log;
	}
}
