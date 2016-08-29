package com.jerry.bluetemperature.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.adapter.HistoryAdapter;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.entity.History;
import com.jerry.bluetemperature.util.DateUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 历史详细界面
 */
@ContentView(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity {

    private static final int GRID_COLOR = 0xff9de9ff;


    @ViewInject(R.id.chart)
    private LineChart chart;

    @ViewInject(R.id.tv_high_fever)
    private TextView mHighFeverText;


    @ViewInject(R.id.tv_start_time)
    private TextView mStartText;


    @ViewInject(R.id.tv_duration_time)
    private TextView mDurationText;


    @ViewInject(R.id.tv_end_time)
    private TextView mEndText;


    private History mHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        // 初始化数据
        initDate();
        initView();
    }


    /**
     * 初始化数据
     */
    private void initDate() {
        Bundle bundle = getIntent().getBundleExtra(History.KEY_BUNDLE);
        if (bundle != null) {
            mHistory = (History) bundle.getSerializable(History.KEY_HISTORY);
        }
    }


    /**
     * 初始化界面
     */
    private void initView() {
        initChart();
        // 设置最高体温
        mHighFeverText.setText(HistoryAdapter.getMaxValue(mHistory.getValues().split(";")) + "°C");
        // 设置开始时间
        mStartText.setText(DateUtil.getYMDHMS(mHistory.getStart()));
        // 设置持续时间
        mDurationText.setText(DateUtil.calculateDuration(mHistory.getStart(), mHistory.getEnd()));
        // 设置结束时间
        mEndText.setText(DateUtil.getYMDHMS(mHistory.getEnd()));
    }

    private void initChart() {
        LineData mLineData = getLineData(36);
//        showChart(chart, mLineData, Color.rgb(114, 188, 223));
        showChart(chart, mLineData);

        //上面右边效果图的部分代码，设置X轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setEnabled(true);
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        xAxis.setDrawAxisLine(true);


        // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(GRID_COLOR);
        xAxis.setYOffset(15);
        xAxis.setXOffset(-100);


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(18);
        yAxis.setGridColor(GRID_COLOR);
        yAxis.setAxisLineColor(GRID_COLOR);
        // 设置y轴的坐标最大值和最小值
        yAxis.setAxisMinValue(20.0f);
        yAxis.setAxisMaxValue(45.0f);
//        chart.getAxisRight().setTextColor(Color.TRANSPARENT);
        chart.getAxisRight().setEnabled(false);
    }

    /**
     * 生成图表数据
     *
     * @param count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData(int count) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        String[] values = mHistory.getValues().split(";");
        for (int i = 0; i < values.length; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add(getXValue(i));
            yValues.add(new Entry(Float.parseFloat(values[i]), i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        String title = DateUtil.getMD(mHistory.getStart()) + "体温图";
        LineDataSet lineDataSet = new LineDataSet(yValues, title);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        // 设置坐标值是否显示
        lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setValueTextColor(Color.WHITE);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets


        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData) {


        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("没有查到体温数据！");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(10);// 字体
        mLegend.setTextSize(12);
        mLegend.setTextColor(Color.WHITE);// 颜色
        mLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(1000); // 立即执行的动画,x轴
    }

    /**
     * 获取x轴时间
     *
     * @param position
     * @return
     */
    private String getXValue(int position) {
        Date start = mHistory.getStart();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.MINUTE, position);
        Date value = calendar.getTime();
        return DateUtil.getHM(value);
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
