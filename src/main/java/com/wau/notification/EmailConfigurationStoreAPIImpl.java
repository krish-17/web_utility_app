package com.wau.notification;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmailConfigurationStoreAPIImpl implements EmailConfigurationStoreAPI {

    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();

    private final LogAPI logAPI = LogAPIImpl.instance();

    private static final String USERID = "userId";
    private static final String USERNAME = "userEmail";
    private static final String PASSWORD = "password";
    private static final String PORT_NUMBER = "portNumber";
    private static final String SMTP_HOST = "smtpHost";

    @Override
    public boolean configureSMTPInfo(EmailSMTP emailSMTP) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(getInsertSMTPQuery(emailSMTP));
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to Configure Smtp" + e.getMessage());
        }
    }

    @Override
    public Map<String, String> getSMTPConfigForUser(String userId) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            ResultSet smtpResultSet =
                    databaseConnectorAPI.getStatement().executeQuery(getFetchQuery(userId));
            Map<String, String> smtpMap = convertResultSetToMap(smtpResultSet);
            databaseConnectorAPI.clearResources();
            return smtpMap;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Failed fetching smtp details for user" + e.getMessage());
        }
    }

    private String getFetchQuery(String userId) {
        if (userId == null) {
            throw new NullPointerException("Primary key can't be null");
        }
        return "select * from email_configuration where userId = '" +
                userId + "'";
    }

    private String getInsertSMTPQuery(EmailSMTP emailSMTP) {
        return "Insert into email_configuration values(" + "'"
                + emailSMTP.getUserId() + "','"
                + emailSMTP.getSmtpHost() + "','"
                + emailSMTP.getPortNumber() + "','"
                + emailSMTP.getUserEmail() + "','"
                + emailSMTP.getPassword() + "')";
    }

    private static Map<String, String> convertResultSetToMap(ResultSet smtpResultSet) {
        Map<String, String> localSmtpMap = new HashMap<>();
        try {
            while (smtpResultSet.next()) {
                localSmtpMap.put(USERID, smtpResultSet.getString(USERID));
                localSmtpMap.put(SMTP_HOST, smtpResultSet.getString(SMTP_HOST));
                localSmtpMap.put(PORT_NUMBER, "" + smtpResultSet.getInt(PORT_NUMBER));
                localSmtpMap.put(USERNAME, smtpResultSet.getString(USERNAME));
                localSmtpMap.put(PASSWORD, smtpResultSet.getString(PASSWORD));
            }
            return localSmtpMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}