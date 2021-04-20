package com.wau.clock.alarm;

import com.wau.WUAConfigTestFactory;
import com.wau.clock.ClockAbstractFactoryAPI;
import com.wau.time.TimeZoneAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlarmAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI = wuaConfigTestFactory.getClockAbstractFactoryAPI();

    @Test
    void testCreateAlarmForNull() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
        assertFalse(alarmAPI.createAlarm(timeZoneAPI));
    }

    @Test
    void testCreateAlarmWhenParametersNull() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject(null, null,
                null, null, null,
                10);
        assertFalse(alarmAPI.createAlarm(timeZoneAPI));
    }

    @Test
    void testCreateAlarmForInvalidTimeZoneId() {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject("5308653086", "group-6",
                "Wake Up!!", "04/15/2021_09:00", 100);
        assertThrows(SQLException.class, () -> alarmAPI.createAlarm(timeZoneAPI));
    }

    @Test
    void testCreateAlarmForProperValues() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject("5308653086", "group-6",
                "Wake Up!!", "04/15/2021_09:00", 0);
        Assertions.assertTrue(alarmAPI.createAlarm(timeZoneAPI));
    }

    @Test
    void testEditTimeInAlarmWhenTimeIsNull() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject("277381394984", "5308653086",
                "group-6", "Wake Up!", null, 0);
        assertFalse(alarmAPI.editTimeInAlarm(timeZoneAPI));
    }

    @Test
    void testFalseValidEditTimeInAlarm() throws SQLException {
        String modifiedAlarmDate = "10/04/1992";
        String modifiedTime = "23:59";
        String modifiedAlarmDateTime = modifiedAlarmDate + "_" + modifiedTime;
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("277381394984", "5308653086",
                        "group-6", "Wake Up!",
                        modifiedAlarmDateTime, 0);
        assertFalse(alarmAPI.editTimeInAlarm(timeZoneAPI));
    }

    @Test
    void testEditAlarmForInvalidTimeZoneId() {
        String modifiedAlarmDate = "10/04/2021";
        String modifiedTime = "23:59";
        String modifiedAlarmDateTime = modifiedAlarmDate + "_" + modifiedTime;
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("277381394984", "5308653086",
                        "group-6", "Wake Up!",
                        modifiedAlarmDateTime, 10000);
        assertThrows(SQLException.class,
                () -> alarmAPI.editTimeInAlarm(timeZoneAPI));
    }

    @Test
    void testValidEditTimeInAlarm() throws SQLException {
        String modifiedAlarmDate = "10/04/2021";
        String modifiedTime = "23:59";
        String modifiedAlarmDateTime = modifiedAlarmDate + "_" + modifiedTime;
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("277381394984", "5308653086",
                        "group-6", "Wake Up!", modifiedAlarmDateTime);
        Assertions.assertTrue(alarmAPI.editTimeInAlarm(timeZoneAPI));
    }

    @Test
    void testNullValueForEditNameInAlarm() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("277381394984", "5308653086",
                        "group-6", null);
        assertFalse(alarmAPI.editAlarmName());
    }

    @Test
    void testInvalidAlarmNameValue() throws SQLException {
        String alarmName = "Whad__up!!!!";
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("277381394984", "5308653086",
                        alarmName, null);
        assertFalse(alarmAPI.editAlarmName());
    }

    @Test
    void testEditAlarmWhenStoreNull() throws SQLException {
        AlarmAPI alarmAPI = new AlarmAPIImpl(null);
        assertFalse(alarmAPI.editAlarmName());
    }

    @Test
    void testValidAlarmNameValue() throws SQLException {
        String alarmName = "Wake Up!!!!";
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject("5308653086", "277381394984",
                        alarmName, null);
        Assertions.assertTrue(alarmAPI.editAlarmName());
    }

    @Test
    void testNullValueGetAlarmsForUserByUserId() {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
        assertThrows(SQLException.class,
                () -> alarmAPI.getAlarmsOfUser(null));
    }

    @Test
    void testGetAlarmWhenStoreNull() {
        AlarmAPI alarmAPI = new AlarmAPIImpl(null);
        assertThrows(SQLException.class, () -> alarmAPI.getAlarmsOfUser("group-6"));
    }

    @Test
    void testValuesGetAlarmsForUserByUserId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertEquals(1, alarmAPI.getAlarmsOfUser("group-6").size());
    }

    @Test
    void testNullValueForGetAlarmByAlarmId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertNull(alarmAPI.getAlarmByAlarmId("group-6", null));
    }

    @Test
    void testProperValueForGetAlarmByAlarmId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertNotNull(alarmAPI.getAlarmByAlarmId("group-6", "277381394984"));

    }

    @Test
    void testGetAlarmByAlarmIdWhenStoreNull() {
        AlarmAPI alarmAPI = new AlarmAPIImpl(null);
        assertThrows(SQLException.class,
                () -> alarmAPI.getAlarmByAlarmId("group-6", "277381394984"));
    }

    @Test
    void testGetAlarmByClockIdWhenStoreNull() {
        AlarmAPI alarmAPI = new AlarmAPIImpl(null);
        assertThrows(SQLException.class,
                () -> alarmAPI.getAlarmsByClockId("group-6", "5308653086",
                        1, timeZoneAPI));
    }

    @Test
    void testNullValueForGetAlarmsByClockId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertEquals(0,
                alarmAPI.getAlarmsByClockId("group-6", null,
                        1, timeZoneAPI).size());
    }

    @Test
    void testProperValueForGetAlarmsByClockId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertEquals(1,
                alarmAPI.getAlarmsByClockId("group-6",
                        "5308653086", 1, timeZoneAPI).size());
    }

    @Test
    void testImProperHumanReadableValueForGetAlarmsByClockId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        List<AlarmAPIImpl> alarmAPIImplList =
                alarmAPI.getAlarmsByClockId("group-6", "5308653086",
                        1, timeZoneAPI);
        Assertions.assertNotEquals("1617820422231",
                alarmAPIImplList.get(0).getDisplayableAlarmTime());
    }

    @Test
    void testProperHumanReadableValueForGetAlarmsByClockId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        List<AlarmAPIImpl> alarmAPIImplList =
                alarmAPI.getAlarmsByClockId("group-6", "5308653086",
                        1, timeZoneAPI);
        Assertions.assertEquals("04/07/2021 14:33:42",
                alarmAPIImplList.get(0).getDisplayableAlarmTime());
    }

    @Test
    void testNullValueForDeleteByAlarmId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        assertFalse(alarmAPI.deleteAlarmByAlarmId(null));
    }

    @Test
    void testForProperValuesForDeleteByAlarmId() throws SQLException {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI
                .createAlarmObject();
        Assertions.assertTrue(alarmAPI.deleteAlarmByAlarmId("277381394984"));
    }

}
