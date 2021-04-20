package com.wau.ato;

import com.wau.ato.milestone.MilestonesAPI;
import com.wau.ato.taskallocator.TaskAllocatorAPI;

public interface ATOFactoryAPI {
    Ato createATOObject(String userId, String taskTitle, String taskName,
                        String dueDate, String difficulty, String hoursAvailablePerDay,
                        String notifierEmailAddress);

    MilestonesAPI createMilestonesObject(Ato ato);

    TaskAllocatorAPI createTaskAllocationObject(MilestonesAPI milestonesAPI, String userId);

}
