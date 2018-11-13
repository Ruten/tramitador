package es.anew.element.tramitador.activiti.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ThymeLeafController {

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    @GetMapping("/tasks")
    public String tasks() {
        return "/user/tasks";
    }

    @GetMapping("/tasks/{id}")
    public String task(@PathVariable String id, Model model) {
        model.addAttribute("taskId",id);
        return "/user/task";
    }

    @GetMapping("/manager")
    public String managerIndex() {
        return "manager/index";
    }


}
