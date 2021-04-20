package com.wau.ato.taskallocator;

import com.wau.WUAConfigTestFactory;
import com.wau.ato.ATOFactoryAPI;
import com.wau.ato.Ato;
import com.wau.ato.milestone.MilestonesAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;
import com.wau.todo.ToDoAbstractFactoryAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskAllocatorAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final ATOFactoryAPI atoTestFactoryAPI = wuaConfigTestFactory.getAtoFactoryAPI();
    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();
    private static final String testEmailAddress = "integTester95@gmail.com";
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI =
            wuaConfigTestFactory.getToDoAbstractFactoryAPI();

    private final TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();

    @Test
    void testEmptyValueForAvailablePerDayIsTaskAllocationPossible() throws SQLException {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "0";
        Ato ato = new Ato("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList,
                hoursAvailablePerDay, "intefTester95@gmail.com");

        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        ato.getUserId());
        assertFalse(taskAllocatorAPI.isTaskAllocationPossible(taskAPI, timeZoneAPI));

    }

    @Test
    void testInvalidValueForDifficultyListForAvailablePerDayIsTaskAllocationPossible() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "03/22/2021 23:59:00,03/24/2021 23:59:00,03/26/2021 23:59:00";
        String difficultyList = "4";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI
                .createATOObject("group-6", atoTitle, taskNamesList,
                        dueDatesList, difficultyList, hoursAvailablePerDay,
                        testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        null);
        Assertions.assertThrows(NullPointerException.class,
                () -> taskAllocatorAPI.isTaskAllocationPossible(taskAPI, timeZoneAPI));
    }

    @Test
    void testValidForIsTaskAllocationPossible() throws SQLException {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI
                .createATOObject("group-6", atoTitle, taskNamesList,
                        dueDatesList, difficultyList, hoursAvailablePerDay
                        , testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        ato.getUserId());
        assertTrue(taskAllocatorAPI.isTaskAllocationPossible(taskAPI, timeZoneAPI));
    }

    @Test
    void testValidValueForGetAllocatedSchedule() throws SQLException {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI
                .createATOObject("group-6", atoTitle, taskNamesList,
                        dueDatesList, difficultyList,
                        hoursAvailablePerDay, testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        ato.getUserId());
        Assertions.assertEquals(5,
                taskAllocatorAPI.getAllocatedSchedule(timeZoneAPI, taskAPI).length);
    }

    @Test
    void testValidEqualValueForGetAllocatedSchedule() throws SQLException {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,0,0";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "12";
        Ato ato = atoTestFactoryAPI
                .createATOObject("group-6", atoTitle, taskNamesList,
                        dueDatesList, difficultyList,
                        hoursAvailablePerDay, testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        ato.getUserId());
        String[] schedules = taskAllocatorAPI.getAllocatedSchedule(timeZoneAPI, taskAPI);
        Assertions.assertEquals(3, schedules.length);
    }

    @Test
    void testInvalidValueForGetAllocatedSchedule() throws SQLException {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList,
                hoursAvailablePerDay, testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        TaskAllocatorAPI taskAllocatorAPI =
                atoTestFactoryAPI.createTaskAllocationObject(milestonesAPI,
                        ato.getUserId());
        Assertions.assertEquals(5,
                taskAllocatorAPI.getAllocatedSchedule(timeZoneAPI, taskAPI).length);
    }
}