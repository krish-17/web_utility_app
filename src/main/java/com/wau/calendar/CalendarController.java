package com.wau.calendar;

import com.wau.WUAConfigFactory;
import com.wau.notification.EmailActionAPI;
import com.wau.time.TimeZoneAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;

@Controller
public class CalendarController {

    private static final String USER_ID = "userId";
    private static final String LOGIN_PAGE = "authentication/userLogin";
    private static final String ATTRIBUTE_NAME = "event";
    private static final String FAILURE_PAGE = "calendar/createCalendarFailed";
    private static final String MESSAGE = "message";

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
    private final CalendarAbstractFactoryAPI calendarAbstractFactoryAPI =
            wuaConfigFactory.getCalendarAbstractFactoryAPI();
    private EventAPI eventAPI = calendarAbstractFactoryAPI.createEventAPIImplObject();
    private final TimeZoneAPI timeZoneAPI = wuaConfigFactory.getTimeZoneAPI();

    private String getUserId(HttpServletRequest request) {
        return String.valueOf(request.getSession().getAttribute(USER_ID));
    }

    @GetMapping("/calendar/calendarHomePage")
    public String displayCalendarHomepage() {
        return "calendar/calendarHomePage";
    }

    @GetMapping("/calendar/createEvent")
    public String createEvent(Model model) {
        model.addAttribute(ATTRIBUTE_NAME, eventAPI);
        return "calendar/createEvent";
    }

    @PostMapping("/calendar/createEvent")
    public String sendUserInput(@RequestParam("eventTitle") String eventTitle,
                                @RequestParam("attendees") String attendees,
                                @RequestParam("eventDescription") String eventDescription,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime, HttpServletRequest request, Model model) {
        String userId = getUserId(request);
        if (userId.equals("null")) {
            return LOGIN_PAGE;
        }
        if (attendees != null) {
            EmailActionAPI emailActionAPI = wuaConfigFactory.getEmailAbstractFactoryAPI().createEmailActionObject();
            emailActionAPI.sendMail(userId, attendees, eventTitle, eventDescription);
        }
        eventAPI = calendarAbstractFactoryAPI.createEventAPIImplObject(null, userId,
                eventTitle, attendees, eventDescription,
                startTime, endTime);
        try {
            if (eventAPI.createEvent(timeZoneAPI)) {
                return "calendar/eventSuccess";
            } else {
                model.addAttribute(MESSAGE, "Unable to Create Event");
                return "calendar/eventCreationFailed";

            }
        } catch (SQLException | ParseException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/calendar/getAllEvents")
    public String getAllTaskView(Model model, HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId.equals("null")) {
            return LOGIN_PAGE;
        }
        try {
            model.addAttribute("list", eventAPI.getEventsForUser(userId, timeZoneAPI));
            return "calendar/viewGetAllEvents";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, "Unable to get Events for user");
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/calendar/deleteEvent")
    public String deleteEventView(Model model, HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId.equals("null")) {
            return LOGIN_PAGE;
        }
        model.addAttribute("delete", eventAPI);
        try {
            model.addAttribute("list", eventAPI.getEventsForUser(userId, timeZoneAPI));
            return "calendar/deleteEvent";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, "Unable to delete event for user");
            return FAILURE_PAGE;
        }
    }

    @PostMapping("/calendar/deleteEvent")
    public String sendDeleteEvent(@RequestParam("eventId") String eventId, Model model) {
        try {
            if (eventAPI.deleteEvent(eventId)) {
                return "calendar/deletedEventSuccessfully";
            } else {
                model.addAttribute(MESSAGE, "Unable to delete event for user");
                return "calendar/eventDeleteFailed";
            }
        } catch (SQLException e) {
            return FAILURE_PAGE;
        }
    }
}