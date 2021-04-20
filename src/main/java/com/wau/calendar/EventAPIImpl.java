package com.wau.calendar;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class EventAPIImpl implements EventAPI {
    private String eventId;
    private String eventTitle;
    private String userId;
    private String attendees;
    private String eventDescription;
    private String startTime;
    private String endTime;
    private final CalendarStoreAPI calendarStoreAPI;
    private final LogAPI log = LogAPIImpl.instance();


    public EventAPIImpl(String eventId, String userId, String eventTitle, String attendees, String eventDescription, String startTime,
                        String endTime, CalendarStoreAPI calendarStoreAPI) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.userId = userId;
        this.attendees = attendees;
        this.eventDescription = eventDescription;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarStoreAPI = calendarStoreAPI;
    }

    public EventAPIImpl(CalendarStoreAPI calendarStoreAPI) {
        this.calendarStoreAPI = calendarStoreAPI;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private boolean isEventInvalid() {
        return this.eventTitle == null || this.startTime == null || this.endTime == null;
    }

    private boolean isStartAndEndDateValid(TimeZoneAPI timeZoneAPI) throws ParseException {
        long currentTime = System.currentTimeMillis();
        return currentTime < timeZoneAPI.changeDateTimeFormat(startTime) &&
                timeZoneAPI.changeDateTimeFormat(startTime) < timeZoneAPI.changeDateTimeFormat(endTime);
    }

    @Override
    public boolean createEvent(TimeZoneAPI timeZoneAPI) throws SQLException, ParseException {
        if (isEventInvalid()) {
            return false;
        }
        if (isStartAndEndDateValid(timeZoneAPI)) {
            setStartTime(String.valueOf(timeZoneAPI.changeDateTimeFormat(getStartTime())));
            setEndTime(String.valueOf(timeZoneAPI.changeDateTimeFormat(getEndTime())));
            log.infoLog("Successfully validated");
            return this.calendarStoreAPI.addEvent(this);
        }
        log.errorLog("Error in creating event");
        return false;
    }

    @Override
    public EventAPIImpl getEventById(String eventId, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (eventId.equals("")) {
            log.errorLog("eventId is null");
            throw new NullPointerException("eventId is null");
        } else {
            EventAPIImpl eventAPI = this.calendarStoreAPI.getEvent(eventId, this);
            eventAPI.setStartTime(timeZoneAPI.userDisplayingDateTimeFormat(Long.parseLong(eventAPI.getStartTime())));
            eventAPI.setEndTime(timeZoneAPI.userDisplayingDateTimeFormat(Long.parseLong(eventAPI.getEndTime())));
            log.infoLog("Successfully getting events");
            return eventAPI;
        }
    }

    @Override
    public boolean deleteEvent(String eventId) throws SQLException {
        if (eventId == null || eventId.isEmpty()) {
            return false;
        } else {
            return this.calendarStoreAPI.deleteEvent(eventId);
        }
    }

    @Override
    public List<EventAPIImpl> getEventsForUser(String userId, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (calendarStoreAPI == null || userId == null) {
            log.errorLog("calendar is null");
            throw new NullPointerException("calendarStore is null");
        } else {
            List<EventAPIImpl> eventAPIList = this.calendarStoreAPI.getEventForUser(userId);
            getModifiedUserList(timeZoneAPI, eventAPIList);
            return eventAPIList;
        }
    }

    private void getModifiedUserList(TimeZoneAPI timeZoneAPI, List<EventAPIImpl> eventAPIList) {
        for (EventAPIImpl event : eventAPIList) {
            event.setStartTime(timeZoneAPI.userDisplayingDateTimeFormat(Long.parseLong(event.getStartTime())));
            event.setEndTime(timeZoneAPI.userDisplayingDateTimeFormat(Long.parseLong(event.getEndTime())));
        }
    }

    @Override
    public List<String> getEventTitleAndEventDescription(String userId) throws SQLException {
        List<EventAPIImpl> eventList = this.calendarStoreAPI.getEventForUser(userId);
        List<String> eventTitleAndDescriptionList = new ArrayList<>();
        for (EventAPIImpl event : eventList) {
            eventTitleAndDescriptionList.add(event.getEventTitle() + "_" + event.getEventDescription());
        }
        log.infoLog("Successfully received users title and description");
        return eventTitleAndDescriptionList;
    }
}