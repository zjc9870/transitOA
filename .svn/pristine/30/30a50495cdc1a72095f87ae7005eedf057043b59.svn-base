package com.expect.admin.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class JacksonJsonUtil {

	private ObjectMapper om = new ObjectMapper();
	private static JacksonJsonUtil jacksonJsonUtil = new JacksonJsonUtil();

	private JacksonJsonUtil() {
	}

	public static JacksonJsonUtil getInstance() {
		return jacksonJsonUtil;
	}

	public int readInt(String content, String path) {
		try {
			JsonNode jsonNode = om.readTree(content);
			return jsonNode.path(path).asInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String readString(String content, String path) {
		try {
			JsonNode jsonNode = om.readTree(content);
			return jsonNode.path(path).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T readObject(String content, Class<T> clazz) {
		try {
			T t = om.readValue(content, clazz);
			return t;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T readObject(String content, String nodeName, Class<T> clazz) {
		if (StringUtils.isEmpty(nodeName)) {
			return readObject(content, clazz);
		} else {
			try {
				JsonNode jsonNode = om.readTree(content);
				JsonNode nodeNameJsonNode = jsonNode.findPath(nodeName);
				return om.readValue(nodeNameJsonNode.toString(), clazz);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public List readList(String content, Class<?> elementClasses) {
		JavaType javaType = getCollectionType(List.class, elementClasses);
		try {
			List list = om.readValue(content, javaType);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List readList(String content, String nodeName, Class<?> elementClasses) {
		if (StringUtils.isEmpty(nodeName)) {
			return readList(content, elementClasses);
		} else {
			try {
				JsonNode jsonNode = om.readTree(content);
				JsonNode nodeNameJsonNode = jsonNode.findPath(nodeName);
				JavaType javaType = getCollectionType(List.class, elementClasses);
				return om.readValue(nodeNameJsonNode.toString(), javaType);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<String> readList(String content) {
		List<String> nodes = new ArrayList<>();
		try {
			JsonNode jsonNode = om.readTree(content);
			Iterator<JsonNode> jsonNodes = jsonNode.elements();
			while (jsonNodes.hasNext()) {
				nodes.add(jsonNodes.next().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	public List<String> readListAsText(String content) {
		List<String> nodes = new ArrayList<>();
		try {
			JsonNode jsonNode = om.readTree(content);
			Iterator<JsonNode> jsonNodes = jsonNode.elements();
			while (jsonNodes.hasNext()) {
				nodes.add(jsonNodes.next().asText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	public Map readMap(String content, Class<?>... elementClasses) {
		JavaType javaType = getCollectionType(Map.class, elementClasses);
		try {
			Map map = om.readValue(content, javaType);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String write(List<?> data) {
		try {
			return om.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String write(Object object) {
		try {
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 */
	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

}
