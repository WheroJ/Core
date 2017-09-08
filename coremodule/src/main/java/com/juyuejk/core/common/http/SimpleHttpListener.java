package com.juyuejk.core.common.http;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Administrator on 2016/3/24.
 * @author shopping
 *
 * 实现了取消网络请求时finish掉当前界面
 */
public abstract  class SimpleHttpListener extends HttpListener {

    public SimpleHttpListener(){
        super();
    }

    public SimpleHttpListener(Context context){
        super(context);
    }

    @Override
    public void onCancel() {
        if (context != null && context instanceof Activity){
            ((Activity)context).finish();
        }
    }
}
