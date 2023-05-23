package com.github.liaoheng.common.ui.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author liaoheng
 * @version 2015-11-27 15:14
 */
public class PagerTab {
    private int id;
    private String name;
    private Object object;
    /**
     * 导航排序索引, -1:未添加到导航中
     */
    private int index = -1;//排序索引
    /**
     * 更新时间
     */
    private long updateTime;//更新时间
    private boolean isTemp;
    private boolean isLock;
    private List<PagerTab> tabs;

    public PagerTab() {
    }

    public <T> PagerTab(int id, String name) {
        this(id, name, null);
    }

    public <T> PagerTab(int id, String name, T object) {
        this.id = id;
        this.name = name;
        this.object = object;
    }

    public <T> void setTabs(int id, String name, T object) {
        if (tabs == null) {
            tabs = new LinkedList<>();
        }
        PagerTab tab = new PagerTab(id, name, object);
        tabs.add(tab);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T) object;
    }

    public <T> void setObject(T object) {
        this.object = object;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public List<PagerTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<PagerTab> tabs) {
        this.tabs = tabs;
    }
}
