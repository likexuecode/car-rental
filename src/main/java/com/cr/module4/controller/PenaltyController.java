package com.cr.module4.controller;

import com.cr.common.Result;
import com.cr.module4.entity.PenaltyVO;
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
 * 模块4：补费接口
 */
@RestController
public class PenaltyController {

    private final ReturnService returnService;

    public PenaltyController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/api/admin/penalties")
    public Result<Void> add(@RequestBody PenaltyReq req) {
        returnService.addPenalty(req.returnId, req.title, req.type, req.fee);
        return Result.ok();
    }

    @GetMapping("/api/penalties/my")
    public Result<List<PenaltyVO>> my(@RequestAttribute("userId") Long userId) {
        return Result.ok(returnService.myPenalties(userId));
    }

    @GetMapping("/api/admin/penalties")
    public Result<List<PenaltyVO>> all() {
        return Result.ok(returnService.allPenalties());
    }

    @GetMapping("/api/admin/penalties/by-return")
    public Result<List<PenaltyVO>> byReturn(@RequestParam Long returnId) {
        return Result.ok(returnService.penaltiesByReturn(returnId));
    }

    @PostMapping("/api/penalties/{id}/pay")
    public Result<Void> pay(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        returnService.payPenalty(userId, id);
        return Result.ok();
    }

    public static class PenaltyReq {
        public Long returnId;
        public String title;
        public String type;
        public Integer fee;
    }
}
