package com.wau;

import com.wau.ato.ATOFactoryAPI;
import com.wau.ato.ATOFactoryAPIImpl;
import com.wau.authentication.AuthenticationAbstractFactoryAPI;
import com.wau.authentication.AuthenticationAbstractFactoryAPIImpl;
import com.wau.calendar.CalendarAbstractFactoryAPI;
import com.wau.calendar.CalendarAbstractFactoryAPIImpl;
import com.wau.calendar.CalendarStoreAPI;
import com.wau.calendar.CalendarStoreAPIImpl;
import com.wau.clock.ClockAbstractFactoryAPI;
import com.wau.clock.ClockAbstractFactoryAPIImpl;
import com.wau.clock.ClockStoreAPI;
import com.wau.clock.ClockStoreAPIImpl;
import com.wau.clock.alarm.AlarmStoreAPI;
import com.wau.clock.alarm.AlarmStoreAPIImpl;
import com.wau.notification.EmailAbstractFactoryAPI;
import com.wau.notification.EmailAbstractFactoryAPIImpl;
import com.wau.time.TimeZoneAPI;
import com.wau.time.TimeZoneAPIImpl;
import com.wau.todo.TaskStoreAPI;
import com.wau.todo.TaskStoreAPIImpl;
import com.wau.todo.ToDoAbstractFactoryAPI;
import com.wau.todo.ToDoAbstractFactoryAPIImpl;
import com.wau.user.UserAbstractFactoryAPI;
import com.wau.user.UserAbstractFactoryAPIImpl;
import com.wau.user.UserStoreAPI;
import com.wau.user.UserStoreAPIImpl;

public class WUAConfigFactory {

    private static WUAConfigFactory wuaConfigFactory = null;

    private final ATOFactoryAPI atoFactoryAPI;
    private final CalendarAbstractFactoryAPI calendarAbstractFactoryAPI;
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI;
    private final EmailAbstractFactoryAPI emailAbstractFactoryAPI;
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI;
    private final UserAbstractFactoryAPI userAbstractFactoryAPI;
    private final TimeZoneAPI timeZoneAPI;
    private final AuthenticationAbstractFactoryAPI authenticationAbstractFactoryAPI;

    private WUAConfigFactory() {
        atoFactoryAPI = new ATOFactoryAPIImpl();
        CalendarStoreAPI calendarStoreAPI = CalendarStoreAPIImpl.instance();
        calendarAbstractFactoryAPI = CalendarAbstractFactoryAPIImpl.instance(calendarStoreAPI);
        ClockStoreAPI clockStoreAPI = ClockStoreAPIImpl.instance();
        AlarmStoreAPI alarmStoreAPI = AlarmStoreAPIImpl.instance();
        clockAbstractFactoryAPI = ClockAbstractFactoryAPIImpl
                .instance(clockStoreAPI, alarmStoreAPI);
        emailAbstractFactoryAPI = EmailAbstractFactoryAPIImpl.instance();
        TaskStoreAPI taskStoreAPI = TaskStoreAPIImpl.instance();
        toDoAbstractFactoryAPI = ToDoAbstractFactoryAPIImpl.instance(taskStoreAPI);
        UserStoreAPI userStoreAPI = UserStoreAPIImpl.instance();
        userAbstractFactoryAPI = UserAbstractFactoryAPIImpl.instance(userStoreAPI);
        timeZoneAPI = TimeZoneAPIImpl.instance();
        authenticationAbstractFactoryAPI = AuthenticationAbstractFactoryAPIImpl.instance();

    }

    public static WUAConfigFactory instance() {
        if (null == wuaConfigFactory) {
            wuaConfigFactory = new WUAConfigFactory();
        }
        return wuaConfigFactory;
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

    public AuthenticationAbstractFactoryAPI getAuthenticationAbstractFactoryAPI() {
        return authenticationAbstractFactoryAPI;
    }
}
