package com.wau.clock.alarm;

import java.sql.SQLException;
import java.util.List;

public interface AlarmStoreAPI {

    boolean createAlarmForAUserAndClock(AlarmAPIImpl alarmAPIImpl) throws SQLException;

    boolean updateTimeInAlarm(AlarmAPIImpl alarmAPIImpl) throws SQLException;

    boolean updateAlarmNameInAlarm(AlarmAPIImpl alarmAPIImpl) throws SQLException;

    List<AlarmAPIImpl> getAlarmsForUserById(String userId) throws SQLException;

    boolean deleteAlarmByAlarmId(String alarmId) throws SQLException;
}
