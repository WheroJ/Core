package com.juyuejk.core.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {
	/**
	 * 获取一个字节码文件对应的所有方法
	 * @param clazz
	 * @return
	 */
	public static List<Field> getFields(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		List<Field> list = new ArrayList<Field>();
		for (Field field : fields) {
			list.add(field);
		}
		
		if (clazz != Object.class && clazz.getSuperclass() != null) {
			list.addAll(getFields(clazz.getSuperclass()));
		}
		return list;
	}
	
	/**
	 * 获取某个成员变量
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException{
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return getField(clazz.getSuperclass(), fieldName);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 执行非静态方法
	 * @param object
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] paramValues) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class clazz = object.getClass();
		Method method = getMethod(clazz, methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(object, paramValues);
	}
	
	/**
	 * 执行静态方法
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @param paramValues
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object invokeStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] paramValues) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = getMethod(clazz, methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(clazz, paramValues);
	}

	/**
	 * 获取相应方法
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method getMethod(Class clazz, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException {
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null) {
				return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 设置成员变量的值
	 * @param object
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException{
		Class<?> clazz = object.getClass();
		Field field = getField(clazz, fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}
	
	/**
	 * 设置静态变量的值
	 * @param clazz
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void setStaticFieldValue(Class<?> clazz, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException{
		Field field = getField(clazz, fieldName);
		field.setAccessible(true);
		field.set(clazz, value);
	}
}
