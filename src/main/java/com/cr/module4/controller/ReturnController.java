package com.cr.module4.controller;

import com.cr.common.Result;
import com.cr.module4.entity.ReturnVO;
import com.cr.module4.service.ReturnService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块4：归还接口
 */
@RestController
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/api/returns")
    public Result<Void> submit(@RequestAttribute("userId") Long userId, @RequestBody ReturnReq req) {
        returnService.submit(userId, req.orderId, req.returnTime,
                req.isDamaged, req.isFuelExceed, req.isViolation);
        return Result.ok();
    }

    @GetMapping("/api/returns/my")
    public Result<List<ReturnVO>> my(@RequestAttribute("userId") Long userId) {
        return Result.ok(returnService.myReturns(userId));
    }

    @GetMapping("/api/admin/returns")
    public Result<List<ReturnVO>> list(@RequestParam(required = false) String auditStatus) {
        return Result.ok(returnService.returnsForAudit(auditStatus));
    }

    @PostMapping("/api/admin/returns/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody AuditReq req) {
        returnService.audit(id, req.pass, req.reply);
        return Result.ok();
    }

    public static class ReturnReq {
        public Long orderId;
        public String returnTime;
        public String isDamaged;
        public String isFuelExceed;
        public String isViolation;
    }

    public static class AuditReq {
        public boolean pass;
        public String reply;
    }
}
