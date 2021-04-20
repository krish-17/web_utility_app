package com.wau.ato.milestone;

import com.wau.WUAConfigTestFactory;
import com.wau.ato.ATOFactoryAPI;
import com.wau.ato.Ato;
import com.wau.time.TimeZoneAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MilestonesTest {

    private final WUAConfigTestFactory wuaConfigTestFactory = WUAConfigTestFactory.instance();
    private final ATOFactoryAPI atoTestFactoryAPI = wuaConfigTestFactory.getAtoFactoryAPI();
    private final TimeZoneAPI timeZoneAPI = wuaConfigTestFactory.getTimeZoneAPI();
    private static final String testEmailAddress = "integTester95@gmail.com";

    @Test
    void testValidValueForValidateTheATOInputs() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        assertTrue(milestonesAPI.validateTheATOInputs(timeZoneAPI));
    }

    static Stream<Arguments> arrayOfDueDatesLessThanCurrentTime() {
        return Stream.of(
                arguments(Arrays.asList("Task1",
                        "05/22/1090 23:59:00", "2")),
                arguments(Arrays.asList("Task1,Task2,Task3",
                        "05/22/1700 23:59:00,05/24/1298 23:59:00,05/26/2041 23:59:00", "2,1,2"))
        );
    }

    @ParameterizedTest
    @MethodSource("arrayOfDueDatesLessThanCurrentTime")
    void testLesserDueDateInPastForValidateTheATOInputs(List<String> taskTestStrings) {
        String taskNamesList = taskTestStrings.get(0);
        String dueDatesList = taskTestStrings.get(1);
        String difficultyList = taskTestStrings.get(2);
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "12";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        Assertions.assertFalse(milestonesAPI.validateTheATOInputs(timeZoneAPI));
    }

    static Stream<Arguments> arrayOfDueDatesAndTaskNames() {
        return Stream.of(
                arguments(Arrays.asList("Task1,Task2,Task3",
                        "05/24/2021 ,05/26/2021 23:59:00")),
                arguments(Arrays.asList("*,Task2,Task3",
                        "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021")),
                arguments(Arrays.asList("*,Task2,Task3",
                        "05/22/2021 23:59:00,#,05/26/2021 23:59:00")),
                arguments(Arrays.asList("Task1",
                        "05/22/* 23"))
        );
    }

    @ParameterizedTest
    @MethodSource("arrayOfDueDatesAndTaskNames")
    void testInvalidDueDateValueForValidateTheATOInputs(List<String> taskTestStrings) {
        String taskNamesList = taskTestStrings.get(0);
        String dueDatesList = taskTestStrings.get(1);
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        assertFalse(milestonesAPI.validateTheATOInputs(timeZoneAPI));
    }

    static Stream<Arguments> arrayOfSingleAndMultipleMilestones() {
        return Stream.of(
                arguments(Arrays.asList("Task1",
                        "05/26/2021 23:59:00", "0", "4")),
                arguments(Arrays.asList("Task1,Task2,Task3",
                        "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00",
                        "0,1,2", "8"))
        );
    }

    @ParameterizedTest
    @MethodSource("arrayOfSingleAndMultipleMilestones")
    void testValidValueForGetTaskNameBasedOnTaskNumber(List<String> milestoneInputs) {
        String taskNamesList = milestoneInputs.get(0);
        String dueDatesList = milestoneInputs.get(1);
        String difficultyList = milestoneInputs.get(2);
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = milestoneInputs.get(3);
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals("Task1",
                milestonesAPI.getTaskNameBasedOnTaskNumber(0));
    }

    static Stream<Arguments> arrayOfInvalidSingleAndMultipleMilestones() {
        return Stream.of(
                arguments(Arrays.asList("Task1",
                        "05/26/2021 23:59:00", "0", "4")),
                arguments(Arrays.asList("Task1,Task2,Task3",
                        "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00",
                        "0,1,2", "8"))
        );
    }

    @ParameterizedTest
    @MethodSource("arrayOfInvalidSingleAndMultipleMilestones")
    void testInvalidValueForGetTaskNameBasedOnTaskNumber(List<String> milestoneInputs) {
        String taskNamesList = milestoneInputs.get(0);
        String dueDatesList = milestoneInputs.get(1);
        String difficultyList = milestoneInputs.get(2);
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = milestoneInputs.get(3);
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals("Task2",
                milestonesAPI.getTaskNameBasedOnTaskNumber(0));
    }

    @Test
    void testInvalidCountValueForGetTaskNameBasedOnTaskNumber() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        assertThrows(IndexOutOfBoundsException.class,
                () -> milestonesAPI.getTaskNameBasedOnTaskNumber(5));
    }

    @Test
    void testValidValueForGetTaskDueDateBasedOnTaskNumber() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        Assertions.assertEquals(1621727940000L,
                milestonesAPI.getTaskDueDateBasedOnTaskNumber(0));
    }

    @Test
    void testInvalidValueForGetTaskDueDateBasedOnTaskNumber() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        Assertions.assertNotEquals(1621708140000L,
                milestonesAPI.getTaskDueDateBasedOnTaskNumber(2));
    }

    @Test
    void testInvalidCountValueForGetTaskDueDateBasedOnTaskNumber() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        assertThrows(IndexOutOfBoundsException.class,
                () -> milestonesAPI.getTaskDueDateBasedOnTaskNumber(5));
    }

    @Test
    void testValidSingleValueForGetTimeToCompleteTaskBasedOnMilliSeconds() {
        String taskNamesList = "Task1";
        String dueDatesList = "05/22/2021 23:59:00";
        String difficultyList = "0";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(43200000,
                milestonesAPI.getTimeToCompleteTaskBasedOnMilliSeconds(0));
    }

    @Test
    void testValidValueForGetTimeToCompleteTaskBasedOnMilliSeconds() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(43200000,
                milestonesAPI.getTimeToCompleteTaskBasedOnMilliSeconds(0));
    }


    @Test
    void testInvalidValueForGetTimeToCompleteTaskBasedOnMilliSeconds() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals(0,
                milestonesAPI.getTimeToCompleteTaskBasedOnMilliSeconds(1));
    }

    @Test
    void testInvalidCountValueForGetTimeToCompleteTaskBasedOnMilliSeconds() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        assertThrows(IndexOutOfBoundsException.class,
                () -> milestonesAPI.getTimeToCompleteTaskBasedOnMilliSeconds(5));
    }

    @Test
    void testValidValueForGetSizeOfMilestonesInATO() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(3, milestonesAPI.getSizeOfMilestonesInATO());
    }

    @Test
    void testInvalidValueForGetSizeOfMilestonesInATO() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals(5, milestonesAPI.getSizeOfMilestonesInATO());
    }

    @Test
    void testValidValueForGetHoursAvailableToWorkPerDay() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(28800000L, milestonesAPI.getHoursAvailableToWorkPerDay());
    }

    @Test
    void testInvalidValueForGetHoursAvailableToWorkPerDay() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals(0, milestonesAPI.getHoursAvailableToWorkPerDay());
    }

    @Test
    void testValidValueForGetDueDateListInUTCMillis() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        List<Long> convertDateIntoMilli = new ArrayList<>();
        convertDateIntoMilli.add(1621727940000L);
        convertDateIntoMilli.add(1621900740000L);
        convertDateIntoMilli.add(1622073540000L);
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI);
        Assertions.assertEquals(convertDateIntoMilli,
                milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI));
    }

    @Test
    void testInvalidValueForGetDueDateListInUTCMillis() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        List<Long> convertDateIntoMilli = new ArrayList<>();
        convertDateIntoMilli.add(1621708140000L);
        convertDateIntoMilli.add(0L);
        convertDateIntoMilli.add(1622053740000L);
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals(convertDateIntoMilli,
                milestonesAPI.getDueDateListInUTCMillis(timeZoneAPI));
    }

    @Test
    void testValidValueForGetTimeToCompleteAllTheTasksInMilliseconds() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(161999999,
                milestonesAPI.getTimeToCompleteAllTheTasksInMilliseconds());
    }

    @Test
    void testInvalidValueForGetTimeToCompleteAllTheTasksInMilliseconds() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNotEquals(0,
                milestonesAPI.getTimeToCompleteAllTheTasksInMilliseconds());
    }

    @Test
    void testInValidDifficultyValueForGetDueDatesCompletionTimeListInUTCMillis() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,3";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertNull(milestonesAPI.getDueDatesCompletionTimeListInUTCMillis());
    }

    @Test
    void testValidValueForGetDueDatesCompletionTimeListInUTCMillis() {
        String taskNamesList = "Task1,Task2,Task3";
        String dueDatesList = "05/22/2021 23:59:00,05/24/2021 23:59:00,05/26/2021 23:59:00";
        String difficultyList = "0,1,2";
        String atoTitle = "TestTask";
        String hoursAvailablePerDay = "8";
        Ato ato = atoTestFactoryAPI.createATOObject("group-6", atoTitle, taskNamesList,
                dueDatesList, difficultyList, hoursAvailablePerDay,
                testEmailAddress);
        MilestonesAPI milestonesAPI = atoTestFactoryAPI.createMilestonesObject(ato);
        Assertions.assertEquals(43200000,
                milestonesAPI.getDueDatesCompletionTimeListInUTCMillis().get(0));
    }
}