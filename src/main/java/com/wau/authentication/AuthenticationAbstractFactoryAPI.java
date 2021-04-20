package com.wau.authentication;

public interface AuthenticationAbstractFactoryAPI {
    SignUpAPI createSignUpAPIImplObject();

    LoginAPI createLoginAPIImplObject();

    ForgetPasswordAPI createForgetPasswordAPIImplObject();
}
