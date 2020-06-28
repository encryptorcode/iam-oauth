package io.github.encryptorcode.example.controllers;

import io.github.encryptorcode.example.entities.Session;
import io.github.encryptorcode.example.entities.User;
import io.github.encryptorcode.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    private static final String NEW_LINE = "<br/>\n";
    private static final AuthenticationService<Session, User> AUTHENTICATION_SERVICE = AuthenticationService.getInstance();

    @RequestMapping("/")
    @ResponseBody
    public String home(
            @RequestParam(value = "message", required = false) String message
    ) {
        User user = AUTHENTICATION_SERVICE.getCurrentUser();
        StringBuilder builder = new StringBuilder();
        if (message != null) {
            builder.append("<p style='color:red'>").append(message).append("</p>").append(NEW_LINE);
        }
        if (user != null) {
            builder.append("<img src=\"").append(user.getProfileImage()).append("\" width=250px height=250px style=\"float: right\">").append(NEW_LINE)
                    .append("Hello ").append(user.getFullName()).append(",").append(NEW_LINE)
                    .append("You are logged in using this authentication framework sucessfully.").append(NEW_LINE)
                    .append("Now, to test logout. <a href=\"/logout\">Click Here</a>");
        } else {
            builder.append("Authentication Framework has detected that you are not logged in.").append(NEW_LINE)
                    .append("<a href=\"/login\">Click Here</a> to check the ways in which you can login.");
        }
        return builder.toString();
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login() {
        return "Below given are the list of strategies loaded. Click on any one to login." + NEW_LINE +
                "<ul>" + NEW_LINE +
                "<li><a href=\"/login?provider=google\">" + "GOOGLE" + "</a></li>" + NEW_LINE +
                "</ul>" + NEW_LINE;
    }
}