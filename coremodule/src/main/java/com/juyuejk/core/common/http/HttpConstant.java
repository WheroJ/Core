package com.juyuejk.core.common.http;

/**
 * Created by Administrator on 2016/3/21.
 */
public class HttpConstant {
    /**
     * 网络请求状态码
     */
    public static final int LOADING = 0x01;
    public static final int SUCCESS = 0x02;
    public static final int LOADING_FAIL = 0x03;
    public static final int NET_ERROR = 0x04;
    public static final int NET_CANCEL = 0x05;
    public static final int EXCEPTION = 0x06;

    /**
     * 网络请求结果：成功获取相应数据
     */
    public static final String RES_SUCCESS = "SUCCESS";

    /**
     * 网络连接超时时间
     */
    public static final int NET_TIMEOUT = 30000;

    /**
     * 网络访问返回提示
     */
    public static final String LOADING_FAIL_MSG = "数据格式异常";//数据格式异常
    public static final String NOT_NET = "网络连接不可用";//没有连网，或者网络状况不好
    public static final String PARSE_EXCEPTION = "系统忙碌，请稍后再试";//没有连网，或者网络状况不好

    /**
     * 网络请求取消Action
     */
    public static final String NET_CANCRL = "cancel_network";
    public static final String BACK_DOWN = "base_down_KEYCODE_BACK";
}
