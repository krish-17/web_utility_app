package com.wau.notification;

import java.sql.SQLException;
import java.util.Map;

public class EmailSMTP {

    private final String userId;
    private String smtpHost;
    private String portNumber;
    private String userEmail;
    private String password;
    private final EmailConfigurationStoreAPI emailConfigurationStoreAPI;

    public EmailSMTP(String userId, EmailConfigurationStoreAPI emailConfigurationStoreAPI) {
        this.userId = userId;
        this.emailConfigurationStoreAPI = emailConfigurationStoreAPI;
    }

    EmailSMTP(String userId, String smtpHost, String portNumber, String userEmail, String password,
              EmailConfigurationStoreAPI emailConfigurationStoreAPI) {
        this.password = password;
        this.userId = userId;
        this.smtpHost = smtpHost;
        this.userEmail = userEmail;
        this.portNumber = portNumber;
        this.emailConfigurationStoreAPI = emailConfigurationStoreAPI;
    }

    boolean validatePortNumber(String portNumber) {
        if (portNumber == null) {
            return false;
        }
        return portNumber.equalsIgnoreCase("25")
                || portNumber.equalsIgnoreCase("465")
                || portNumber.equalsIgnoreCase("587")
                || portNumber.equalsIgnoreCase("2525");
    }

    boolean validateUserEmail(String userEmail) {
        if (userEmail == null) {
            return false;
        }
        return userEmail.matches("[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+");
    }

    public String getUserId() {
        return userId;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    private boolean isValidSMTP() {

        if (this.userId == null) {
            return false;
        }
        if (this.password == null) {
            return false;
        }
        if (this.smtpHost == null) {
            return false;
        }
        if (this.emailConfigurationStoreAPI == null) {
            return false;
        }
        return validatePortNumber(this.portNumber)
                && validateUserEmail(this.userEmail);
    }

    Map<String, String> getSMTPConfigForUser() throws SQLException {
        if (this.emailConfigurationStoreAPI == null) {
            return null;
        }
        return this.emailConfigurationStoreAPI.getSMTPConfigForUser(userId);
    }

    boolean configureSMTPInfo() throws SQLException {
        if (isValidSMTP()) {
            return this.emailConfigurationStoreAPI.configureSMTPInfo(this);
        }
        return false;
    }
}
