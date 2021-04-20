package com.wau.clock;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClockStoreAPIImpl implements ClockStoreAPI {

    private static ClockStoreAPI clockStoreAPI = null;
    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();
    private final LogAPI logAPI = LogAPIImpl.instance();
    private static final String TABLE_NAME = "clock_meta";
    private static final String TIMEZONE_ID = "timeZoneId";
    private static final String CLOCK_ID = "clockId";
    private static final String USER_ID = "userId";
    private static final String CLOCK_NAME = "clockName";
    private static final String WHERE = " where ";

    public static ClockStoreAPI instance() {
        if (null == clockStoreAPI) {
            clockStoreAPI = new ClockStoreAPIImpl();
        }
        return clockStoreAPI;
    }

    @Override
    public boolean createClock(ClockMetaAPIImpl clockMeta) throws SQLException {
        try {
            String queryToAddClock = constructAddClockQuery(clockMeta);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(queryToAddClock);
            databaseConnectorAPI.clearResources();
            logAPI.infoLog(queryToAddClock);
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to create Clock! DB Failure");
        }
    }

    @Override
    public List<ClockMetaAPIImpl> getClocksByUserId(String userId) throws SQLException {
        try {
            String fetchClockListQuery = getClockQueryByUserId(userId);
            databaseConnectorAPI.getConnection();
            ResultSet clockMetaDBResult = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(fetchClockListQuery);
            List<ClockMetaAPIImpl> clockUsersById =
                    convertResultFromDatabaseIntoListOfClockMeta(clockMetaDBResult);
            databaseConnectorAPI.clearResources();
            logAPI.infoLog(fetchClockListQuery);
            return clockUsersById;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid user details to fetch! Please try again");
        }
    }

    @Override
    public ClockMetaAPIImpl getClockByClockId(String clockId) throws SQLException {
        try {
            String getClockByClockIdQuery = getClockQueryByClockId(clockId);
            databaseConnectorAPI.getConnection();
            ResultSet clockMetaDBResult =
                    databaseConnectorAPI.getStatement().executeQuery(getClockByClockIdQuery);
            ClockMetaAPIImpl userClockByClockId =
                    convertResultFromDatabaseIntoClockMeta(clockMetaDBResult);
            databaseConnectorAPI.clearResources();
            logAPI.infoLog(getClockByClockIdQuery);
            return userClockByClockId;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid Clock Id");
        }
    }

    @Override
    public boolean modifyTimeZone(String userId, String clockId, int timeZoneId) throws SQLException {
        try {
            String updateQueryToModifyTimeZoneByUserId =
                    getUpdateQueryToModifyTimeZone(userId, clockId, timeZoneId);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeQuery(updateQueryToModifyTimeZoneByUserId);
            databaseConnectorAPI.clearResources();
            logAPI.infoLog(updateQueryToModifyTimeZoneByUserId);
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Illegal parameters");
        }
    }

    private String getUpdateQueryToModifyTimeZone(String userId, String clockId, int timeZoneId) {
        return "update " + TABLE_NAME + " set " + TIMEZONE_ID + " = " + timeZoneId
                + WHERE + CLOCK_ID + " = '" + clockId
                + "' and " + USER_ID + "='" + userId + "'";
    }

    private ClockMetaAPIImpl convertResultFromDatabaseIntoClockMeta(ResultSet clockMetaDBResult) throws SQLException {
        if (clockMetaDBResult.next()) {
            String clockId = clockMetaDBResult.getString(CLOCK_ID);
            String userId = clockMetaDBResult.getString(USER_ID);
            String clockName = clockMetaDBResult.getString(CLOCK_NAME);
            int timeZoneId = clockMetaDBResult.getInt(TIMEZONE_ID);
            return new ClockMetaAPIImpl(clockId, userId, clockName, timeZoneId);
        }
        throw new SQLException("Empty entries in Result Set");
    }

    private List<ClockMetaAPIImpl> convertResultFromDatabaseIntoListOfClockMeta(ResultSet clockMetaDBResult)
            throws SQLException {
        List<ClockMetaAPIImpl> userClockList = new ArrayList<>();
        while (clockMetaDBResult.next()) {
            String clockId = clockMetaDBResult.getString(CLOCK_ID);
            String userId = clockMetaDBResult.getString(USER_ID);
            String clockName = clockMetaDBResult.getString(CLOCK_NAME);
            int timeZoneId = clockMetaDBResult.getInt(TIMEZONE_ID);
            ClockMetaAPIImpl userClock =
                    new ClockMetaAPIImpl(clockId, userId, clockName, timeZoneId);
            userClockList.add(userClock);
        }
        return userClockList;
    }


    private String constructAddClockQuery(ClockMetaAPIImpl clockMeta) {
        return "insert into " + TABLE_NAME + " values('" + clockMeta.getUserId() +
                "','" + clockMeta.getClockId() +
                "','" + clockMeta.getClockName() +
                "'," + clockMeta.getTimeZoneId() +
                ")";
    }

    private String getClockQueryByUserId(String userId) {
        return "select * from " + TABLE_NAME + WHERE + USER_ID + "='" + userId + "'";
    }


    private String getClockQueryByClockId(String clockId) {
        return "select * from " + TABLE_NAME + WHERE + CLOCK_ID + " = '" + clockId + "'";
    }
}
