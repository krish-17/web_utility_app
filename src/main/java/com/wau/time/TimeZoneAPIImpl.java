package com.wau.time;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeZoneAPIImpl implements TimeZoneAPI {

    private static TimeZoneAPI timeZoneAPI = null;

    private static final String DATE_STRING_FORMAT = "MM/dd/yyyy HH:mm:ss";

    private TimeZone currentTimeZone;

    public static TimeZoneAPI instance() {
        if (null == timeZoneAPI) {
            timeZoneAPI = new TimeZoneAPIImpl();
        }
        return timeZoneAPI;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        this.currentTimeZone = timeZone;
    }

    @Override
    public String displayableTimeFormat(long timeInMillis) {
        Date convertedDate = new Date(timeInMillis);
        DateFormat expectedFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        expectedFormat.setTimeZone(java.util.TimeZone.getTimeZone(currentTimeZone.getOffSet()));
        return expectedFormat.format(convertedDate);
    }

    @Override
    public String displayableTimeFormatForToDo(long timeInMillis, int timeZoneToBeConverted) {
        Date convertedDate = new Date(timeInMillis);
        DateFormat expectedFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
        currentTimeZone = getTimeZoneById(timeZoneToBeConverted);
        expectedFormat.setTimeZone(java.util.TimeZone.getTimeZone(currentTimeZone.getOffSet()));
        return expectedFormat.format(convertedDate);
    }

    @Override
    public TimeZone getTimeZoneById(int timeZoneId) {
        for (TimeZone timeZone : TimeZone.values()) {
            if (timeZone.getId() == timeZoneId) {
                return timeZone;
            }
        }
        return TimeZone.NONE;
    }

    @Override
    public boolean isValidTimeZoneId(int timeZoneId) {
        for (TimeZone timeZone : TimeZone.values()) {
            if (timeZone.getId() == timeZoneId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getOffSet(int id) {
        if (isValidTimeZoneId(id)) {
            TimeZone timeZone = getTimeZoneById(id);
            return timeZone.getOffSet();
        }
        return TimeZone.NONE.getOffSet();
    }

    @Override
    public String getTimeInMillis(String displayableFormat, int timeZoneId) {
        displayableFormat = parseAndFormatAlarmString(displayableFormat);
        if (displayableFormat == null) {
            return null;
        }
        if (displayableFormat.contains("/")) {
            return getString(displayableFormat, timeZoneId);
        }
        return displayableFormat;
    }

    @Override
    public String getTimeInMillisForToDo(String displayableFormat, int timeZoneId) {

        if (displayableFormat == null) {
            return null;
        }
        return getString(displayableFormat, timeZoneId);
    }

    private String getString(String displayableFormat, int timeZoneId) {
        try {
            String offSet = getOffSet(timeZoneId);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
            simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(offSet));
            return String.valueOf(simpleDateFormat.parse(displayableFormat).getTime());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String displayableTimeFormatWithDate(int timeZoneId, long timeInMillis) {
        Date convertedDate = new Date(timeInMillis);
        DateFormat expectedFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
        expectedFormat.setTimeZone(java.util.TimeZone.getTimeZone(getOffSet(timeZoneId)));
        return expectedFormat.format(convertedDate);
    }

    @Override
    public String getTimeZoneName(int timeZoneId) {
        return getTimeZoneById(timeZoneId).getTimeZoneName();
    }

    @Override
    public long changeDateTimeFormat(String datetime) throws ParseException {
        if (datetime == null) {
            throw new ParseException("Date Time can't be null!!", 1);
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone(TimeZone.NONE.getOffSet()));
        Date date = dateFormat.parse(datetime);
        return date.getTime();
    }

    @Override
    public String userDisplayingDateTimeFormat(long timeInMillis) {
        Date convertedDate = new Date(timeInMillis);
        DateFormat expectedFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
        if (currentTimeZone != null) {
            expectedFormat.setTimeZone(java.util.TimeZone.getTimeZone(currentTimeZone.getOffSet()));
        }
        return expectedFormat.format(convertedDate);
    }

    private String parseAndFormatAlarmString(String alarmTime) {
        if (alarmTime == null) {
            return null;
        }
        String[] inputAlarmComponentsFromView = alarmTime.split("_");
        if (inputAlarmComponentsFromView.length == 2) {
            String addSecondsToTheFormat = ":00";
            return inputAlarmComponentsFromView[0] + " " + inputAlarmComponentsFromView[1] + addSecondsToTheFormat;
        }
        return null;
    }
}
