package com.wau.clock;

import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.util.List;

public interface ClockMetaAPI {

    boolean createAClockForUser(TimeZoneAPI timeZoneAPI) throws SQLException;

    boolean updateTimeZoneInAClockForUser(TimeZoneAPI timeZoneAPI, int timeZoneId) throws SQLException;

    String getCurrentTimeByClockId(String clockId, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<ClockMetaAPIImpl> getListOfClocksForUser(String userId, TimeZoneAPI timeZoneAPI) throws SQLException;

    int getTimeZoneIdSetOfAClock(String clockId) throws SQLException;
}
