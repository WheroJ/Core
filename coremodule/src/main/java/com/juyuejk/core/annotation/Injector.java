package com.juyuejk.core.annotation;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.view.View;

public class Injector {
	/**
	 * 注入绑定Activity
	 * @param fieldOwner
	 * @param activity
	 */
	public static void inject(Object fieldOwner, Activity activity){
		try {
			injectView(fieldOwner, activity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 注入绑定view
	 * @param fieldOwner
	 * @param view
	 */
	public static void inject(Object fieldOwner, View view){
		try {
			injectView(fieldOwner, view);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void injectView(Object fieldOwner, Object viewProvider) throws Exception{
		List<Field> fields = ReflectUtils.getFields(fieldOwner.getClass());
		
		for (Field field : fields) {
			ViewId viewId = field.getAnnotation(ViewId.class);
			
			if (viewId == null) {
				continue;
			}
			
			int value = viewId.value();
			View view = null;
			if (viewProvider instanceof Activity) {
				view = ((Activity)viewProvider).findViewById(value);
			} else if (viewProvider instanceof View) {
				view = ((View)viewProvider).findViewById(value);
			}
			
			if (view != null) {
//				field.setAccessible(true);
//				field.set(fieldOwner, view);
				ReflectUtils.setFieldValue(fieldOwner, field.getName(), view);
			}
		}
	}
}
