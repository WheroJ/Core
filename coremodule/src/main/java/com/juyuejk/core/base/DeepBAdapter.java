package com.juyuejk.core.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public abstract class DeepBAdapter<T> extends BAdapter<T>{

    public DeepBAdapter(List<T> datas, Context context){
        super(datas, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = newView(position, parent);
        bindView(position, convertView, parent);
        return convertView;
    }

    /**
     * 填充数据到界面
     * @param position
     * @param convertView
     * @param parent
     */
    protected abstract void bindView(int position, View convertView, ViewGroup parent);

    /**
     * 新建一个View填充到listView中
     * @param position
     * @param parent
     * @return
     */
    protected abstract View newView(int position, ViewGroup parent);
}
