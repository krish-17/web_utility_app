package com.wau.clock;

import com.wau.WUAConfigTestFactory;
import com.wau.time.TimeZoneAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ClockMetaTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI = wuaConfigTestFactory.getClockAbstractFactoryAPI();

    @Test
    void testCreateAClockForUserForAValidValue() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject("group-6", "Test Clock",
                        1);
        Assertions.assertTrue(clockMetaAPI.createAClockForUser(timeZoneAPI));
    }

    @Test
    void testClockCreationWhenTheTimeZoneIsValid() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject("group-6", "Test Clock",
                        1000);
        assertThrows(SQLException.class, () -> clockMetaAPI.createAClockForUser(timeZoneAPI));

    }

    @Test
    void testCreatAClockForNullUser() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject(null, null, 1);
        assertThrows(SQLException.class,
                () -> clockMetaAPI.createAClockForUser(timeZoneAPI));
    }

    @Test
    void testUpdateTimeZoneInAClockForUserNull() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject(null, null, 1);
        Assertions.assertFalse(
                clockMetaAPI.updateTimeZoneInAClockForUser(timeZoneAPI, 1));
    }

    @Test
    void testUpdateTimeZoneInAClockForUserStoreNull() {
        ClockMetaAPI clockMetaAPI = new ClockMetaAPIImpl(null);
        assertThrows(SQLException.class, () ->
                clockMetaAPI.updateTimeZoneInAClockForUser(timeZoneAPI, 1));
    }

    @Test
    void testUpdateTimeZoneInAClockForInvalidTimeZone() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject("5308653086", "group-6",
                        5980);
        assertThrows(SQLException.class, () ->
                clockMetaAPI.updateTimeZoneInAClockForUser(timeZoneAPI,
                        1290));
    }

    @Test
    void testUpdateTimeZoneInAClockForProperValue() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject("5308653086", "group-6",
                        2);
        Assertions.assertFalse(clockMetaAPI
                .updateTimeZoneInAClockForUser(timeZoneAPI, 2));
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "5308653086", "5308653085"})
    void testGetCurrentTimeByClock(String clockId) {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        assertThrows(SQLException.class,
                () -> clockMetaAPI.getCurrentTimeByClockId(clockId, null));
    }

    @Test
    void testGetCurrentTimeByClockStoreNull() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        assertThrows(SQLException.class, () ->
                clockMetaAPI.getCurrentTimeByClockId(null, timeZoneAPI));
    }

    @Test
    void testGetCurrentTimeByClockIdIsNull() {
        ClockMetaAPI clockMetaAPI = new ClockMetaAPIImpl(null);
        assertThrows(SQLException.class, () ->
                clockMetaAPI.getCurrentTimeByClockId("group-6", timeZoneAPI));
    }


    @Test
    void testGetCurrentTimeByClockId() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        String clockId = "5308653086";
        Assertions.assertNotNull(clockMetaAPI.getCurrentTimeByClockId(clockId,
                timeZoneAPI));
    }

    @Test
    void testGetListOfClockForUserForNull() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        assertThrows(SQLException.class,
                () -> clockMetaAPI.getListOfClocksForUser(null, null));
    }

    @Test
    void testGetListOfClockForInvalidTimeZone() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        assertThrows(SQLException.class,
                () -> clockMetaAPI.getListOfClocksForUser(null, null));
    }

    @Test
    void testGetListOfClockForProperUser() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        Assertions.assertEquals(1, clockMetaAPI
                .getListOfClocksForUser("group-6", timeZoneAPI).size());
    }

    @Test
    void testGetTimeZoneIdSetOfAClockValid() throws SQLException {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        String clockId = "5308653086";
        Assertions.assertEquals(1,
                clockMetaAPI.getTimeZoneIdSetOfAClock(clockId));
    }

    @Test
    void testGetTimeZoneIdSetOfAInvalidClockId() {
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        String clockId = "jdk";
        assertThrows(NullPointerException.class,
                () -> clockMetaAPI.getTimeZoneIdSetOfAClock(clockId));
    }
}
