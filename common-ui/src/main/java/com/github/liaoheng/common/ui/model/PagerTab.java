package com.github.liaoheng.common.ui.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author liaoheng
 * @version 2015-11-27 15:14
 */
public class PagerTab {
    private String         name;
    private Object         object;
    private List<PagerTab> tabs;

    public PagerTab() {
    }

    public <T> PagerTab(String name, T object) {
        this.name = name;
        this.object = object;
    }

    public void setTabs(String name, Object object) {
        if (tabs == null)
            tabs = new LinkedList<>();
        PagerTab tab = new PagerTab(name, object);
        tabs.add(tab);
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

    public List<PagerTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<PagerTab> tabs) {
        this.tabs = tabs;
    }
}
