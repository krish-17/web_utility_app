package com.wau.calendar;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalendarStoreAPIImpl implements CalendarStoreAPI {

    private static CalendarStoreAPI calendarStoreAPI = null;
    private static final String TABLE_NAME_FOR_TASK = "event";
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_TITLE = "event_title";
    private static final String USER_ID = "user_id";
    private static final String EVENT_ATTENDEES = "event_attendees";
    private static final String EVENT_DESCRIPTION = "event_description";
    private static final String EVENT_START_TIME = "event_starttime";
    private static final String EVENT_END_TIME = "event_endtime";
    private static final String EVENT_WHERE_CLAUSE = " WHERE ";
    private static final String ESCAPE_CHAR_FOR_QUERY = "\", \"";
    private final LogAPI logAPI = LogAPIImpl.instance();

    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();

    public static CalendarStoreAPI instance() {
        if (null == calendarStoreAPI) {
            calendarStoreAPI = new CalendarStoreAPIImpl();
        }
        return calendarStoreAPI;
    }

    @Override
    public boolean addEvent(EventAPIImpl event) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(getInsertEventQuery(event));
            databaseConnectorAPI.clearResources();
            logAPI.infoLog("Inserted Event Successfully");
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Create Event Failed");
        }
    }

    @Override
    public EventAPIImpl getEvent(String eventId, EventAPIImpl event) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            ResultSet resultSetEvent = databaseConnectorAPI.getStatement().executeQuery(getFetchQuery(eventId));
            while (resultSetEvent.next()) {
                event.setEventId(resultSetEvent.getString(EVENT_ID));
                event.setUserId(resultSetEvent.getString(USER_ID));
                event.setEventTitle(resultSetEvent.getString(EVENT_TITLE));
                event.setEventDescription(resultSetEvent.getString(EVENT_DESCRIPTION));
                event.setStartTime(resultSetEvent.getString(EVENT_START_TIME));
                event.setEndTime(resultSetEvent.getString(EVENT_END_TIME));
                event.setAttendees(resultSetEvent.getString(EVENT_ATTENDEES));
            }
            databaseConnectorAPI.clearResources();
            logAPI.infoLog("Event returned Successfully");
            return event;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid Event Creation");
        }
    }

    @Override
    public boolean deleteEvent(String eventId) throws SQLException {
        try {
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(getDeleteQuery(eventId));
            databaseConnectorAPI.clearResources();
            logAPI.infoLog("Deleted Event Successfully");
            return true;
        } catch (Exception e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Invalid Event Creation");
        }
    }

    @Override
    public List<EventAPIImpl> getEventForUser(String userId) throws SQLException {
        try {
            String fetchEventListQuery = getEventQueryByUserId(userId);
            databaseConnectorAPI.getConnection();
            ResultSet eventDBResult = databaseConnectorAPI.getStatement().executeQuery(fetchEventListQuery);
            List<EventAPIImpl> eventAPIList = convertResultFromDatabaseIntoListOfEvent(eventDBResult);
            databaseConnectorAPI.clearResources();
            logAPI.infoLog("Successfully retrieved logs");
            return eventAPIList;
        } catch (SQLException e) {
            logAPI.errorLog("Failed to retrieve user data");
            throw new SQLException(e.getMessage());
        }
    }

    private List<EventAPIImpl> convertResultFromDatabaseIntoListOfEvent(ResultSet eventDBResult) throws SQLException {
        try {
            List<EventAPIImpl> userEventList = new ArrayList<>();
            while (eventDBResult.next()) {
                String eventId = eventDBResult.getString(EVENT_ID);
                String userId = eventDBResult.getString(USER_ID);
                String eventTitle = eventDBResult.getString(EVENT_TITLE);
                String eventDescription = eventDBResult.getString(EVENT_DESCRIPTION);
                String eventStartTime = eventDBResult.getString(EVENT_START_TIME);
                String eventEndTime = eventDBResult.getString(EVENT_END_TIME);
                String eventAttendees = eventDBResult.getString(EVENT_ATTENDEES);
                EventAPIImpl event = new EventAPIImpl(eventId, userId, eventTitle, eventAttendees, eventDescription, eventStartTime, eventEndTime, null);
                userEventList.add(event);
            }
            return userEventList;
        } catch (SQLException s) {
            logAPI.errorLog(s.getMessage());
            throw new SQLException("Failed to retrieve user data");
        }
    }

    private String getInsertEventQuery(EventAPIImpl event) {
        String id = String.valueOf(System.currentTimeMillis());
        return "INSERT INTO " + TABLE_NAME_FOR_TASK + " (" + EVENT_ID + ", " + EVENT_TITLE + ", " + USER_ID + ", " + EVENT_ATTENDEES + "," + EVENT_DESCRIPTION + ", " +
                "" + EVENT_START_TIME + ", " + EVENT_END_TIME + ") VALUES (\"" + id + ESCAPE_CHAR_FOR_QUERY + event.getEventTitle() + ESCAPE_CHAR_FOR_QUERY +
                "" + event.getUserId() + ESCAPE_CHAR_FOR_QUERY + event.getAttendees() + ESCAPE_CHAR_FOR_QUERY + event.getEventDescription() + ESCAPE_CHAR_FOR_QUERY +
                "" + event.getStartTime() + ESCAPE_CHAR_FOR_QUERY +
                "" + event.getEndTime() + "\")";
    }

    private String getFetchQuery(String eventId) {
        return "SELECT " + EVENT_ID + ", " + EVENT_TITLE + ", " + EVENT_ATTENDEES + ", " + EVENT_START_TIME + ", " + EVENT_END_TIME + ", " + EVENT_DESCRIPTION + " " +
                "FROM " + TABLE_NAME_FOR_TASK + EVENT_WHERE_CLAUSE + EVENT_ID + " ='" + eventId + "'";
    }

    private String getDeleteQuery(String eventId) {
        return "DELETE FROM " + TABLE_NAME_FOR_TASK + EVENT_WHERE_CLAUSE + EVENT_ID + " ='" + eventId + "'";
    }

    private String getEventQueryByUserId(String userId) {
        return "SELECT * FROM  " + TABLE_NAME_FOR_TASK + EVENT_WHERE_CLAUSE + USER_ID + "= '" + userId + "'";
    }
}