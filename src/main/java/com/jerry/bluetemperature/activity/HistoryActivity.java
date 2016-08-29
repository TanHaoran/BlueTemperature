package com.jerry.bluetemperature.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.adapter.HistoryAdapter;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.entity.History;
import com.jerry.bluetemperature.business.HistoryService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史体温记录
 */
@ContentView(R.layout.activity_history)
public class HistoryActivity extends Activity {

    private static final int DEFAULT_LIMIT_DAYS = 30;

    @ViewInject(R.id.listview)
    private ListView mListView;

    private HistoryAdapter mAdapter;
    private List<History> mHistories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        // 初始化数据
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 从数据库中查找体温数据
        HistoryService historyService = new HistoryService();
        // 默认查找最近30天内的所有体温数据
        mHistories = historyService.queryLastSomeDay(DEFAULT_LIMIT_DAYS);
    }

    /**
     * 初始化界面
     */
    private void initView() {

        mAdapter = new HistoryAdapter(this, mHistories, R.layout.item_history);
        if (mHistories != null) {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(History.KEY_HISTORY, mHistories.get(position));
                intent.putExtra(History.KEY_BUNDLE, bundle);
                startActivity(intent);
            }
        });

    }


    /**
     * 关闭页面
     *
     * @param v
     */
    @Event(R.id.ll_close)
    private void onClose(View v) {
        finish();
    }
}
