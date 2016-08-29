package com.jerry.bluetemperature.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.entity.Alarm;
import com.jerry.bluetemperature.view.SwipeListView;

import java.util.List;

public class AlarmAdapter extends CommonAdapter<Alarm> {

    private SwipeListView mSwipeListView;


    public AlarmAdapter(Context context, List<Alarm> mDatas, int itemLayoutId, SwipeListView swipeListView) {
        super(context, mDatas, itemLayoutId);
        mSwipeListView = swipeListView;
    }

    @Override
    public void convert(ViewHolder helper, final Alarm item) {
        // 设置删除按钮的点击事件
        helper.getView(R.id.id_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(item);
                notifyDataSetChanged();
                /**
                 * 关闭SwipeListView
                 * 不关闭的话，刚删除位置的item存在问题
                 * 在监听事件中onListChange中关闭，会出现问题
                 */
                mSwipeListView.closeOpenedItems();
            }
        });


        // 开关按钮
        ImageView switchImg = helper.getView(R.id.iv_switch);
        switchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

} 