package com.juyuejk.core.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

public class SPUtil {
	private static SharedPreferences sharedPreferences;

	private static Context mContext;

	public static void init(Context context){
		SPUtil.mContext = context;

		if (sharedPreferences == null) {
			synchronized (SPUtil.class){
				if (sharedPreferences == null){
					String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
					sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
				}
			}
		}
	}

	// 存储
	@Deprecated
	public static void saveString(Context context, String key, String value) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	// 读取
	@Deprecated
	public static String getString(Context context, String key, String defValue) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}

	@Deprecated
	public static void saveInt(Context context, String key, int value) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, value).commit();
	}

	// 读取
	@Deprecated
	public static int getInt(Context context, String key, int defValue) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	@Deprecated
	public static void saveSet(Context context, String key, Set<String> value) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putStringSet(key, value).commit();
	}

	// 读取
	@Deprecated
	public static Set<String> getSet(Context context, String key, Set<String> defValue) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getStringSet(key, defValue);
	}

	/**
	 * 获取userId
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static int getUserId(Context context) {
		return getInt(context, "userId", -1);
	}

	/**
	 * 获取手机号码
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getMobile(Context context) {
		return getString(context, "mobile", "");
	}

	/**
	 * 获取性别
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getSex(Context context) {
		return getString(context, "sex", "");
	}

	/**
	 * 获取性别ID
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getSexId(Context context) {
		return getString(context, "sexId", "");
	}

	/**
	 * 获取年龄
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getAge(Context context) {
		return getString(context, "age", "");
	}

	/**
	 * 移除某一个Key
	 *
	 * @param context
	 * @param key
	 */
	@Deprecated
	public static void removeKey(Context context, String key) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().remove(key).commit();
	}


	/**
	 * 储存一个boolean类型的变量
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	@Deprecated
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 读取一个boolean类型的变量
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	@Deprecated
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if (sharedPreferences == null) {
			String packName = context.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = context.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}





	/**
	 * 保存复杂数据类型的数据到SharedPreferences
	 * 
	 * @param key
	 * @param object
	 */
	public static <T> void saveObject(String key, T object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			String saveObject = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
			oos.close();
			
			if (sharedPreferences == null) {
				String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
				sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
			}
			sharedPreferences.edit().putString(key, saveObject).commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取保存的复杂类型的数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static <T> T getObject(String key, T defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		String saveObject = sharedPreferences.getString(key, "");
		if (!TextUtils.isEmpty(saveObject)) {
			byte[] bs = Base64.decode(saveObject.getBytes(), Base64.DEFAULT);
			ByteArrayInputStream bis = new ByteArrayInputStream(bs);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bis);
				T readObject = (T) ois.readObject();
				return readObject;
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return defValue;
	}


	// 存储
	public static void saveString(String key, String value) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	// 读取
	public static String getString(String key, String defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}

	public static void saveInt(String key, int value) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, value).commit();
	}

	// 读取
	public static int getInt(String key, int defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	public static void saveLong(String key, long value) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putLong(key, value).commit();
	}

	// 读取
	public static long getLong(String key, long defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getLong(key, defValue);
	}

	public static void saveSet(String key, Set<String> value) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putStringSet(key, value).commit();
	}


	// 读取
	public static Set<String> getSet(String key, Set<String> defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getStringSet(key, defValue);
	}

	/**
	 * 获取userId
	 *
	 * @return
	 */
	public static int getUserId() {
		return getInt(mContext, "userId", -1);
	}

	/**
	 * 获取手机号码
	 *
	 * @return
	 */
	public static String getMobile() {
		return getString(mContext, "mobile", "");
	}


	/**
	 * 获取性别
	 *
	 * @return
	 */
	public static String getSex() {
		return getString(mContext, "sex", "");
	}

	/**
	 * 获取性别ID
	 *
	 * @return
	 */
	public static String getSexId() {
		return getString(mContext, "sexId", "");
	}

	/**
	 * 获取年龄
	 *
	 * @return
	 */
	public static String getAge() {
		return getString(mContext, "age", "");
	}

	/**
	 * 移除某一个Key
	 *
	 * @param key
	 */
	public static void removeKey(String key) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().remove(key).commit();
	}


	/**
	 * 储存一个boolean类型的变量
	 *
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(String key, boolean value) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 读取一个boolean类型的变量
	 *
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		if (sharedPreferences == null) {
			String packName = mContext.getPackageName().replaceAll("\\.", "_") + "_config";
			sharedPreferences = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}


	/**
	 * 清除所有保存在SharePrefrence中的字段
	 * @param keys
     */
	public static void removeAll(String ...keys){
		try {
			if (keys == null) return;
			for ( String key:keys) {
                SPUtil.removeKey(key);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
