package io.github.encryptorcode.iam.example.controllers;

import io.github.encryptorcode.iam.example.entities.Session;
import io.github.encryptorcode.iam.example.entities.User;
import io.github.encryptorcode.iam.example.services.AuthenticationManager;
import io.github.encryptorcode.iam.oauth.OAuthException;
import io.github.encryptorcode.iam.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    private static final String NEW_LINE = "<br/>\n";

    private AuthenticationService<User, Session> authenticationService = AuthenticationManager.getAuthenticationService();

    @RequestMapping("/")
    @ResponseBody
    public String home(
            HttpServletRequest request
    ){
        User user = authenticationService.getCurrentUser(request);
        if(user != null){
            return "<img src=\""+user.getProfileImage()+"\" width=250px height=250px style=\"float: right\">" + NEW_LINE +
                    "Hello "+user.getFullName()+"," + NEW_LINE +
                    "You are logged in using this authentication framework sucessfully." + NEW_LINE +
                    "Now, to test logout. <a href=\"/logout\">Click Here</a>";
        } else {
            return "Authentication Framework has detected that you are not logged in." + NEW_LINE +
                    "<a href=\"/login\">Click Here</a> to check the ways in which you can login.";
        }
    }

    @RequestMapping
    @ResponseBody
    public String login(){
        StringBuilder stringBuilder = new StringBuilder("Below given are the list of strategies loaded. Click on any one to login.").append(NEW_LINE);
        stringBuilder.append("<ul>").append(NEW_LINE);
        for (String strategyName : AuthenticationManager.strategyNames) {
            stringBuilder.append("<li><a href=\"/login/").append(strategyName).append("\">").append(strategyName.toUpperCase()).append("</a></li>").append(NEW_LINE);
        }
        stringBuilder.append("</ul>").append(NEW_LINE);
        return stringBuilder.toString();
    }

    @RequestMapping("/login/{strategy}")
    @ResponseBody
    public ModelAndView login(
            HttpServletRequest request,
            @PathVariable(value = "strategy", required = false) String strategyName,
            @RequestParam(value = "redirect", defaultValue = "/") String redirect
    ){
        return new ModelAndView("redirect:"+authenticationService.getLoginRedirectPath(request,strategyName,redirect));
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "redirect", defaultValue = "/") String redirect
    ) throws OAuthException {
        return new ModelAndView("redirect:"+authenticationService.logout(request, response, redirect));
    }

    @RequestMapping("/auth/callback")
    @ResponseBody
    public ModelAndView callback(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("code") String code,
            @RequestParam("state") String strategyName
    ) throws OAuthException {
        return new ModelAndView("redirect:"+authenticationService.login(request, response, strategyName, code));
    }
}