package com.leng.common.plus.model;

/**
 * Spinner 数据
 *
 * @author liaoheng
 * @version 2015年9月22日
 */
public class SpinnerItem {
    private String name;
    private Object object;

    public SpinnerItem() {
    }

    public SpinnerItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
