package com.expect.admin.weixin.common.bean.menu;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.expect.admin.weixin.common.util.ToStringUtils;
import com.expect.admin.weixin.common.util.json.WxGsonBuilder;

/**
 * 菜单（公众号和企业号共用的）
 *
 * @author Daniel Qian
 */
public class WxMenu implements Serializable {

  private static final long serialVersionUID = -7083914585539687746L;

  private List<WxMenuButton> buttons = new ArrayList<>();

  private WxMenuRule matchRule;

  /**
   * 要用 http://mp.weixin.qq.com/wiki/16/ff9b7b85220e1396ffa16794a9d95adc.html 格式来反序列化
   * 相比 http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html 的格式，外层多套了一个menu
   */
  public static WxMenu fromJson(String json) {
    return WxGsonBuilder.create().fromJson(json, WxMenu.class);
  }

  /**
   * 要用 http://mp.weixin.qq.com/wiki/16/ff9b7b85220e1396ffa16794a9d95adc.html 格式来反序列化
   * 相比 http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html 的格式，外层多套了一个menu
   */
  public static WxMenu fromJson(InputStream is) {
    return WxGsonBuilder.create().fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), WxMenu.class);
  }

  public List<WxMenuButton> getButtons() {
    return this.buttons;
  }

  public void setButtons(List<WxMenuButton> buttons) {
    this.buttons = buttons;
  }

  public WxMenuRule getMatchRule() {
    return this.matchRule;
  }

  public void setMatchRule(WxMenuRule matchRule) {
    this.matchRule = matchRule;
  }

  public String toJson() {
    return WxGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return ToStringUtils.toSimpleString(this);
  }

}
