package com.wau.clock;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.util.List;

public class ClockMetaAPIImpl implements ClockMetaAPI {

    private final LogAPI logAPI = LogAPIImpl.instance();

    private String clockId;
    private String userId;
    private String clockName;
    private int timeZoneId;
    private ClockStoreAPI clockStoreAPI;
    private String timeZoneName;

    public ClockMetaAPIImpl(String userId, String clockName, int timeZoneId, ClockStoreAPI clockStoreAPI) {
        this.clockId = generateClockId();
        this.userId = userId;
        this.clockName = clockName;
        this.timeZoneId = timeZoneId;
        this.clockStoreAPI = clockStoreAPI;
    }

    public ClockMetaAPIImpl(String clockId, String userId, String clockName, int timeZoneId) {
        this.clockId = clockId;
        this.userId = userId;
        this.clockName = clockName;
        this.timeZoneId = timeZoneId;
    }

    public ClockMetaAPIImpl(ClockStoreAPI clockStoreAPI) {
        this.clockStoreAPI = clockStoreAPI;
    }

    public ClockMetaAPIImpl(String userId, String clockId, ClockStoreAPI clockStoreAPI) {
        this.userId = userId;
        this.clockId = clockId;
        this.clockStoreAPI = clockStoreAPI;
    }

    String generateClockId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String getClockId() {
        return this.clockId;
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
    }


    public String getUserId() {
        return this.userId;
    }

    public int getTimeZoneId() {
        return this.timeZoneId;
    }

    public void setTimeZoneId(int id) {
        this.timeZoneId = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClockName() {
        return clockName;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    @Override
    public boolean createAClockForUser(TimeZoneAPI timeZoneAPI) throws SQLException {
        if (this.clockStoreAPI == null ||
                this.clockId == null ||
                this.clockName == null) {
            logAPI.errorLog("Invalid parameters");
            throw new SQLException("Improper Clock inputs");
        }
        if (isValidTimeZoneId(timeZoneAPI, this.timeZoneId)) {
            return clockStoreAPI.createClock(this);
        }
        logAPI.errorLog("Invalid Time zone Id");
        throw new SQLException("Time Zone not added yet");
    }

    private boolean isValidTimeZoneId(TimeZoneAPI timeZoneAPI, int timeZoneId) {
        return timeZoneAPI.isValidTimeZoneId(timeZoneId);
    }

    @Override
    public boolean updateTimeZoneInAClockForUser(TimeZoneAPI timeZoneAPI, int timeZoneId) throws SQLException {
        if (clockStoreAPI == null) {
            logAPI.errorLog("Invalid parameters");
            throw new SQLException("Bad parameters");
        }
        if (isValidTimeZoneId(timeZoneAPI, timeZoneId)) {
            return clockStoreAPI.modifyTimeZone(this.userId,
                    this.clockId, timeZoneId);
        }
        throw new SQLException("Invalid time zone");
    }

    @Override
    public String getCurrentTimeByClockId(String clockId, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (clockStoreAPI == null || clockId == null) {
            throw new SQLException("Improper inputs to lookup current time. Contact admin");
        }
        ClockMetaAPIImpl clockMeta = clockStoreAPI.getClockByClockId(clockId);
        if (timeZoneAPI == null) {
            throw new SQLException("Unable to perform Time Conversion Operations!");
        }
        timeZoneAPI.setTimeZone(timeZoneAPI.getTimeZoneById(clockMeta.getTimeZoneId()));
        if (timeZoneAPI.isValidTimeZoneId(clockMeta.getTimeZoneId())) {
            return timeZoneAPI.displayableTimeFormat(System.currentTimeMillis());
        }
        throw new SQLException("Invalid TimeZone Id associated with Clock!!");
    }

    @Override
    public List<ClockMetaAPIImpl> getListOfClocksForUser(String userId, TimeZoneAPI timeZoneAPI)
            throws SQLException {
        if (clockStoreAPI == null || userId == null) {
            throw new SQLException("Insufficient inputs");
        }
        List<ClockMetaAPIImpl> clockMetaAPImplList = clockStoreAPI.getClocksByUserId(userId);
        setTimeZoneNameForClockList(timeZoneAPI, clockMetaAPImplList);
        return clockMetaAPImplList;
    }

    private void setTimeZoneNameForClockList(TimeZoneAPI timeZoneAPI,
                                             List<ClockMetaAPIImpl> clockMetaAPImplList) {
        for (ClockMetaAPIImpl clockMetaAPI : clockMetaAPImplList) {
            clockMetaAPI.setTimeZoneName(timeZoneAPI.getTimeZoneName(clockMetaAPI.getTimeZoneId()));
        }
    }

    @Override
    public int getTimeZoneIdSetOfAClock(String clockId) throws SQLException {
        ClockMetaAPIImpl clockMeta = clockStoreAPI.getClockByClockId(clockId);
        return clockMeta.getTimeZoneId();
    }
}
