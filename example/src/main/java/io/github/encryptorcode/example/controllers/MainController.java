package io.github.encryptorcode.example.controllers;

import io.github.encryptorcode.example.entities.Session;
import io.github.encryptorcode.example.entities.User;
import io.github.encryptorcode.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    private static final String NEW_LINE = "<br/>\n";
    private static final AuthenticationService<Session, User> AUTHENTICATION_SERVICE = AuthenticationService.getInstance();

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        User user = AUTHENTICATION_SERVICE.getCurrentUser();
        if (user != null) {
            return "<img src=\"" + user.getProfileImage() + "\" width=250px height=250px style=\"float: right\">" + NEW_LINE +
                    "Hello " + user.getFullName() + "," + NEW_LINE +
                    "You are logged in using this authentication framework successfully." + NEW_LINE +
                    "Now, to test logout. <a href=\"/logout\">Click Here</a>";
        } else {
            return "Authentication Framework has detected that you are not logged in." + NEW_LINE +
                    "<a href=\"/login\">Click Here</a> to check the ways in which you can login.";
        }

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