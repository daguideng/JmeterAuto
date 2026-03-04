package com.atguigu.gmall.entity;

public class User {
    private Integer id;

    private String username;

    private String password;

    private String pwd ;

    private String emailaddress;

    private Integer runstate ;


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getRunstate() {
        return runstate;
    }

    public void setRunstate(Integer runstate) {
        this.runstate = runstate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress == null ? null : emailaddress.trim();
    }

    public User(){
      //  new User("dengdagui", "123456", "dengdagui@sina.com");
    }



    public User(String username, String password, String emailaddress) {
        this.username = username;
        this.password = password;
        this.emailaddress = emailaddress;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.emailaddress = null;
    }





    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                ", runstate=" + runstate +
                '}';
    }
}