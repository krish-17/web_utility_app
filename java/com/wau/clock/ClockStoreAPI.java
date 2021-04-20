package com.wau.clock;

import java.sql.SQLException;
import java.util.List;

public interface ClockStoreAPI {

    boolean createClock(ClockMetaAPIImpl clockMeta) throws SQLException;

    List<ClockMetaAPIImpl> getClocksByUserId(String userId) throws SQLException;

    ClockMetaAPIImpl getClockByClockId(String clockId) throws SQLException;

    boolean modifyTimeZone(String userId, String clockId, int timeZoneId) throws SQLException;

}
