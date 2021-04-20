package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;

public class LoginAPIImpl implements LoginAPI {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean verifyUserEnteredPassword(LoginAPIImpl loginAPIImpl, UserAPI userAPI) throws SQLException {
        return userAPI.getPasswordByEmail(loginAPIImpl.getEmail()).equals(loginAPIImpl.getPassword());
    }

    @Override
    public long getUserId(String email, UserAPI userAPI) {
        return Long.parseLong(userAPI.getUserIdByEmail(email));
    }
}
