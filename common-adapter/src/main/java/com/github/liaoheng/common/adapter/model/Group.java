package com.github.liaoheng.common.adapter.model;

/**
 * @author liaoheng
 * @version 2017-01-25 10:06
 */
public class Group<T> {
    public enum GroupType {
        HEADER(1), FOOTER(2), CONTENT(0);
        private int code;

        GroupType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public Group(GroupType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Group(GroupType type, Object other) {
        this.type = type;
        this.other = other;
    }

    public Group(T content) {
        this.type = GroupType.CONTENT;
        this.content = content;
    }

    private GroupType type;
    private String    text;
    private T         content;
    private Object    other;

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }
}
