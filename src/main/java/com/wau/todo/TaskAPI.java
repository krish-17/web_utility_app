package com.wau.todo;

import com.wau.time.TimeZoneAPI;
import com.wau.user.UserAPI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface TaskAPI {

    boolean loadTask(TimeZoneAPI timeZoneAPI) throws SQLException;

    boolean deleteTask(String userID, String taskID) throws SQLException;

    boolean updateTaskName(String userID, String taskID, String updatedNameValue) throws SQLException;

    boolean updateTaskDescription(String userID, String taskID, String updatedDescriptionValue) throws SQLException;

    List<TaskAPIImpl> listOfTaskBetweenDates(String userID, String startDate, String endDate, TimeZoneAPI timeZoneAPI) throws SQLException, ParseException;

    int countOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException;

    boolean loadTaskFromATO(Map<Long, List<String>> allocatedTaskMap) throws SQLException;

    List<TaskAPIImpl> getListOfTaskToDisplay(String userId, String taskLabel, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<TaskAPIImpl> getAllTaskOfUserToDisplay(String userId, TimeZoneAPI timeZoneAPI) throws SQLException;

    List<String> getTasksTitleAndDescription(String userId) throws SQLException;

    List<TaskAPIImpl> listOfTaskSortedByDate(String userID, TimeZoneAPI timeZoneAPI) throws SQLException;

    String getUserEmail(String userID) throws SQLException;

    boolean setTaskToAssignee(String taskID, String email, UserAPI userAPI) throws SQLException;

}
