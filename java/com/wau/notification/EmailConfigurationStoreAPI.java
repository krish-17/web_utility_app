package com.wau.notification;

import java.sql.SQLException;
import java.util.Map;

public interface EmailConfigurationStoreAPI {

    boolean configureSMTPInfo(EmailSMTP emailSMTP) throws SQLException;

    Map<String, String> getSMTPConfigForUser(String userId) throws SQLException;

}
