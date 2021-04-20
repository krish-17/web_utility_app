package com.wau.ato;

import com.wau.ato.milestone.Milestones;
import com.wau.ato.milestone.MilestonesAPI;
import com.wau.ato.taskallocator.TaskAllocatorAPI;
import com.wau.ato.taskallocator.TaskAllocatorAPIImpl;

public class ATOFactoryAPIImpl implements ATOFactoryAPI {

    @Override
    public Ato createATOObject(String userId, String taskTitle, String taskName,
                               String dueDate, String difficulty, String hoursAvailablePerDay,
                               String notifierEmailAddress) {
        return new Ato(userId, taskTitle, taskName, dueDate,
                difficulty, hoursAvailablePerDay,
                notifierEmailAddress);
    }

    @Override
    public MilestonesAPI createMilestonesObject(Ato ato) {
        return new Milestones(ato);
    }

    @Override
    public TaskAllocatorAPI createTaskAllocationObject(MilestonesAPI milestonesAPI, String userId) {
        return new TaskAllocatorAPIImpl(milestonesAPI, userId);
    }
}
