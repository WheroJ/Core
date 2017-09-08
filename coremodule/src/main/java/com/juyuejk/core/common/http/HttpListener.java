package com.juyuejk.core.common.http;

import android.content.Context;
import android.text.TextUtils;

import com.juyuejk.core.common.utils.ProgressDlg;

public abstract class HttpListener {
	private boolean isShowDlg = true;
	private boolean haveCancel = false;

	/**
	 * 加载时显示的文字
	 */
	private String loadingText;

	protected Context context;

	/**
	 * 不显示进度条
	 */
	public HttpListener() {
		isShowDlg = false;
	}
	
	/**
	 * 显示进度条
	 * @param context 传入activity的context
	 */
	public HttpListener(Context context){
		this.context = context;
	}

	/**
	 * 显示进度条
	 * @param context 传入activity的context
	 * @param loadingText 加载中显示的文字
	 */
	public HttpListener(Context context, String loadingText){
		this.context = context;
		setLoadingText(loadingText);
	}

	public Context getContext() {
		return context;
	}

	public String getLoadingText() {
		return loadingText;
	}

	public void setLoadingText(String loadingText) {
		this.loadingText = loadingText;
	}

	public void setIsShowDlg(Boolean isShowDlg) {
		this.isShowDlg = isShowDlg;
	}

	/**
	 * 请求开始时
	 */
	public void OnStart() {
		if (isShowDlg) {
			if (!TextUtils.isEmpty(loadingText)){
				ProgressDlg.showDlg(context, loadingText);
			} else {
				ProgressDlg.showDlg(context);
			}
		}
	}

	/**
	 * 加载中
	 */
	public void onLoading() {
	}

	/**
	 * 加载数据成功(for 接口 2.0)
	 * @param result
	 * @param retCode
	 * @param retMessage
	 */
	public final void onResultSuccess(String result, String retCode,
			String retMessage) {
		OnEnd();
		if (!haveCancel) {
			if (result == null)
				this.OnSucess("", retCode, retMessage);
			else this.OnSucess(result, retCode, retMessage);
		}
	}


	/**
	 * 请求成功，且返回的参数不是空(for 接口 2.0)
	 *
	 * @param result
	 * @param retCode
	 * @param retMessage
	 */
	public abstract void OnSucess(String result, String retCode, String retMessage);

	public final void onResultFail(String retCode, String retMessage) {
		OnEnd();
		if (!haveCancel) {
			this.OnFail(retCode, retMessage);
		}
	}

	/**
	 * 请求失败(for 接口 2.0)
	 *
	 * @param retCode
	 * @param retMessage
	 */
	public abstract void OnFail(String retCode, String retMessage);

	/**
	 * 请求结束时
	 */
	public void OnEnd() {
		if (isShowDlg) {
			ProgressDlg.cancleDlg();
		}
	}

	/**
	 * 请求取消
	 */
	public final void onResultCancel() {
		OnEnd();
		haveCancel = true;
		onCancel();
	}

	/**
	 * 请求取消
	 */
	public void onCancel() {}

}
