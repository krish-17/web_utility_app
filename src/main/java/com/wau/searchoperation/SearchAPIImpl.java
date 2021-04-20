package com.wau.searchoperation;

import com.wau.calendar.EventAPI;
import com.wau.clock.alarm.AlarmAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchAPIImpl implements SearchAPI {

    private final AlarmAPI alarmAPI;
    private final TaskAPI taskAPI;
    private final EventAPI eventAPI;

    public SearchAPIImpl(AlarmAPI alarmAPI, TaskAPI taskAPI, EventAPI eventAPI) {
        this.alarmAPI = alarmAPI;
        this.taskAPI = taskAPI;
        this.eventAPI = eventAPI;
    }

    @Override
    public List<String> utilitySearch(String pattern, List<String> data) {
        List<String> matchedString = new ArrayList<>();
        for (String s : data) {
            boolean match = s.toLowerCase().matches("(.*)" + pattern.toLowerCase() + "(.*)");
            if (match) {
                matchedString.add(s);
            }
        }
        return matchedString;
    }

    @Override
    public List<String> searchAll(String userId, String pattern, TimeZoneAPI timeZoneAPI)
            throws SQLException {
        List<String> allResults = new ArrayList<>();
        allResults.add("Alarms");
        allResults.add(String.join(", ",
                searchAlarms(userId, pattern, timeZoneAPI)));
        allResults.add("Events");
        allResults.add(String.join(", ", searchEvents(userId, pattern)));
        allResults.add("Tasks");
        allResults.add(String.join(", ", searchTasks(userId, pattern)));
        return allResults;
    }

    @Override
    public List<String> searchTasks(String userId, String pattern) throws SQLException {
        return utilitySearch(pattern, taskAPI.getTasksTitleAndDescription(userId));
    }

    @Override
    public List<String> searchAlarms(String userId, String pattern, TimeZoneAPI timeZoneAPI)
            throws SQLException {
        return utilitySearch(pattern,
                alarmAPI.getAlarmsTitleAndTime(userId, timeZoneAPI));
    }

    @Override
    public List<String> searchEvents(String userId, String pattern) throws SQLException {
        return utilitySearch(pattern, eventAPI.getEventTitleAndEventDescription(userId));
    }
}