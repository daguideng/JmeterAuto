package com.atguigu.gmall.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



public class Interface_current_report {
    private Integer id;

    private Integer uploadid;

    private String lastruntime;

    private String scriptname;

    private String threads;

    private String label;

    private String samples;

    private String ko;

    private String error;

    private String average;

    private String min;

    private String max;

    private String median;

    private String thpct90;

    private String thpct95;

    private String thpct99;

    private String throughput;

    private String ip;

    private String starttime;

    private String endtime;

    private String jtlpath;

    private String indexpath;

    private Integer state;

    private String received ;

    private String sent ;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUploadid() {
        return uploadid;
    }

    public void setUploadid(Integer uploadid) {
        this.uploadid = uploadid;
    }

    public String getLastruntime() {
        return lastruntime;
    }

    public void setLastruntime(String lastruntime) {
        this.lastruntime = lastruntime == null ? null : lastruntime.trim();
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname == null ? null : scriptname.trim();
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads == null ? null : threads.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples == null ? null : samples.trim();
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko == null ? null : ko.trim();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average == null ? null : average.trim();
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min == null ? null : min.trim();
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max == null ? null : max.trim();
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median == null ? null : median.trim();
    }

    public String getThpct90() {
        return thpct90;
    }

    public void setThpct90(String thpct90) {
        this.thpct90 = thpct90 == null ? null : thpct90.trim();
    }

    public String getThpct95() {
        return thpct95;
    }

    public void setThpct95(String thpct95) {
        this.thpct95 = thpct95 == null ? null : thpct95.trim();
    }

    public String getThpct99() {
        return thpct99;
    }

    public void setThpct99(String thpct99) {
        this.thpct99 = thpct99 == null ? null : thpct99.trim();
    }

    public String getThroughput() {
        return throughput;
    }

    public void setThroughput(String throughput) {
        this.throughput = throughput == null ? null : throughput.trim();
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received == null ? null : received.trim();
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent == null ? null : sent.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public String getJtlpath() {
        return jtlpath;
    }

    public void setJtlpath(String jtlpath) {
        this.jtlpath = jtlpath == null ? null : jtlpath.trim();
    }

    public String getIndexpath() {
        return indexpath;
    }

    public void setIndexpath(String indexpath) {
        this.indexpath = indexpath == null ? null : indexpath.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public Interface_current_report(){}


    // 使用Map<String, Object>的更健壮构造函数
    public Interface_current_report(Map<String, Object> data) {
        this.uploadid = getInteger(data, "uploadid");
        this.lastruntime = getString(data, "lastruntime");
        this.scriptname = getString(data, "scriptname");
        this.threads = getString(data, "threads");
        this.label = getString(data, "label");
        this.samples = getString(data, "samples");
        this.ko = getString(data, "ko");
        this.error = getString(data, "error");
        this.average = getString(data, "average");
        this.min = getString(data, "min");
        this.max = getString(data, "max");
        this.median = getString(data, "median");
        this.thpct90 = getString(data, "thpct90");
        this.thpct95 = getString(data, "thpct95");
        this.thpct99 = getString(data, "thpct99");
        this.throughput = getString(data, "throughput");
        this.received = getString(data, "received");
        this.sent = getString(data, "sent");
        this.ip = getString(data, "ip");
        this.starttime = getString(data, "starttime");
        this.endtime = getString(data, "endtime");
        this.jtlpath = getString(data, "jtlpath");
        this.indexpath = getString(data, "indexpath");
        this.state = getInteger(data, "state", 0); // 默认值为0
    }

    // 安全获取String类型值
    private String getString(Map<String, Object> data, String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }
        Object value = data.get(key);
        if (value == null) {
            return null;
        }
        return value.toString().trim();
    }

    // 安全获取Integer类型值，支持默认值
    private Integer getInteger(Map<String, Object> data, String key, Integer defaultValue) {
        if (data == null || !data.containsKey(key)) {
            return defaultValue;
        }
        Object value = data.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            if (value instanceof Integer) {
                return (Integer) value;
            } else {
                return Integer.parseInt(value.toString());
            }
        } catch (NumberFormatException | ClassCastException e) {
            System.err.println("Failed to parse " + key + ": " + e.getMessage());
            return defaultValue;
        }
    }

    // 安全获取Integer类型值
    private Integer getInteger(Map<String, Object> data, String key) {
        return getInteger(data, key, null);
    }



    /**
     * 通用设置字段值的方法
     * @param fieldName 字段名
     * @param value 字段值
     * @return 是否设置成功
     */
    public boolean setObject(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            
            // 根据字段类型进行适当转换
            if (field.getType() == String.class) {
                field.set(this, value == null ? null : value.toString().trim());
            } else if (field.getType() == Integer.class) {
                if (value == null) {
                    field.set(this, null);
                } else if (value instanceof Integer) {
                    field.set(this, value);
                } else {
                    try {
                        field.set(this, Integer.parseInt(value.toString()));
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse " + fieldName + " to Integer: " + e.getMessage());
                        return false;
                    }
                }
            } else {
                // 对于其他类型，直接设置（如果类型不匹配会抛出异常）
                field.set(this, value);
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Failed to set field " + fieldName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * 使用Map设置多个字段值
     * @param data 包含字段名和值的Map
     */
    public void setObject(Map<String, Object> data) {
        if (data == null) return;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            setObject(entry.getKey(), entry.getValue());
        }
    }
}