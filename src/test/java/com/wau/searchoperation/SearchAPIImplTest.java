package com.wau.searchoperation;

import com.wau.WUAConfigTestFactory;
import com.wau.calendar.CalendarAbstractFactoryAPI;
import com.wau.calendar.EventAPI;
import com.wau.clock.ClockAbstractFactoryAPI;
import com.wau.clock.alarm.AlarmAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;
import com.wau.todo.ToDoAbstractFactoryAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = wuaConfigTestFactory.getToDoAbstractFactoryAPI();
    private final TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();
    private final CalendarAbstractFactoryAPI calendarAbstractFactoryAPI = wuaConfigTestFactory.getCalendarAbstractFactoryAPI();
    private final EventAPI eventAPI = calendarAbstractFactoryAPI.createEventAPIImplObject();
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI = wuaConfigTestFactory.getClockAbstractFactoryAPI();
    private final AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();

    @Test
    void testValidValueForUtilitySearch() {
        String pattern = "Test";
        List<String> testing = new ArrayList<>();
        testing.add("This is a test");
        testing.add("I'm Testing the search function");
        testing.add("This is a valid function");
        List<String> matchingString = new ArrayList<>();
        matchingString.add("This is a test");
        matchingString.add("I'm Testing the search function");
        SearchAPI searching = new SearchAPIImpl(null, null, null);
        assertEquals(matchingString,
                searching.utilitySearch(pattern, testing));
    }

    @Test
    void testInvalidValueForUtilitySearch() {
        String pattern = "Test";
        List<String> testing = new ArrayList<>();
        testing.add("This is a valid function");
        List<String> matchingString = new ArrayList<>();
        SearchAPI searching = new SearchAPIImpl(null, null, null);
        assertEquals(matchingString,
                searching.utilitySearch(pattern, testing));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "3"})
    void testInvalidValuesForSearchTasks(String userId) throws SQLException {
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, taskAPI, null);
        String pattern = "test";
        assertEquals(0, searchAPI.searchTasks(userId, pattern).size());
    }

    @Test
    void testTaskSearchWhenTaskAPIIsNull() {
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, null, null);
        String pattern = "test";
        assertThrows(NullPointerException.class,
                () -> searchAPI.searchTasks("userId", pattern));
    }

    @ParameterizedTest
    @ValueSource(strings = {"demo", ""})
    void testTaskSearchWithValidValues(String validPatterns) throws SQLException {
        String userId = "1617746924762";
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, taskAPI, null);
        assertEquals(2, searchAPI.searchTasks(userId, validPatterns).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "3"})
    void testInvalidValuesForSearchEvents(String userId) {
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, null, eventAPI);
        String pattern = "test";
        assertThrows(NullPointerException.class,
                () -> searchAPI.searchEvents(userId, pattern));
    }

    @Test
    void testTaskSearchWhenEventAPIIsNull() {
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, null, null);
        assertThrows(NullPointerException.class,
                () -> searchAPI.searchEvents("userId", "test"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", ""})
    void testEventSearchWithValidValues(String validPatterns) throws SQLException {
        String userId = "123456";
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, taskAPI, eventAPI);
        assertEquals(2, searchAPI.searchEvents(userId, validPatterns).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "3"})
    void testInvalidValuesForSearchAlarms(String userId) {
        SearchAPIImpl searchAPI = new SearchAPIImpl(alarmAPI, null, null);
        String pattern = "test";
        assertThrows(SQLException.class,
                () -> searchAPI.searchAlarms(userId, pattern, timeZoneAPI));
    }

    @Test
    void testTaskSearchWhenAlarmAPIIsNull() {
        SearchAPIImpl searchAPI = new SearchAPIImpl(null, null, null);
        String pattern = "test";
        assertThrows(NullPointerException.class,
                () -> searchAPI.searchAlarms("userId", pattern, timeZoneAPI));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Wake", ""})
    void testAlarmSearchWithValidValues(String validPatterns) throws SQLException {
        String userId = "group-6";
        SearchAPIImpl searchAPI = new SearchAPIImpl(alarmAPI, null, null);
        assertEquals(1,
                searchAPI.searchAlarms(userId, validPatterns, timeZoneAPI).size());
    }

    @Test
    void testSearchAllWithPatternNull() {
        String userId = "group-6";
        SearchAPIImpl searchAPI = new SearchAPIImpl(alarmAPI, taskAPI, eventAPI);
        assertThrows(NullPointerException.class,
                () -> searchAPI.searchAll(userId, null, timeZoneAPI));
    }

    @Test
    void testSearchAllWithInvalidUser() {
        String userId = "Boeing boeing!!";
        SearchAPIImpl searchAPI = new SearchAPIImpl(alarmAPI, taskAPI, eventAPI);
        assertThrows(SQLException.class,
                () -> searchAPI.searchAll(userId, "test", timeZoneAPI));
    }

    @Test
    void testSearchAllWithValidValues() throws SQLException {
        String validPatterns = "wake";
        String userId = "group-6";
        SearchAPIImpl searchAPI = new SearchAPIImpl(alarmAPI, taskAPI, eventAPI);
        assertEquals(6,
                searchAPI.searchAll(userId, validPatterns, timeZoneAPI).size());
    }

}