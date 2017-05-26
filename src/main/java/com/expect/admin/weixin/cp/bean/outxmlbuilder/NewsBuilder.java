package com.expect.admin.weixin.cp.bean.outxmlbuilder;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.weixin.cp.bean.WxCpXmlOutNewsMessage;
import com.expect.admin.weixin.cp.bean.WxCpXmlOutNewsMessage.Item;

/**
 * 图文消息builder
 *
 * @author Daniel Qian
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxCpXmlOutNewsMessage> {

  protected final List<Item> articles = new ArrayList<>();

  public NewsBuilder addArticle(Item item) {
    this.articles.add(item);
    return this;
  }

  @Override
  public WxCpXmlOutNewsMessage build() {
    WxCpXmlOutNewsMessage m = new WxCpXmlOutNewsMessage();
    for (Item item : this.articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }

}
