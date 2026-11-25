package com.cr.module1.controller;

import com.cr.common.Result;
import com.cr.common.TokenStore;
import com.cr.module1.entity.User;
import com.cr.module1.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块1：用户与认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterReq req) {
        userService.register(req.account, req.password, req.confirmPassword,
                req.name, req.phone, req.idCard);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginReq req) {
        TokenStore.SessionUser session = userService.login(req.account, req.password);
        String token = TokenStore.create(session.getUserId(), session.getAccount(), session.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", userService.getById(session.getUserId()));
        return Result.ok(data);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute("token") String token) {
        TokenStore.remove(token);
        return Result.ok();
    }

    @GetMapping("/profile")
    public Result<User> profile(@RequestAttribute("userId") Long userId) {
        return Result.ok(userService.getById(userId));
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestAttribute("userId") Long userId, @RequestBody User user) {
        userService.updateProfile(userId, user.getName(), user.getPhone(), user.getIdCard());
        return Result.ok();
    }

    public static class RegisterReq {
        public String account;
        public String password;
        public String confirmPassword;
        public String name;
        public String phone;
        public String idCard;
    }

    public static class LoginReq {
        public String account;
        public String password;
    }
}
