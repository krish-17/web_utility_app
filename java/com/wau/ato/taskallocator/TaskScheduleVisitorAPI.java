package com.wau.ato.taskallocator;

import java.util.List;
import java.util.Map;

public interface TaskScheduleVisitorAPI {

    Map<Long, List<Integer>> implementShortestDeadlineFirstAlgorithm(ShortestDeadlineFirstElement taskSchedulerElementAPIImpl);
}
