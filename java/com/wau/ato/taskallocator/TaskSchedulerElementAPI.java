package com.wau.ato.taskallocator;

import java.util.List;
import java.util.Map;

public interface TaskSchedulerElementAPI {

    Map<Long, List<Integer>> accept(TaskScheduleVisitorAPI taskScheduleVisitorAPI);

}
