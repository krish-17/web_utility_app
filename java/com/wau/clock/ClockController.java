package com.wau.clock;

import com.wau.LogAPI;
import com.wau.LogAPIImpl;
import com.wau.WUAConfigFactory;
import com.wau.clock.alarm.AlarmAPI;
import com.wau.clock.alarm.AlarmAPIImpl;
import com.wau.notification.EmailActionAPI;
import com.wau.time.TimeZoneAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ClockController {

    private final LogAPI logAPI = LogAPIImpl.instance();

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
    private final ClockAbstractFactoryAPI clockAbstractFactoryAPI =
            wuaConfigFactory.getClockAbstractFactoryAPI();
    private final TimeZoneAPI timeZoneAPI = wuaConfigFactory.getTimeZoneAPI();
    private final EmailActionAPI emailActionAPI =
            wuaConfigFactory.getEmailAbstractFactoryAPI().createEmailActionObject();
    private static final String USER_ID = "userId";
    private static final String CLOCK_ID = "clockId";
    private static final String LOGIN_PAGE = "homePageWUA";
    private static final String SUCCESS_PAGE = "clock/clockCreationSuccess";
    private static final String FAILURE_PAGE = "clock/clockCreationFailed";
    private static final String MESSAGE = "message";

    private String getUserId(HttpServletRequest request) {
        return String.valueOf(request.getSession().getAttribute(USER_ID));
    }

    @GetMapping("/clock")
    public String displayClockHomePage() {
        return "clock/clockHome";
    }


    @GetMapping("/clock/displayClock")
    public String getAllClocksOfUser(Model model,
                                     HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId == null) {
            return LOGIN_PAGE;
        }
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI.createClockMetaObject();
        try {
            model.addAttribute("list",
                    clockMetaAPI.getListOfClocksForUser(userId, timeZoneAPI));
            return "clock/displayClockView";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/clock/createClock")
    public String createClock() {
        return "clock/createClock";

    }

    @PostMapping("/clock/createClock")
    public String createClock(@RequestParam("clockName") String clockName,
                              @RequestParam("timeZoneId") int timeZoneId,
                              @RequestParam("toAddress") String notifierEmail,
                              HttpServletRequest request,
                              Model model) {
        String userId = getUserId(request);
        if (userId == null) {
            return LOGIN_PAGE;
        }
        try {
            ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                    .createClockMetaObject(userId, clockName,
                            timeZoneId);
            if (clockMetaAPI.createAClockForUser(timeZoneAPI)) {
                String successMessage = "Your new clock is created for" +
                        timeZoneAPI.getTimeZoneName(timeZoneId);
                model.addAttribute(MESSAGE, successMessage);
                emailActionAPI.sendMail(userId, notifierEmail,
                        clockName, successMessage);
                return SUCCESS_PAGE;
            } else {
                model.addAttribute(MESSAGE, "Unable to Create Clock");
                return FAILURE_PAGE;
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }


    @GetMapping("/clock/displayTime")
    public String displayClock(@RequestParam("clockId") String clockId,
                               HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String userId = getUserId(request);
        session.setAttribute(CLOCK_ID, clockId);
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject(userId, clockId);
        try {
            String displayTime = clockMetaAPI.getCurrentTimeByClockId(clockId, timeZoneAPI);
            model.addAttribute("time", displayTime);
            return "clock/displayTime";
        } catch (Exception e) {
            return "clock/clockDisplayError";
        }
    }

    @GetMapping("/clock/modifyTimeZone")
    public String modifyTimeZone(@RequestParam("timeZoneId") int timeZoneId,
                                 @RequestParam("clockId") String clockId,
                                 Model model,
                                 HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId == null) {
            return LOGIN_PAGE;
        }
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject(userId, clockId);
        try {
            if (clockMetaAPI.updateTimeZoneInAClockForUser(timeZoneAPI, timeZoneId)) {
                model.addAttribute(MESSAGE, "Successfully updated Clock");
                return SUCCESS_PAGE;
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            logAPI.errorLog(e.getMessage());
            return FAILURE_PAGE;
        }
        model.addAttribute(MESSAGE, "Failed to update Clock timezone. Check back later!");
        return FAILURE_PAGE;
    }

    @GetMapping("/clock/createAlarm")
    public String createAlarm(@RequestParam(CLOCK_ID) String clockId,
                              HttpServletRequest request) {
        request.getSession().setAttribute(CLOCK_ID, clockId);
        return "clock/createAlarmView";
    }

    @PostMapping("/clock/createAlarm")
    public String createAlarm(@RequestParam("alarmName") String alarmName,
                              @RequestParam("alarmDate") String alarmDate,
                              @RequestParam("alarmTime") String alarmTime,
                              @RequestParam("toAddress") String notifierEmail,
                              Model model,
                              HttpServletRequest request) {
        String userId = getUserId(request);
        String clockId = String.valueOf(request.getSession().getAttribute(CLOCK_ID));
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject();
        try {
            int timeZoneId = clockMetaAPI.getTimeZoneIdSetOfAClock(clockId);
            AlarmAPI alarmAPI = clockAbstractFactoryAPI
                    .createAlarmObject(clockId, userId, alarmName,
                            alarmDate + "_" + alarmTime, timeZoneId);
            if (alarmAPI.createAlarm(timeZoneAPI)) {
                model.addAttribute(MESSAGE, "Alarm successfully created");
                emailActionAPI.sendMail(userId, notifierEmail, alarmName, alarmDate);
                return SUCCESS_PAGE;
            }
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
        model.addAttribute(MESSAGE, "Alarm Creation failed");
        return FAILURE_PAGE;
    }

    @GetMapping("/clock/getAlarm")
    public String getAlarms(@RequestParam("clockId") String clockId,
                            HttpServletRequest request,
                            Model model) {
        String userId = getUserId(request);
        if (userId == null) {
            return LOGIN_PAGE;
        }
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject();
        try {
            int timeZoneId = clockMetaAPI.getTimeZoneIdSetOfAClock(clockId);
            List<AlarmAPIImpl> alarmAPIImplList = alarmAPI.getAlarmsByClockId(userId, clockId,
                    timeZoneId, timeZoneAPI);
            model.addAttribute("list", alarmAPIImplList);
            return "clock/alarmListView";
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @PostMapping("/clock/modifyAlarm")
    public String updateAlarm(@RequestParam("alarmId") String alarmId,
                              @RequestParam("alarmName") String alarmName,
                              @RequestParam("clockId") String clockId,
                              @RequestParam("modifiedDate") String alarmDate,
                              @RequestParam("modifiedTime") String alarmTime,
                              Model model,
                              HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId == null) {
            return LOGIN_PAGE;
        }
        ClockMetaAPI clockMetaAPI = clockAbstractFactoryAPI
                .createClockMetaObject();
        try {
            int timeZoneId = clockMetaAPI.getTimeZoneIdSetOfAClock(clockId);
            AlarmAPI alarmAPI = clockAbstractFactoryAPI
                    .createAlarmObject(alarmId, clockId, userId, alarmName,
                            alarmDate + "_" + alarmTime, timeZoneId);
            if (alarmAPI.editTimeInAlarm(timeZoneAPI)) {
                model.addAttribute(MESSAGE, "Alarm successfully edited");
                return SUCCESS_PAGE;
            }
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
        model.addAttribute(MESSAGE, "Failed to edit alarm");
        return FAILURE_PAGE;
    }

    @GetMapping("/clock/deleteAlarm")
    public String deleteAlarm(@RequestParam("alarmId") String alarmId,
                              Model model) {
        AlarmAPI alarmAPI = clockAbstractFactoryAPI.createAlarmObject();
        try {
            if (alarmAPI.deleteAlarmByAlarmId(alarmId)) {
                model.addAttribute(MESSAGE, "Alarm deleted Successfully");
                return SUCCESS_PAGE;
            }
        } catch (SQLException e) {
            logAPI.errorLog(e.getMessage());
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
        model.addAttribute(MESSAGE, "Failed to delete alarm");
        return FAILURE_PAGE;
    }
}
