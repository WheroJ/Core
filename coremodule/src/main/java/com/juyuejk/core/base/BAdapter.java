package com.juyuejk.core.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BAdapter<T> extends BaseAdapter {

	protected List<T> datas;
	protected Context mContext;

	public BAdapter(List<T> datas, Context context) {
		this.datas = new ArrayList<>();
		if (datas != null && datas.size() > 0) {
			this.datas.addAll(datas);
		}
		this.mContext = context;
	}
	
	/**
	 * 更新数据
	 * @param datas
	 */
	public void updateDataSet(List<T> datas){
		if (datas != null && this.datas != null) {
			this.datas.clear();
			this.datas.addAll(datas);
		} else if (this.datas == null){
			this.datas = new ArrayList<>();
			if (datas != null)
				this.datas.addAll(datas);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (datas == null)
			return 0;
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		if (datas == null)
			return null;
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent) ;

}
