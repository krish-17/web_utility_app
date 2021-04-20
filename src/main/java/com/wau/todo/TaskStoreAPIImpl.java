package com.wau.todo;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.database.DatabaseConnectorAPI;
import com.wau.database.DatabaseConnectorAPIImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskStoreAPIImpl implements TaskStoreAPI {

    private static TaskStoreAPI taskStoreAPI = null;

    private final DatabaseConnectorAPI databaseConnectorAPI = DatabaseConnectorAPIImpl.instance();
    private final LogAPI logAPI = LogAPIImpl.instance();
    public static final String TABLE_NAME_FOR_TASK = "userTask1";
    public static final String TABLE_NAME_FOR_USER = "user";
    private static final String TASK_DB_ERROR = "Unable to get list of task! DB Failure";
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String SELECT_QUERY = "SELECT * FROM ";

    private static final String WHERE_USER_ID = " WHERE userId = '";

    private static final String INSERT_VALUES = " VALUES(";

    public static TaskStoreAPI instance() {
        if (null == taskStoreAPI) {
            taskStoreAPI = new TaskStoreAPIImpl();
        }
        return taskStoreAPI;
    }

    @Override
    public boolean loadTask(TaskAPIImpl taskAPIImpl) throws SQLException {
        try {
            String insertTaskQuery = getInsertTaskQuery(taskAPIImpl);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(insertTaskQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to create task! DB Failure");
        }
    }

    @Override
    public boolean loadTaskForATO(List<String> taskName, Long dueDate) throws SQLException {

        try {
            String insertQueryForATO = getInsertQueryForATO(taskName, dueDate);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(insertQueryForATO);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to create task! DB Failure");
        }
    }

    @Override
    public boolean deleteTask(String userID, String taskID) throws SQLException {

        try {
            String deleteTaskQuery = getDeleteTaskQuery(userID, taskID);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(deleteTaskQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to delete task! DB Failure");
        }
    }

    @Override
    public boolean setTaskToAssignee(String userID, String taskID) throws SQLException {
        try {
            String selectQueryForTask = getSelectQueryForTask(taskID);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(selectQueryForTask);
            if (resultSet.next()) {
                databaseConnectorAPI.getStatement().executeUpdate(getInsertTaskQuery(userID, resultSet));
            }
            databaseConnectorAPI.clearResources();
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to set task! DB Failure");
        }
        return true;
    }

    @Override
    public boolean updateTaskName(String userID, String taskID, String updatedNameValue) throws SQLException {

        try {
            String updateTaskNameQuery = getUpdateTaskNameQuery(userID, taskID, updatedNameValue);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(updateTaskNameQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to update task! DB Failure");
        }
    }

    @Override
    public boolean updateTaskDescription(String userID, String taskID, String updatedDescriptionValue) throws SQLException {

        try {
            String updateTaskDescriptionQuery = getUpdateTaskDescriptionQuery(userID, taskID, updatedDescriptionValue);
            databaseConnectorAPI.getConnection();
            databaseConnectorAPI.getStatement().executeUpdate(updateTaskDescriptionQuery);
            databaseConnectorAPI.clearResources();
            return true;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to update task! DB Failure");
        }
    }

    @Override
    public List<TaskAPIImpl> listOfTaskBasedOnLabel(String userID, String label) throws SQLException {

        try {
            String listOfTaskBasedOnLabelQuery = getListOfTaskBasedOnLabelQuery(userID, label);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(listOfTaskBasedOnLabelQuery);
            List<TaskAPIImpl> taskByLabel =
                    getListFromResultSet(resultSet);
            databaseConnectorAPI.clearResources();
            return taskByLabel;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException(TASK_DB_ERROR);
        }
    }

    @Override
    public List<TaskAPIImpl> getAllTaskOfUser(String userId) throws SQLException {

        try {
            String listOfTaskBasedOnUserId = getListOfTaskBasedOnUserId(userId);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(listOfTaskBasedOnUserId);
            List<TaskAPIImpl> taskByUserId =
                    getListFromResultSet(resultSet);
            databaseConnectorAPI.clearResources();
            return taskByUserId;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException(TASK_DB_ERROR);
        }
    }

    @Override
    public List<TaskAPIImpl> listOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException {

        try {
            String listOfTaskBetweenDates = getForListOfTaskBetweenDatesQuery(userID, startMillis, endMillis);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(listOfTaskBetweenDates);
            List<TaskAPIImpl> taskByDates =
                    getListFromResultSet(resultSet);
            databaseConnectorAPI.clearResources();
            return taskByDates;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException(TASK_DB_ERROR);
        }
    }

    @Override
    public List<TaskAPIImpl> listOfTaskSortedByDate(String userID) throws SQLException {
        try {
            String sortedDueDateQuery = getSortedDueDateQuery(userID);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(sortedDueDateQuery);
            List<TaskAPIImpl> taskSortedByDates =
                    getListFromResultSet(resultSet);
            databaseConnectorAPI.clearResources();
            return taskSortedByDates;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException(TASK_DB_ERROR);
        }
    }

    private List<TaskAPIImpl> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<TaskAPIImpl> taskList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String dueDateString = resultSet.getString("taskDueDate");
                String userID = resultSet.getString("userId");
                String taskID = resultSet.getString("taskId");
                String taskName = resultSet.getString("taskName");
                String taskDescription = resultSet.getString("taskDescription");
                String taskLabel = resultSet.getString("taskLabel");
                int userTimeZone = resultSet.getInt("userTimezone");
                int timeZoneToBeConverted = resultSet.getInt(("timeZoneToBeConverted"));

                TaskAPIImpl taskAPI = new TaskAPIImpl(userID, taskName, taskID, taskDescription,
                        taskLabel, dueDateString, userTimeZone, timeZoneToBeConverted);
                taskList.add(taskAPI);
            }
            return taskList;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to get result! DB Failure");
        }
    }

    @Override
    public int countOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException {
        try {
            int count = 0;
            String countOfTaskQuery = getCountOfTaskQuery(userID, startMillis, endMillis);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(countOfTaskQuery);
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            databaseConnectorAPI.clearResources();
            return count;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to get count! DB Failure");
        }
    }

    @Override
    public String getUserEmail(String userID) throws SQLException {
        try {
            String email = null;
            String selectQueryForUserID = getSelectQueryForUserEmail(userID);
            databaseConnectorAPI.getConnection();
            ResultSet resultSet = databaseConnectorAPI
                    .getStatement()
                    .executeQuery(selectQueryForUserID);
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }
            databaseConnectorAPI.clearResources();
            return email;
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            throw new SQLException("Unable to get user email! DB Failure");
        }
    }

    public String getInsertTaskQuery(String userID, ResultSet resultSet) {

        try {
            return INSERT_INTO + TABLE_NAME_FOR_TASK + INSERT_VALUES + "'" + userID +
                    "','" + resultSet.getString("taskId") + "','" + resultSet.getString("taskName") +
                    "','" + resultSet.getString("taskDescription") + "','" + resultSet.getString("taskDueDate") +
                    "','" + resultSet.getString("taskLabel") + "','" + resultSet.getString("userTimezone") +
                    "','" + resultSet.getString("timeZoneToBeConverted") + "')";
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public String getSelectQueryForTask(String taskID) {
        return SELECT_QUERY + TABLE_NAME_FOR_TASK + " WHERE taskId = " + taskID;
    }


    private String getSelectQueryForUserEmail(String userID) {
        return "SELECT email FROM " + TABLE_NAME_FOR_USER + " WHERE user_id = '" + userID + "'";
    }


    private String getInsertTaskQuery(TaskAPIImpl taskAPIImpl) {
        try {

            return INSERT_INTO + TABLE_NAME_FOR_TASK + INSERT_VALUES + "'" + taskAPIImpl.getUserID() + "','" + taskAPIImpl.getTaskID() + "','" + taskAPIImpl.getTaskName() +
                    "','" + taskAPIImpl.getTaskDescription() + "','" + taskAPIImpl.getDueDate() +
                    "','" + taskAPIImpl.getTaskLabel() + "','" + taskAPIImpl.getUserTimezone() +
                    "','" + taskAPIImpl.getTimeZoneToBeConverted() + "')";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getInsertQueryForATO(List<String> taskName, Long dueDate) {
        return INSERT_INTO + TABLE_NAME_FOR_TASK + INSERT_VALUES + "'" + "1" + "','" + 1 + "','" + taskName +
                "','" + "From ATO" + "','" + dueDate +
                "','" + "ATO" + "','" + "1" +
                "','" + "1" + "')";
    }

    private String getDeleteTaskQuery(String userID, String taskID) {
        return "DELETE FROM " + TABLE_NAME_FOR_TASK + " WHERE taskId =" + taskID + " AND userId= '" + userID + "'";
    }

    private String getUpdateTaskNameQuery(String userID, String taskID, String updatedNameValue) {
        return "UPDATE " + TABLE_NAME_FOR_TASK + " SET taskName = '" + updatedNameValue + "' WHERE taskId = '" + taskID + "' AND userId = '" + userID + "'";
    }

    private String getUpdateTaskDescriptionQuery(String userID, String taskID, String updatedDescriptionValue) {
        return "UPDATE " + TABLE_NAME_FOR_TASK + " SET taskDescription = '" + updatedDescriptionValue + "' WHERE taskId = '" + taskID + "' AND userId= '" + userID + "'";
    }

    private String getForListOfTaskBetweenDatesQuery(String userID, long startMillis, long endMillis) {
        return SELECT_QUERY + TABLE_NAME_FOR_TASK + WHERE_USER_ID + userID + "' AND taskDueDate >= '" + startMillis + "' AND taskDueDate <= '" + endMillis + "'";
    }

    private String getListOfTaskBasedOnLabelQuery(String userID, String label) {
        return SELECT_QUERY + TABLE_NAME_FOR_TASK + WHERE_USER_ID + userID + "' AND taskLabel = '" + label + "'";
    }

    private String getListOfTaskBasedOnUserId(String userId) {
        return SELECT_QUERY + TABLE_NAME_FOR_TASK + WHERE_USER_ID + userId + "'";
    }

    private String getCountOfTaskQuery(String userID, long startMillis, long endMillis) {
        return "SELECT COUNT(*) AS count FROM " + TABLE_NAME_FOR_TASK + WHERE_USER_ID + userID + "' AND taskDueDate >= '" + startMillis + "' AND taskDueDate <= '" + endMillis + "'";
    }

    private String getSortedDueDateQuery(String userID) {
        return SELECT_QUERY + TABLE_NAME_FOR_TASK + WHERE_USER_ID + userID + "' ORDER BY taskDueDate";
    }
}
