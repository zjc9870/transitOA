package com.expect.admin.weixin.cp.api;

import java.util.Map;

import com.expect.admin.service.WxCpService;
import com.expect.admin.weixin.common.exception.WxErrorException;
import com.expect.admin.weixin.common.session.WxSessionManager;
import com.expect.admin.weixin.cp.bean.WxCpXmlMessage;

/**
 * 微信消息拦截器，可以用来做验证
 *
 * @author Daniel Qian
 */
public interface WxCpMessageInterceptor {

  /**
   * 拦截微信消息
   *
   * @param wxMessage
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @param wxCpService
   * @param sessionManager
   * @return true代表OK，false代表不OK
   */
  boolean intercept(WxCpXmlMessage wxMessage,
                    Map<String, Object> context,
                    WxCpService wxCpService,
                    WxSessionManager sessionManager) throws WxErrorException;

}
