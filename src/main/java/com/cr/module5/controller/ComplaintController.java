package com.cr.module5.controller;

import com.cr.common.Result;
import com.cr.module5.entity.ComplaintVO;
import com.cr.module5.service.Module5Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块5：投诉反馈接口
 */
@RestController
public class ComplaintController {

    private final Module5Service service;

    public ComplaintController(Module5Service service) {
        this.service = service;
    }

    @PostMapping("/api/complaints")
    public Result<Void> submit(@RequestAttribute("userId") Long userId, @RequestBody ComplaintReq req) {
        service.submitComplaint(userId, req.title, req.content, req.picture);
        return Result.ok();
    }

    @GetMapping("/api/complaints/my")
    public Result<List<ComplaintVO>> my(@RequestAttribute("userId") Long userId) {
        return Result.ok(service.myComplaints(userId));
    }

    @GetMapping("/api/admin/complaints")
    public Result<List<ComplaintVO>> all() {
        return Result.ok(service.allComplaints());
    }

    @PostMapping("/api/admin/complaints/{id}/reply")
    public Result<Void> reply(@PathVariable Long id, @RequestBody ReplyReq req) {
        service.replyComplaint(id, req.reply);
        return Result.ok();
    }

    public static class ComplaintReq {
        public String title;
        public String content;
        public String picture;
    }

    public static class ReplyReq {
        public String reply;
    }
}
