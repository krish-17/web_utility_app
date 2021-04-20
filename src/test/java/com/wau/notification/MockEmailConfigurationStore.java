package com.wau.notification;

import java.util.HashMap;
import java.util.Map;

public class MockEmailConfigurationStore implements EmailConfigurationStoreAPI {

    private static final String USERID = "userId";
    private static final String USER_EMAIL = "userEmail";
    private static final String PASSWORD = "password";
    private static final String PORT_NUMBER = "portNumber";
    private static final String SMTP_HOST = "smtpHost";

    @Override
    public boolean configureSMTPInfo(EmailSMTP emailSMTP) {
        return emailSMTP.getUserId().equalsIgnoreCase("group6");
    }

    @Override
    public Map<String, String> getSMTPConfigForUser(String userId) {
        if (userId.equalsIgnoreCase("group6")) {
            Map<String, String> testEmailConfigurationMap = new HashMap<>();
            testEmailConfigurationMap.put(USERID, userId);
            testEmailConfigurationMap.put(SMTP_HOST, "smtp.google.com");
            testEmailConfigurationMap.put(PASSWORD, "User123");
            testEmailConfigurationMap.put(PORT_NUMBER, "465");
            testEmailConfigurationMap.put(USER_EMAIL, "group6@gmail.com");
            return testEmailConfigurationMap;
        }
        return null;
    }
}
