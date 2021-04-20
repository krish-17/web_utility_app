package com.wau.authentication;

import com.wau.WUAConfigTestFactory;
import com.wau.user.UserAPI;
import com.wau.user.UserAbstractFactoryAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;


class ForgetPasswordAPIImplTest {
    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigTestFactory.getUserAbstractFactoryAPI();
    private final UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject();

    @Test
    void testFirstSecurityQuestionForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("Test@dal.ca");
        Assertions.assertEquals("Test1", securityQuestions.get(0));
    }

    @Test
    void testSecondSecurityQuestionForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("Test@dal.ca");
        Assertions.assertEquals("Test2", securityQuestions.get(1));
    }

    @Test
    void testThirdSecurityQuestionForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("Test@dal.ca");
        Assertions.assertEquals("Test3", securityQuestions.get(2));
    }

    @Test
    void testInvalidEmailForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("false@dal.ca");
        Assertions.assertNull(securityQuestions);
    }

    @Test
    void testEmptyEmailForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("");
        Assertions.assertNull(securityQuestions);
    }

    @Test
    void testMoreThanThreeQuestionForUserEnteredSecurityQuestions() throws SQLException {
        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail("Test@dal.ca");
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> securityQuestions.get(4));
    }

    @Test
    void testValidCredentialsForModifyPassword() throws SQLException {
        Assertions.assertTrue(userAPI.modifyPassword("demo@gmail.com", "123"));
    }

    @Test
    void testNullEmailForModifyPassword() throws SQLException {
        Assertions.assertFalse(userAPI.modifyPassword(null, "123"));
    }

    @Test
    void testNullPasswordForModifyPassword() throws SQLException {
        Assertions.assertFalse(userAPI.modifyPassword("demo@gmail.com", null));
    }

    @Test
    void testEmptyPasswordForModifyPassword() throws SQLException {
        Assertions.assertFalse(userAPI.modifyPassword("demo@gmail.com", ""));
    }

    @Test
    void testEmptyEmailForModifyPassword() throws SQLException {
        Assertions.assertFalse(userAPI.modifyPassword("", "123"));
    }
}