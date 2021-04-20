package com.wau.notification;


import com.wau.LogAPI;
import com.wau.LogAPIImpl;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class EmailActionsAPIImpl implements EmailActionAPI {

    private static final String USERNAME = "userEmail";
    private static final String PASSWORD = "password";
    private static final String PORT_NUMBER = "portNumber";
    private static final String SMTP_HOST = "smtpHost";

    private final LogAPI logAPI = LogAPIImpl.instance();

    @Override
    public boolean sendMail(String userId, String toAddress, String subject, String content) {
        try {
            Map<String, String> smtpDetailsMap = getSMTPConfigForUser(userId);
            logAPI.infoLog(smtpDetailsMap.toString());
            Properties sendMailProps = new Properties();
            sendMailProps.put("mail.smtp.auth", "true");
            sendMailProps.put("mail.smtp.starttls.enable", "true");
            sendMailProps.put("mail.smtp.host", smtpDetailsMap.get(SMTP_HOST));
            sendMailProps.put("mail.smtp.port", smtpDetailsMap.get(PORT_NUMBER));
            Session session = Session.getInstance(sendMailProps, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpDetailsMap.get(USERNAME), smtpDetailsMap.get(PASSWORD));
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(smtpDetailsMap.get(USERNAME), false));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            msg.setSubject(subject);
            msg.setContent(content, "text/plain");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            logAPI.infoLog(userId + toAddress + subject + content);
        }
        return false;
    }

    private Map<String, String> getSMTPConfigForUser(String userId) throws SQLException {
        EmailAbstractFactoryAPI emailAbstractFactoryAPI = EmailAbstractFactoryAPIImpl.instance();
        EmailConfigurationStoreAPI emailConfigurationStoreAPI =
                emailAbstractFactoryAPI.createEmailConfigurationStoreObject();
        EmailSMTP emailSMTP =
                emailAbstractFactoryAPI.createEmailSMTP(userId, emailConfigurationStoreAPI);
        return emailSMTP.getSMTPConfigForUser();
    }
}
