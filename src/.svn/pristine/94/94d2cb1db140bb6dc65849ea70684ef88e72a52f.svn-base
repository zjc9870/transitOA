package com.expect.admin.weixin.cp.util.json;

import com.expect.admin.weixin.common.bean.result.WxError;
import com.expect.admin.weixin.common.util.json.WxErrorAdapter;
import com.expect.admin.weixin.cp.bean.WxCpDepart;
import com.expect.admin.weixin.cp.bean.WxCpMessage;
import com.expect.admin.weixin.cp.bean.WxCpTag;
import com.expect.admin.weixin.cp.bean.WxCpUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WxCpGsonBuilder {

  public static final GsonBuilder INSTANCE = new GsonBuilder();

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WxCpMessage.class, new WxCpMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WxCpDepart.class, new WxCpDepartGsonAdapter());
    INSTANCE.registerTypeAdapter(WxCpUser.class, new WxCpUserGsonAdapter());
    INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
    INSTANCE.registerTypeAdapter(WxCpTag.class, new WxCpTagGsonAdapter());
  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
