package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;

public interface SignUpAPI {
    boolean addUser(UserAPI userAPI) throws SQLException;
}
