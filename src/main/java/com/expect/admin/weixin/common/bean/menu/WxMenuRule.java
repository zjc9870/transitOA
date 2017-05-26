package com.expect.admin.weixin.common.bean.menu;

import com.expect.admin.weixin.common.util.ToStringUtils;

public class WxMenuRule {
  private String tagId;
  private String sex;
  private String country;
  private String province;
  private String city;
  private String clientPlatformType;
  private String language;

  public String getTagId() {
    return this.tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public String getSex() {
    return this.sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getCountry() {
    return this.country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvince() {
    return this.province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getClientPlatformType() {
    return this.clientPlatformType;
  }

  public void setClientPlatformType(String clientPlatformType) {
    this.clientPlatformType = clientPlatformType;
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public String toString() {
    return ToStringUtils.toSimpleString(this);
  }
}
