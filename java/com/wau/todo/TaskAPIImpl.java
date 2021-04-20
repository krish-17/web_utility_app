package com.wau.todo;

import com.wau.time.TimeZoneAPI;
import com.wau.user.UserAPI;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TaskAPIImpl implements TaskAPI {

    private String userID;
    private String taskID;
    private String taskName;
    private String taskDescription;
    private String dueDate;
    private String taskLabel;
    private int userTimezone;
    private int timeZoneToBeConverted;
    private String convertedDueDate;
    private TaskStoreAPI taskStoreAPI;
    private String displayableDueDate;
    private String displayableConvertedDate;

    public TaskAPIImpl(TaskStoreAPI taskStoreAPI) {
        this.taskStoreAPI = taskStoreAPI;
    }

    public TaskAPIImpl(String userID, String taskName, String taskDescription, String dueDate,
                       String taskLabel, int userTimezone,
                       int timeZoneToBeConverted, TaskStoreAPI taskStoreAPI) {
        this.taskStoreAPI = taskStoreAPI;
        this.userID = userID;
        this.taskID = String.valueOf(System.currentTimeMillis());
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.taskLabel = taskLabel;
        this.userTimezone = userTimezone;
        this.timeZoneToBeConverted = timeZoneToBeConverted;
    }

    public TaskAPIImpl(String userID, String taskName, String taskID, String taskDescription,
                       String taskLabel, String dueDate, int userTimeZone, int timeZoneToBeConverted) {
        this.userID = userID;
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskLabel = taskLabel;
        this.dueDate = dueDate;
        this.userTimezone = userTimeZone;
        this.timeZoneToBeConverted = timeZoneToBeConverted;
    }

    private String convertDueDate(TimeZoneAPI timeZoneAPI, String dueDate) {
        return timeZoneAPI.getTimeInMillisForToDo(dueDate, this.timeZoneToBeConverted);

    }

    private String dueDate(TimeZoneAPI timeZoneAPI) {
        return timeZoneAPI.getTimeInMillisForToDo(this.dueDate, this.userTimezone);

    }

    public String getUserID() {
        return userID;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTaskLabel() {
        return taskLabel;
    }

    public int getUserTimezone() {
        return userTimezone;
    }

    public int getTimeZoneToBeConverted() {
        return timeZoneToBeConverted;
    }

    public String getConvertedDueDate() {
        return convertedDueDate;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTaskLabel(String taskLabel) {
        this.taskLabel = taskLabel;
    }

    public void setUserTimezone(int userTimezone) {
        this.userTimezone = userTimezone;
    }

    public void setTimeZoneToBeConverted(int timeZoneToBeConverted) {
        this.timeZoneToBeConverted = timeZoneToBeConverted;
    }

    public void setConvertedDueDate(String convertedDueDate) {
        this.convertedDueDate = convertedDueDate;
    }

    public String getDisplayableDueDate() {
        return displayableDueDate;
    }

    public void setDisplayableDueDate(String displayableDueDate) {
        this.displayableDueDate = displayableDueDate;
    }

    public String getDisplayableConvertedDate() {
        return displayableConvertedDate;
    }

    public void setDisplayableConvertedDate(String displayableConvertedDate) {
        this.displayableConvertedDate = displayableConvertedDate;
    }

    private boolean inValidTask() {
        boolean emptyString = this.taskName.isEmpty() || this.taskDescription.isEmpty() ||
                this.taskLabel.isEmpty() || this.dueDate.isEmpty();
        boolean validTimeZone = this.userTimezone < 0 || this.timeZoneToBeConverted < 0 || this.userTimezone > 3 ||
                this.timeZoneToBeConverted > 3;
        boolean validTaskID = this.taskID.isEmpty();
        return emptyString || validTimeZone || validTaskID;
    }

    @Override
    public boolean loadTask(TimeZoneAPI timeZoneAPI) throws SQLException {
        if (inValidTask()) {
            return false;
        }
        String dueDateString = this.dueDate;
        this.dueDate = dueDate(timeZoneAPI);
        this.convertedDueDate = convertDueDate(timeZoneAPI, dueDateString);
        return this.taskStoreAPI.loadTask(this);
    }

    private void loadTaskBasedOnATO(List<String> taskName, Long dueDate) throws SQLException {
        this.taskStoreAPI.loadTaskForATO(taskName, dueDate);
    }

    @Override
    public boolean deleteTask(String userID, String taskID) throws SQLException {
        if (taskID.isEmpty()) {
            return false;
        }
        return this.taskStoreAPI.deleteTask(userID, taskID);
    }

    @Override
    public boolean updateTaskName(String userID, String taskID, String updatedNameValue) throws SQLException {
        if (taskID.isEmpty() || updatedNameValue.isEmpty()) {
            return false;
        }
        return this.taskStoreAPI.updateTaskName(userID, taskID, updatedNameValue);
    }

    @Override
    public boolean updateTaskDescription(String userID, String taskID, String updatedDescriptionValue) throws SQLException {
        if (taskID.isEmpty() || updatedDescriptionValue.isEmpty()) {
            return false;
        }
        return this.taskStoreAPI.updateTaskDescription(userID, taskID, updatedDescriptionValue);
    }

    @Override
    public List<TaskAPIImpl> listOfTaskSortedByDate(String userID, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (timeZoneAPI == null) {
            return new ArrayList<>();
        }
        List<TaskAPIImpl> taskList = this.taskStoreAPI.listOfTaskSortedByDate(userID);
        setDisplayableDate(taskList, timeZoneAPI);
        return taskList;
    }

    @Override
    public List<TaskAPIImpl> listOfTaskBetweenDates(String userID, String startDate, String endDate, TimeZoneAPI timeZoneAPI) throws SQLException, ParseException {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            return new ArrayList<>();
        }
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        long startDateMillis = dateFormat.parse(startDate).getTime();
        long endDateMillis = dateFormat.parse(endDate).getTime();
        List<TaskAPIImpl> taskList = this.taskStoreAPI.listOfTaskBetweenDates(userID, startDateMillis, endDateMillis);
        setDisplayableDate(taskList, timeZoneAPI);
        return taskList;
    }

    @Override
    public int countOfTaskBetweenDates(String userID, long startMillis, long endMillis) throws SQLException {
        if (startMillis == 0 || endMillis == 0) {
            return 0;
        }
        return this.taskStoreAPI.countOfTaskBetweenDates(userID, startMillis, endMillis);
    }

    @Override
    public boolean loadTaskFromATO(Map<Long, List<String>> allocatedTaskMap) throws SQLException {
        List<Long> sortingTaskByDay = new ArrayList<>(allocatedTaskMap.keySet());
        Collections.sort(sortingTaskByDay);
        for (Long aLong : sortingTaskByDay) {
            loadTaskBasedOnATO(allocatedTaskMap.get(aLong), aLong);
        }
        return true;
    }

    @Override
    public boolean setTaskToAssignee(String taskID, String email, UserAPI userAPI) throws SQLException {
        if (email.isEmpty() || taskID.isEmpty()) {
            return false;
        }
        userID = userAPI.getUserIdByEmail(email);
        if (userID == null) {
            return false;
        }
        return taskStoreAPI.setTaskToAssignee(userID, taskID);
    }

    @Override
    public List<TaskAPIImpl> getListOfTaskToDisplay(String userId, String taskLabel, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (taskLabel.isEmpty() || taskStoreAPI == null) {
            return new ArrayList<>();
        }
        List<TaskAPIImpl> taskList = this.taskStoreAPI.listOfTaskBasedOnLabel(userId, taskLabel);
        setDisplayableDate(taskList, timeZoneAPI);
        return taskList;

    }

    private void setDisplayableDate(List<TaskAPIImpl> taskList, TimeZoneAPI timeZoneAPI) {
        for (TaskAPIImpl task : taskList) {
            task.setDisplayableDueDate(timeZoneAPI.displayableTimeFormatForToDo(Long.parseLong(task.getDueDate()), task.getUserTimezone()));
            task.setDisplayableConvertedDate(timeZoneAPI.displayableTimeFormatForToDo(Long.parseLong(task.getDueDate()), task.getTimeZoneToBeConverted()));
        }
    }

    private List<TaskAPIImpl> getAllTaskOfUser(String userId) throws SQLException {
        return this.taskStoreAPI.getAllTaskOfUser(userId);
    }

    @Override
    public List<TaskAPIImpl> getAllTaskOfUserToDisplay(String userId, TimeZoneAPI timeZoneAPI) throws SQLException {
        if (timeZoneAPI == null) {
            return new ArrayList<>();
        }
        List<TaskAPIImpl> taskList = this.getAllTaskOfUser(userId);
        setDisplayableDate(taskList, timeZoneAPI);
        return taskList;
    }

    @Override
    public List<String> getTasksTitleAndDescription(String userId) throws SQLException {

        List<TaskAPIImpl> taskList = getAllTaskOfUser(userId);
        List<String> taskTitleAndDescription = new ArrayList<>();
        for (TaskAPIImpl task : taskList) {
            taskTitleAndDescription.add(task.getTaskName() + "_" + task.getTaskDescription());
        }
        return taskTitleAndDescription;
    }

    @Override
    public String getUserEmail(String userID) throws SQLException {
        return this.taskStoreAPI.getUserEmail(userID);
    }
}
