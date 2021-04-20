package com.wau.authentication;

import com.wau.WUAConfigTestFactory;
import com.wau.user.UserAPI;
import com.wau.user.UserAbstractFactoryAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class LoginAPIImplTest {
    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigTestFactory.getUserAbstractFactoryAPI();
    private final UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject();

    @Test
    void testValidPasswordForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertEquals("TestPassword", userAPI.getPasswordByEmail("Test@dal.ca"));
    }

    @Test
    void testInvalidEmailForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertNull(userAPI.getPasswordByEmail("false@dal.ca"));
    }

    @Test
    void testNullEmailForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertNull(userAPI.getPasswordByEmail(null));
    }

    @Test
    void testEmptyEmailForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertNull(userAPI.getPasswordByEmail(""));
    }

    @Test
    void testNotNullPasswordForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertNotNull(userAPI.getPasswordByEmail("Test@dal.ca"));
    }

    @Test
    void testInvalidPasswordForVerifyUserEnteredPassword() throws SQLException {
        Assertions.assertNotEquals("wrongPassword", userAPI.getPasswordByEmail("Test@dal.ca"));
    }

    @Test
    void testForGetUserId() {
        Assertions.assertEquals("1617746924762", userAPI.getUserIdByEmail("demo@gmail.com"));
    }

    @Test
    void testInvalidEmailForGetUserId() {
        Assertions.assertNotEquals("1617746924762", userAPI.getUserIdByEmail("invalid@gmail.com"));
    }

    @Test
    void testNullEmailForGetUserId() {
        Assertions.assertNotEquals("1617746924762", userAPI.getUserIdByEmail(null));
    }

    @Test
    void testEmptyEmailForGetUserId() {
        Assertions.assertNotEquals("1617746924762", userAPI.getUserIdByEmail(""));
    }

    @Test
    void testInvalidPasswordEmailForGetUserId() {
        Assertions.assertNotEquals("123", userAPI.getUserIdByEmail("demo@gmail.com"));
    }
}