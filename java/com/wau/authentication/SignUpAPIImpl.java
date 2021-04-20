package com.wau.authentication;

import com.wau.user.UserAPI;

import java.sql.SQLException;

public class SignUpAPIImpl implements SignUpAPI {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nickName;
    private String favColour;
    private String favCar;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public boolean addUser(UserAPI userAPI) throws SQLException {
        return userAPI.addUser();
    }
}
