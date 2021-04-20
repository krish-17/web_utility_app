package com.wau.user;

import java.util.List;

public class UserAbstractFactoryAPIImpl implements UserAbstractFactoryAPI {

    private static UserAbstractFactoryAPI userAbstractFactoryAPI = null;

    private final UserStoreAPI userStoreAPI;

    private UserAbstractFactoryAPIImpl(UserStoreAPI userStoreAPI) {
        this.userStoreAPI = userStoreAPI;
    }

    public static UserAbstractFactoryAPI instance(UserStoreAPI userStoreAPI) {
        if (null == userAbstractFactoryAPI) {
            userAbstractFactoryAPI = new UserAbstractFactoryAPIImpl(userStoreAPI);
        }
        return userAbstractFactoryAPI;
    }

    @Override
    public UserAPI createUserAPIImplObject(String email, String firstName, String lastName,
                                           String password, List<String> questionsQuestion) {
        return new UserAPIImpl(email, firstName, lastName, password,
                questionsQuestion,
                userStoreAPI);
    }

    @Override
    public UserAPI createUserAPIImplObject() {
        return new UserAPIImpl(userStoreAPI);
    }
}
