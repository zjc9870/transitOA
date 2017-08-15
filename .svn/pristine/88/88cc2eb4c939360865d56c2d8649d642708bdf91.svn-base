package com.expect.admin.service.interceptor;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.expect.admin.data.dao.LogDbParamRepository;
import com.expect.admin.data.dao.LogDbRepository;
import com.expect.admin.data.dataobject.LogDb;
import com.expect.admin.data.dataobject.LogDbParam;

/**
 * 增删改的数据库操作记录
 */
@Component
@Aspect
public class LogDbInterceptor {

	private Logger logger = LoggerFactory.getLogger(LogDbInterceptor.class);

	@Autowired
	private LogDbRepository logDbRepository;
	@Autowired
	private LogDbParamRepository logDbParamRepository;

	@Pointcut(value = "execution(* *..service.*.save*(..)) || execution(* *..service.*.update*(..)) || execution(* *..service.*.delete*(..))")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object execute(ProceedingJoinPoint pjp) {
		logger.info("增删改开始");
		try {
			LogDb logDb = new LogDb();
			// 执行时间
			long executeTime = System.currentTimeMillis();
			Object object = pjp.proceed();
			executeTime = System.currentTimeMillis() - executeTime;
			// 类型+方法名
			String methodName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
			logger.info("方法名：" + methodName);
			logDb.setMethodName(methodName);
			logDb.setExecuteTime(executeTime);
			logDb.setDateTime(new Date());
			logDb.setResult(object + "");
			LogDb result = logDbRepository.save(logDb);

			if (result != null) {
				// 参数
				Object[] args = pjp.getArgs();
				for (Object arg : args) {
					LogDbParam logDbParam = new LogDbParam();
					logDbParam.setParam(arg + "");
					logDbParam.setLogDb(logDb);
					logDbParamRepository.save(logDbParam);
				}
			}
			logger.info("增删改成功");
			return object;
		} catch (Throwable e) {
			e.printStackTrace();
			logger.info("增删改失败：" + e.getMessage());
		}
		return null;
	}
}
