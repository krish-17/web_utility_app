package com.wau.clock.alarm;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmStoreAPIImpl implements AlarmStoreAPI {

    private static AlarmStoreAPI alarmStoreAPI = null;
    private static final String TABLE_NAME = "clock_alarm_table";
    private static final String ALARM_ID = "alarmId";
    private static final String CLOCK_ID = "clockId";
    private static final String USER_ID = "userId";
    private static final String ALARM_NAME = "alarmName";
    private static final String ALARM_IN_UTC = "alarmTimeInUTC";
    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();
    private final LogAPI logAPI = LogAPIImpl.instance();

    public static AlarmStoreAPI instance() {
        if (null == alarmStoreAPI) {
            alarmStoreAPI = new AlarmStoreAPIImpl();
        }
        return alarmStoreAPI;
    }

    @Override
    public boolean createAlarmForAUserAndClock(AlarmAPIImpl alarmAPIImpl) throws SQLException {
        try {
            String addAlarmQuery = getInsertAlarmQuery(alarmAPIImpl);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(addAlarmQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to create alarm");
        }
    }


    @Override
    public boolean updateTimeInAlarm(AlarmAPIImpl alarmAPIImpl) throws SQLException {
        try {
            String updateAlarmTimeQuery = getUpdateAlarmTimeQuery(alarmAPIImpl);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(updateAlarmTimeQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid Alarm Chosen to edit");
        }
    }

    @Override
    public boolean updateAlarmNameInAlarm(AlarmAPIImpl alarmAPIImpl) throws SQLException {
        try {
            String updateAlarmTimeQuery = getUpdateAlarmNameQuery(alarmAPIImpl);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(updateAlarmTimeQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid Alarm Chosen to edit");
        }
    }


    @Override
    public List<AlarmAPIImpl> getAlarmsForUserById(String userId) throws SQLException {
        try {
            String selectAlarmListForUser = getSelectAlarmsByUserIdQuery(userId);
            databaseConnectorAPI.getConnection();
            List<AlarmAPIImpl> alarmAPIImplList = fetchResultSetAndConvertToAlarmList(
                    databaseConnectorAPI.getStatement().executeQuery(selectAlarmListForUser));
            databaseConnectorAPI.clearResources();
            return alarmAPIImplList;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to Find alarms for the user at the moment! Please try later");
        }
    }

    @Override
    public boolean deleteAlarmByAlarmId(String alarmId) throws SQLException {
        try {
            String deleteAlarmQuery = getDeleteAlarmQuery(alarmId);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(deleteAlarmQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to delete the selected alarm at the moment! Please try later");
        }
    }

    private List<AlarmAPIImpl> fetchResultSetAndConvertToAlarmList(ResultSet alarmResultSet)
            throws SQLException {
        List<AlarmAPIImpl> alarmAPIImplList = new ArrayList<>();
        while (alarmResultSet.next()) {
            String alarmId = alarmResultSet.getString(ALARM_ID);
            String clockId = alarmResultSet.getString(CLOCK_ID);
            String userId = alarmResultSet.getString(USER_ID);
            String alarmName = alarmResultSet.getString(ALARM_NAME);
            String alarmTimeInUTC = alarmResultSet.getString(ALARM_IN_UTC);
            AlarmAPIImpl alarmAPIImpl = new AlarmAPIImpl(alarmId, clockId, userId,
                    alarmName, alarmTimeInUTC);
            alarmAPIImplList.add(alarmAPIImpl);
        }
        return alarmAPIImplList;
    }

    private String getInsertAlarmQuery(AlarmAPIImpl alarmAPIImpl) {
        return "insert into " + TABLE_NAME + " values('" +
                alarmAPIImpl.getAlarmId() + "','" +
                alarmAPIImpl.getClockId() + "','" +
                alarmAPIImpl.getUserId() + "','" +
                alarmAPIImpl.getAlarmName() + "','" +
                alarmAPIImpl.getAlarmTimeInUTC() + "')";
    }

    private String getUpdateAlarmTimeQuery(AlarmAPIImpl alarmAPIImpl) {
        return "update " + TABLE_NAME + " set " + ALARM_IN_UTC + "='" +
                alarmAPIImpl.getAlarmTimeInUTC() + "' where " + ALARM_ID + "='" +
                alarmAPIImpl.getAlarmId() + "'";
    }

    private String getUpdateAlarmNameQuery(AlarmAPIImpl alarmAPIImpl) {
        return "update " + TABLE_NAME + " set " + ALARM_NAME + "='" +
                alarmAPIImpl.getAlarmName() + "' where " + ALARM_ID + "='" +
                alarmAPIImpl.getAlarmId() + "'";
    }

    private String getSelectAlarmsByUserIdQuery(String userId) {
        return "select * from " + TABLE_NAME + " where " + USER_ID + "='" +
                userId + "'";
    }

    private String getDeleteAlarmQuery(String alarmId) {
        return "delete from " + TABLE_NAME + " where " + ALARM_ID + "='" +
                alarmId + "'";
    }
}
