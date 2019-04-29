package com.technohack.movie_analytica.Models;

public class HeaderModel {
    String userName;
    String userEmail;
    String userPass;

    public HeaderModel(String userName, String userEmail,String userPass) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass=userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
