package com.example.web.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * ホームページのコントローラー
 */
@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ホームページを表示
     */
    @GetMapping("/")
    public String home(Model model) {
        Map<String, Object> stats = userService.getUserStatistics();
        model.addAttribute("stats", stats);
        return "index";
    }
}