package com.juyuejk.core;

import android.content.Context;

import com.juyuejk.core.common.http.OkHttpClientManager;
import com.juyuejk.core.common.utils.SPUtil;
import com.juyuejk.core.common.utils.ToastUtils;

import java.io.IOException;

/**
 * Created by shopping on 2016/3/23.
 */
public class CoreInit {

    private static CoreInit coreInit = new CoreInit();

    public Context mContext;

    /**
     * 是否是Debug状态
     */
    public boolean isDebug ;

    public static CoreInit getInstance() {
        return coreInit;
    }

    /**
     * @param context
     * @param isDebug 是否处于debug状态
     */
    public void init(Context context, boolean isDebug) {
        init(context, isDebug, 1);
    }

    /**
     * @param context
     * @param isDebug 是否处于debug状态
     * @param NET_TYPE 0：外网测试   1：外网    2:仿真服务器  3：测试状态
     */
    public void init(Context context, boolean isDebug, int NET_TYPE) {
        ToastUtils.init(context);
        SPUtil.init(context);
        this.isDebug = isDebug;
        mContext = context;
        try {
            if (1 == NET_TYPE) {
                OkHttpClientManager.getHttpsDelegate().setCertificates(mContext.getAssets().open("httpsrequest.cer"));
            } else if (2 == NET_TYPE) {
                OkHttpClientManager.getHttpsDelegate().setCertificates(mContext.getAssets().open("www.joyjk.cn.crt"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
