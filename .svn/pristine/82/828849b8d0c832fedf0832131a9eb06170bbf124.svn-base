package com.expect.admin.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dataobject.WxCpInMemoryConfigStorage;
import com.expect.admin.weixin.common.bean.WxAccessToken;
import com.expect.admin.weixin.common.bean.WxJsapiSignature;
import com.expect.admin.weixin.common.bean.menu.WxMenu;
import com.expect.admin.weixin.common.bean.result.WxError;
import com.expect.admin.weixin.common.bean.result.WxMediaUploadResult;
import com.expect.admin.weixin.common.exception.WxErrorException;
import com.expect.admin.weixin.common.session.StandardSessionManager;
import com.expect.admin.weixin.common.session.WxSession;
import com.expect.admin.weixin.common.session.WxSessionManager;
import com.expect.admin.weixin.common.util.RandomUtils;
import com.expect.admin.weixin.common.util.crypto.SHA1;
import com.expect.admin.weixin.common.util.fs.FileUtils;
import com.expect.admin.weixin.common.util.http.MediaDownloadRequestExecutor;
import com.expect.admin.weixin.common.util.http.MediaUploadRequestExecutor;
import com.expect.admin.weixin.common.util.http.RequestExecutor;
import com.expect.admin.weixin.common.util.http.SimpleGetRequestExecutor;
import com.expect.admin.weixin.common.util.http.SimplePostRequestExecutor;
import com.expect.admin.weixin.common.util.http.URIUtil;
import com.expect.admin.weixin.common.util.json.GsonHelper;
import com.expect.admin.weixin.cp.bean.WxCpDepart;
import com.expect.admin.weixin.cp.bean.WxCpMessage;
import com.expect.admin.weixin.cp.bean.WxCpTag;
import com.expect.admin.weixin.cp.bean.WxCpUser;
import com.expect.admin.weixin.cp.util.json.WxCpGsonBuilder;
//import com.expect.admin.weixin.common.bean.WxJsapiSignature;
//import com.expect.admin.weixin.common.bean.menu.WxMenu;
//import com.expect.admin.weixin.common.bean.result.WxError;
//import com.expect.admin.weixin.common.bean.result.WxMediaUploadResult;
//import com.expect.admin.weixin.common.exception.WxErrorException;
//import com.expect.admin.weixin.common.session.StandardSessionManager;
//import com.expect.admin.weixin.common.session.WxSession;
//import com.expect.admin.weixin.common.session.WxSessionManager;
//import com.expect.admin.weixin.common.util.RandomUtils;
//import com.expect.admin.weixin.common.util.crypto.SHA1;
//import com.expect.admin.weixin.common.util.fs.FileUtils;
//import com.expect.admin.weixin.common.util.http.ApacheHttpClientBuilder;
//import com.expect.admin.weixin.common.util.http.DefaultApacheHttpClientBuilder;
//import com.expect.admin.weixin.common.util.http.MediaDownloadRequestExecutor;
//import com.expect.admin.weixin.common.util.http.MediaUploadRequestExecutor;
//import com.expect.admin.weixin.common.util.http.RequestExecutor;
//import com.expect.admin.weixin.common.util.http.SimpleGetRequestExecutor;
//import com.expect.admin.weixin.common.util.http.SimplePostRequestExecutor;
//import com.expect.admin.weixin.common.util.http.URIUtil;
//import com.expect.admin.weixin.common.util.json.GsonHelper;
//import com.expect.admin.weixin.cp.api.WxCpInMemoryConfigStorage;
//import com.expect.admin.weixin.cp.api.WxCpInMemoryConfigStorage;
//import com.expect.admin.weixin.cp.bean.WxCpDepart;
//import com.expect.admin.weixin.cp.bean.WxCpMessage;
//import com.expect.admin.weixin.cp.bean.WxCpTag;
//import com.expect.admin.weixin.cp.bean.WxCpUser;
//import com.expect.admin.weixin.cp.util.json.WxCpGsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;

@Service
public class WxCpService {

  protected final Logger log = LoggerFactory.getLogger(WxCpService.class);

  /**
   * 全局的是否正在刷新access token的锁
   */
  protected final Object globalAccessTokenRefreshLock = new Object();

  /**
   * 全局的是否正在刷新jsapi_ticket的锁
   */
  protected final Object globalJsapiTicketRefreshLock = new Object();
 
  
  protected CloseableHttpClient httpClient;

  protected HttpHost httpProxy;
  protected WxSessionManager sessionManager = new StandardSessionManager();
  /**
   * 临时文件目录
   */
  protected File tmpDirFile;
  private int retrySleepMillis = 1000;
  private int maxRetryTimes = 5;
  @Autowired
  WxCpInMemoryConfigStorage config;
  public WxCpInMemoryConfigStorage getWxCpConfig(){
//	  InputStream is1 = ClassLoader.getSystemResourceAsStream("weixinconfig.xml");
//	  config = WxCpInMemoryConfigStorage
//	          .fromXml(is1);
//	  
//	  InputStream xmlInputStream;
//	try {
//		xmlInputStream = new ClassPathResource("weixinconfig.xml").getInputStream();
//      XStream xStream = new XStream();
//      xStream.alias("WxCpInMemoryConfigStorage", WxCpInMemoryConfigStorage.class);
//      Object ob =  xStream.fromXML(xmlInputStream);
//      config = (WxCpInMemoryConfigStorage) ob;

	  return config;
//	} catch (IOException e) {
//		 //TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return config;
  }
  public boolean checkSignature(String msgSignature, String timestamp, String nonce, String data) {
    try {
    	System.out.println("token="+getWxCpConfig().getToken());
      return SHA1.gen(getWxCpConfig().getToken(), timestamp, nonce, data)
        .equals(msgSignature);
    } catch (Exception e) {
    	e.printStackTrace();
      return false;
    }
  }

  public void userAuthenticated(String userId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/authsucc?userid=" + userId;
    get(url, null);
  }

  public String getAccessToken() throws WxErrorException {
    return getAccessToken(false);
  }

  public String getAccessToken(boolean forceRefresh) throws WxErrorException {
    if (forceRefresh) {
      getWxCpConfig().expireAccessToken();
    }
    if (getWxCpConfig().isAccessTokenExpired()) {
      synchronized (this.globalAccessTokenRefreshLock) {
        if (getWxCpConfig().isAccessTokenExpired()) {
          String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?"
            + "&corpid=" + getWxCpConfig().getCorpId()
            + "&corpsecret=" + getWxCpConfig().getCorpSecret();
          try {
            HttpGet httpGet = new HttpGet(url);
            if (this.httpProxy != null) {
              RequestConfig config = RequestConfig.custom()
                .setProxy(this.httpProxy).build();
              httpGet.setConfig(config);
            }
            String resultContent = null;
            try (CloseableHttpClient httpclient = getHttpclient();
                 CloseableHttpResponse response = httpclient.execute(httpGet)) {
              resultContent = new BasicResponseHandler().handleResponse(response);
            } finally {
              httpGet.releaseConnection();
            }
            WxError error = WxError.fromJson(resultContent);
            if (error.getErrorCode() != 0) {
              throw new WxErrorException(error);
            }
            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
            getWxCpConfig().updateAccessToken(
              accessToken.getAccessToken(), accessToken.getExpiresIn());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return getWxCpConfig().getAccessToken();
  }

  public String getJsapiTicket() throws WxErrorException {
    return getJsapiTicket(false);
  }

  public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
    if (forceRefresh) {
      getWxCpConfig().expireJsapiTicket();
    }
    if (getWxCpConfig().isJsapiTicketExpired()) {
      synchronized (this.globalJsapiTicketRefreshLock) {
        if (getWxCpConfig().isJsapiTicketExpired()) {
          String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket";
          String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
          JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
          JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
          String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
          int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
          getWxCpConfig().updateJsapiTicket(jsapiTicket,
            expiresInSeconds);
        }
      }
    }
    return getWxCpConfig().getJsapiTicket();
  }

  public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
    long timestamp = System.currentTimeMillis() / 1000;
    String noncestr = RandomUtils.getRandomStr();
    String jsapiTicket = getJsapiTicket(false);
    String signature = SHA1.genWithAmple(
      "jsapi_ticket=" + jsapiTicket,
      "noncestr=" + noncestr,
      "timestamp=" + timestamp,
      "url=" + url
    );
    WxJsapiSignature jsapiSignature = new WxJsapiSignature();
    jsapiSignature.setTimestamp(timestamp);
    jsapiSignature.setNoncestr(noncestr);
    jsapiSignature.setUrl(url);
    jsapiSignature.setSignature(signature);

    // Fixed bug
    jsapiSignature.setAppid(getWxCpConfig().getCorpId());

    return jsapiSignature;
  }

  public void messageSend(WxCpMessage message) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send";
    post(url, message.toJson());
  }

  public void menuCreate(WxMenu menu) throws WxErrorException {
    menuCreate(getWxCpConfig().getAgentId(), menu);
  }

  public void menuCreate(Integer agentId, WxMenu menu) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?agentid="
      + getWxCpConfig().getAgentId();
    post(url, menu.toJson());
  }

  public void menuDelete() throws WxErrorException {
    menuDelete(getWxCpConfig().getAgentId());
  }

  public void menuDelete(Integer agentId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/delete?agentid=" + agentId;
    get(url, null);
  }

  public WxMenu menuGet() throws WxErrorException {
    return menuGet(getWxCpConfig().getAgentId());
  }

  public WxMenu menuGet(Integer agentId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?agentid=" + agentId;
    try {
      String resultContent = get(url, null);
      return WxMenu.fromJson(resultContent);
    } catch (WxErrorException e) {
      // 46003 不存在的菜单数据
      if (e.getError().getErrorCode() == 46003) {
        return null;
      }
      throw e;
    }
  }

  
  public WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream)
    throws WxErrorException, IOException {
    return mediaUpload(mediaType, FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), fileType));
  }

  
  public WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?type=" + mediaType;
    return execute(new MediaUploadRequestExecutor(), url, file);
  }

  
  public File mediaDownload(String media_id) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/media/get";
    return execute(
      new MediaDownloadRequestExecutor(
        getWxCpConfig().getTmpDirFile()),
      url, "media_id=" + media_id);
  }


  
  public Integer departCreate(WxCpDepart depart) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create";
    String responseContent = execute(
      new SimplePostRequestExecutor(),
      url,
      depart.toJson());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return GsonHelper.getAsInteger(tmpJsonElement.getAsJsonObject().get("id"));
  }

  
  public void departUpdate(WxCpDepart group) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update";
    post(url, group.toJson());
  }

  
  public void departDelete(Integer departId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?id=" + departId;
    get(url, null);
  }

  
  public List<WxCpDepart> departGet() throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list";
    String responseContent = get(url, null);
    /*
     * 操蛋的微信API，创建时返回的是 { group : { id : ..., name : ...} }
     * 查询时返回的是 { groups : [ { id : ..., name : ..., count : ... }, ... ] }
     */
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("department"),
        new TypeToken<List<WxCpDepart>>() {
        }.getType()
      );
  }

  
  public void userCreate(WxCpUser user) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/create";
    post(url, user.toJson());
  }

  
  public void userUpdate(WxCpUser user) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/update";
    post(url, user.toJson());
  }

  
  public void userDelete(String userid) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?userid=" + userid;
    get(url, null);
  }

  
  public void userDelete(String[] userids) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete";
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    for (String userid : userids) {
      jsonArray.add(new JsonPrimitive(userid));
    }
    jsonObject.add("useridlist", jsonArray);
    post(url, jsonObject.toString());
  }

  
  public WxCpUser userGet(String userid) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?userid=" + userid;
    String responseContent = get(url, null);
    return WxCpUser.fromJson(responseContent);
  }

  
  public List<WxCpUser> userList(Integer departId, Boolean fetchChild, Integer status) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?department_id=" + departId;
    String params = "";
    if (fetchChild != null) {
      params += "&fetch_child=" + (fetchChild ? "1" : "0");
    }
    if (status != null) {
      params += "&status=" + status;
    } else {
      params += "&status=0";
    }

    String responseContent = get(url, params);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  
  public List<WxCpUser> departGetUsers(Integer departId, Boolean fetchChild, Integer status) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?department_id=" + departId;
    String params = "";
    if (fetchChild != null) {
      params += "&fetch_child=" + (fetchChild ? "1" : "0");
    }
    if (status != null) {
      params += "&status=" + status;
    } else {
      params += "&status=0";
    }

    String responseContent = get(url, params);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  
  public String tagCreate(String tagName) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/create";
    JsonObject o = new JsonObject();
    o.addProperty("tagname", tagName);
    String responseContent = post(url, o.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return tmpJsonElement.getAsJsonObject().get("tagid").getAsString();
  }

  
  public void tagUpdate(String tagId, String tagName) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/update";
    JsonObject o = new JsonObject();
    o.addProperty("tagid", tagId);
    o.addProperty("tagname", tagName);
    post(url, o.toString());
  }

  
  public void tagDelete(String tagId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?tagid=" + tagId;
    get(url, null);
  }

  
  public List<WxCpTag> tagGet() throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/list";
    String responseContent = get(url, null);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("taglist"),
        new TypeToken<List<WxCpTag>>() {
        }.getType()
      );
  }

  
  public List<WxCpUser> tagGetUsers(String tagId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?tagid=" + tagId;
    String responseContent = get(url, null);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  
  public void tagAddUsers(String tagId, List<String> userIds, List<String> partyIds) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/addtagusers";
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("tagid", tagId);
    if (userIds != null) {
      JsonArray jsonArray = new JsonArray();
      for (String userId : userIds) {
        jsonArray.add(new JsonPrimitive(userId));
      }
      jsonObject.add("userlist", jsonArray);
    }
    if (partyIds != null) {
      JsonArray jsonArray = new JsonArray();
      for (String userId : partyIds) {
        jsonArray.add(new JsonPrimitive(userId));
      }
      jsonObject.add("partylist", jsonArray);
    }
    post(url, jsonObject.toString());
  }

  
  public void tagRemoveUsers(String tagId, List<String> userIds) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/deltagusers";
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("tagid", tagId);
    JsonArray jsonArray = new JsonArray();
    for (String userId : userIds) {
      jsonArray.add(new JsonPrimitive(userId));
    }
    jsonObject.add("userlist", jsonArray);
    post(url, jsonObject.toString());
  }

  
  public String oauth2buildAuthorizationUrl(String state) {
    return this.oauth2buildAuthorizationUrl(
      getWxCpConfig().getOauth2redirectUri(),
      state
    );
  }

  
  public String oauth2buildAuthorizationUrl(String redirectUri, String state) {
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    url += "appid=" + getWxCpConfig().getCorpId();
    url += "&redirect_uri=" + URIUtil.encodeURIComponent(redirectUri);
    url += "&response_type=code";
    url += "&scope=snsapi_base";
    if (state != null) {
      url += "&state=" + state;
    }
    url += "#wechat_redirect";
    return url;
  }

  
  public String[] oauth2getUserInfo(String code) throws WxErrorException {
    return oauth2getUserInfo(getWxCpConfig().getAgentId(), code);
  }

  
  public String[] oauth2getUserInfo(Integer agentId, String code) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?"
      + "code=" + code
      + "&agentid=" + agentId;
    String responseText = get(url, null);
    JsonElement je = new JsonParser().parse(responseText);
    JsonObject jo = je.getAsJsonObject();
    return new String[]{GsonHelper.getString(jo, "UserId"), GsonHelper.getString(jo, "DeviceId")};
  }

  
  public int invite(String userId, String inviteTips) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/invite/send";
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("userid", userId);
    if (StringUtils.isNotEmpty(inviteTips)) {
      jsonObject.addProperty("invite_tips", inviteTips);
    }
    String responseContent = post(url, jsonObject.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return tmpJsonElement.getAsJsonObject().get("type").getAsInt();
  }

  
  public String[] getCallbackIp() throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/getcallbackip";
    String responseContent = get(url, null);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    JsonArray jsonArray = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
    String[] ips = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      ips[i] = jsonArray.get(i).getAsString();
    }
    return ips;
  }

  
  public String get(String url, String queryParam) throws WxErrorException {
    return execute(new SimpleGetRequestExecutor(), url, queryParam);
  }

  
  public String post(String url, String postData) throws WxErrorException {
    return execute(new SimplePostRequestExecutor(), url, postData);
  }

  /**
   * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
   */
  
  public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
    int retryTimes = 0;
    do {
      try {
        return executeInternal(executor, uri, data);
      } catch (WxErrorException e) {
        WxError error = e.getError();
        /*
         * -1 系统繁忙, 1000ms后重试
         */
        if (error.getErrorCode() == -1) {
          int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
          try {
            this.log.debug("微信系统繁忙，{}ms 后重试(第{}次)", sleepMillis,
              retryTimes + 1);
            Thread.sleep(sleepMillis);
          } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
          }
        } else {
          throw e;
        }
      }
    } while (++retryTimes < this.maxRetryTimes);

    throw new RuntimeException("微信服务端异常，超出重试次数");
  }

  protected synchronized <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
    if (uri.contains("access_token=")) {
      throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
    }
    String accessToken = getAccessToken(false);

    String uriWithAccessToken = uri;
    uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token=" + accessToken : "&access_token=" + accessToken;

    try {
      return executor.execute(getHttpclient(), this.httpProxy,
        uriWithAccessToken, data);
    } catch (WxErrorException e) {
      WxError error = e.getError();
      /*
       * 发生以下情况时尝试刷新access_token
       * 40001 获取access_token时AppSecret错误，或者access_token无效
       * 42001 access_token超时
       */
      if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
        // 强制设置WxCpInMemoryConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
        getWxCpConfig().expireAccessToken();
        return execute(executor, uri, data);
      }
      if (error.getErrorCode() != 0) {
        throw new WxErrorException(error);
      }
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected CloseableHttpClient getHttpclient() {
	if(httpClient==null){
		return HttpClients.createDefault();
	}
    return this.httpClient;
  }

  
//  public void setWxCpInMemoryConfigStorage(WxCpInMemoryConfigStorage wxConfigProvider) {
//    getWxCpConfig() = wxConfigProvider;
//    ApacheHttpClientBuilder apacheHttpClientBuilder = getWxCpConfig()
//      .getApacheHttpClientBuilder();
//    if (null == apacheHttpClientBuilder) {
//      apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
//    }
//
//    apacheHttpClientBuilder.httpProxyHost(getWxCpConfig().getHttpProxyHost())
//      .httpProxyPort(getWxCpConfig().getHttpProxyPort())
//      .httpProxyUsername(getWxCpConfig().getHttpProxyUsername())
//      .httpProxyPassword(getWxCpConfig().getHttpProxyPassword());
//
//    if (getWxCpConfig().getHttpProxyHost() != null && getWxCpConfig().getHttpProxyPort() > 0) {
//      this.httpProxy = new HttpHost(getWxCpConfig().getHttpProxyHost(), getWxCpConfig().getHttpProxyPort());
//    }
//
//    this.httpClient = apacheHttpClientBuilder.build();
//  }

  
  public void setRetrySleepMillis(int retrySleepMillis) {
    this.retrySleepMillis = retrySleepMillis;
  }


  
  public void setMaxRetryTimes(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
  }

  
  public WxSession getSession(String id) {
    if (this.sessionManager == null) {
      return null;
    }
    return this.sessionManager.getSession(id);
  }

  
  public WxSession getSession(String id, boolean create) {
    if (this.sessionManager == null) {
      return null;
    }
    return this.sessionManager.getSession(id, create);
  }


  
  public void setSessionManager(WxSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  
  public String replaceParty(String mediaId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/batch/replaceparty";
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("media_id", mediaId);
    return post(url, jsonObject.toString());
  }

  
  public String replaceUser(String mediaId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/batch/replaceuser";
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("media_id", mediaId);
    return post(url, jsonObject.toString());
  }

  
  public String getTaskResult(String joinId) throws WxErrorException {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/batch/getresult?jobid=" + joinId;
    return get(url, null);
  }

  public File getTmpDirFile() {
    return this.tmpDirFile;
  }

  public void setTmpDirFile(File tmpDirFile) {
    this.tmpDirFile = tmpDirFile;
  }


}
