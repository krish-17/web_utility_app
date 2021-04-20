package com.wau.user;

import com.wau.WUAConfigTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class UserAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();

    UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigTestFactory.getUserAbstractFactoryAPI();

    @Test
    void testInvalidValueForGetUserByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getUserByEmail("invalid@dal.ca"));
    }

    @Test
    void testValidValueForGetUserByEmail() throws SQLException {
        String email = "Test@dal.ca";
        String firstname = "TestFirstName";
        String lastname = "TestLastName";
        String password = "TestPassword";
        String questions = "SecurityQuestion1,SecurityQuestion2,SecurityQuestion3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(email, firstname,
                lastname, password, questionList);
        Assertions.assertEquals(user, user.getUserByEmail("Test@dal.ca"));
    }

    @Test
    void testNullValueForGetUserByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getUserByEmail(null));
    }

    @Test
    void testValidValueForGetUserById() throws SQLException {
        String email = "Test@dal.ca";
        String firstname = "TestFirstName";
        String lastname = "TestLastName";
        String password = "TestPassword";
        String questions = "SecurityQuestion1,SecurityQuestion2,SecurityQuestion3";

        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(email, firstname,
                lastname, password, questionList);
        Assertions.assertEquals(user, user.getUserById(12345L));
    }

    @Test
    void testInvalidValueForGetUserById() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getUserById(123L));
    }

    @Test
    void testValidValueForGetPasswordByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertEquals("TestPassword", user.getPasswordByEmail("Test@dal.ca"));
    }

    @Test
    void testInvalidValueForGetPasswordByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getPasswordByEmail("invalid"));
    }

    @Test
    void testNullValueForGetPasswordByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getPasswordByEmail(null));
    }

    @Test
    void testValidValueForAddUser() throws SQLException {
        String email = "Test@dal.ca";
        String firstname = "TestFirstName";
        String lastname = "TestLastName";
        String password = "TestPassword";
        String questions = "SecurityQuestion1,SecurityQuestion2,SecurityQuestion3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(email, firstname
                , lastname, password, questionList);
        Assertions.assertTrue(user.addUser());
    }

    @Test
    void testNullValueAddUser() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(null, null,
                null, null,
                null);
        Assertions.assertFalse(user.addUser());
    }

    @Test
    void testEmailEmptyValueAddUser() throws SQLException {
        String email = "";
        String firstname = "TestFirstName";
        String lastname = "TestLastName";
        String password = "TestPassword";
        String questions = "SecurityQuestion1,SecurityQuestion2,SecurityQuestion3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(email, firstname,
                lastname, password, questionList);
        Assertions.assertFalse(user.addUser());
    }

    @Test
    void testPasswordEmptyValueAddUser() throws SQLException {
        String email = "Test@dal.ca";
        String firstname = "TestFirstName";
        String lastname = "TestLastName";
        String password = "";
        String questions = "SecurityQuestion1,SecurityQuestion2,SecurityQuestion3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject(email, firstname,
                lastname, password, questionList);
        Assertions.assertFalse(user.addUser());
    }

    @Test
    void testValidValueForGetSecurityQuestionsByEmail() throws SQLException {
        String email = "Test@dal.ca";
        List<String> securityQuestions = new ArrayList<>();
        securityQuestions.add("Test1");
        securityQuestions.add("Test2");
        securityQuestions.add("Test3");
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertEquals(securityQuestions, user.getSecurityQuestionsByEmail(email));
    }

    @Test
    void testInvalidValueForGetSecurityQuestionsByEmail() throws SQLException {
        String email = "invalid";
        List<String> securityQuestions = new ArrayList<>();
        securityQuestions.add("Test1");
        securityQuestions.add("Test2");
        securityQuestions.add("Test3");
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNotEquals(securityQuestions, user.getSecurityQuestionsByEmail(email));
    }

    @Test
    void testNullValueForGetSecurityQuestionsByEmail() throws SQLException {
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertNull(user.getSecurityQuestionsByEmail(null));
    }

    @Test
    void testValidValueForModifyPassword() throws SQLException {
        String email = "Test@dal.ca";
        String password = "TestPassword";
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertTrue(user.modifyPassword(email, password));
    }

    @Test
    void testEmailNullValueForModifyPassword() throws SQLException {
        String password = "TestPassword";
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertFalse(user.modifyPassword(null, password));
    }

    @Test
    void testPasswordNullValueForModifyPassword() throws SQLException {
        String email = "Test@dal.ca";
        UserAPI user = userAbstractFactoryAPI.createUserAPIImplObject();
        Assertions.assertFalse(user.modifyPassword(email, null));
    }
}