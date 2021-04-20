package com.wau.user;

import java.util.List;

public interface UserAbstractFactoryAPI {

    UserAPI createUserAPIImplObject(String email, String firstName,
                                    String lastName, String password,
                                    List<String> questionsList);

    UserAPI createUserAPIImplObject();

}
