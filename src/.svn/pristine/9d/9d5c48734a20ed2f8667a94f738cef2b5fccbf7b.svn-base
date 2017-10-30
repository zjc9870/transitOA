/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package com.expect.admin.weixin.cp.util.json;

import java.lang.reflect.Type;

import com.expect.admin.weixin.common.util.json.GsonHelper;
import com.expect.admin.weixin.cp.bean.WxCpTag;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Daniel Qian
 */
public class WxCpTagGsonAdapter implements JsonSerializer<WxCpTag>, JsonDeserializer<WxCpTag> {

  @Override
  public JsonElement serialize(WxCpTag tag, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject o = new JsonObject();
    o.addProperty("tagid", tag.getId());
    o.addProperty("tagname", tag.getName());
    return o;
  }

  @Override
  public WxCpTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    return new WxCpTag(GsonHelper.getString(jsonObject, "tagid"), GsonHelper.getString(jsonObject, "tagname"));
  }

}
