package com.wau.calendar;

import java.util.ArrayList;
import java.util.List;

public class MockCalendarStore implements CalendarStoreAPI {

    @Override
    public boolean addEvent(EventAPIImpl event) {
        String startTime = "1621146600000";
        String endTime = "1621150200000";
        String eventTitle = "TestTitle";
        String eventId = "123456";
        return event.getEventId().equals(eventId) && event.getEventTitle().equals(eventTitle)
                && event.getStartTime().equals(startTime) && event.getEndTime().equals(endTime);
    }

    @Override
    public EventAPIImpl getEvent(String eventId, EventAPIImpl event) {
        if (eventId.equals("123456")) {
            event.setEventTitle("TestTitle");
            event.setStartTime("1621126800000");
            event.setEndTime("1621134000000");
            event.setAttendees("test1@gmail.com, test2@dal.ca");
            return event;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteEvent(String eventId) {
        return eventId.equals("123456");
    }

    @Override
    public List<EventAPIImpl> getEventForUser(String userId) {
        if (userId.equals("123456")) {
            List<EventAPIImpl> eventList = new ArrayList<>();
            EventAPIImpl event1 = new EventAPIImpl("123456", "123456", "TestTitle", "test1@gmail.com, test2@dal.ca",
                    "TestDescription", "1621126800000", "1621130400000", null);
            EventAPIImpl event2 = new EventAPIImpl("1234", "123456", "TestTitle1", "test1@gmail.com, test2@dal.ca",
                    "TestDescription1", "1621134000000", "1621137600000", null);
            eventList.add(event1);
            eventList.add(event2);
            return eventList;
        } else if (userId.equalsIgnoreCase("group-6")) {
            List<EventAPIImpl> eventList = new ArrayList<>();
            EventAPIImpl event1 = new EventAPIImpl("123456", "123456", "TestTitle", "test1@gmail.com, test2@dal.ca",
                    "Wake TestDescription", "1621126800000", "1621130400000", null);
            EventAPIImpl event2 = new EventAPIImpl("1234", "123456", "TestTitle1", "test1@gmail.com, test2@dal.ca",
                    "Wake TestDescription1", "1621134000000", "1621137600000", null);
            eventList.add(event1);
            eventList.add(event2);
            return eventList;

        } else {
            return null;
        }
    }
}