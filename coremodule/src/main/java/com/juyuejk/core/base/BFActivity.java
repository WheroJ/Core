package com.juyuejk.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View.OnClickListener;

import com.juyuejk.core.annotation.Injector;

import java.util.LinkedList;
import java.util.List;

public abstract class  BFActivity extends FragmentActivity implements OnClickListener{

	private static List<Activity> mActivities = new LinkedList<Activity>();

	public static List<Activity> getmActivities() {
		return mActivities;
	}

	protected Activity thisContext ;

	/**
	 * 当前的布局ID
	 */
	private int layoutId ;

	protected FragmentManager fragmentManager;
	protected Fragment showFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		beforeInit();
		synchronized (mActivities) {
			mActivities.add(this);
		}
		layoutId = getLayoutId();
		setContentView(layoutId);
		thisContext = this;
		fragmentManager = getSupportFragmentManager();

//		Injector.inject(thisContext, thisContext);
	}

	/**
	 * 在界面填充布局之前需要执行的操作
	 */
	protected  void beforeInit(){}
	
	/**
	 * 替换Fragment
	 * @param fragment
	 */
	protected void replaceShow(Fragment fragment){
		try {
			if (showFragment == null) {
				fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
				showFragment = fragment;
			} else {
				fragmentManager.beginTransaction().hide(showFragment).show(fragment).commitAllowingStateLoss();
				showFragment = fragment;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换Fragment
	 * @param layoutId
	 * @param fragment
	 */
	protected void replaceShow(Fragment fragment, int layoutId){
		try {
			fragmentManager.beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
			showFragment = fragment;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换Fragment
	 * @param layoutId
	 * @param fragment
	 */
	protected void replaceShow(Fragment fragment, int layoutId, String tag){
		try {
			fragmentManager.beginTransaction().replace(layoutId, fragment, tag).commitAllowingStateLoss();
			showFragment = fragment;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Activity的布局的ID
	 */
	protected abstract int getLayoutId() ;

	public static void killAll() {
		List<Activity> clone;
		synchronized (mActivities) {
			clone = new LinkedList<>(mActivities);
		}
		for (Activity baseActivity : clone) {
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
