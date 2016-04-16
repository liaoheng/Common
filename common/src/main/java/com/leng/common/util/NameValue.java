// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package com.leng.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple name-value holder.
 */
public class NameValue<N, V> {

    protected N name;
    protected V value;
    private List<NameValue<N, V>> items;

    public NameValue() {
    }

    public NameValue(N name, V value) {
        this.name = name;
        this.value = value;
    }

    public void setItem(N name, V value){
        if (items==null) {
            items=new ArrayList<>();
        }
        items.add(new NameValue<>(name,value));
    }

    public List<NameValue<N, V>> getItems() {
        return items;
    }

    public void setItems(List<NameValue<N, V>> items) {
        this.items = items;
    }

    /**
     * Sets name.
     */
    public void setName(N name) {
        this.name = name;
    }

    /**
     * Returns name.
     */
    public N getName() {
        return name;
    }

    /**
     * Returns value.
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets value.
     */
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NameValue)) {
            return false;
        }
        NameValue that = (NameValue) o;

        Object n1 = getName();
        Object n2 = that.getName();

        if (n1 == n2 || (n1 != null && n1.equals(n2))) {
            Object v1 = getValue();
            Object v2 = that.getValue();
            if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (name == null ? 0 : name.hashCode()) ^
                (value == null ? 0 : value.hashCode());
    }

}