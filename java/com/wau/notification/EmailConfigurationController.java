package com.wau.notification;

import com.wau.WUAConfigFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
public class EmailConfigurationController {

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
    EmailAbstractFactoryAPI emailAbstractFactoryAPI = wuaConfigFactory.getEmailAbstractFactoryAPI();
    private static final String RESP_MESSAGE = "message";

    private static final String ERROR_PAGE = "email/failedConfiguringSmtp";

    @GetMapping("/email/configure")
    public String viewSMTPConfigurationPage() {
        return "email/configureView";

    }

    @GetMapping("/email/send")
    public String viewSendMailPage() {
        return "email/emailSendView";
    }

    @PostMapping("/email/smtp/save")
    public String saveSMTPConfigurations(@RequestParam("userEmail") String userEmail,
                                         @RequestParam("password") String password,
                                         @RequestParam("smtpHost") String smtpHost,
                                         @RequestParam("portNumber") String portNumber,
                                         HttpServletRequest request,
                                         Model model) {
        String userId = String.valueOf(request.getSession().getAttribute("userId"));
        EmailSMTP emailSMTP = emailAbstractFactoryAPI.createEmailSMTP(userId, smtpHost,
                portNumber, userEmail, password,
                emailAbstractFactoryAPI.createEmailConfigurationStoreObject());
        try {
            if (emailSMTP.configureSMTPInfo()) {
                model.addAttribute(RESP_MESSAGE, "Configured Successfully");
                return "email/successRespPage";
            }
        } catch (SQLException e) {
            model.addAttribute(RESP_MESSAGE, e.getMessage());
            return ERROR_PAGE;
        }
        model.addAttribute(RESP_MESSAGE, "Configuration Failed");
        return ERROR_PAGE;
    }

    @PostMapping("/email/smtp/send")
    public String sendEmail(@RequestParam("toAddress") String toAddress,
                            @RequestParam("subject") String subject,
                            @RequestParam("content") String content,
                            HttpServletRequest request,
                            Model model) {
        String userId = String.valueOf(request.getSession().getAttribute("userId"));
        EmailActionAPI emailAPI = emailAbstractFactoryAPI.createEmailActionObject();
        if (emailAPI.sendMail(userId, toAddress, subject, content)) {
            model.addAttribute(RESP_MESSAGE, "Mail Sent Successfully");
            return "email/successRespPage";
        } else {
            model.addAttribute(RESP_MESSAGE, "Mail Sending Failed");
            return ERROR_PAGE;
        }
    }
}
