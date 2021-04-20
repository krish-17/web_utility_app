package com.wau.ato.taskallocator;

import java.util.List;
import java.util.Map;

class ShortestDeadlineFirstElement implements TaskSchedulerElementAPI {

    private final int daysAvailableForWork;
    private final List<Long> dueDatesList;
    private final long availableHoursToWork;
    private final long startDate;
    private final List<Long> completionTimesOfTasks;


    ShortestDeadlineFirstElement(long startDate, int daysAvailableForWork, List<Long> dueDatesList,
                                 long availableHoursToWork, List<Long> completionTimesOfTasks) {
        this.daysAvailableForWork = daysAvailableForWork;
        this.dueDatesList = dueDatesList;
        this.availableHoursToWork = availableHoursToWork;
        this.startDate = startDate;
        this.completionTimesOfTasks = completionTimesOfTasks;
    }

    public List<Long> getDueDatesList() {
        return dueDatesList;
    }

    public long getStartTime() {
        return startDate;
    }

    public long getTimeToComplete(int currentTaskNumber) {
        return completionTimesOfTasks.get(currentTaskNumber);
    }

    public int getTotalDaysAvailable() {
        return daysAvailableForWork;
    }

    public long getDailyAvailableHours() {
        return availableHoursToWork;
    }

    @Override
    public Map<Long, List<Integer>> accept(TaskScheduleVisitorAPI taskScheduleVisitorAPI) {
        return taskScheduleVisitorAPI.implementShortestDeadlineFirstAlgorithm(this);
    }
}
