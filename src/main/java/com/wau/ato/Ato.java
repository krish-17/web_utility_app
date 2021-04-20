package com.wau.ato;

public class Ato {

    private String userId;
    private String taskTitle;
    private String taskName;
    private String dueDate;
    private String difficulty;
    private String hoursAvailablePerDay;
    private String notifierEmailAddress;

    public Ato() {

    }

    public Ato(String userId, String taskTitle, String taskName,
               String dueDate, String difficulty, String hoursAvailablePerDay, String notifierEmailAddress) {

        this.taskName = taskName;
        this.userId = userId;
        this.taskTitle = taskTitle;
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.hoursAvailablePerDay = hoursAvailablePerDay;
        this.notifierEmailAddress = notifierEmailAddress;

    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getHoursAvailablePerDay() {
        return hoursAvailablePerDay;
    }

    public void setHoursAvailablePerDay(String noOfHoursAvailable) {
        this.hoursAvailablePerDay = noOfHoursAvailable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotifierEmailAddress() {
        return notifierEmailAddress;
    }

    public void setNotifierEmailAddress(String notifierEmailAddress) {
        this.notifierEmailAddress = notifierEmailAddress;
    }
}
