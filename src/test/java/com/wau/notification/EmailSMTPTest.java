package com.wau.notification;

import com.wau.WUAConfigTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class EmailSMTPTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final EmailAbstractFactoryAPI emailAbstractFactoryAPI = wuaConfigTestFactory.getEmailAbstractFactoryAPI();

    @Test
    void testInvalidValueForValidatePortNumber() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("", null);
        Assertions.assertFalse(emailSMTP.validatePortNumber("200"));
    }

    @Test
    void testValidValueForValidatePortNumber() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("", null);
        String[] portNumbers = new String[]{"25", "465", "587", "2525"};
        for (String portNumber : portNumbers) {
            Assertions.assertTrue(emailSMTP.validatePortNumber(portNumber));
        }
    }

    @Test
    void testNullValueForValidatePortNumber() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("", null);
        Assertions.assertFalse(emailSMTP.validatePortNumber(null));
    }

    @Test
    void testInvalidValueForValidateUserEmail() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("", null);
        Assertions.assertFalse(emailSMTP.validateUserEmail("www.google.com"));
    }

    @Test
    void testValidValueForValidateUserEmail() {
        EmailSMTP emailSMTP = new EmailSMTP("", null);
        Assertions.assertTrue(emailSMTP.validateUserEmail("group6@gmail.com"));
    }

    @Test
    void testNullValueForValidateUserEmail() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("", null);
        Assertions.assertFalse(emailSMTP.validateUserEmail(null));
    }

    @Test
    void testStoreAPINullValueForGetSMTPConfigForUser() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("group6", null);
        try {
            Assertions.assertNull(emailSMTP.getSMTPConfigForUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUserIdNullValueForGetSMTPConfigForUser() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP(null, null);
        try {
            Assertions.assertNull(emailSMTP.getSMTPConfigForUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInvalidUserIDForGetSMTPConfigForUser() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("group10",
                new MockEmailConfigurationStore());
        try {
            Assertions.assertNull(emailSMTP.getSMTPConfigForUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testValidUserIDForGetSMTPConfigForUser() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI
                .createEmailSMTP("group6", new MockEmailConfigurationStore());
        try {
            Assertions.assertEquals("group6", emailSMTP.getSMTPConfigForUser().get("userId"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNullValueForConfigureSMTPInfo() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("null", null);
        try {
            Assertions.assertFalse(emailSMTP.configureSMTPInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInvalidValueForConfigureSMTPInfo() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI
                .createEmailSMTP("group6", "", "",
                        "", "", null);
        try {
            Assertions.assertFalse(emailSMTP.configureSMTPInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testValidValueForConfigureSMTPInfo() {
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP("group6", "smtp.google.com",
                "465", "group6@gmail.com",
                "testing", new MockEmailConfigurationStore());
        try {
            Assertions.assertTrue(emailSMTP.configureSMTPInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
