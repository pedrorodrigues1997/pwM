package com.ossnm.data;

public class passwordHelp {

    private String AccountName;
    private String UserName;
    private String password;

    public passwordHelp(String accountName, String UserName, String password) {
        this.AccountName = accountName;
        this.UserName = UserName;
        this.password = password;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        this.AccountName = accountName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "User[username " + getUserName() + " " +
                "password" + getPassword() + " " +
                "account" + getAccountName();
    }
}
