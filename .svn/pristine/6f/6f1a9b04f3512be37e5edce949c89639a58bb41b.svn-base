package com.expect.admin.data.dataobject;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.expect.admin.weixin.common.bean.WxAccessToken;
import com.expect.admin.weixin.common.util.ToStringUtils;

/**
 * 基于内存的微信配置provider，在实际生产环境中应该将这些配置持久化
 *
 * @author Daniel Qian
 */
@Component

@ConfigurationProperties(prefix = "WxCpInMemoryConfigStorage")
public class WxCpInMemoryConfigStorage{

  private  String corpId ;
  private  String corpSecret ;
  private  String token ;
  private  String accessToken;
  private  String aesKey ;
  private  Integer agentId ;
  private  long expiresTime;

  private  String oauth2redirectUri ;
  private  String httpProxyHost;
  private  int httpProxyPort;
  private  String httpProxyUsername;
  private  String httpProxyPassword;
  private  String jsapiTicket;
  private  long jsapiTicketExpiresTime;

  private  File tmpDirFile;


  public String getAccessToken() {
    return this.accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  
  public boolean isAccessTokenExpired() {
    return System.currentTimeMillis() > this.expiresTime;
  }

   
  public void expireAccessToken() {
    this.expiresTime = 0;
  }

   
  public synchronized void updateAccessToken(WxAccessToken accessToken) {
    updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
  }

   
  public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
    this.accessToken = accessToken;
    this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
  }

   
  public String getJsapiTicket() {
    return this.jsapiTicket;
  }

  public void setJsapiTicket(String jsapiTicket) {
    this.jsapiTicket = jsapiTicket;
  }

  public long getJsapiTicketExpiresTime() {
    return this.jsapiTicketExpiresTime;
  }

  public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
    this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
  }

   
  public boolean isJsapiTicketExpired() {
    return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
  }

   
  public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
    this.jsapiTicket = jsapiTicket;
    // 预留200秒的时间
    this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
  }

   
  public void expireJsapiTicket() {
    this.jsapiTicketExpiresTime = 0;
  }

   
  public String getCorpId() {
    return this.corpId;
  }

  public void setCorpId(String corpId) {
    this.corpId = corpId;
  }

   
  public String getCorpSecret() {
    return this.corpSecret;
  }

  public void setCorpSecret(String corpSecret) {
    this.corpSecret = corpSecret;
  }

   
  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

   
  public long getExpiresTime() {
    return this.expiresTime;
  }

  public void setExpiresTime(long expiresTime) {
    this.expiresTime = expiresTime;
  }

   
  public String getAesKey() {
    return this.aesKey;
  }

  public void setAesKey(String aesKey) {
    this.aesKey = aesKey;
  }

   
  public Integer getAgentId() {
    return this.agentId;
  }

  public void setAgentId(Integer agentId) {
    this.agentId = agentId;
  }

   
  public String getOauth2redirectUri() {
    return this.oauth2redirectUri;
  }

  public void setOauth2redirectUri(String oauth2redirectUri) {
    this.oauth2redirectUri = oauth2redirectUri;
  }

   
  public String getHttpProxyHost() {
    return this.httpProxyHost;
  }

  public void setHttpProxyHost(String httpProxyHost) {
    this.httpProxyHost = httpProxyHost;
  }

   
  public int getHttpProxyPort() {
    return this.httpProxyPort;
  }

  public void setHttpProxyPort(int httpProxyPort) {
    this.httpProxyPort = httpProxyPort;
  }

   
  public String getHttpProxyUsername() {
    return this.httpProxyUsername;
  }

  public void setHttpProxyUsername(String httpProxyUsername) {
    this.httpProxyUsername = httpProxyUsername;
  }

   
  public String getHttpProxyPassword() {
    return this.httpProxyPassword;
  }

  public void setHttpProxyPassword(String httpProxyPassword) {
    this.httpProxyPassword = httpProxyPassword;
  }

   
  public String toString() {
    return ToStringUtils.toSimpleString(this);
  }

   
  public File getTmpDirFile() {
    return this.tmpDirFile;
  }

  public void setTmpDirFile(File tmpDirFile) {
    this.tmpDirFile = tmpDirFile;
  }
//
//  public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
//    return this.apacheHttpClientBuilder;
//  }
//
//  public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
//    this.apacheHttpClientBuilder = apacheHttpClientBuilder;
//  }
//


}
