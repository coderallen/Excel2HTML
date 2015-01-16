package cn.coderallen.excel2html.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * JSON工具类。把Java 字符串、数组、集合、Map、POJO类型转换成JSON字符串类型，把JSON字符串转换成Java
 * 字符串、数组、集合、Map、POJO等类型。
 * 
 */
public final class JsonUtils {

	// /////////////////////////////////////////////////////////////////
	// //////////////将Java数据类型转换成JSON格式的字符串//////////////////
	// /////////////////////////////////////////////////////////////////
	/**
	 * 将Java字符串转换成JSON格式的字符串
	 * 
	 * @param key
	 *            Java字符串形式的键
	 * @param value
	 *            Java字符串形式的值
	 * @return JSON格式的字符串
	 */
	public static String stringToJSON(String key, String value) {
		JSONObject object = new JSONObject();
		object.put(key, value);
		return object.toString();
	}

	/**
	 * 将Collection转换成JSON
	 * 
	 * @param object
	 *            java集合对象
	 * @return JSON格式的字符串
	 */
	public static String collectionToJSON(Object object) {
		return arrayOrCollectionToJSON(object);
	}

	/**
	 * 将数组转换成JSON
	 * 
	 * @param object
	 *            java数组对象
	 * @return JSON格式的字符串
	 */
	public static String arrayToJSON(Object object) {
		return arrayOrCollectionToJSON(object);
	}

	private static String arrayOrCollectionToJSON(Object object) {
		JSONArray jsonArray = JSONArray.fromObject(object);
		return jsonArray.toString();
	}

	/**
	 * 将Map转换成JSON
	 * 
	 * @param object
	 *            java Map对象
	 * @return JSON格式的字符串
	 */
	public static String mapToJSON(Object object) {
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}

	/**
	 * 将POJO转换成JSON
	 * 
	 * @param object
	 *            Java Bean对象
	 * @return JSON格式的字符串
	 */
	public static String beanToJSON(Object object) {
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}

	// /////////////////////////////////////////////////////////////////
	// //////////////将JSON格式的字符串转换成Java数据类型//////////////////
	// /////////////////////////////////////////////////////////////////
	/**
	 * 将JSON格式的字符串转换成数组
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param clazz
	 *            数组中存放的对象的Class
	 * @return Java对象数组
	 */
	public static Object json2Array(String json, Class<?> clazz) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toArray(jsonArray, clazz);
	}

	/**
	 * 将JSON格式的字符串转换成Java Collection
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param collectionClazz
	 *            Collection具体子类的Class
	 * @param clazz
	 *            Collection中存放的对象的Class
	 * @return Java Collection
	 */
	public static Collection json2Collection(String json,
			Class collectionClazz, Class clazz) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toCollection(jsonArray, clazz);
	}

	/**
	 * JSON格式的字符串转换成Java Map
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param keyArray键数组
	 * @param valueClazz
	 *            值的Class
	 * 
	 * @return Java Map对象
	 */
	public static Map json2Map(String json, Object[] keyArray,
			Class<?> valueClazz) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map classMap = new HashMap();

		for (int i = 0; i < keyArray.length; i++) {
			classMap.put(keyArray[i], valueClazz);
		}

		return (Map) JSONObject.toBean(jsonObject, Map.class, classMap);
	}

	public static Map json2Map2(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}

		JSONObject jsonObject = JSONObject.fromObject(json);

		return (Map) JSONObject.toBean(jsonObject, Map.class);
	}

	/**
	 * 将JSON转换成POJO
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param beanClazz
	 *            Java对象的Class
	 * @return Java对象
	 */
	public static Object json2Object(String json, Class<?> beanClazz) {
		return JSONObject.toBean(JSONObject.fromObject(json), beanClazz);
	}

	/**
	 * 将JSON转换成String
	 * 
	 * @param json
	 *            JSON格式的字符串
	 * @param key
	 *            键
	 * @return Java 字符串
	 */
	public static String json2String(String json, String key) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		return jsonObject.get(key).toString();
	}

}