package com.example.web.controller;

import com.example.common.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ユーザー管理のWebコントローラー
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ユーザー一覧ページを表示
     */
    @GetMapping
    public String userList(@RequestParam(value = "search", required = false) String search, Model model) {
        List<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsersByName(search);
            model.addAttribute("search", search);
        } else {
            users = userService.getAllUsers();
        }

        model.addAttribute("users", users);
        model.addAttribute("userCount", users.size());
        return "users/list";
    }

    /**
     * ユーザー詳細ページを表示
     */
    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "users/detail";
        } else {
            return "redirect:/users?error=not_found";
        }
    }

    /**
     * ユーザー作成フォームを表示
     */
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    /**
     * ユーザー編集フォームを表示
     */
    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("isEdit", true);
            return "users/form";
        } else {
            return "redirect:/users?error=not_found";
        }
    }

    /**
     * ユーザーを作成
     */
    @PostMapping
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            userService.createUser(user.getName(), user.getEmail());
            return "redirect:/users?success=created";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "users/form";
        }
    }

    /**
     * ユーザーを更新
     */
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
        try {
            userService.updateUser(id, user.getName(), user.getEmail());
            return "redirect:/users/" + id + "?success=updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("isEdit", true);
            return "users/form";
        }
    }

    /**
     * ユーザーを削除
     */
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return "redirect:/users?success=deleted";
        } else {
            return "redirect:/users?error=not_found";
        }
    }

    // REST API エンドポイント

    /**
     * ユーザー一覧API
     */
    @GetMapping("/api")
    @ResponseBody
    public List<User> getAllUsersApi() {
        return userService.getAllUsers();
    }

    /**
     * ユーザー詳細API
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserApi(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ユーザー統計API
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getUserStatsApi() {
        return userService.getUserStatistics();
    }
}