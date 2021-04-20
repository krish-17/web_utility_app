package com.wau.ato.milestone;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.ato.Ato;
import com.wau.time.TimeZoneAPI;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Milestones implements MilestonesAPI {

    private final LogAPI logAPI = LogAPIImpl.instance();

    enum Difficulty {
        EASY(0, 12),
        MEDIUM(1, 15),
        HARD(2, 18);

        private final int difficultyId;

        private final int hoursToCompleteTask;

        Difficulty(int difficultyId, int hoursToComplete) {
            this.difficultyId = difficultyId;
            this.hoursToCompleteTask = hoursToComplete;
        }

        int getDifficultyId() {
            return this.difficultyId;
        }

        long getHoursToCompleteInMilliSeconds() {
            return (long) this.hoursToCompleteTask * 60 * 60 * 1000;
        }
    }

    private static final String ALARM_NAME_REGEX = "^[a-zA-Z0-9 ,.'@!]+$";

    private final List<String> taskNamesList;
    private final List<String> dueDatesList;
    private final List<Integer> difficultyList;
    private final String hoursAvailablePerDay;
    private List<Difficulty> difficultyObjectList;
    private List<Long> difficultyObjectInMillis;
    private List<Long> dueDatesListInUTC;

    public Milestones(Ato ato) {
        this.taskNamesList = convertStringInputToList(ato.getTaskName());
        this.dueDatesList = convertStringInputToList(ato.getDueDate());
        this.difficultyList = convertDifficultyStringToIntegerList(ato.getDifficulty());
        this.hoursAvailablePerDay = ato.getHoursAvailablePerDay();
        this.difficultyObjectList = createDifficultyListBasedOnId();
        convertTimeToCompleteEachTaskToMillis();
    }

    public List<String> getTaskNamesList() {
        return taskNamesList;
    }

    public List<String> getDueDatesList() {
        return dueDatesList;
    }

    public List<Integer> getDifficultyList() {
        return difficultyList;
    }

    public String getHoursAvailablePerDay() {
        return hoursAvailablePerDay;
    }

    private List<Integer> convertDifficultyStringToIntegerList(String difficultValues) {
        List<Integer> integerList = new ArrayList<>();
        for (String value : difficultValues.split(",")) {
            integerList.add(Integer.parseInt(value));
        }
        return integerList;
    }


    private List<String> convertStringInputToList(String stringValues) {
        List<String> stringList = new ArrayList<>();
        if (stringValues.contains(",")) {
            Collections.addAll(stringList, stringValues.split(","));
        } else {
            stringList.add(stringValues);
        }
        return stringList;
    }

    private Difficulty getDifficultyBasedOnId(int difficultyId) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getDifficultyId() == difficultyId) {
                return difficulty;
            }
        }
        return null;
    }

    private List<Difficulty> createDifficultyListBasedOnId() {
        difficultyObjectList = new ArrayList<>();
        for (Integer difficultyId : getDifficultyList()) {
            difficultyObjectList.add(getDifficultyBasedOnId(difficultyId));
        }
        return difficultyObjectList;
    }

    private void convertHumanReadableDueDateListToUTC(TimeZoneAPI timeZoneAPI) {
        try {
            dueDatesListInUTC = new ArrayList<>();
            for (String dueDate : getDueDatesList()) {
                long dueDateInUTC = timeZoneAPI.changeDateTimeFormat(dueDate);
                dueDatesListInUTC.add(dueDateInUTC);
            }
        } catch (ParseException e) {
            dueDatesListInUTC = null;
            logAPI.errorLog(e.getMessage());
        }

    }

    private void convertTimeToCompleteEachTaskToMillis() {
        difficultyObjectInMillis = new ArrayList<>();
        for (Difficulty difficulty : difficultyObjectList) {
            if (difficulty == null) {
                difficultyObjectInMillis = null;
                break;
            }
            difficultyObjectInMillis.add(difficulty.getHoursToCompleteInMilliSeconds());
        }
    }

    @Override
    public boolean validateTheATOInputs(TimeZoneAPI timeZoneAPI) {
        convertHumanReadableDueDateListToUTC(timeZoneAPI);
        return isLengthOfListsSame()
                && isDueDateTimesGreaterThanCurrentTime()
                && isTaskNamesValid();
    }

    @Override
    public String getTaskNameBasedOnTaskNumber(int taskNumber) {
        return taskNamesList.get(taskNumber);
    }

    @Override
    public long getTaskDueDateBasedOnTaskNumber(int taskNumber) {
        return dueDatesListInUTC.get(taskNumber);
    }

    @Override
    public long getTimeToCompleteTaskBasedOnMilliSeconds(int taskNumber) {
        return difficultyObjectList.get(taskNumber).getHoursToCompleteInMilliSeconds();
    }

    @Override
    public int getSizeOfMilestonesInATO() {
        return getTaskNamesList().size();
    }

    @Override
    public long getHoursAvailableToWorkPerDay() {
        int numberOfHoursAvailablePerDay = Integer.parseInt(getHoursAvailablePerDay());
        return (long) numberOfHoursAvailablePerDay * 60 * 60 * 1000;
    }

    @Override
    public List<Long> getDueDateListInUTCMillis(TimeZoneAPI timeZoneAPI) {
        try {
            convertHumanReadableDueDateListToUTC(timeZoneAPI);
            return dueDatesListInUTC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Long getTimeToCompleteAllTheTasksInMilliseconds() {
        long totalTimeToComplete = -1L;
        for (Difficulty difficulty : difficultyObjectList) {
            totalTimeToComplete += difficulty.getHoursToCompleteInMilliSeconds();
        }
        return totalTimeToComplete;
    }

    @Override
    public List<Long> getDueDatesCompletionTimeListInUTCMillis() {
        return difficultyObjectInMillis;
    }

    private boolean isLengthOfListsSame() {
        return dueDatesList.size() == taskNamesList.size()
                && dueDatesList.size() == difficultyList.size();
    }

    private boolean isDueDateTimesGreaterThanCurrentTime() {
        try {
            long currentTimeInUTC = System.currentTimeMillis();
            if (dueDatesListInUTC == null) {
                return false;
            }
            for (long dueDateInUTC : dueDatesListInUTC) {
                if (dueDateInUTC <= currentTimeInUTC) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isTaskNamesValid() {
        boolean isValid = true;
        for (String taskName : getTaskNamesList()) {
            isValid = isValid && taskName.matches(ALARM_NAME_REGEX);
        }
        return isValid;
    }
}