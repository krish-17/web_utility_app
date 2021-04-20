package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;
import java.util.List;

public class ForgetPasswordAPIImpl implements ForgetPasswordAPI {
    private String email;
    private String newPassword;
    private String nickName;
    private String favColour;
    private String favCar;

    public String getnewPassword() {
        return newPassword;
    }

    public void setnewPassword(String password) {
        newPassword = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFavColour() {
        return favColour;
    }

    public void setFavColour(String favColour) {
        this.favColour = favColour;
    }

    public String getFavCar() {
        return favCar;
    }

    public void setFavCar(String favCar) {
        this.favCar = favCar;
    }

    @Override
    public boolean verifyUserEnteredSecurityQuestions(ForgetPasswordAPIImpl forgetPasswordAPIImpl, UserAPI userAPI) throws SQLException {

        List<String> securityQuestions = userAPI.getSecurityQuestionsByEmail(forgetPasswordAPIImpl.getEmail());
        String q1 = securityQuestions.get(0);
        String q2 = securityQuestions.get(1);
        String q3 = securityQuestions.get(2);
        return q1.equals(forgetPasswordAPIImpl.getNickName()) && q2.equals(forgetPasswordAPIImpl.getFavColour())
                && q3.equals(forgetPasswordAPIImpl.getFavCar());
    }

    @Override
    public boolean modifyVerifiedUserPassword(String email, String newPassword, UserAPI userAPI) throws SQLException {
        return userAPI.modifyPassword(email, newPassword);
    }
}
