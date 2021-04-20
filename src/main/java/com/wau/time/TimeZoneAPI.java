package com.wau.time;

import java.text.ParseException;

public interface TimeZoneAPI {

    void setTimeZone(TimeZone timeZone);

    String displayableTimeFormat(long timeInMillis);

    TimeZone getTimeZoneById(int timeZoneId);

    boolean isValidTimeZoneId(int id);

    String getOffSet(int id);

    String getTimeInMillis(String displayableFormat, int timeZoneId);

    String displayableTimeFormatWithDate(int timeZoneId, long timeInMillis);

    String getTimeInMillisForToDo(String displayableFormat, int timeZoneId);

    String displayableTimeFormatForToDo(long timeInMillis, int timeZoneToBeConverted);

    String getTimeZoneName(int timeZoneId);

    long changeDateTimeFormat(String datetime) throws ParseException;

    String userDisplayingDateTimeFormat(long timeInMillis);

}
