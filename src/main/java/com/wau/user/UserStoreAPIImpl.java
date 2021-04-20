package com.wau.user;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserStoreAPIImpl implements UserStoreAPI {

    private static UserStoreAPI userStoreAPI = null;
    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();
    private final LogAPI log = LogAPIImpl.instance();

    private static final String NULL_EMAIL = "Email can't be null";
    private static final String USER = " user ";
    private static final String USER_ID = " user_id ";
    private static final String ESCAPE_CHARACTER_QUERY = "\", \"";

    public static UserStoreAPI instance() {
        if (null == userStoreAPI) {
            userStoreAPI = new UserStoreAPIImpl();
        }
        return userStoreAPI;
    }

    @Override
    public UserAPIImpl getUserByEmail(String email, UserAPIImpl user) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            ResultSet result = databaseConnectorAPI.getStatement().executeQuery(getQueryUsingEmail(email));
            while (result.next()) {
                user = new UserAPIImpl(result.getLong("user_id"),
                        result.getString("email"),
                        result.getString("first_name"),
                        result.getString("last_name"));
            }
            databaseConnectorAPI.clearResources();
            return user;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("getting User details failed");
        }
    }

    @Override
    public UserAPIImpl getUserById(Long id, UserAPIImpl user) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            ResultSet result = databaseConnectorAPI.getStatement().executeQuery(getQueryUsingId(id));
            while (result.next()) {
                user = new UserAPIImpl(result.getLong("Id"),
                        result.getString("email"),
                        result.getString("first_name"),
                        result.getString("last_name"));
            }
            databaseConnectorAPI.clearResources();
            return user;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("cannot fetch user details using Id");
        }
    }

    @Override
    public String getPasswordByEmail(String email) throws SQLException {
        String password = null;
        try {
            databaseConnectorAPI.getConnection();
            ResultSet result = databaseConnectorAPI.getStatement().executeQuery(getQueryForPassword(email));
            while (result.next()) {
                password = result.getString(1);
            }
            databaseConnectorAPI.clearResources();
            return password;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("cannot fetch user password");
        }
    }

    @Override
    public boolean addUser(UserAPIImpl user) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(insertQueryforAddUser(user));
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("cannot add user");
        }
    }

    @Override
    public List<String> getSecurityQuestionsByEmail(String email) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            ResultSet result = databaseConnectorAPI.getStatement().executeQuery(getQueryforSecurityQuestions(email));
            List<String> securityQuestions = new ArrayList<>();
            while (result.next()) {
                securityQuestions.add(result.getString("security_question1"));
                securityQuestions.add(result.getString("security_question2"));
                securityQuestions.add(result.getString("security_question3"));
            }
            databaseConnectorAPI.clearResources();
            return securityQuestions;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("cannot provide security questions");
        }
    }

    @Override
    public boolean modifyPassword(String email, String password) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(updateQueryForPassword(email, password));
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            log.errorLog(e.getMessage());
            throw new SQLException("cannot modify user password");
        }
    }

    @Override
    public String getUserIdByEmail(String email) {
        String userId = null;
        try {
            databaseConnectorAPI.getConnection();
            ResultSet result = databaseConnectorAPI.getStatement().executeQuery(getQueryforUserId(email));
            while (result.next()) {
                userId = result.getString(1);
            }
            databaseConnectorAPI.clearResources();
        } catch (Exception e) {
            log.errorLog("cannot fetch UserId");
            return null;
        }
        return userId;
    }

    public String getQueryUsingEmail(String email) {
        if (email == null) {
            throw new NullPointerException(NULL_EMAIL);
        }
        return "SELECT " + USER_ID + ", first_name, last_name, email FROM " + USER + " WHERE email = \"" + email + "\"";
    }

    public String getQueryUsingId(Long id) {
        if (id == null) {
            throw new NullPointerException("Id can't be null");
        }
        return "SELECT " + USER_ID + ", first_name, last_name, email FROM " + USER + " WHERE user_id = \"" + id + "\"";
    }

    public String getQueryForPassword(String email) {
        if (email == null) {
            throw new NullPointerException(NULL_EMAIL);
        }
        return "SELECT password FROM " + USER + "WHERE email = '" + email + "'";
    }

    public String getQueryforUserId(String email) {
        if (email == null) {
            throw new NullPointerException(NULL_EMAIL);
        }
        return "SELECT " + USER_ID + " FROM " + USER + " WHERE email = \"" + email + "\"";
    }

    public String insertQueryforAddUser(UserAPIImpl u) {
        long id = System.currentTimeMillis();
        u.setUserId(id);
        String firstName = u.getFirstName();
        String lastName = u.getLastName();
        String email = u.getEmail();
        String password = u.getPassword();
        List<String> securityQuestions = u.getSecurityQuestionList();
        String securityQuestion1 = securityQuestions.get(0);
        String securityQuestion2 = securityQuestions.get(1);
        String securityQuestion3 = securityQuestions.get(2);
        return "INSERT INTO user (user_id, first_name, last_name, email, password, security_question1," +
                "security_question2, security_question3) VALUES (\"" + id + ESCAPE_CHARACTER_QUERY + firstName + "\"," +
                " \"" + lastName + ESCAPE_CHARACTER_QUERY + email + "\",\"" + password + "\", " +
                "\"" + securityQuestion1 + ESCAPE_CHARACTER_QUERY + securityQuestion2 + ESCAPE_CHARACTER_QUERY + securityQuestion3 + "\")";
    }

    public String getQueryforSecurityQuestions(String email) {
        if (email == null) {
            throw new NullPointerException(NULL_EMAIL);
        }
        return "SELECT security_question1, security_question2, security_question3 FROM " + USER + " WHERE email = \"" + email + "\"";
    }

    public String updateQueryForPassword(String email, String password) {
        if (email == null || password == null) {
            throw new NullPointerException(NULL_EMAIL);
        }
        return "UPDATE " + USER + " SET password = \"" + password + "\" WHERE email = \"" + email + "\"";
    }
}