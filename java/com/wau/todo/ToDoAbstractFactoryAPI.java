package com.wau.todo;


public interface ToDoAbstractFactoryAPI {

    TaskAPI createTaskAPIImplObject();

    TaskAPI createTaskAPIImplObject(String userID, String taskName, String taskDescription, String dueDate,
                                    String taskLabel, int userTimezone,
                                    int timeZoneToBeConverted);

    TaskAPI createTaskAPIImplObject(String userID, String taskName,
                                    String taskID, String taskDescription,
                                    String taskLabel, String dueDate,
                                    int userTimeZone, int timeZoneToBeConverted);

}
