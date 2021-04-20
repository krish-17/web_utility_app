package com.wau.user;

import java.sql.SQLException;
import java.util.List;

public class UserAPIImpl implements UserAPI {

    private long userid;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String securityQuestion1;
    private String securityQuestion2;
    private String securityQuestion3;
    private List<String> securityQuestionList;
    private UserStoreAPI userStoreAPI;

    public UserAPIImpl(String email, String firstName, String lastName,
                       String password, List<String> securityQuestions,
                       UserStoreAPI userStoreAPI) {
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
        this.password = password;
        this.securityQuestionList = securityQuestions;
        this.userStoreAPI = userStoreAPI;
        convertSecurityQuestionListToSecurityQuestions();
    }

    public UserAPIImpl(long userid, String email, String firstName, String lastName) {
        this.userid = userid;
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
    }

    public UserAPIImpl(UserStoreAPI userStoreAPI) {
        this.userStoreAPI = userStoreAPI;
    }

    public long getUserId() {
        return userid;
    }

    public void setUserId(long userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        lastname = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        password = pwd;
    }

    public String getSecurityQuestion1() {
        return securityQuestion1;
    }

    public void setSecurityQuestion1(String securityQuestion1) {
        this.securityQuestion1 = securityQuestion1;
    }

    public String getSecurityQuestion2() {
        return securityQuestion2;
    }

    public void setSecurityQuestion2(String securityQuestion2) {
        this.securityQuestion2 = securityQuestion2;
    }

    public String getSecurityQuestion3() {
        return securityQuestion3;
    }

    public void setSecurityQuestion3(String securityQuestion3) {
        this.securityQuestion3 = securityQuestion3;
    }

    public List<String> getSecurityQuestionList() {
        return securityQuestionList;
    }

    public void setSecurityQuestionList(List<String> securityQuestionList) {
        this.securityQuestionList = securityQuestionList;
    }

    @Override
    public UserAPIImpl getUserByEmail(String email) throws SQLException {

        if (email == null) {
            return null;
        } else {
            return this.userStoreAPI.getUserByEmail(email, this);
        }
    }

    @Override
    public UserAPIImpl getUserById(Long userid) throws SQLException {
        if (userid == 0) {
            return null;
        } else {
            return this.userStoreAPI.getUserById(userid, this);
        }
    }

    @Override
    public String getPasswordByEmail(String email) throws SQLException {
        if (email == null) {
            return null;
        } else {
            return this.userStoreAPI.getPasswordByEmail(email);
        }
    }

    @Override
    public boolean addUser() throws SQLException {
        if (this.email == null || this.password == null) {
            return false;
        }
        if (email.matches("^(.+)@(.+)$")) {
            return this.userStoreAPI.addUser(this);
        }
        return false;
    }

    @Override
    public List<String> getSecurityQuestionsByEmail(String email) throws SQLException {
        if (email == null) {
            return null;
        } else {
            return this.userStoreAPI.getSecurityQuestionsByEmail(email);
        }
    }

    @Override
    public boolean modifyPassword(String email, String password) throws SQLException {
        if (email == null || password == null) {
            return false;
        } else {
            return this.userStoreAPI.modifyPassword(email, password);
        }
    }

    @Override
    public String getUserIdByEmail(String email) {
        if (email == null) {
            return null;
        } else {
            return this.userStoreAPI.getUserIdByEmail(email);
        }
    }

    private void convertSecurityQuestionListToSecurityQuestions() {
        if (this.securityQuestionList == null || this.securityQuestionList.size() == 3) {
            return;
        }
        this.securityQuestion1 = securityQuestionList.get(0);
        this.securityQuestion2 = securityQuestionList.get(1);
        this.securityQuestion3 = securityQuestionList.get(2);
    }
}
