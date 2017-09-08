package com.juyuejk.core.common.http.handler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.juyuejk.core.CoreInit;
import com.juyuejk.core.common.http.HttpConstant;
import com.juyuejk.core.common.http.HttpListener;
import com.juyuejk.core.common.http.OkHttpClientManager;

public class HttpHandler extends Handler {

	private NetCancelBroadReceiver receiver;
	private HttpListener httpListener;
	private Context mContext;

	/**
	 * 当前Handler处理的url连接的地址字符串
	 */
	private String handlerUrl;

	public HttpHandler(HttpListener listener) {
		super(Looper.getMainLooper());
		initHandler(listener);
	}

	/**
	 * 一般的基类中创建Handler使用的构造方法
	 * @param listener
	 * @param looper
	 */
	public HttpHandler(HttpListener listener, Looper looper) {
		super(looper);
		initHandler(listener);
	}

	private void initHandler(HttpListener listener) {
		this.httpListener = listener;
		this.mContext = CoreInit.getInstance().mContext;

		if (receiver == null && mContext != null){
			receiver = new NetCancelBroadReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(HttpConstant.NET_CANCRL);
//			filter.addAction(HttpConstant.BACK_DOWN);
			mContext.registerReceiver(receiver, filter);
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case HttpConstant.LOADING:
			httpListener.OnStart();
			httpListener.onLoading();
			break;
		case HttpConstant.LOADING_FAIL:
			String err_msg = msg.getData().getString("err_msg");
			String err_code = msg.getData().getString("err_code");
			if (!TextUtils.isEmpty(err_code)){
				httpListener.onResultFail(err_code, err_msg);
			} else if(msg.arg1 != 0 && !TextUtils.isEmpty((String) msg.obj)){
				httpListener.onResultFail(msg.arg1 + "", (String) msg.obj);
			}else{
				httpListener.onResultFail(HttpConstant.LOADING_FAIL + "", HttpConstant.PARSE_EXCEPTION);
			}
			unregisterReceiver();
			break;
		case HttpConstant.NET_ERROR:
			httpListener.onResultFail(HttpConstant.NET_ERROR + "", HttpConstant.NOT_NET);
			unregisterReceiver();
			break;
		case HttpConstant.SUCCESS:
			String retMessage = msg.getData().getString("retMessage");
			String business_code = msg.getData().getString("business_code");
			String business_message = msg.getData().getString("business_message");
			if (!TextUtils.isEmpty(business_code)){
				httpListener.onResultSuccess((String) msg.obj, business_code, business_message);
			} else {
				httpListener.onResultSuccess((String) msg.obj, msg.arg1 + "", retMessage == null ? "" : retMessage);
			}
			unregisterReceiver();
			break;
		case HttpConstant.NET_CANCEL:// 网络请求取消
			httpListener.onResultCancel();
			unregisterReceiver();
			break;
		}
	}

	private void unregisterReceiver() {
		if (receiver != null && mContext != null){
			mContext.unregisterReceiver(receiver);
			receiver = null;
		}
	}

	public String getHandlerUrl() {
		return handlerUrl;
	}

	public void setHandlerUrl(String handlerUrl) {
		this.handlerUrl = handlerUrl;
	}

	class NetCancelBroadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String tag = getHandlerUrl();
			if (!TextUtils.isEmpty(action) && HttpConstant.NET_CANCRL.equals(action)) {
				//取消网络请求
				HttpHandler.this.httpListener.onResultCancel();
				if (!TextUtils.isEmpty(tag)){
					OkHttpClientManager.cancelTag(tag);
				}
			}
		}
	}
}
