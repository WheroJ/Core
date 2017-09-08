package com.juyuejk.core.common.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.KeyEvent;

import com.juyuejk.core.common.http.HttpConstant;

/**
 * 加载数据时显示进度对话框 context要传getParent()
 * 
 * @author shopping
 *
 */
public class ProgressDlg {
	private static ProgressDialog progressDialog;

	/**
	 * 每一次调用showDlg方法该计数器增加1，调用cancelDlg方法该计数器减小1
	 */
	private static int openCount ;

	public static void showDlg(final Context context) {
		if (!(context != null && context instanceof Activity)) {
			return;
		}
		openCount ++;
		if (isShowing()) {
			return;
		}

		if (!((Activity) context).isFinishing()) {
			progressDialog = new ProgressDialog(context);
			// 设置进度条风格，风格为圆形，旋转的
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置ProgressDialog 标题
			// progressDialog.setTitle("提示");
			// 设置ProgressDialog 提示信息
			progressDialog.setMessage("正在获取数据，请稍后...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			// 设置ProgressDialog 标题图标
			// progressDialog.setIcon(R.drawable.wait);
			// 设置ProgressDialog 的进度条是否不明确
			progressDialog.setIndeterminate(false);
			progressDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return ProgressDlg.onKeyDown(context, keyCode);
				}

			});

			progressDialog.show();
		}
	}

	private static boolean onKeyDown(final Context context, int keyCode) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Intent intent = new Intent();
				intent.setAction(HttpConstant.NET_CANCRL);
				context.sendBroadcast(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void showDlg(final Context context, String text) {

		if (!(context != null && context instanceof Activity)) {
			return;
		}

		openCount ++;

		if (isShowing()) {
			return;
		}

		if (!((Activity) context).isFinishing()) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(text);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return ProgressDlg.onKeyDown(context, keyCode);
				}

			});

			progressDialog.show();
		}
	}

	public static void cancleDlg() {
		try {
			openCount--;
			if (openCount <= 0){//当show的Dlg都调用了cancelDlg方法之后该Dailog才会dismiss
                openCount = 0;
                if (isShowing()) {
                    progressDialog.dismiss();
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isShowing() {
		if (progressDialog == null) {
			return false;
		}
		return progressDialog.isShowing();
	}
}