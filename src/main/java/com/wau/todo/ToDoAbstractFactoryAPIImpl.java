package com.wau.todo;

public class ToDoAbstractFactoryAPIImpl implements ToDoAbstractFactoryAPI {

    private static ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = null;

    private final TaskStoreAPI taskStoreAPI;

    private ToDoAbstractFactoryAPIImpl(TaskStoreAPI taskStoreAPI) {
        this.taskStoreAPI = taskStoreAPI;
    }

    public static ToDoAbstractFactoryAPI instance(TaskStoreAPI taskStoreAPI) {
        if (null == toDoAbstractFactoryAPI) {
            toDoAbstractFactoryAPI = new ToDoAbstractFactoryAPIImpl(taskStoreAPI);
        }
        return toDoAbstractFactoryAPI;
    }

    @Override
    public TaskAPI createTaskAPIImplObject() {
        return new TaskAPIImpl(taskStoreAPI);
    }

    @Override
    public TaskAPI createTaskAPIImplObject(String userID, String taskName, String taskDescription, String dueDate,
                                           String taskLabel, int userTimezone,
                                           int timeZoneToBeConverted) {
        return new TaskAPIImpl(userID, taskName, taskDescription, dueDate,
                taskLabel, userTimezone, timeZoneToBeConverted, taskStoreAPI);
    }

    @Override
    public TaskAPI createTaskAPIImplObject(String userID, String taskName, String taskID, String taskDescription,
                                           String taskLabel, String dueDate, int userTimeZone, int timeZoneToBeConverted) {
        return new TaskAPIImpl(userID, taskName, taskID, taskDescription, taskLabel, dueDate, userTimeZone,
                timeZoneToBeConverted);
    }
}