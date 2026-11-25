package com.cr.module5.controller;

import com.cr.common.Result;
import com.cr.module5.entity.CustomerService;
import com.cr.module5.service.Module5Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块5：在线客服接口
 */
@RestController
public class ServiceController {

    private final Module5Service service;

    public ServiceController(Module5Service service) {
        this.service = service;
    }

    @PostMapping("/api/service/ask")
    public Result<Void> ask(@RequestAttribute("userId") Long userId, @RequestBody AskReq req) {
        service.ask(userId, req.ask);
        return Result.ok();
    }

    @GetMapping("/api/service/my")
    public Result<List<CustomerService>> my(@RequestAttribute("userId") Long userId) {
        return Result.ok(service.myService(userId));
    }

    @GetMapping("/api/admin/service")
    public Result<List<CustomerService>> all() {
        return Result.ok(service.allService());
    }

    @PostMapping("/api/admin/service/{id}/reply")
    public Result<Void> reply(@RequestAttribute("userId") Long adminId,
                              @PathVariable Long id, @RequestBody ReplyReq req) {
        service.replyService(adminId, id, req.reply);
        return Result.ok();
    }

    public static class AskReq {
        public String ask;
    }

    public static class ReplyReq {
        public String reply;
    }
}
