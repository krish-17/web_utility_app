package com.wau.clock.alarm;

import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.util.List;

public interface AlarmAPI {

    boolean createAlarm(TimeZoneAPI timeZoneAPI) throws SQLException;

    boolean editTimeInAlarm(TimeZoneAPI timeZoneAPI) throws SQLException;

    boolean editAlarmName() throws SQLException;

    List<AlarmAPIImpl> getAlarmsOfUser(String userId) throws SQLException;

    List<AlarmAPIImpl> getAlarmsByClockId(String userId, String clockId, int timeZoneId, TimeZoneAPI timeZoneAPI) throws SQLException;

    AlarmAPIImpl getAlarmByAlarmId(String userId, String alarmId) throws SQLException;

    boolean deleteAlarmByAlarmId(String alarmId) throws SQLException;

    List<String> getAlarmsTitleAndTime(String userId, TimeZoneAPI timeZoneAPI) throws SQLException;
}
