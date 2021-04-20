package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;

public interface ForgetPasswordAPI {
    boolean verifyUserEnteredSecurityQuestions(ForgetPasswordAPIImpl forgetPasswordAPIImpl, UserAPI userAPI) throws SQLException;

    boolean modifyVerifiedUserPassword(String email, String newPassword, UserAPI userAPI) throws SQLException;
}
