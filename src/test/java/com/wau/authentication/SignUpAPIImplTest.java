package com.wau.authentication;

import com.wau.WUAConfigTestFactory;
import com.wau.user.UserAPI;
import com.wau.user.UserAbstractFactoryAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class SignUpAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigTestFactory.getUserAbstractFactoryAPI();

    @Test
    void testValidEmailAndPasswordForAddUser() throws SQLException {
        String questions = "test1,test2,test3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject("Test@dal.ca", "parth", "thummar"
                , "TestPassword", questionList);
        Assertions.assertTrue(userAPI.addUser());
    }

    static Stream<Arguments> arrayOfDueDatesAndTaskNames() {
        return Stream.of(
                arguments(Arrays.asList("dummy@gmail.com",
                        "123")),
                arguments(Arrays.asList("dummy@gmail.com",
                        "TestPassword")),
                arguments(Arrays.asList("Test@gmail.com",
                        null)),
                arguments(Arrays.asList("Test@gmail.com",
                        "")),
                arguments(Arrays.asList("",
                        "TestPassword")),
                arguments(Arrays.asList("test",
                        "TestPassword"))
        );

    }

    @ParameterizedTest
    @MethodSource("arrayOfDueDatesAndTaskNames")
    void testInvalidEmailAndPasswordForAddUser(List<String> userNamesAndPasswords) throws SQLException {
        String questions = "test1,test2,test3";
        List<String> questionList = Arrays.asList(questions.split(","));
        UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject(userNamesAndPasswords.get(0), "parth", "thummar"
                , userNamesAndPasswords.get(1), questionList);
        Assertions.assertFalse(userAPI.addUser());
    }

}