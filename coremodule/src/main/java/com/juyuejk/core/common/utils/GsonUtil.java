package com.juyuejk.core.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * 将Json字符串解析成为相应对象
 * @author Administrator
 *
 */
public class GsonUtil{
	
	/**
	 * 正常解析返回泛型类型的对象，其他情况返回null
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T json2Bean(String json,Class<T> clazz){
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return t;//这样避免空指针
		} 
	}
	
	/**
	 * 正常情况返回泛型类型的List，其他情况返回一个null对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> ArrayList<T> jsonArray2Bean(String json,Class<T> clazz){
		ArrayList<T> ts = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonArray Jarray = parser.parse(json).getAsJsonArray();
			if (Jarray == null) {
				return null;
			}
			for (JsonElement obj : Jarray) {
				T t = gson.fromJson(obj, clazz);
				ts.add(t);
			}
			System.out.println("大小：" + ts.size());
//			return ts.size() == 0 ? null : ts;
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
			return ts;
		} 
	}
}
