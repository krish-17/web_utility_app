package com.wau.user;

import java.sql.SQLException;
import java.util.List;

public interface UserStoreAPI {

    UserAPIImpl getUserByEmail(String email, UserAPIImpl user) throws SQLException;

    UserAPIImpl getUserById(Long id, UserAPIImpl user) throws SQLException;

    String getPasswordByEmail(String email) throws SQLException;

    boolean addUser(UserAPIImpl user) throws SQLException;

    List<String> getSecurityQuestionsByEmail(String email) throws SQLException;

    boolean modifyPassword(String email, String password) throws SQLException;

    String getUserIdByEmail(String email);
}
