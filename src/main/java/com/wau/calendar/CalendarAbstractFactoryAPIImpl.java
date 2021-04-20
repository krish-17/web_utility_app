package com.wau.calendar;


public class CalendarAbstractFactoryAPIImpl implements CalendarAbstractFactoryAPI {

    private static CalendarAbstractFactoryAPI calendarAbstractFactoryAPI = null;
    private final CalendarStoreAPI calendarStoreAPI;

    private CalendarAbstractFactoryAPIImpl(CalendarStoreAPI calendarStoreAPI) {
        this.calendarStoreAPI = calendarStoreAPI;
    }

    public static CalendarAbstractFactoryAPI instance(CalendarStoreAPI calendarStoreAPI) {
        if (null == calendarAbstractFactoryAPI) {
            calendarAbstractFactoryAPI = new CalendarAbstractFactoryAPIImpl(calendarStoreAPI);
        }
        return calendarAbstractFactoryAPI;
    }

    @Override
    public EventAPI createEventAPIImplObject(String eventId, String userId, String eventTitle, String attendees,
                                             String eventDescription, String startTime, String endTime) {
        return new EventAPIImpl(eventId, userId, eventTitle,
                attendees, eventDescription, startTime,
                endTime, calendarStoreAPI);
    }

    @Override
    public EventAPI createEventAPIImplObject() {
        return new EventAPIImpl(calendarStoreAPI);
    }

}
