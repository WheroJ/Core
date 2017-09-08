package com.juyuejk.core.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {
    private static Toast myToast;
    private static Context context;
    private static final String TAG = "ToastUtils";

    public static void init(Context con) {
        context = con;
    }

    /**
     * 输入需要提示的内容即可
     *
     * @param text
     */
    public static void show(String text) {
        if (myToast == null) {
            myToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }

        if (!TextUtils.isEmpty(text)){
            myToast.setText(text);
            myToast.show();
        }
    }

    /**
     * 输入需要提示的内容即可
     *
     * @param text
     */
    public static void showLong(String text) {
        if (myToast == null) {
            myToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }

        if (!TextUtils.isEmpty(text)){
            myToast.setText(text);
            myToast.show();
        }
    }
}
