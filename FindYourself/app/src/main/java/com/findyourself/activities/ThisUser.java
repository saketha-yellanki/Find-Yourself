package com.findyourself.activities;

public class ThisUser {
    private static ThisUser instance;
    public String username;
    public String fullname;
    public String gender;
    public String birthday;

    private ThisUser() {
    }

    public ThisUser(String username, String fullname, String gender, String birthday) {
        this.username = username;
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
    }

    static ThisUser getInstance() {
        if (instance == null) {
            instance = new ThisUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}