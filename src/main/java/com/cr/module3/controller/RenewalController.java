package com.cr.module3.controller;

import com.cr.common.Result;
import com.cr.module3.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块3：续租接口
 */
@RestController
public class RenewalController {

    private final OrderService orderService;

    public RenewalController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/renewals")
    public Result<Void> apply(@RequestAttribute("userId") Long userId, @RequestBody RenewalReq req) {
        orderService.applyRenewal(userId, req.orderId, req.days, req.startTime);
        return Result.ok();
    }

    @GetMapping("/api/renewals/my")
    public Result<List<?>> my(@RequestAttribute("userId") Long userId) {
        return Result.ok(orderService.myRenewals(userId));
    }

    @GetMapping("/api/admin/renewals")
    public Result<List<?>> list(@RequestParam(required = false) String auditStatus) {
        return Result.ok(orderService.renewalsForAudit(auditStatus));
    }

    @PostMapping("/api/admin/renewals/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody AuditReq req) {
        orderService.auditRenewal(id, req.pass, req.reply);
        return Result.ok();
    }

    public static class RenewalReq {
        public Long orderId;
        public Integer days;
        public String startTime;
    }

    public static class AuditReq {
        public boolean pass;
        public String reply;
    }
}
