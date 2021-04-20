package com.wau.authentication;

public class AuthenticationAbstractFactoryAPIImpl implements AuthenticationAbstractFactoryAPI {

    private static AuthenticationAbstractFactoryAPI authenticationAbstractFactoryAPI = null;

    private AuthenticationAbstractFactoryAPIImpl() {
    }

    public static AuthenticationAbstractFactoryAPI instance() {
        if (null == authenticationAbstractFactoryAPI) {
            authenticationAbstractFactoryAPI = new AuthenticationAbstractFactoryAPIImpl();
        }
        return authenticationAbstractFactoryAPI;
    }

    @Override
    public SignUpAPI createSignUpAPIImplObject() {
        return new SignUpAPIImpl();
    }

    @Override
    public LoginAPI createLoginAPIImplObject() {
        return new LoginAPIImpl();
    }

    @Override
    public ForgetPasswordAPI createForgetPasswordAPIImplObject() {
        return new ForgetPasswordAPIImpl();
    }
}
