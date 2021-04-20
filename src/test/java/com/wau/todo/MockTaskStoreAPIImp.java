package com.wau.todo;

import java.util.ArrayList;
import java.util.List;

public class MockTaskStoreAPIImp implements TaskStoreAPI {

    @Override
    public boolean loadTask(TaskAPIImpl taskAPIImpl) {
        return taskAPIImpl.getUserID().equals("1617746924762") &&
                taskAPIImpl.getTaskName().equals("testing") &&
                taskAPIImpl.getTaskDescription().equals("Learn testing from 5408 course") &&
                taskAPIImpl.getDueDate().equals("1639291332000") &&
                taskAPIImpl.getTaskLabel().equals("test") &&
                taskAPIImpl.getUserTimezone() == 2 &&
                taskAPIImpl.getTimeZoneToBeConverted() == 2;
    }

    @Override
    public boolean deleteTask(String userID, String taskID) {
        return userID.equals("1617746924762") && taskID.equals("1617746924762");
    }

    @Override
    public boolean updateTaskName(String userID, String taskID, String updatedNameValue) {
        return userID.equals("1617746924762")
                && taskID.equals("1617746924762")
                && updatedNameValue.equals("updatedName");
    }

    @Override
    public boolean updateTaskDescription(String userID, String taskID, String updatedDescriptionValue) {
        return userID.equals("1617746924762")
                && taskID.equals("1617746924762")
                && updatedDescriptionValue.equals("updatedDescription");
    }

    @Override
    public List<TaskAPIImpl> listOfTaskBetweenDates(String userID, long startMillis, long endMillis) {
        if (userID.equals("1617746924762") && startMillis == 1639291332000L && endMillis == 1639291932000L) {
            List<TaskAPIImpl> taskAPIList = new ArrayList<>();
            TaskAPIImpl taskAPI1 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924762"
                    , "testing", "label", "1639291332000",
                    2, 2);
            TaskAPIImpl taskAPI2 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924763"
                    , "testing", "label", "1639291332000",
                    2, 2);
            taskAPIList.add(taskAPI1);
            taskAPIList.add(taskAPI2);
            return taskAPIList;
        }
        return null;

    }

    @Override
    public int countOfTaskBetweenDates(String userID, long startMillis, long endMillis) {
        if (userID.equals("1617746924762") && startMillis == 1652122500000L && endMillis == 1652295300000L) {
            return 2;
        }
        return 0;
    }

    @Override
    public boolean loadTaskForATO(List<String> taskName, Long dueDate) {
        return taskName.get(0).equals("First") && taskName.get(1).equals("Second") && dueDate == 1617746924762L;
    }

    @Override
    public List<TaskAPIImpl> listOfTaskBasedOnLabel(String userID, String label) {
        if (userID.equals("1617746924762") && label.equals("label")) {
            List<TaskAPIImpl> taskAPIList = new ArrayList<>();
            TaskAPIImpl taskAPI1 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924762"
                    , "testing", "label", "1639291332000",
                    2, 2);
            TaskAPIImpl taskAPI2 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924763"
                    , "testing", "label", "1639291332000",
                    2, 2);
            taskAPIList.add(taskAPI1);
            taskAPIList.add(taskAPI2);
            return taskAPIList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<TaskAPIImpl> getAllTaskOfUser(String userId) {
        if (userId.equals("1617746924762")) {
            List<TaskAPIImpl> taskAPIList = new ArrayList<>();
            TaskAPIImpl taskAPI1 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924762"
                    , "testing", "label", "1639291332000",
                    2, 2);
            TaskAPIImpl taskAPI2 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924763"
                    , "testing", "label", "1639291332000",
                    2, 2);
            taskAPIList.add(taskAPI1);
            taskAPIList.add(taskAPI2);
            return taskAPIList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<TaskAPIImpl> listOfTaskSortedByDate(String userID) {
        if (userID.equals("1617746924762")) {
            List<TaskAPIImpl> taskAPIList = new ArrayList<>();
            TaskAPIImpl taskAPI1 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924762"
                    , "testing", "label", "1639291332000",
                    2, 2);
            TaskAPIImpl taskAPI2 = new TaskAPIImpl("1617746924762", "demo",
                    "1617746924763"
                    , "testing", "label", "1639291332000",
                    2, 2);
            taskAPIList.add(taskAPI1);
            taskAPIList.add(taskAPI2);
            return taskAPIList;
        }
        return new ArrayList<>();
    }

    @Override
    public String getUserEmail(String userID) {
        if (userID.equals("1617746924762")) {
            return "demo@gmail.com";
        }
        return null;
    }

    @Override
    public boolean setTaskToAssignee(String userID, String taskID) {
        return userID.equals("1617746924762") && taskID.equals("1617746924762");
    }
}
