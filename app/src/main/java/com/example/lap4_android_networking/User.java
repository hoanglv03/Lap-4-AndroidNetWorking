package com.example.lap4_android_networking;

public class User {
    String _id;
    String userName;
    String passWord;
    String email;
    String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public User() {
    }

    public User(String id, String userName, String passWord, String email) {
        this._id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
