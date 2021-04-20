package com.wau.todo;

import com.wau.WUAConfigTestFactory;
import com.wau.time.TimeZoneAPI;
import com.wau.time.TimeZoneAPIImpl;
import com.wau.user.UserAPI;
import com.wau.user.UserAbstractFactoryAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

class TaskAPIImplTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final UserAbstractFactoryAPI userAbstractFactoryAPI = wuaConfigTestFactory.getUserAbstractFactoryAPI();
    private final UserAPI userAPI = userAbstractFactoryAPI.createUserAPIImplObject();
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = wuaConfigTestFactory.getToDoAbstractFactoryAPI();
    private final TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
            "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", 2,
            2);

    @Test
    void testLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        Assertions.assertTrue(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testDeleteTask() throws SQLException {
        Assertions.assertTrue(taskAPIImpl.deleteTask("1617746924762", "1617746924762"));
    }

    @Test
    void testEmptyUserIDForDeleteTask() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.deleteTask("", "1617746924762"));
    }

    @Test
    void testEmptyTaskIDForDeleteTask() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.deleteTask("1617746924762", ""));
    }

    @Test
    void testCountOfTaskBetweenDates() throws SQLException {
        Assertions.assertEquals(2, taskAPIImpl.countOfTaskBetweenDates("1617746924762",
                1652122500000L, 1652295300000L));
    }

    @Test
    void testCountOfTaskBetweenDatesWithInvalidUserID() throws SQLException {
        Assertions.assertEquals(0, taskAPIImpl.countOfTaskBetweenDates("0",
                1652122500000L, 1652295300000L));
    }

    @Test
    void testCountOfTaskBetweenDatesWithInvalidStartMillis() throws SQLException {
        Assertions.assertEquals(0, taskAPIImpl.countOfTaskBetweenDates("1617746924762",
                1552122500000L, 1652295300000L));
    }

    @Test
    void testCountOfTaskBetweenDatesWithInvalidEndMillis() throws SQLException {
        Assertions.assertEquals(0, taskAPIImpl.countOfTaskBetweenDates("1617746924762",
                1652122500000L, 1752295300000L));
    }

    @Test
    void testCountOfTaskBetweenDatesWithNullStartMillis() throws SQLException {
        Assertions.assertEquals(0, taskAPIImpl.countOfTaskBetweenDates("1617746924762",
                0, 1652295300000L));
    }

    @Test
    void testCountOfTaskBetweenDatesWithNullEndMillis() throws SQLException {
        Assertions.assertEquals(0, taskAPIImpl.countOfTaskBetweenDates("1617746924762",
                1652122500000L, 0));
    }

    @Test
    void testEmptyStartDateForListOfTaskBetweenDates() throws SQLException, ParseException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskBetweenDates("1617746924762", "",
                "12/12/2021 12:22:12", timeZoneAPI);
        Assertions.assertTrue(taskList.isEmpty());
    }

    @Test
    void testEmptyEndDateForListOfTaskBetweenDates() throws SQLException, ParseException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskBetweenDates("1617746924762", "12/12/2021 12:12:12",
                "", timeZoneAPI);
        Assertions.assertTrue(taskList.isEmpty());
    }

    @Test
    void testTaskIDForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1617746924762", taskList.get(0).getTaskID());
    }

    @Test
    void testTaskNameForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("demo", taskList.get(0).getTaskName());
    }

    @Test
    void testTaskDescriptionForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("testing", taskList.get(0).getTaskDescription());
    }

    @Test
    void testTaskLabelForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("label", taskList.get(0).getTaskLabel());
    }

    @Test
    void testTaskDueDateForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1639291332000", taskList.get(0).getDueDate());
    }

    @Test
    void testUserTimeZoneForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals(2, taskList.get(0).getUserTimezone());
    }

    @Test
    void testUserTimeZoneToBeConvertedForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals(2, taskList.get(0).getTimeZoneToBeConverted());
    }

    @Test
    void testSecondTaskIdForListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1617746924763", taskList.get(1).getTaskID());
    }

    @Test
    void testNullTimeZoneListOfTaskSortedByDates() throws SQLException {
        TimeZoneAPI timeZoneAPI = null;
        List<TaskAPIImpl> taskList = taskAPIImpl.listOfTaskSortedByDate("1617746924762", timeZoneAPI);
        Assertions.assertTrue(taskList.isEmpty());
    }

    @Test
    void testTaskIDForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1617746924762", taskList.get(0).getTaskID());
    }

    @Test
    void testTaskNameForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("demo", taskList.get(0).getTaskName());
    }

    @Test
    void testTaskDescriptionForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("testing", taskList.get(0).getTaskDescription());
    }

    @Test
    void testTaskLabelForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("label", taskList.get(0).getTaskLabel());
    }

    @Test
    void testTaskDueDateForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1639291332000", taskList.get(0).getDueDate());
    }

    @Test
    void testUserTimeZoneForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals(2, taskList.get(0).getUserTimezone());
    }

    @Test
    void testTimeZoneToBeConvertedForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals(2, taskList.get(0).getTimeZoneToBeConverted());
    }

    @Test
    void testSecondTaskIdForGetAllTaskOfUser() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertEquals("1617746924763", taskList.get(1).getTaskID());
    }

    @Test
    void testNullTimeZoneAPIForGetListOfTaskToDisplay() throws SQLException {
        TimeZoneAPI timeZoneAPI = null;
        List<TaskAPIImpl> taskList = taskAPIImpl.getAllTaskOfUserToDisplay("1617746924762", timeZoneAPI);
        Assertions.assertTrue(taskList.isEmpty());

    }

    @Test
    void testForGetTasksTitleAndDescription() throws SQLException {
        List<String> taskList = taskAPIImpl.getTasksTitleAndDescription("1617746924762");
        Assertions.assertEquals("demo_testing", taskList.get(0));
    }

    @Test
    void testSecondTaskForGetTasksTitleAndDescription() throws SQLException {
        List<String> taskList = taskAPIImpl.getTasksTitleAndDescription("1617746924762");
        Assertions.assertEquals("demo_testing", taskList.get(1));
    }

    @Test
    void testForgetUserEmail() throws SQLException {
        Assertions.assertEquals("demo@gmail.com", taskAPIImpl.getUserEmail("1617746924762"));
    }

//    @Test
//    void testForLoadTaskForATO() throws SQLException {
//        TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();
//        List<String> taskNames = new ArrayList<>();
//        taskNames.add("Firstq");
//        taskNames.add("Second");
//        Map<Long, List<String>> allocatedTaskMap= new HashMap();
//        allocatedTaskMap.put(1617746924762L,taskNames);
//        Assertions.assertTrue(taskAPI.loadTaskFromATO(allocatedTaskMap));
//    }
//
//    @Test
//    void testInvalidFirstTaskValueForLoadTaskForATO() throws SQLException {
//        List<String> taskNames = new ArrayList<>();
//        taskNames.add("First");
//        taskNames.add("Second");
//        Map<Long, List<String>> allocatedTaskMap= new HashMap();
//        allocatedTaskMap.put(1617746924763L,taskNames);
//        System.out.println(taskAPIImpl.loadTaskFromATO(allocatedTaskMap));
//        Assertions.assertFalse(taskAPIImpl.loadTaskFromATO(allocatedTaskMap));
//    }

    @Test
    void testUpdateTaskName() throws SQLException {
        Assertions.assertTrue(taskAPIImpl.updateTaskName("1617746924762", "1617746924762", "updatedName"));
    }

    @Test
    void testEmptyTaskIdForUpdateTaskName() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.updateTaskName("1617746924762", "", "updatedName"));
    }

    @Test
    void testEmptyTaskNameForUpdateTaskName() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.updateTaskName("1617746924762", "1617746924762", ""));
    }

    @Test
    void testUpdateTaskDescription() throws SQLException {
        Assertions.assertTrue(taskAPIImpl.updateTaskDescription("1617746924762", "1617746924762", "updatedDescription"));
    }

    @Test
    void testEmptyTaskIDUpdateTaskDescription() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.updateTaskDescription("1617746924762", "", "updatedDescription"));
    }

    @Test
    void testEmptyTaskDescriptionUpdateTaskDescription() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.updateTaskDescription("1617746924762", "1617746924762", ""));
    }

    @Test
    void testEmptyTaskNameForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", 2,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testEmptyTaskDescriptionForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "", "12/12/2021 12:12:12", "test", 2,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testEmptyTaskDueDateForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "", "test", 2,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testEmptyTaskLabelForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "", 2,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testNegativeUserTimeZoneForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", -1,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testPositiveUserTimeZoneForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", 4,
                2);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testNegativeTimeZoneTobeConvertedForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", 2,
                -1);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testPositiveTimeZoneTobeConvertedForLoadTask() throws SQLException {
        TimeZoneAPI timeZoneAPI = new TimeZoneAPIImpl();
        TaskAPI taskAPIImpl = toDoAbstractFactoryAPI.createTaskAPIImplObject("1617746924762", "testing",
                "Learn testing from 5408 course", "12/12/2021 12:12:12", "test", 2,
                5);
        Assertions.assertFalse(taskAPIImpl.loadTask(timeZoneAPI));
    }

    @Test
    void testEmptyTaskIDAndEmailForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("", "", userAPI));
    }

    @Test
    void testEmptyTaskIDForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("", "demo@gmail.com", userAPI));
    }

    @Test
    void testEmptyEmailIDForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("1617746924762", "", userAPI));
    }

    @Test
    void testInvalidEmailForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("1617746924762", "false@gmail.com", userAPI));
    }

    @Test
    void testWithValidParametersForSetTaskToAssignee() throws SQLException {
        Assertions.assertTrue(taskAPIImpl.setTaskToAssignee("1617746924762", "demo@gmail.com", userAPI));
    }

    @Test
    void testInvalidTaskIDForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("1617746924456", "demo@gmail.com", userAPI));
    }

    @Test
    void testInvalidTaskIDAndInvalidEmailIDForSetTaskToAssignee() throws SQLException {
        Assertions.assertFalse(taskAPIImpl.setTaskToAssignee("1617746924456", "false@gmail.com", userAPI));
    }

}