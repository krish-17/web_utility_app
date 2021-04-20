package com.wau.notification;

public interface EmailActionAPI {

    boolean sendMail(String userId, String toAddress, String subject, String content);

}
