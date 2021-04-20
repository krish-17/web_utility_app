package com.wau.clock;

import com.wau.clock.alarm.AlarmAPI;
import com.wau.clock.alarm.AlarmAPIImpl;
import com.wau.clock.alarm.AlarmStoreAPI;

public class ClockAbstractFactoryAPIImpl implements ClockAbstractFactoryAPI {

    private static ClockAbstractFactoryAPI clockAbstractFactoryAPI = null;
    private final ClockStoreAPI clockStoreAPI;
    private final AlarmStoreAPI alarmStoreAPI;

    private ClockAbstractFactoryAPIImpl(ClockStoreAPI clockStoreAPI, AlarmStoreAPI alarmStoreAPI) {
        this.clockStoreAPI = clockStoreAPI;
        this.alarmStoreAPI = alarmStoreAPI;
    }

    public static ClockAbstractFactoryAPI instance(ClockStoreAPI clockStoreAPI,
                                                   AlarmStoreAPI alarmStoreAPI) {
        if (null == clockAbstractFactoryAPI) {
            clockAbstractFactoryAPI =
                    new ClockAbstractFactoryAPIImpl(clockStoreAPI, alarmStoreAPI);
        }
        return clockAbstractFactoryAPI;
    }

    @Override
    public ClockMetaAPI createClockMetaObject() {
        return new ClockMetaAPIImpl(clockStoreAPI);
    }

    @Override
    public ClockMetaAPI createClockMetaObject(String userId, String clockName,
                                              int timeZoneId) {
        return new ClockMetaAPIImpl(userId, clockName,
                timeZoneId, clockStoreAPI);
    }

    @Override
    public AlarmAPI createAlarmObject() {
        return new AlarmAPIImpl(alarmStoreAPI);
    }

    @Override
    public AlarmAPI createAlarmObject(String alarmId, String clockId, String userId,
                                      String alarmName, String alarmTimeInUTC) {
        return new AlarmAPIImpl(alarmId, clockId, userId,
                alarmName, alarmTimeInUTC, alarmStoreAPI);
    }

    @Override
    public AlarmAPI createAlarmObject(String clockId, String userId,
                                      String alarmName, String alarmTimeInUTC) {
        return new AlarmAPIImpl(clockId, userId, alarmName,
                alarmName, alarmTimeInUTC, alarmStoreAPI);
    }

    @Override
    public ClockMetaAPI createClockMetaObject(String userId, String clockId) {
        return new ClockMetaAPIImpl(userId, clockId, clockStoreAPI);
    }

    @Override
    public AlarmAPI createAlarmObject(String clockId, String userId, String alarmName,
                                      String alarmDate, int timeZoneId) {
        return new AlarmAPIImpl(clockId, userId, alarmName,
                alarmDate, timeZoneId, alarmStoreAPI);
    }

    @Override
    public AlarmAPI createAlarmObject(String alarmId, String clockId, String userId,
                                      String alarmName, String modifiedAlarmTime,
                                      int timeZoneId) {
        return new AlarmAPIImpl(alarmId, clockId, userId,
                alarmName, modifiedAlarmTime, timeZoneId,
                alarmStoreAPI);
    }
}
