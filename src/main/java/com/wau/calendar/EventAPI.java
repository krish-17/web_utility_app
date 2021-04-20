package com.wau.calendar;


import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface EventAPI {

    boolean createEvent(TimeZoneAPI timeZoneAPI) throws SQLException, ParseException;

    EventAPIImpl getEventById(String eventId, TimeZoneAPI timeZoneAPI) throws SQLException;

    boolean deleteEvent(String eventId) throws SQLException;

    List<EventAPIImpl> getEventsForUser(String userId, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<String> getEventTitleAndEventDescription(String userId) throws SQLException;

}