package com.wau.notification;

public interface EmailAbstractFactoryAPI {

    EmailConfigurationStoreAPI createEmailConfigurationStoreObject();

    EmailSMTP createEmailSMTP(String userId, EmailConfigurationStoreAPI emailConfigurationStoreAPI);

    EmailSMTP createEmailSMTP(String userId, String smtpHost, String portNumber, String userEmail, String password,
                              EmailConfigurationStoreAPI emailConfigurationStoreAPI);

    EmailActionAPI createEmailActionObject();

}
