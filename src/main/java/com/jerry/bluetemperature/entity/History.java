package com.jerry.bluetemperature.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jerry on 2016/7/5.
 * 历史体温数据
 */
@Table(name = "history")
public class History implements Serializable {

    public static final String KEY_HISTORY = "history";
    public static final String KEY_BUNDLE = "bundle";

    public static final String NORMAL = "正常";
    public static final String HIGHER = "偏高";
    public static final String LOWER = "偏低";

    public static final float NORMAL_LOW = 36.0f;
    public static final float NORMAL_HIGH = 37.0f;

    /**
     * 记录id（主键、自增长）
     */
    @Column(name = "id", isId = true, autoGen = true)
    private int id;

    /**
     * 测量开始时间
     */
    @Column(name = "start")
    private Date start;
    /**
     * 测量结束时间
     */
    @Column(name = "end")
    private Date end;
    /**
     * 体温值（多个体温中间使用“；”分割）
     */
    @Column(name = "values")
    private String values;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }


    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", values='" + values + '\'' +
                '}';
    }
}
