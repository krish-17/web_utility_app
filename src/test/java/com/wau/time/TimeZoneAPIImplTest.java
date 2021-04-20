package com.wau.time;

import com.wau.WUAConfigTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.stream.Stream;

class TimeZoneAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();

    @Test
    void testIsTimeZoneIdNotSetCorrect() {
        long timeNow = 1617842085918L;
        timeZoneAPI = new TimeZoneAPIImpl();
        Assertions.assertThrows(NullPointerException.class,
                () -> timeZoneAPI.displayableTimeFormat(timeNow));
    }

    @Test
    void testIsTimeZoneIdIsSetCorrect() {
        long timeNow = 1617842085918L;
        timeZoneAPI.setTimeZone(TimeZone.AST);
        Assertions.assertEquals("20:34:45.918",
                timeZoneAPI.displayableTimeFormat(timeNow));
    }

    @Test
    void testForNullValuesInTimeZone() {
        long timeNow = 1617842085918L;
        timeZoneAPI.setTimeZone(null);
        Assertions.assertThrows(NullPointerException.class,
                () -> timeZoneAPI.displayableTimeFormat(timeNow));
    }


    @Test
    void testValidValueForDisplayableTimeFormat() {
        long timeNow = 1617842085918L;
        timeZoneAPI.setTimeZone(TimeZone.AST);
        Assertions.assertEquals("20:34:45.918",
                timeZoneAPI.displayableTimeFormat(timeNow));
    }

    @Test
    void testInvalidValueForDisplayableTimeFormat() {
        long timeNow = 123123388493910232L;
        timeZoneAPI.setTimeZone(TimeZone.AST);
        Assertions.assertNotEquals("20:34:45.918",
                timeZoneAPI.displayableTimeFormat(timeNow));
    }

    @Test
    void testGetTimeZoneByIdForValidValue() {
        int timeZoneId = 1;
        Assertions.assertEquals(TimeZone.AST, timeZoneAPI.getTimeZoneById(timeZoneId));
    }

    @Test
    void testGetTimeZoneByIdForInvalidValue() {
        int timeZoneId = 1987273;
        Assertions.assertEquals(TimeZone.NONE, timeZoneAPI.getTimeZoneById(timeZoneId));
    }

    @Test
    void testIsValidTimeZoneIdForCorrectValue() {
        Assertions.assertTrue(timeZoneAPI.isValidTimeZoneId(1));
    }

    @Test
    void testIsValidTimeZoneIdForIncorrectValue() {
        Assertions.assertFalse(timeZoneAPI.isValidTimeZoneId(12887389));
    }

    @Test
    void testGetOffSetForCorrectValue() {
        Assertions.assertEquals("GMT-0400", timeZoneAPI.getOffSet(1));
    }

    @Test
    void testGetOffSetForIncorrectValues() {
        Assertions.assertNotEquals("GMT-4", timeZoneAPI.getOffSet(100));
    }

    @Test
    void testGetTimeInMillisForEmptyDisplayFormat() {
        Assertions.assertNull(timeZoneAPI.getTimeInMillis("", 1));
    }

    @Test
    void testGetTimeInMillisForInvalidDisplayValue() {
        String displayFormat = "28/04/1995 24:00";
        Assertions.assertNull(timeZoneAPI.getTimeInMillis(displayFormat, 1));
    }

    @Test
    void testGetTimeInMillisForValidDisplayValue() {
        String date = "10/04/2021";
        String time = "23:59";
        String displayTime = date + "_" + time;
        Assertions.assertEquals("1633406340000",
                timeZoneAPI.getTimeInMillis(displayTime, 1));

    }

    @Test
    void testDisplayableTimeFormatWithDateForNullValues() {
        Assertions.assertNotEquals("10/04/2021 23:59:00",
                timeZoneAPI.displayableTimeFormatWithDate(293799813, 1633406340000L));
    }

    @Test
    void testDisplayableTimeFormatWithDateForProperValues() {
        Assertions.assertEquals("10/04/2021 23:59:00",
                timeZoneAPI.displayableTimeFormatWithDate(1, 1633406340000L));
    }

    @Test
    void testGetTimeInMillisForToDoInvalidValues() {
        Assertions.assertNull(timeZoneAPI.getTimeInMillisForToDo("2992 2ii923", 0));
    }

    @Test
    void testGetTimeInMillisForToDoValidValues() {
        Assertions.assertEquals("1633406340000",
                timeZoneAPI.getTimeInMillisForToDo("10/04/2021 23:59:00", 1));
    }

    @Test
    void testDisplayableTimeFormatForToDoInvalidValues() {
        Assertions.assertNotEquals("10/04/2021 23:59:00",
                timeZoneAPI.displayableTimeFormatForToDo(Long.parseLong("2989203"),
                        1));
    }

    @Test
    void testDisplayableTimeFormatForToDoValidValues() {
        timeZoneAPI.setTimeZone(TimeZone.AST);
        Assertions.assertEquals("10/04/2021 23:59:00",
                timeZoneAPI.displayableTimeFormatForToDo(1633406340000L,
                        1));
    }

    @Test
    void testGetTimeZoneNameForInvalidValues() {
        Assertions.assertNotEquals("AST", timeZoneAPI.getTimeZoneName(8990392));
    }

    @Test
    void testGetTimeZoneNameForValidValues() {
        Assertions.assertNotEquals("AST", timeZoneAPI.getTimeZoneName(1));
    }

    static Stream<String> testDatesStrings() {
        return Stream.of("its not even a date!@@ What you will do?", "", null);
    }

    @ParameterizedTest
    @MethodSource("testDatesStrings")
    void testChangeDateTimeFormatInvalidValues(String date) {
        Assertions.assertThrows(ParseException.class,
                () -> timeZoneAPI.changeDateTimeFormat(date));
    }

    @Test
    void testChangeDateTimeFormatProperString() throws ParseException {
        String date = "10/04/2021 23:59:00";
        Assertions.assertEquals(1633391940000L, timeZoneAPI.changeDateTimeFormat(date));
    }

    @Test
    void testUserDisplayingDateTimeFormatForInvalidLong() {
        long time = -1L;
        Assertions.assertNotEquals("null",
                timeZoneAPI.userDisplayingDateTimeFormat(time));
    }

    @Test
    void testUserDisplayingDateTimeFormatForValidLong() {
        long time = 1633391940000L;
        timeZoneAPI.setTimeZone(TimeZone.AST);
        Assertions.assertEquals("10/04/2021 19:59:00",
                timeZoneAPI.userDisplayingDateTimeFormat(time));
    }

}
