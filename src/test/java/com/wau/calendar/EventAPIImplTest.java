package com.wau.calendar;

import com.wau.WUAConfigTestFactory;
import com.wau.time.TimeZoneAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class EventAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();

    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();

    private final CalendarAbstractFactoryAPI calendarTestAbstractFactory
            = wuaConfigTestFactory.getCalendarAbstractFactoryAPI();

    @Test
    void testValidValuesForCreateEvent() throws ParseException, SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456", null,
                "TestTitle", "test1@gmail.com, test2@dal.ca",
                "Test Event Description", "05/16/2021 6:30:00",
                "05/16/2021 7:30:00");
        Assertions.assertTrue(event.createEvent(timeZoneAPI));
    }

    @Test
    void testInvalidValuesForCreateEvent() throws ParseException, SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456",
                null, "Test", null, null,
                null, null);
        Assertions.assertFalse(event.createEvent(timeZoneAPI));
    }

    @Test
    void testNullValuesForCreateEvent() throws ParseException, SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject(null, null,
                null, null, null,
                null, null);
        Assertions.assertFalse(event.createEvent(timeZoneAPI));
    }

    @Test
    void testInvalidTimeValueForCreateEvent() throws ParseException, SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456", null,
                "TestTitle", "test1@gmail.com, test2@dal.ca",
                "Test Event Description", "03/16/2021 6:30:00",
                "03/16/2021 7:30:00");
        Assertions.assertFalse(event.createEvent(timeZoneAPI));
    }

    @Test
    void testValidValueForGetEventById() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456",
                null, "TestTitle",
                "test1@gmail.com, test2@dal.ca", "Test Event Description",
                "05/16/2021 6:30:00", "05/16/2021 7:30:00");
        Assertions.assertEquals(event, event.getEventById("123456", timeZoneAPI));
    }

    @Test
    void testInvalidValueForGetEventById() {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456",
                null, "TestTitle",
                "test1@gmail.com, test2@dal.ca", "Test Event Description",
                "05/16/2021 6:30:00", "05/16/2021 7:30:00");
        Assertions.assertThrows(NullPointerException.class, () -> event.getEventById("234567", timeZoneAPI));
    }

    @Test
    void testNullValueForGetEventById() {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject(null,
                null, null, null, null,
                null, null);
        Assertions.assertThrows(NullPointerException.class, () -> event.getEventById(null, timeZoneAPI));
    }

    @Test
    void testValidValueDeleteEvent() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456",
                null, "TestTitle",
                "test1@gmail.com, test2@dal.ca", "Test Event Description",
                "05/16/2021 6:30:00", "05/16/2021 7:30:00");
        Assertions.assertTrue(event.deleteEvent("123456"));
    }

    @Test
    void testInValidValueDeleteEvent() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject("123456",
                null, "TestTitle",
                "test1@gmail.com, test2@dal.ca", "Test Event Description",
                "05/16/2021 6:30:00", "05/16/2021 7:30:00");
        Assertions.assertFalse(event.deleteEvent("234567"));
    }

    @Test
    void testNullValueDeleteEvent() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject(null, null,
                null, null, null,
                null, null);
        Assertions.assertFalse(event.deleteEvent(null));
    }

    @Test
    void testNullValueForGetEventForUser() {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject(null, null,
                null, null, null,
                null, null);
        Assertions.assertThrows(NullPointerException.class, () -> event.getEventsForUser(null, timeZoneAPI));
    }

    @Test
    void testValidValueForGetEventForUser() throws Exception {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject();
        Assertions.assertEquals(2,
                event.getEventsForUser("123456", timeZoneAPI).size());
    }

    @Test
    void testEmptyStoreAPIValueForGetEventsForUser() {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject();
        Assertions.assertThrows(NullPointerException.class, () -> event.getEventsForUser("1", timeZoneAPI));
    }

    @Test
    void testValidValueForGetEventTitleAndEventDescription() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject();
        List<String> eventList = new ArrayList<>();
        eventList.add("TestTitle_TestDescription");
        eventList.add("TestTitle1_TestDescription1");
        Assertions.assertEquals(eventList, event.getEventTitleAndEventDescription("123456"));
    }

    @Test
    void testInvalidValueForGetEventTitleAndEventDescription() throws SQLException {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject();
        List<String> eventList = new ArrayList<>();
        eventList.add("TestTitle_TestDescription");
        Assertions.assertNotEquals(eventList, event.getEventTitleAndEventDescription("123456"));
    }

    @Test
    void testNullValueForGetEventTitleAndEventDescription() {
        EventAPI event = calendarTestAbstractFactory.createEventAPIImplObject();
        Assertions.assertThrows(NullPointerException.class, () -> event.getEventTitleAndEventDescription(null));
    }
}