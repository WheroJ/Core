package com.juyuejk.core.common.http;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.juyuejk.core.common.downloadProgress.ProgressHelper;
import com.juyuejk.core.common.downloadProgress.ProgressListener;
import com.juyuejk.core.common.http.handler.HttpHandler;
import com.juyuejk.core.common.utils.DesUtil;
import com.juyuejk.core.common.utils.LogPrinter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class NetManager{

	public NetManager(HttpListener listener) {
		this.handler = new HttpHandler(listener);
	}
	
	/**
	 * 一般的基类中需要进行网络请求时，使用传入Looper的形式来创建一个Handler
	 * @param listener
	 * @param looper  主界面中的上下文对象中获取到的mContext.getMainLooper()
	 */
	public NetManager(HttpListener listener, Looper looper) {
		this.handler = new HttpHandler(listener, looper);
	}

	public NetManager() {
		HttpListener listener = new HttpListener() {
			@Override
			public void OnSucess(String result, String retCode, String retMessage) {

			}

			@Override
			public void OnFail(String retCode, String retMessage) {

			}
		};
		this.handler = new HttpHandler(listener);
	}

	private static final String NET_MANAGER = "NetManager";
	private String resultjson = null;
	private String result = null;
	private HttpHandler handler;
	protected int retCode = -1;
	protected String retMessage = "";
	public static String PORT_1 = "1.0";
	public static String PORT_2 = "2.0";

	/**
	 * 下载文件，提供下载回调接口
	 * @param url 下载的地址
	 * @param destDir 下载带改文件夹下
	 * @param uiProgressListener  下载期间回调进度监听，ProgressListener是非UI线程回调， UIProgressListener为UI线程回调
	 */
	public void download(String url, String destDir, ProgressListener uiProgressListener) {

		handler.setHandlerUrl(url);

		final File file = new File(destDir, getFileNameFromUrl(url));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		OkHttpClient client = new OkHttpClient();
		//构造请求
		final Request request = new Request.Builder().url(url).tag(url).build();
		final Message msg = Message.obtain();

		//包装Response使其支持进度回调
		ProgressHelper.addProgressResponseListener(client, uiProgressListener).newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Request request, IOException e) {
				handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
			}

			@Override
			public void onResponse(Response response) throws IOException {
				InputStream inputStream = null;
				FileOutputStream fos = null;
				try {
					inputStream = response.body().byteStream();
					fos = new FileOutputStream(file);

					byte[] bytes = new byte[1024];
					int length = 0;
					while ((length = inputStream.read(bytes)) != -1) {
						fos.write(bytes, 0, length);
						fos.flush();
					}

					msg.what = HttpConstant.SUCCESS;
					msg.obj = file.getAbsolutePath();
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					if (fos != null) {
						fos.close();
					}
				}
			}
		});
	}

	/**
	 * 下载文件，不显示进度条
	 * @param url 下载的地址
	 */
	public void download(final String url, final String destDir) {

		handler.setHandlerUrl(url);
		final Message msg = Message.obtain();

		OkHttpClientManager.getInstance().getDownloadDelegate().downloadAsyn(url, destDir, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onBefore(Request request) {
				super.onBefore(request);
				handler.sendEmptyMessage(HttpConstant.LOADING);
			}

			@Override
			public void onError(Request request, Exception e) {
				handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
			}

			@Override
			public void onResponse(String response) {
				//如果成功了，response就是改下载文件的绝对路径
				msg.what = HttpConstant.SUCCESS;
				msg.obj = response;
				handler.sendMessage(msg);
			}
		});

	}

	/**
	 * 发送异步post请求，替代原来的load方法
	 * @param url  访问的url地址
	 * @param params  请求的参数
	 * @param version 访问的网络接口版本：1.0  2.0
	 */
	public void post(final String url, final String params, final String version){
		handler.setHandlerUrl(url);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					final Message msg = Message.obtain();
					OkHttpClientManager.getInstance().getPostDelegate().postAsyn(url, params, new OkHttpClientManager.ResultCallback<String>() {
						@Override
						public void onBefore(Request request) {
							super.onBefore(request);
							handler.sendEmptyMessage(HttpConstant.LOADING);
						}

						@Override
						public void onError(Request request, Exception e) {
							if(e != null && e.getMessage() != null && e.getMessage().contains("Network is unreachable")){
								handler.sendEmptyMessage(HttpConstant.NET_ERROR);
							}else if(e != null && (e instanceof SocketTimeoutException)){
								handler.sendEmptyMessage(HttpConstant.NET_ERROR);
							}else if(e != null && (e instanceof UnknownHostException)){
								handler.sendEmptyMessage(HttpConstant.NET_ERROR);
							}else{
								handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
							}
						}

						@Override
						public void onResponse(String response) {
							if (PORT_1.equals(version)){
								simpleParseResult(msg, response);
							} else if (PORT_2.equals(version)){
								simpleParseResult2(msg, response);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
				}
			}
		};
		ThreadManager.getInstance().executeLongTask(runnable);
	}

	/**
	 * 发送异步post请求，替代原来的load方法
	 * @param url  访问的url地址
	 * @param params  请求的参数
	 */
	public void post(final String url, final String params){
		post(url, params, PORT_2);//默认访问2.0
	}

	/**
	 * 使用okhttp上传文件，替代原来的uploadFile方法
	 * @param url  上传地址
	 * @param bodyFile 要上传的文件对象
	 */
	public void uploadFile(String url, File bodyFile){
		try {
			handler.setHandlerUrl(url);
			OkHttpClientManager.getUploadDelegate().postAsyn(url, bodyFile.getName(), bodyFile, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onBefore(Request request) {
					super.onBefore(request);
					handler.sendEmptyMessage(HttpConstant.LOADING);
				}

                @Override
                public void onError(Request request, Exception e) {
					handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
                }

                @Override
                public void onResponse(String response) {
					uploadRetParse(response);
				}
            }, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用okhttp上传文件，替代原来的uploadFile方法
	 * @param url  上传地址
	 * @param bodyFiles 要上传的所有文件文件对象
	 */
	public void uploadFile(String url, File[] bodyFiles){
		try {
			handler.setHandlerUrl(url);
			String[] fileKeys = new String[bodyFiles.length];
			for (int i = 0; i < bodyFiles.length; i++) {
				fileKeys[i] = bodyFiles[i].getName();
			}

			OkHttpClientManager.getUploadDelegate().postAsyn(url, fileKeys, bodyFiles, null, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onBefore(Request request) {
					super.onBefore(request);
					handler.sendEmptyMessage(HttpConstant.LOADING);
				}

				@Override
				public void onError(Request request, Exception e) {
					handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
				}

				@Override
				public void onResponse(String response) {
					uploadRetParse(response);
				}
			}, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取文件的MIMEtype
	 * @param path
	 * @return
	 */
	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(path);
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		return contentTypeFor;
	}

	/**
	 * 根据1.0接口返回规范编写的解析
	 * @param msg
	 * @param response
	 */
	private void simpleParseResult(Message msg, String response) {
		if (response == null) {// 当返回值为null时，不需要进行下面的判断
			handler.sendEmptyMessage(HttpConstant.NET_ERROR);
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(response);
			retCode = jsonObject.optInt("retCode");
			retMessage = jsonObject.optString("retMessage");
			resultjson = jsonObject.optString("result");
			LogPrinter.i(NET_MANAGER, "接口：" + handler.getHandlerUrl() + ", 返回结果: retCode:" + retCode + "  retMessage:" + retMessage);

			if (TextUtils.isEmpty(resultjson)) {
				msg.arg1 = retCode;
				msg.obj = resultjson;
				msg.getData().putString("retMessage", retMessage);
				msg.what = HttpConstant.SUCCESS;
				handler.sendMessage(msg);
				return;
			}
			resultjson = DesUtil.decrypt(resultjson);
			LogPrinter.i(NET_MANAGER, "接口：" + handler.getHandlerUrl() + ",返回结果: resultjson = " + resultjson);

			msg.arg1 = retCode;
			msg.what = HttpConstant.SUCCESS;
			msg.getData().putString("retMessage", retMessage);
			msg.obj = resultjson;
			handler.sendMessage(msg);
		} catch (Exception e) {
			LogPrinter.i(NET_MANAGER, "接口："+handler.getHandlerUrl() + "返回结果: result:" + response);
			msg.arg1 = retCode;
			msg.obj = retMessage;
			msg.what = HttpConstant.LOADING_FAIL;
			handler.sendMessage(msg);
			e.printStackTrace();
		}
	}

	/**
	 * 根据2.0接口返回规范编写的解析
	 * @param msg
	 * @param response
	 */
	private void simpleParseResult2(Message msg, String response) {
		if (response == null) {// 当返回值为null时，不需要进行下面的判断
			handler.sendEmptyMessage(HttpConstant.NET_ERROR);
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(response);
			int ret_code = jsonObject.optInt("ret_code");
			if (ret_code == 1){//失败
				String err_msg = jsonObject.optString("err_msg");
				String err_code = jsonObject.optString("err_code");
				msg.getData().putString("err_msg", err_msg);
				msg.getData().putString("err_code", err_code);
				msg.what = HttpConstant.LOADING_FAIL;
				handler.sendMessage(msg);
				LogPrinter.i(NET_MANAGER, "接口：" + handler.getHandlerUrl() + ",获取数据失败：接口返回结果: err_code:" + err_code + "  err_msg:" + err_msg);
				return ;
			}

			String return_msg = jsonObject.optString("return_msg");
			return_msg = DesUtil.decrypt(return_msg);
			LogPrinter.i(NET_MANAGER, "接口：" + handler.getHandlerUrl() + ",获取数据成功：接口返回结果: resultjson = " + return_msg);

			JSONObject returnMsg = new JSONObject(return_msg);
			String business_code = returnMsg.optString("business_code");
			String result = returnMsg.optString("result");
			if ("null".equals(result)){
				result = null;
			}
			String business_message = returnMsg.optString("business_message");
			if (!HttpConstant.RES_SUCCESS.equals(business_code)){//业务逻辑失败
				msg.getData().putString("business_code", business_code);
				msg.getData().putString("business_message", business_message);
				msg.what = HttpConstant.SUCCESS;
				msg.obj = result;
				handler.sendMessage(msg);
			} else {
				msg.getData().putString("business_code", business_code);
				msg.getData().putString("business_message", business_message);
				msg.what = HttpConstant.SUCCESS;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			LogPrinter.i(NET_MANAGER, "接口："+handler.getHandlerUrl() + "返回结果: result:" + response);
			msg.getData().putString("err_msg", HttpConstant.PARSE_EXCEPTION);
			msg.getData().putString("err_code", HttpConstant.EXCEPTION + "");
			msg.what = HttpConstant.LOADING_FAIL;
			handler.sendMessage(msg);
			e.printStackTrace();
		}
	}

	/**
	 * 文件和图片上传接口数据解析
	 * @param result
	 */
	private void uploadRetParse(String result) {
		try {
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				String type = jsonObject.optString("type");
				Message msg = Message.obtain();
				if ("success".equals(type)){
					msg.getData().putString("business_code", type);
					msg.getData().putString("business_message", "保存成功");
					msg.what = HttpConstant.SUCCESS;
					msg.obj = result;
					handler.sendMessage(msg);
				} else {
					if ("save err".equals(type)) {
						msg.getData().putString("business_code", type);
						msg.getData().putString("business_message", "保存失败");
						msg.what = HttpConstant.LOADING_FAIL;
						handler.sendMessage(msg);
					} else if ("FileUploadException".equals(type)){
						msg.getData().putString("business_code", type);
						msg.getData().putString("business_message", "文件上传异常");
						msg.what = HttpConstant.LOADING_FAIL;
						handler.sendMessage(msg);
					} else {
						msg.getData().putString("business_code", type);
						msg.what = HttpConstant.LOADING_FAIL;
						handler.sendMessage(msg);
					}
				}

			} else {
				handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(HttpConstant.LOADING_FAIL);
		}
	}


	private String getFileNameFromUrl(String url){
		try {
			int indexOf = url.lastIndexOf("/");
			if (indexOf >= 0){
				return url.substring(indexOf + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
