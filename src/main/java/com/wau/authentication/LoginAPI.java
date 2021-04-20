package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;

public interface LoginAPI {
    boolean verifyUserEnteredPassword(LoginAPIImpl loginAPIImpl, UserAPI userAPI) throws SQLException;

    long getUserId(String email, UserAPI userAPI);
}
