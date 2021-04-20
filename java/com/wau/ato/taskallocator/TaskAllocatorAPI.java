package com.wau.ato.taskallocator;

import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;

import java.sql.SQLException;

public interface TaskAllocatorAPI {

    boolean isTaskAllocationPossible(TaskAPI taskAPI, TimeZoneAPI timeZoneAPI) throws SQLException;

    String[] getAllocatedSchedule(TimeZoneAPI timeZoneAPI, TaskAPI taskAPI) throws SQLException;
}
