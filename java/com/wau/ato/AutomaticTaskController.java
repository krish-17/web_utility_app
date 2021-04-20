package com.wau.ato;

import com.wau.WUAConfigFactory;
import com.wau.ato.milestone.MilestonesAPI;
import com.wau.ato.taskallocator.TaskAllocatorAPI;
import com.wau.time.TimeZoneAPI;
import com.wau.todo.TaskAPI;
import com.wau.todo.ToDoAbstractFactoryAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class AutomaticTaskController {

    private final WUAConfigFactory wuaConfigFactory = WUAConfigFactory.instance();

    private static final String MESSAGE = "message";
    private static final String ERROR = "errorHtml";
    private static final String ATO_SUBJECT = "Here is your Automatic schedule! Plan wise";

    @GetMapping("/ato")
    public String ato(Model model) {
        model.addAttribute("ato", new Ato());
        return "ato";
    }

    @PostMapping("/ato")
    public String allocateTasks(@ModelAttribute Ato ato, Model model,
                                HttpServletRequest request) {
        model.addAttribute("ato", ato);
        String userId = String.valueOf(request.getSession().getAttribute("userId"));
        ato.setUserId(userId);
        ATOFactoryAPI atoFactoryAPI = wuaConfigFactory.getAtoFactoryAPI();
        MilestonesAPI milestonesAPI = atoFactoryAPI.createMilestonesObject(ato);
        TimeZoneAPI timeZoneAPI = wuaConfigFactory.getTimeZoneAPI();
        if (milestonesAPI.validateTheATOInputs(timeZoneAPI)) {
            ToDoAbstractFactoryAPI toDoAbstractFactoryAPI = wuaConfigFactory.getToDoAbstractFactoryAPI();
            TaskAPI taskAPI = toDoAbstractFactoryAPI.createTaskAPIImplObject();
            TaskAllocatorAPI taskAllocatorAPI =
                    atoFactoryAPI.createTaskAllocationObject(milestonesAPI,
                            ato.getUserId());
            try {
                if (taskAllocatorAPI.isTaskAllocationPossible(taskAPI, timeZoneAPI)) {
                    List<String> schedule =
                            Arrays.asList(taskAllocatorAPI
                                    .getAllocatedSchedule(timeZoneAPI, taskAPI));
                    model.addAttribute(MESSAGE, schedule);
                    wuaConfigFactory.getEmailAbstractFactoryAPI()
                            .createEmailActionObject()
                            .sendMail(ato.getUserId(),
                                    ato.getNotifierEmailAddress(),
                                    ATO_SUBJECT,
                                    String.join("\n", schedule));
                    return "successHtml";
                } else {
                    model.addAttribute(MESSAGE, "Uh-Oh! Tight Schedule, Task Allocation not possible");
                    return ERROR;
                }
            } catch (Exception e) {
                model.addAttribute(MESSAGE, e.getMessage());
                return ERROR;
            }
        } else {
            model.addAttribute(MESSAGE, "Invalid inputs");
            return ERROR;
        }

    }
}
