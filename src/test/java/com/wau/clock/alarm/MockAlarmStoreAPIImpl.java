package com.wau.clock.alarm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MockAlarmStoreAPIImpl implements AlarmStoreAPI {

    @Override
    public boolean createAlarmForAUserAndClock(AlarmAPIImpl alarmAPIImpl) {
        return alarmAPIImpl.getUserId().equalsIgnoreCase("group-6");
    }

    @Override
    public boolean updateTimeInAlarm(AlarmAPIImpl alarmAPIImpl) {
        return alarmAPIImpl.getAlarmId().equalsIgnoreCase("277381394984");
    }

    @Override
    public boolean updateAlarmNameInAlarm(AlarmAPIImpl alarmAPIImpl) {
        return alarmAPIImpl.getAlarmName().equalsIgnoreCase("Wake Up!!!!");
    }

    @Override
    public List<AlarmAPIImpl> getAlarmsForUserById(String userId) throws SQLException {
        if (userId.equalsIgnoreCase("group-6")) {
            List<AlarmAPIImpl> alarmAPIImplList = new ArrayList<>();
            AlarmAPIImpl alarmAPIImpl = new AlarmAPIImpl("277381394984", "5308653086",
                    "group-6", "Wake up!",
                    String.valueOf(1617820422231L));
            alarmAPIImplList.add(alarmAPIImpl);
            return alarmAPIImplList;
        }
        throw new SQLException("User doesn't exist");
    }

    @Override
    public boolean deleteAlarmByAlarmId(String alarmId) {
        return alarmId.equalsIgnoreCase("277381394984");
    }
}
