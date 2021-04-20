package com.wau.notification;

public class EmailAbstractFactoryAPIImpl implements EmailAbstractFactoryAPI {

    private static EmailAbstractFactoryAPI emailAbstractFactoryAPI = null;

    public static EmailAbstractFactoryAPI instance() {
        if (null == emailAbstractFactoryAPI) {
            emailAbstractFactoryAPI = new EmailAbstractFactoryAPIImpl();
        }
        return emailAbstractFactoryAPI;
    }

    @Override
    public EmailConfigurationStoreAPI createEmailConfigurationStoreObject() {
        return new EmailConfigurationStoreAPIImpl();
    }

    @Override
    public EmailSMTP createEmailSMTP(String userId, EmailConfigurationStoreAPI emailConfigurationStoreAPI) {
        return new EmailSMTP(userId, emailConfigurationStoreAPI);
    }

    @Override
    public EmailSMTP createEmailSMTP(String userId, String smtpHost, String portNumber, String userEmail,
                                     String password, EmailConfigurationStoreAPI emailConfigurationStoreAPI) {
        return new EmailSMTP(userId, smtpHost, portNumber,
                userEmail, password, emailConfigurationStoreAPI);
    }

    @Override
    public EmailActionAPI createEmailActionObject() {
        return new EmailActionsAPIImpl();
    }
}