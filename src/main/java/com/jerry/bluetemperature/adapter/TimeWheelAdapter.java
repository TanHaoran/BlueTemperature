package com.jerry.bluetemperature.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerry.bluetemperature.R;
import com.wx.wheelview.adapter.BaseWheelAdapter;

/**
 * 时间选择的适配器
 */
public class TimeWheelAdapter extends BaseWheelAdapter<String> {
    private Context mContext;
    private LayoutInflater mInflater;

    public TimeWheelAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_time, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}