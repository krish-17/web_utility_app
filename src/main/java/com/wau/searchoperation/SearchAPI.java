package com.wau.searchoperation;

import com.wau.time.TimeZoneAPI;

import java.sql.SQLException;
import java.util.List;

public interface SearchAPI {

    List<String> utilitySearch(String pattern, List<String> data);

    List<String> searchAll(String userId, String pattern, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<String> searchTasks(String userId, String pattern) throws SQLException;

    List<String> searchAlarms(String userId, String pattern, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<String> searchEvents(String userId, String pattern) throws SQLException;
}
