package com.wau.ato.taskallocator;

import java.util.*;

public class TaskSchedulerVisitorAPIImpl implements TaskScheduleVisitorAPI {

    private static final long TIME_IN_MILLIS_FOR_ONE_DAY = (long) 24 * 60 * 60 * 1000;

    @Override
    public Map<Long, List<Integer>> implementShortestDeadlineFirstAlgorithm(ShortestDeadlineFirstElement taskSchedulerElement) {
        Map<Long, List<Integer>> allocatedTaskMap = new HashMap<>();
        List<Long> dueDatesList = taskSchedulerElement.getDueDatesList();
        List<Long> sortedDueDatesList = taskSchedulerElement.getDueDatesList();
        Collections.sort(sortedDueDatesList);
        long startTime = taskSchedulerElement.getStartTime();
        int currentNumber = 0;
        int currentTaskNumber = dueDatesList.indexOf(sortedDueDatesList.get(currentNumber));
        long timeToCompleteCurrentTask = taskSchedulerElement.getTimeToComplete(currentTaskNumber);
        int day = 0;
        int totalNumberOfTasks = dueDatesList.size();
        int totalDaysAvailable = taskSchedulerElement.getTotalDaysAvailable();
        long availableHoursToWork = taskSchedulerElement.getDailyAvailableHours();
        List<Integer> taskNumberList = new ArrayList<>();
        while (day < totalDaysAvailable) {
            availableHoursToWork = availableHoursToWork - timeToCompleteCurrentTask;
            if (availableHoursToWork > 0) {
                taskNumberList.add(currentTaskNumber);
                currentNumber = currentNumber + 1;
                if (currentNumber < totalNumberOfTasks) {
                    currentTaskNumber = dueDatesList.indexOf(sortedDueDatesList.get(currentNumber));
                    timeToCompleteCurrentTask = taskSchedulerElement.getTimeToComplete(currentTaskNumber);
                }
            }
            if (availableHoursToWork == 0) {
                taskNumberList.add(currentTaskNumber);
                allocatedTaskMap.put(startTime + day * TIME_IN_MILLIS_FOR_ONE_DAY, taskNumberList);
                currentNumber = currentNumber + 1;
                if (currentNumber < totalNumberOfTasks) {
                    currentTaskNumber = dueDatesList.indexOf(sortedDueDatesList.get(currentNumber));
                    timeToCompleteCurrentTask = taskSchedulerElement.getTimeToComplete(currentTaskNumber);
                    taskNumberList = new ArrayList<>();
                    day = day + 1;
                    availableHoursToWork = taskSchedulerElement.getDailyAvailableHours();
                }
            }
            if (availableHoursToWork < 0) {
                timeToCompleteCurrentTask = Math.abs(availableHoursToWork);
                taskNumberList.add(currentTaskNumber);
                allocatedTaskMap.put(startTime + day * TIME_IN_MILLIS_FOR_ONE_DAY, taskNumberList);
                taskNumberList = new ArrayList<>();
                day = day + 1;
                availableHoursToWork = taskSchedulerElement.getDailyAvailableHours();
            }
            if (currentNumber >= totalNumberOfTasks) {
                break;
            }
        }
        return allocatedTaskMap;
    }
}
