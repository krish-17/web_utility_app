package com.wau.searchoperation;

import com.wau.WUAConfigFactory;
import com.wau.calendar.CalendarAbstractFactoryAPI;
import com.wau.calendar.EventAPI;
import com.wau.clock.ClockAbstractFactoryAPI;
import com.wau.clock.alarm.AlarmAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;
import com.wau.todo.ToDoAbstractFactoryAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
public class SearchController {

    @GetMapping("/search/searchPattern")
    public String getSearchView() {
        return "search/searchPattern";
    }

    @PostMapping("/search/searchPattern")
    public String getSearchBasedOnPattern(@RequestParam("pattern") String pattern,
                                          HttpServletRequest request, Model model) {
        String userId = String.valueOf(request.getSession().getAttribute("userId"));
        if (userId.equalsIgnoreCase("null")) {
            return "homePageWUA";
        }
        try {
            WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
            ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = wuaConfigFactory.getToDoAbstractFactoryAPI();
            ClockAbstractFactoryAPI clockAbstractFactoryAPI =
                    wuaConfigFactory.getClockAbstractFactoryAPI();
            CalendarAbstractFactoryAPI calendarAbstractFactoryAPI =
                    wuaConfigFactory.getCalendarAbstractFactoryAPI();
            TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();
            AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
            EventAPI eventAPI = calendarAbstractFactoryAPI.createEventAPIImplObject();
            TimeZoneAPI timeZoneAPI = wuaConfigFactory.getTimeZoneAPI();
            SearchAPI searchAPI = new SearchAPIImpl(alarmAPI, taskAPI, eventAPI);
            model.addAttribute("results",
                    searchAPI.searchAll(userId, pattern, timeZoneAPI));
            return "search/searchDisplay";
        } catch (SQLException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "search/searchFailDisplay";
    }
}
