package com.wau.authentication;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.WUAConfigFactory;
import com.wau.user.UserAPI;
import com.wau.user.UserAbstractFactoryAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthenticateController {

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
    private final AuthenticationAbstractFactoryAPI authenticationAbstractFactoryAPI =
            wuaConfigFactory.getAuthenticationAbstractFactoryAPI();
    private final SignUpAPI signUpAPI = authenticationAbstractFactoryAPI.createSignUpAPIImplObject();
    private final ForgetPasswordAPI forgetPasswordAPI =
            authenticationAbstractFactoryAPI.createForgetPasswordAPIImplObject();
    private final LoginAPI loginAPI = authenticationAbstractFactoryAPI.createLoginAPIImplObject();
    private final UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigFactory.getUserAbstractFactoryAPI();
    private final UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject();
    private final LogAPI log = LogAPIImpl.instance();

    private static final String MESSAGE = "message";
    private static final String HOME_PAGE = "homePageWUA";

    @GetMapping("/authentication/signup")
    public String signup(Model model) {
        model.addAttribute("signup", signUpAPI);
        return "authentication/signup";
    }

    @PostMapping("/authentication/signup")
    public String saveUserData(@ModelAttribute SignUpAPIImpl signup, Model model) {
        model.addAttribute("signup", signup);
        List<String> questionsList = new ArrayList<>();
        questionsList.add(signup.getNickName());
        questionsList.add(signup.getFavColour());
        questionsList.add(signup.getFavCar());
        UserAPI userAPISignup =
                userAbstractFactoryAPI.createUserAPIImplObject(signup.getEmail(),
                        signup.getFirstName(), signup.getLastName(),
                        signup.getPassword(), questionsList);
        try {
            if (signUpAPI.addUser(userAPISignup)) {
                log.infoLog("Signup Successfully for user: " + signup.getEmail());
                return "authentication/signupSuccess";
            } else {
                model.addAttribute(MESSAGE, "Unable to signup");
                log.errorLog("Signup failed for user: " + signup.getEmail());
                return HOME_PAGE;
            }
        } catch (Exception e) {
            model.addAttribute(MESSAGE, e.getMessage());
            log.errorLog("Signup failed for user: " + signup.getEmail());
            return HOME_PAGE;
        }
    }

    @GetMapping("/authentication/userLogin")
    public String loginSubmit(Model model) {
        model.addAttribute("userLogin", loginAPI);
        return "authentication/userLogin";
    }

    @PostMapping("/authentication/userLogin")
    public String loginUser(@ModelAttribute LoginAPIImpl userLogin, Model model, HttpServletRequest request) {
        model.addAttribute("userLogin", userLogin);
        HttpSession session = request.getSession();
        try {
            if (loginAPI.verifyUserEnteredPassword(userLogin, userAPI)) {
                session.setAttribute("userId", loginAPI.getUserId(userLogin.getEmail(), userAPI));
                log.infoLog("Login Successfully for user: " + userLogin.getEmail());
                return "authentication/loginSuccessPage";
            } else {
                model.addAttribute(MESSAGE, "Unable to login");
                log.errorLog("login failed for user: " + userLogin.getEmail());
                return "authentication/loginfail";
            }
        } catch (Exception e) {
            model.addAttribute(MESSAGE, e.getMessage());
            log.errorLog("login failed for user: " + userLogin.getEmail());
            return HOME_PAGE;
        }
    }

    @GetMapping("/authentication/forgetpassword")
    public String resetPasswordForm(Model model) {
        model.addAttribute("forgetpassword", forgetPasswordAPI);
        return "authentication/forgetpassword";
    }

    @PostMapping("/authentication/forgetpassword")
    public String questionVerification(@ModelAttribute ForgetPasswordAPIImpl forgetpassword, Model model) {
        model.addAttribute("forgetpassword", forgetpassword);
        try {
            if (forgetPasswordAPI
                    .verifyUserEnteredSecurityQuestions(forgetpassword, userAPI)) {
                forgetPasswordAPI
                        .modifyVerifiedUserPassword(forgetpassword.getEmail(),
                                forgetpassword.getnewPassword(), userAPI);
                log.infoLog("password changed successfully for user: " + forgetpassword.getEmail());
                return "authentication/verified";
            } else {
                model.addAttribute(MESSAGE, "Verification failed");
                log.errorLog("Security question's verification failed for User: " + forgetpassword.getEmail());
                return "authentication/verificationfailed";
            }
        } catch (Exception e) {
            model.addAttribute(MESSAGE, e.getMessage());
            log.errorLog("reset password failed for user: " + forgetpassword.getEmail());
            return HOME_PAGE;
        }
    }
}
