package com.wau.calendar;


import java.sql.SQLException;
import java.util.List;

public interface CalendarStoreAPI {
    boolean addEvent(EventAPIImpl event) throws SQLException;

    EventAPIImpl getEvent(String eventId, EventAPIImpl event) throws SQLException;

    boolean deleteEvent(String eventId) throws SQLException;

    List<EventAPIImpl> getEventForUser(String userId) throws SQLException;
}