package com.wau;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebUtilityGeneralController {
    @GetMapping("/")
    public String helloWorld() {
        return "homePageWUA";
    }

    @GetMapping("/WUAMainMenu")
    public String mainMenu(HttpServletRequest request) {
        String userId = String.valueOf(request.getSession().getAttribute("userId"));
        if (userId.equalsIgnoreCase("null")) {
            return "authentication/userLogin";
        }
        return "WUAMainMenu";
    }

    @GetMapping("/search")
    public String getSearchResult() {
        return "search";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}