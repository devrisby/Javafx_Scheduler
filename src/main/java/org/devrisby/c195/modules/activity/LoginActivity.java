package org.devrisby.c195.modules.activity;

/** Class for LoginActivity Model */
public class LoginActivity {
    private String userName;
    private String timeStamp;
    private boolean loginSuccess;

    /** Set timestamp of LoginActivity */
    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    /** Retrieve timestamp from LoginActivity */
    public String getTimeStamp() {
        return timeStamp;
    }

    /** Retrieve username from LoginActivity */
    public String getUserName() {
        return userName;
    }

    /** Set username for LoginActivity */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Retrieve whether login attempt was successful */
    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    /** Set status for login attempt */
    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    /** Display Login attempt with username, attempt status, and timestamp */
    @Override
    public String toString() {
        String success = this.loginSuccess ? "succeeded":"failed";
        return "Login attempt by " + this.userName + ", " + success + " on " + this.timeStamp;
    }
}
