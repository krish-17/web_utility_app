package com.wau.clock.alarm;

import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmAPIImpl implements AlarmAPI {

    private static final String ALARM_NAME_REGEX = "^[a-zA-Z0-9 ,.'@!]+$";

    private String userId;
    private String alarmId;
    private String clockId;
    private String alarmName;
    private String alarmTimeInUTC;
    private AlarmStoreAPI alarmStoreAPI;
    private String displayableAlarmTime;

    private int timeZoneId;

    public AlarmAPIImpl(String alarmId, String clockId, String userId, String alarmName,
                        String alarmTime, AlarmStoreAPI alarmStoreAPI) {
        this.userId = userId;
        this.alarmId = alarmId;
        this.alarmName = alarmName;
        this.alarmStoreAPI = alarmStoreAPI;
        this.alarmTimeInUTC = alarmTime;
        this.clockId = clockId;
    }

    public AlarmAPIImpl(String alarmId, String clockId, String userId, String alarmName, String alarmTime) {
        this.alarmId = alarmId;
        this.clockId = clockId;
        this.userId = userId;
        this.alarmName = alarmName;
        this.alarmTimeInUTC = alarmTime;
    }

    public AlarmAPIImpl(AlarmStoreAPI alarmStoreAPI) {
        this.alarmStoreAPI = alarmStoreAPI;
    }

    public AlarmAPIImpl(String clockId, String userId,
                        String alarmName, String alarmDate,
                        int timeZoneId, AlarmStoreAPI alarmStoreAPI) {
        this.alarmId = generateAlarmId();
        this.clockId = clockId;
        this.userId = userId;
        this.alarmName = alarmName;
        this.alarmTimeInUTC = alarmDate;
        this.timeZoneId = timeZoneId;
        this.alarmStoreAPI = alarmStoreAPI;
    }

    public AlarmAPIImpl(String alarmId, String clockId, String userId, String alarmName,
                        String modifiedAlarmTime, int timeZoneId, AlarmStoreAPI alarmStoreAPI) {
        this.alarmId = alarmId;
        this.clockId = clockId;
        this.userId = userId;
        this.alarmName = alarmName;
        this.alarmTimeInUTC = modifiedAlarmTime;
        this.timeZoneId = timeZoneId;
        this.alarmStoreAPI = alarmStoreAPI;
    }

    private String generateAlarmId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String getUserId() {
        return userId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public String getAlarmTimeInUTC() {
        return alarmTimeInUTC;
    }

    public String getClockId() {
        return clockId;
    }

    public int getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(int timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getDisplayableAlarmTime() {
        return displayableAlarmTime;
    }

    public void setDisplayableAlarmTime(String displayableAlarmTime) {
        this.displayableAlarmTime = displayableAlarmTime;
    }

    @Override
    public boolean createAlarm(TimeZoneAPI timeZoneAPI) throws SQLException {
        this.alarmTimeInUTC = timeZoneAPI.getTimeInMillis(alarmTimeInUTC, timeZoneId);
        if (alarmStoreAPI == null || getAlarmId() == null || getAlarmName() == null
                || getAlarmTimeInUTC() == null || getClockId() == null) {
            return false;
        }
        if (timeZoneAPI.isValidTimeZoneId(timeZoneId)) {
            return alarmStoreAPI.createAlarmForAUserAndClock(this);
        }
        throw new SQLException("Set the Clock in Valid TimeZone to create Alarm");
    }

    @Override
    public boolean editTimeInAlarm(TimeZoneAPI timeZoneAPI) throws SQLException {
        this.alarmTimeInUTC = timeZoneAPI.getTimeInMillis(alarmTimeInUTC, timeZoneId);
        if (alarmStoreAPI == null || getAlarmTimeInUTC() == null || isInvalidTime()) {
            return false;
        }
        if (timeZoneAPI.isValidTimeZoneId(timeZoneId)) {
            return alarmStoreAPI.updateTimeInAlarm(this);
        }
        throw new SQLException("TimeZone Id is invalid to edit the alarm");
    }

    private boolean isInvalidTime() {
        long currentTime = System.currentTimeMillis();
        long alarmTime = Long.parseLong(getAlarmTimeInUTC());
        return alarmTime < currentTime;
    }

    @Override
    public boolean editAlarmName() throws SQLException {
        if (alarmStoreAPI == null || getAlarmName() == null) {
            return false;
        }
        if (isRegexMatch(getAlarmName())) {
            return alarmStoreAPI.updateAlarmNameInAlarm(this);
        }
        return false;
    }

    @Override
    public List<AlarmAPIImpl> getAlarmsOfUser(String userId) throws SQLException {
        if (alarmStoreAPI == null || userId == null) {
            throw new SQLException("Insufficient Inputs to fetch alarm details");
        }
        return alarmStoreAPI.getAlarmsForUserById(userId);
    }

    @Override
    public List<AlarmAPIImpl> getAlarmsByClockId(String userId, String clockId, int timeZoneId,
                                                 TimeZoneAPI timeZoneAPI) throws SQLException {
        if (alarmStoreAPI == null) {
            throw new SQLException("No Storage found");
        }
        List<AlarmAPIImpl> alarmAPIImplListForUser = alarmStoreAPI.getAlarmsForUserById(userId);
        alarmAPIImplListForUser = filterByClockId(alarmAPIImplListForUser, clockId);
        setDisplayableAlarmTimeForTheList(alarmAPIImplListForUser,
                timeZoneId, timeZoneAPI);
        return alarmAPIImplListForUser;
    }

    private void setDisplayableAlarmTimeForTheList(List<AlarmAPIImpl> alarmAPIImplListForUser,
                                                   int timeZoneId, TimeZoneAPI timeZoneAPI) {
        for (AlarmAPIImpl alarmAPIImpl : alarmAPIImplListForUser) {
            alarmAPIImpl.setDisplayableAlarmTime(timeZoneAPI
                    .displayableTimeFormatWithDate(timeZoneId,
                            Long.parseLong(alarmAPIImpl.getAlarmTimeInUTC())));
        }
    }

    @Override
    public AlarmAPIImpl getAlarmByAlarmId(String userId, String alarmId) throws SQLException {
        if (alarmStoreAPI == null) {
            throw new SQLException("No Store Present");
        }
        List<AlarmAPIImpl> alarmAPIImplListForUser = alarmStoreAPI.getAlarmsForUserById(userId);
        return filterByAlarmId(alarmAPIImplListForUser, alarmId);
    }

    @Override
    public boolean deleteAlarmByAlarmId(String alarmId) throws SQLException {
        if (alarmStoreAPI == null || alarmId == null) {
            return false;
        }
        return alarmStoreAPI.deleteAlarmByAlarmId(alarmId);
    }

    @Override
    public List<String> getAlarmsTitleAndTime(String userId, TimeZoneAPI timeZoneAPI) throws SQLException {
        List<AlarmAPIImpl> alarmAPIImplList = getAlarmsOfUser(userId);
        List<String> alarmTitleAndNameList = new ArrayList<>();
        for (AlarmAPIImpl alarmAPIImpl : alarmAPIImplList) {
            alarmTitleAndNameList.add(alarmAPIImpl.getAlarmName() + "__" +
                    timeZoneAPI.userDisplayingDateTimeFormat(
                            Long.parseLong(alarmAPIImpl.getAlarmTimeInUTC())));
        }
        return alarmTitleAndNameList;
    }

    private AlarmAPIImpl filterByAlarmId(List<AlarmAPIImpl> alarmAPIImplListForUser, String alarmId) {
        for (AlarmAPIImpl alarmAPIImpl : alarmAPIImplListForUser) {
            if (alarmAPIImpl.getAlarmId().equalsIgnoreCase(alarmId)) {
                return alarmAPIImpl;
            }
        }
        return null;
    }

    private List<AlarmAPIImpl> filterByClockId(List<AlarmAPIImpl> alarmAPIImplListForUser, String clockId) {
        List<AlarmAPIImpl> alarmAPIImplListInAClock = new ArrayList<>();
        for (AlarmAPIImpl alarmAPIImpl : alarmAPIImplListForUser) {
            if (alarmAPIImpl.getClockId().equalsIgnoreCase(clockId)) {
                alarmAPIImplListInAClock.add(alarmAPIImpl);
            }
        }
        return alarmAPIImplListInAClock;
    }

    private boolean isRegexMatch(String alarmName) {
        return alarmName.matches(ALARM_NAME_REGEX);
    }

}
