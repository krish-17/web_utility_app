package com.wau.ato.taskallocator;

import com.wau.ato.milestone.MilestonesAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;

import java.sql.SQLException;
import java.util.*;


public class TaskAllocatorAPIImpl implements TaskAllocatorAPI {

    private static final long TIME_IN_MILLIS_FOR_ONE_DAY = (long) 24 * 60 * 60 * 1000;
    private final MilestonesAPI milestonesAPI;
    private final String userId;
    private final long timeToStartATOGeneration;
    private long lastDueDateInTheATO;
    private int daysAvailableForTheATO;
    private final long availableWorkHoursEachDay;

    public TaskAllocatorAPIImpl(MilestonesAPI milestonesAPI, String userId) {
        this.milestonesAPI = milestonesAPI;
        this.userId = userId;
        this.timeToStartATOGeneration = System.currentTimeMillis();
        this.availableWorkHoursEachDay = milestonesAPI.getHoursAvailableToWorkPerDay();
    }

    @Override
    public boolean isTaskAllocationPossible(TaskAPI taskAPI, TimeZoneAPI timeZoneAPI) throws SQLException {
        setLastDueDateInTheATO(timeZoneAPI);
        setDaysAvailableForTheATO();
        int numberOfTaskAlreadyExist = taskAPI.countOfTaskBetweenDates(userId,
                timeToStartATOGeneration,
                lastDueDateInTheATO);
        long totalNumberOfHoursRequiredForAlreadyExistsTask =
                numberOfTaskAlreadyExist * 15 * TIME_IN_MILLIS_FOR_ONE_DAY;
        long totalNumberOfHoursAvailable = availableWorkHoursEachDay * daysAvailableForTheATO;
        long totalHoursRequiredToCompleteAllMilestone =
                milestonesAPI.getTimeToCompleteAllTheTasksInMilliseconds();
        long hoursLeftAfterCompletingExistingTasks =
                totalNumberOfHoursAvailable - totalNumberOfHoursRequiredForAlreadyExistsTask;
        return hoursLeftAfterCompletingExistingTasks > totalHoursRequiredToCompleteAllMilestone;
    }

    @Override
    public String[] getAllocatedSchedule(TimeZoneAPI timeZoneAPI, TaskAPI taskAPI) throws SQLException {
        setLastDueDateInTheATO(timeZoneAPI);
        setDaysAvailableForTheATO();
        TaskSchedulerElementAPI shortestDeadlineFirstElement =
                new ShortestDeadlineFirstElement(timeToStartATOGeneration,
                        daysAvailableForTheATO,
                        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI),
                        availableWorkHoursEachDay,
                        milestonesAPI.getDueDatesCompletionTimeListInUTCMillis());
        TaskScheduleVisitorAPI taskSchedulerVisitorAPI = new TaskSchedulerVisitorAPIImpl();
        Map<Long, List<Integer>> allocatedTaskMap =
                shortestDeadlineFirstElement.accept(taskSchedulerVisitorAPI);
        getTaskNamesAndStoreTasksUnderUser(allocatedTaskMap, taskAPI);
        return convertATOMapToDisplayableString(allocatedTaskMap, timeZoneAPI);
    }

    private void getTaskNamesAndStoreTasksUnderUser(Map<Long, List<Integer>> allocatedTaskMap,
                                                    TaskAPI taskAPI) throws SQLException {
        Map<Long, List<String>> taskNamesMap = new HashMap<>();
        for (Map.Entry<Long, List<Integer>> taskEntry : allocatedTaskMap.entrySet()) {
            List<String> taskNamesForTheDay = new ArrayList<>();
            for (int taskNumber : taskEntry.getValue()) {
                taskNamesForTheDay.add(milestonesAPI.getTaskNameBasedOnTaskNumber(taskNumber));
            }
            taskNamesMap.put(taskEntry.getKey(), taskNamesForTheDay);
            taskAPI.loadTaskFromATO(taskNamesMap);
        }
    }


    private int getTotalNumberOfDaysLeft(long startUTCTimeInMillis, long endUTCTimeInMillis) {
        long totalMillisWithinRange = endUTCTimeInMillis - startUTCTimeInMillis;
        return Math.toIntExact((totalMillisWithinRange / TIME_IN_MILLIS_FOR_ONE_DAY));

    }

    private long getMaxUTCTimeInMillis(List<Long> dueDatesInMillis) {
        long maxValue = Long.MIN_VALUE;
        for (long dueDate : dueDatesInMillis) {
            if (dueDate > maxValue) {
                maxValue = dueDate;
            }
        }
        return maxValue;
    }

    private String[] convertATOMapToDisplayableString(Map<Long, List<Integer>> allocatedSchedule,
                                                      TimeZoneAPI timeZoneAPI) {
        StringBuilder scheduleBuilder = new StringBuilder();
        List<Long> sortingTaskByDay = new ArrayList<>(allocatedSchedule.keySet());
        Collections.sort(sortingTaskByDay);
        for (Long days : sortingTaskByDay) {
            scheduleBuilder.append(timeZoneAPI.userDisplayingDateTimeFormat(days));
            for (int taskId : allocatedSchedule.get(days)) {
                scheduleBuilder.append(" ").append(milestonesAPI.getTaskNameBasedOnTaskNumber(taskId));
            }
            scheduleBuilder.append("__");
        }
        return scheduleBuilder.toString().split("__");
    }

    private void setDaysAvailableForTheATO() {
        this.daysAvailableForTheATO = getTotalNumberOfDaysLeft(timeToStartATOGeneration, lastDueDateInTheATO);
    }

    private void setLastDueDateInTheATO(TimeZoneAPI timeZoneAPI) {
        this.lastDueDateInTheATO =
                getMaxUTCTimeInMillis(milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI));
    }
}
