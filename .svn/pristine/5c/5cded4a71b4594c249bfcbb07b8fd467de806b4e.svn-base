package com.expect.admin.weixin.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expect.admin.weixin.common.api.WxErrorExceptionHandler;
import com.expect.admin.weixin.common.exception.WxErrorException;


public class LogExceptionHandler implements WxErrorExceptionHandler {

  private Logger log = LoggerFactory.getLogger(WxErrorExceptionHandler.class);

  @Override
  public void handle(WxErrorException e) {

    this.log.error("Error happens", e);

  }

}
