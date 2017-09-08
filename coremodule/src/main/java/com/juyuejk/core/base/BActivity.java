package com.juyuejk.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.juyuejk.core.annotation.Injector;

import java.util.LinkedList;
import java.util.List;

public abstract class BActivity extends Activity implements OnClickListener {

	private static List<BActivity> mActivities = new LinkedList<BActivity>();

	public static List<BActivity> getmActivities() {
		return mActivities;
	}

	protected Activity thisContext;

	/**
	 * 当前的布局ID
	 */
	private int layoutId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		beforeInit();
		layoutId = getLayoutId();
		setContentView(layoutId);
		synchronized (mActivities) {
			mActivities.add(this);
		}
		thisContext = this;

//		Injector.inject(thisContext, thisContext);
	}

	/**
	 * 在界面填充布局之前需要执行的操作
	 */
	protected  void beforeInit(){}

	/**
	 * 设置Activity的布局的ID
	 */
	protected abstract int getLayoutId();

	public static void killAll() {
		List<BActivity> clone;
		synchronized (mActivities) {
			clone = new LinkedList<>(mActivities);
		}
		for (BActivity baseActivity : clone) {
			baseActivity.finish();
			mActivities.remove(baseActivity);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		thisContext = this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		synchronized (mActivities) {
			mActivities.remove(this);
		}
	}
}
