package com.atguigu.gmall.entity;

public class Jmeter_agentip_states  {
    private Integer id;

    private String ipaddress;

    private String states;

    private String type ;

    private String time;

    private String runusername ;

    private Integer runstate ;

    public String getRunusername() {
        return runusername;
    }

    public void setRunusername(String runusername) {
        this.runusername = runusername;
    }

    public Integer getRunstate() {
        return runstate;
    }

    public void setRunstate(Integer runstate) {
        this.runstate = runstate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states == null ? null : states.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public Jmeter_agentip_states(){}

    public Jmeter_agentip_states(String ipaddress, String states,String type, String time) {
        this.ipaddress = ipaddress;
        this.states = states;
        this.type = type ;
        this.time = time;
    }

    public Jmeter_agentip_states(String ipaddress, String states, String type, String time, String runusername, Integer runstate) {
        this.ipaddress = ipaddress;
        this.states = states;
        this.type = type;
        this.time = time;
        this.runusername = runusername;
        this.runstate = runstate;
    }

    public Jmeter_agentip_states(Integer id, String ipaddress, String states, String type, String time, String runusername, Integer runstate) {
        this.id = id;
        this.ipaddress = ipaddress;
        this.states = states;
        this.type = type;
        this.time = time;
        this.runusername = runusername;
        this.runstate = runstate;
    }
}