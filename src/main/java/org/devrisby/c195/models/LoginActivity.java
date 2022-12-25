package org.devrisby.c195.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class LoginActivity {
    private String userName;
    private String timeStamp;
    private boolean loginSuccess;

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }
    public void setTimeStamp(Timestamp timeStamp){
        this.timeStamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(timeStamp);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    @Override
    public String toString() {
        String success = this.loginSuccess ? "succeeded":"failed";
        return "Login attempt by " + this.userName + ", " + success + " on " + this.timeStamp;
    }

}
