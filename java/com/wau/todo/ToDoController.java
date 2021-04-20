package com.wau.todo;

import com.wau.WUAConfigFactory;
import com.wau.notification.EmailActionAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.user.UserAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;

@Controller
public class ToDoController {

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();
    private final UserAPI userAPI = wuaConfigFactory.getUserAbstractFactoryAPI().createUserAPIImplObject();
    private final ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = wuaConfigFactory.getToDoAbstractFactoryAPI();
    private final TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();
    private final EmailActionAPI emailActionAPI = wuaConfigFactory.getEmailAbstractFactoryAPI().createEmailActionObject();
    private final TimeZoneAPI timeZoneAPI = wuaConfigFactory.getTimeZoneAPI();

    private static final String FAILURE_PAGE = "todo/todoFailurePage";
    private static final String TASK_UPDATE_SUCCESS = "todo/updateTaskSuccess";

    private static final String TASK_UPDATE_FAIL_MESSAGE = "Unable to update Task";
    private static final String VIEW_RESULT = "todo/viewResult";
    private static final String EMAIL_SUBJECT = "Task Assigned";
    private static final String EMAIL_CONTENT = "Please open your WUA account. Task has been assigned to you by : ";
    private static final String MESSAGE = "message";

    private String getUserId(HttpServletRequest request) {
        return String.valueOf(request.getSession().getAttribute("userId"));
    }

    @GetMapping("/todo/toDoHomePage")
    public String toDoHomePage() {
        return "todo/toDoHomePage";
    }

    @GetMapping("/todo/setTask")
    public String setTask(Model model) {
        model.addAttribute("todo", taskAPI);
        return "todo/setTask";
    }

    @PostMapping("/todo/setTask")
    public String saveTask(@ModelAttribute TaskAPIImpl todo, Model model, HttpServletRequest request) {
        model.addAttribute("todo", todo);
        String userId = getUserId(request);
        TaskAPI taskAPISetTask = toDoAbstractFactoryAPI.createTaskAPIImplObject(userId, todo.getTaskName(),
                todo.getTaskDescription(), todo.getDueDate(),
                todo.getTaskLabel(), todo.getUserTimezone(),
                todo.getTimeZoneToBeConverted());
        try {
            if (taskAPISetTask.loadTask(timeZoneAPI)) {
                return "todo/TaskSetSuccess";
            } else {
                model.addAttribute(MESSAGE, "Unable to Create Task");
                return FAILURE_PAGE;
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/getAllTask")
    public String getAllTaskView(Model model, HttpServletRequest request) {
        String userId = getUserId(request);
        TaskAPI taskAPI1 = toDoAbstractFactoryAPI.createTaskAPIImplObject();
        try {
            model.addAttribute("list", taskAPI1.getAllTaskOfUserToDisplay(userId,
                    timeZoneAPI));
            return VIEW_RESULT;
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/viewTask")
    public String viewTask(Model model) {
        TaskAPI taskAPI1 = toDoAbstractFactoryAPI.createTaskAPIImplObject();

        model.addAttribute("view", taskAPI1);
        return "todo/viewTask";
    }

    @PostMapping("/todo/viewTask")
    public String viewTaskList(@ModelAttribute TaskAPIImpl view, Model model, HttpServletRequest request) {
        model.addAttribute("view", view);
        String userId = getUserId(request);
        try {
            model.addAttribute("list", taskAPI.getListOfTaskToDisplay(userId,
                    view.getTaskLabel(), timeZoneAPI));
            return VIEW_RESULT;
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/deleteTask")
    public String deleteTask(Model model, HttpServletRequest request) {
        model.addAttribute("delete", taskAPI);
        String userId = getUserId(request);

        try {
            model.addAttribute("list", taskAPI.getAllTaskOfUserToDisplay(userId,
                    timeZoneAPI));
            return "todo/deleteTask";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @PostMapping("/todo/deleteTask")
    public String deleteTaskBasedOnTaskID(@ModelAttribute TaskAPIImpl delete, Model model,
                                          HttpServletRequest request) {
        model.addAttribute("delete", delete);
        String userId = getUserId(request);

        try {
            if (taskAPI.deleteTask(userId, delete.getTaskID())) {
                return "todo/deleteTaskSuccess";
            } else {
                model.addAttribute(MESSAGE, "Unable to Delete Task");
                return FAILURE_PAGE;
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/updateTask")
    public String updateTask(Model model, HttpServletRequest request) {
        model.addAttribute("update", taskAPI);
        String userId = getUserId(request);
        try {
            model.addAttribute("list",
                    taskAPI.getAllTaskOfUserToDisplay(userId, timeZoneAPI));
            return "todo/updateTask";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @PostMapping("/todo/updateTask")
    public String updateTaskBasedOnTaskID(@ModelAttribute TaskAPIImpl update, Model model, HttpServletRequest request) {
        model.addAttribute("update", update);
        String userId = getUserId(request);
        try {
            if (update.getTaskName().isEmpty()) {
                if (taskAPI.updateTaskDescription(userId, update.getTaskID(), update.getTaskDescription())) {
                    return TASK_UPDATE_SUCCESS;
                } else {
                    model.addAttribute(MESSAGE, TASK_UPDATE_FAIL_MESSAGE);
                    return FAILURE_PAGE;
                }
            } else if (update.getTaskDescription().isEmpty()) {
                if (taskAPI.updateTaskName(userId, update.getTaskID(), update.getTaskName())) {
                    return TASK_UPDATE_SUCCESS;
                } else {
                    model.addAttribute(MESSAGE, TASK_UPDATE_FAIL_MESSAGE);
                    return FAILURE_PAGE;
                }
            } else {
                if (taskAPI.updateTaskName(userId, update.getTaskID(), update.getTaskName()) &&
                        taskAPI.updateTaskDescription(userId, update.getTaskID(), update.getTaskDescription())) {
                    return TASK_UPDATE_SUCCESS;
                } else {
                    model.addAttribute(MESSAGE, TASK_UPDATE_FAIL_MESSAGE);
                    return FAILURE_PAGE;
                }
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/filterTaskBasedOnDate")
    public String filterTask(Model model) {
        model.addAttribute("filter", taskAPI);
        return "todo/filterTaskBasedOnDate";
    }

    @PostMapping("/todo/filterTaskBasedOnDate")
    public String filterTaskBasedOnDate(@ModelAttribute TaskAPIImpl filter, Model model, HttpServletRequest request) {
        model.addAttribute("filter", filter);
        String userId = getUserId(request);
        try {
            model.addAttribute("list", taskAPI.listOfTaskBetweenDates(userId, filter.getDueDate(),
                    filter.getConvertedDueDate(), timeZoneAPI));
            return VIEW_RESULT;
        } catch (SQLException | ParseException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/assignTask")
    public String assignTask(Model model, HttpServletRequest request) {
        model.addAttribute("assign", taskAPI);
        String userId = getUserId(request);
        try {
            model.addAttribute("list", taskAPI.getAllTaskOfUserToDisplay(userId, timeZoneAPI));
            return "todo/assignTask";
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @PostMapping("/todo/assignTask")
    public String assignTaskToOtherEmail(@ModelAttribute TaskAPIImpl assign, Model model, HttpServletRequest request) {
        model.addAttribute("assign", assign);
        String userId = getUserId(request);
        try {
            if (taskAPI.setTaskToAssignee(assign.getTaskID(), assign.getUserID(), userAPI)) {
                emailActionAPI.sendMail(userId, assign.getUserID(), EMAIL_SUBJECT,
                        EMAIL_CONTENT + taskAPI.getUserEmail(userId));
                return "todo/assignTaskSuccess";
            } else {
                model.addAttribute(MESSAGE, "Unable to Assign Task");
                return FAILURE_PAGE;
            }
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }

    @GetMapping("/todo/sortTask")
    public String sortTask(Model model, HttpServletRequest request) {
        model.addAttribute("sort", taskAPI);
        String userId = getUserId(request);
        try {
            model.addAttribute("list", taskAPI.listOfTaskSortedByDate(userId, timeZoneAPI));
            return VIEW_RESULT;
        } catch (SQLException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            return FAILURE_PAGE;
        }
    }
}
