package com.wau.user;

import java.util.ArrayList;
import java.util.List;

public class MockUserStoreAPIImpl implements UserStoreAPI {

    private final long userid = 12345L;
    private final String email = "Test@dal.ca";
    private final String firstname = "TestFirstName";
    private final String lastname = "TestLastName";
    private final String password = "TestPassword";

    @Override
    public UserAPIImpl getUserByEmail(String email, UserAPIImpl user) {
        if (email.equals("Test@dal.ca")) {
            user.setEmail(email);
            user.setUserId(userid);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public UserAPIImpl getUserById(Long id, UserAPIImpl user) {
        if (id.equals(12345L)) {
            user.setEmail(email);
            user.setUserId(userid);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public String getPasswordByEmail(String email) {
        if (email.equals("Test@dal.ca")) {
            return password;
        } else {
            return null;
        }
    }

    @Override
    public boolean addUser(UserAPIImpl user) {
        return user.getEmail().equals(email) && user.getPassword().equals(password);
    }

    @Override
    public List<String> getSecurityQuestionsByEmail(String email) {
        List<String> securityQuestions = new ArrayList<>();
        if (email.equals("Test@dal.ca")) {
            String securityQuestion1 = "Test1";
            securityQuestions.add(securityQuestion1);
            String securityQuestion2 = "Test2";
            securityQuestions.add(securityQuestion2);
            String securityQuestion3 = "Test3";
            securityQuestions.add(securityQuestion3);
            return securityQuestions;
        } else {
            return null;
        }
    }

    @Override
    public boolean modifyPassword(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getUserIdByEmail(String email) {
        if(email.equals("demo@gmail.com")){
            return "1617746924762";
        }
        return null;
    }
}
