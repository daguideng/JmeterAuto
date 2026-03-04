package com.atguigu.gmall.entity;

public class Jmeter_perf_agent_source {
    private Integer id;

    private String ipaddress;

    private String username;

    private String osname;

    private String hostname;

    private String cpucount;

    private String cpufree;

    private String systemcpuusage;

    private String memory;

    private String diskram;

    private String javaversion;

    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOsname() {
        return osname;
    }

    public void setOsname(String osname) {
        this.osname = osname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCpucount() {
        return cpucount;
    }

    public void setCpucount(String cpucount) {
        this.cpucount = cpucount;
    }

    public String getCpufree() {
        return cpufree;
    }

    public void setCpufree(String cpufree) {
        this.cpufree = cpufree;
    }

    public String getSystemcpuusage() {
        return systemcpuusage;
    }

    public void setSystemcpuusage(String systemcpuusage) {
        this.systemcpuusage = systemcpuusage;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDiskram() {
        return diskram;
    }

    public void setDiskram(String diskram) {
        this.diskram = diskram;
    }

    public String getJavaversion() {
        return javaversion;
    }

    public void setJavaversion(String javaversion) {
        this.javaversion = javaversion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public Jmeter_perf_agent_source(){}


    public Jmeter_perf_agent_source(String ipaddress, String username, String osname, String hostname, String cpucount, String cpufree, String systemcpuusage, String memory, String diskram, String javaversion, String time) {
        this.ipaddress = ipaddress;
        this.username = username;
        this.osname = osname;
        this.hostname = hostname;
        this.cpucount = cpucount;
        this.cpufree = cpufree;
        this.systemcpuusage = systemcpuusage;
        this.memory = memory;
        this.diskram = diskram;
        this.javaversion = javaversion;
        this.time = time;
    }
}