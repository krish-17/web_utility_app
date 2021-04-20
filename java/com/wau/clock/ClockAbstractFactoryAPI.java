package com.wau.clock;

import com.wau.clock.alarm.AlarmAPI;

public interface ClockAbstractFactoryAPI {

    ClockMetaAPI createClockMetaObject();

    ClockMetaAPI createClockMetaObject(String userId, String clockName, int timeZoneId);

    AlarmAPI createAlarmObject();

    AlarmAPI createAlarmObject(String alarmId, String clockId, String userId, String alarmName,
                               String alarmTimeInUTC);

    AlarmAPI createAlarmObject(String clockId, String userId, String alarmName,
                               String alarmTimeInUTC);

    ClockMetaAPI createClockMetaObject(String userId, String clockId);

    AlarmAPI createAlarmObject(String clockId, String userId, String alarmName,
                               String alarmDate, int timeZoneId);

    AlarmAPI createAlarmObject(String alarmId, String clockId, String userId,
                               String alarmName, String modifiedAlarmTime,
                               int timeZoneId);
}
