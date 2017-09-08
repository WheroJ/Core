package com.juyuejk.core.common.utils;

import android.view.View;

public class MeasureUtils {
	
	/**
	 * 获取控件测量宽度
	 * @param view
	 * @return
	 */
	public static int getMeasureWidth(View view) {
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(widthMeasureSpec, widthMeasureSpec);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件测量高度
	 * @param view
	 * @return
	 */
	public static int getMeasureHeight(View view) {
		int heigthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(heigthMeasureSpec, heigthMeasureSpec);
		return view.getMeasuredHeight();
	}
}
