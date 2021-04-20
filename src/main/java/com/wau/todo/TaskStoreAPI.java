package com.wau.todo;

import java.sql.SQLException;
import java.util.List;

public interface TaskStoreAPI {

    boolean loadTask(TaskAPIImpl taskAPIImpl) throws SQLException;

    boolean deleteTask(String userID, String taskID) throws SQLException;

    boolean updateTaskName(String userID, String taskID, String updatedNameValue) throws SQLException;

    boolean updateTaskDescription(String userID, String taskID, String updatedDescriptionValue) throws SQLException;

    List<TaskAPIImpl> listOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException;

    int countOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException;

    boolean loadTaskForATO(List<String> taskName, Long dueDate) throws SQLException;

    List<TaskAPIImpl> listOfTaskBasedOnLabel(String userID, String label) throws SQLException;

    List<TaskAPIImpl> getAllTaskOfUser(String userId) throws SQLException;

    List<TaskAPIImpl> listOfTaskSortedByDate(String userID) throws SQLException;

    String getUserEmail(String userID) throws SQLException;

    boolean setTaskToAssignee(String userID, String taskID) throws SQLException;
}
