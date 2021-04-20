package com.wau.calendar;

public interface CalendarAbstractFactoryAPI {

    EventAPI createEventAPIImplObject(String eventId, String userId, String eventTitle, String attendees, String eventDescription, String startTime,
                                      String endTime);

    EventAPI createEventAPIImplObject();
}
