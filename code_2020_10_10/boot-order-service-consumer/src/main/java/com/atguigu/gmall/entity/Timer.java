package com.atguigu.gmall.entity;

public class Timer {
    private Integer id;

    private String ids;

    private String timertask;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids == null ? null : ids.trim();
    }

    public String getTimertask() {
        return timertask;
    }

    public void setTimertask(String timertask) {
        this.timertask = timertask == null ? null : timertask.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}