package com.wau.ato.milestone;

import com.wau.time.TimeZoneAPI;

import java.util.List;

public interface MilestonesAPI {

    boolean validateTheATOInputs(TimeZoneAPI timeZoneAPI);

    String getTaskNameBasedOnTaskNumber(int taskNumber);

    long getTaskDueDateBasedOnTaskNumber(int taskNumber);

    long getTimeToCompleteTaskBasedOnMilliSeconds(int taskNumber);

    int getSizeOfMilestonesInATO();

    long getHoursAvailableToWorkPerDay();

    List<Long> getDueDateListInUTCMillis(TimeZoneAPI timeZoneAPI);

    Long getTimeToCompleteAllTheTasksInMilliseconds();

    List<Long> getDueDatesCompletionTimeListInUTCMillis();
}