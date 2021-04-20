package com.wau.user;

import java.sql.SQLException;
import java.util.List;

public interface UserAPI {
    UserAPIImpl getUserByEmail(String email) throws SQLException;

    UserAPIImpl getUserById(Long id) throws SQLException;

    String getPasswordByEmail(String email) throws SQLException;

    boolean addUser() throws SQLException;

    List<String> getSecurityQuestionsByEmail(String email) throws SQLException;

    boolean modifyPassword(String email, String password) throws SQLException;

    String getUserIdByEmail(String email);
}
