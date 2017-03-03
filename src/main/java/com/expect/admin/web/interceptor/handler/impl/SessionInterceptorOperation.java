package com.expect.admin.web.interceptor.handler.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expect.admin.contants.Constants;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.RequestUtil;
import com.expect.admin.utils.ResponseBuilder;
import com.expect.admin.web.interceptor.handler.InterceptorOperation;

/**
 * session过期处理:<br>
 * 如果session中不存在用户，则登录超时，进行超时处理 <br>
 */
public class SessionInterceptorOperation implements InterceptorOperation {

	@Override
	public boolean operation(HttpServletRequest request, HttpServletResponse response, Object handler) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				handlerTimeoutResult(request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * 处理session过期返回情况
	 */
	private void handlerTimeoutResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean isAjax = RequestUtil.isAjax(request);
		if (isAjax) {
			ResultVo rv = new ResultVo();
			rv.setMessage("登录超时，请重新登录");
			rv.setResult(false);
			String url = Constants.LOGIN_URL.substring(1,Constants.LOGIN_URL.length());
			rv.setObj(url);
			rv.setCode(Constants.CODE_TIMEOUT);
			ResponseBuilder.writeJsonResponse(response, rv);
		} else {
			response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
		}
	}
}
