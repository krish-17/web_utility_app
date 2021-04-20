package com.wau;

import com.wau.ato.ATOFactoryAPI;
import com.wau.ato.ATOFactoryAPIImpl;
import com.wau.calendar.CalendarAbstractFactoryAPI;
import com.wau.calendar.CalendarAbstractFactoryAPIImpl;
import com.wau.calendar.CalendarStoreAPI;
import com.wau.calendar.MockCalendarStore;
import com.wau.clock.ClockAbstractFactoryAPI;
import com.wau.clock.ClockAbstractFactoryAPIImpl;
import com.wau.clock.ClockStoreAPI;
import com.wau.clock.MockClockStoreAPIImpl;
import com.wau.clock.alarm.AlarmStoreAPI;
import com.wau.clock.alarm.MockAlarmStoreAPIImpl;
import com.wau.notification.EmailAbstractFactoryAPI;
import com.wau.notification.EmailAbstractFactoryAPIImpl;
import com.wau.time.TimeZoneAPI;
import com.wau.time.TimeZoneAPIImpl;
import com.wau.todo.MockTaskStoreAPIImp;
import com.wau.todo.TaskStoreAPI;
import com.wau.todo.ToDoAbstractFactoryAPI;
import com.wau.todo.ToDoAbstractFactoryAPIImpl;
import com.wau.user.*;

public class WUAConfigTestFactory {

    private static WUAConfigTestFactory wuaConfigTestFactory = null;
    private final ATOFactoryAPI atoFactoryAPI;
    private final CalendarAbstractFactoryAPI calendarAbstractFactoryAPI;
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI;
    private final EmailAbstractFactoryAPI emailAbstractFactoryAPI;
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI;
    private final UserAbstractFactoryAPI userAbstractFactoryAPI;
    private final TimeZoneAPI timeZoneAPI;

    private WUAConfigTestFactory() {
        atoFactoryAPI = new ATOFactoryAPIImpl();
        CalendarStoreAPI calendarStoreAPI = new MockCalendarStore();
        calendarAbstractFactoryAPI = CalendarAbstractFactoryAPIImpl.instance(calendarStoreAPI);
        ClockStoreAPI clockStoreAPI = new MockClockStoreAPIImpl();
        AlarmStoreAPI alarmStoreAPI = new MockAlarmStoreAPIImpl();
        clockAbstractFactoryAPI = ClockAbstractFactoryAPIImpl
                .instance(clockStoreAPI, alarmStoreAPI);
        emailAbstractFactoryAPI = EmailAbstractFactoryAPIImpl.instance();
        TaskStoreAPI taskStoreAPI = new MockTaskStoreAPIImp();
        toDoAbstractFactoryAPI = ToDoAbstractFactoryAPIImpl.instance(taskStoreAPI);
        UserStoreAPI userStoreAPI = new MockUserStoreAPIImpl();
        userAbstractFactoryAPI = UserAbstractFactoryAPIImpl.instance(userStoreAPI);
        timeZoneAPI = TimeZoneAPIImpl.instance();
    }

    public static WUAConfigTestFactory instance() {
        if (null == wuaConfigTestFactory) {
            wuaConfigTestFactory = new WUAConfigTestFactory();
        }
        return wuaConfigTestFactory;
    }

    public ATOFactoryAPI getAtoFactoryAPI() {
        return atoFactoryAPI;
    }

    public CalendarAbstractFactoryAPI getCalendarAbstractFactoryAPI() {
        return calendarAbstractFactoryAPI;
    }

    public ClockAbstractFactoryAPI getClockAbstractFactoryAPI() {
        return clockAbstractFactoryAPI;
    }

    public EmailAbstractFactoryAPI getEmailAbstractFactoryAPI() {
        return emailAbstractFactoryAPI;
    }

    public ToDoAbstractFactoryAPI getToDoAbstractFactoryAPI() {
        return toDoAbstractFactoryAPI;
    }

    public UserAbstractFactoryAPI getUserAbstractFactoryAPI() {
        return userAbstractFactoryAPI;
    }

    public TimeZoneAPI getTimeZoneAPI() {
        return timeZoneAPI;
    }
}
