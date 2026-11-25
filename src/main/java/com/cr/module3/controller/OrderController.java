package com.cr.module3.controller;

import com.cr.common.Result;
import com.cr.module3.entity.OrderVO;
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
 * 模块3：租赁订单接口
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public Result<Object> create(@RequestAttribute("userId") Long userId, @RequestBody CreateOrderReq req) {
        return Result.ok(orderService.createOrder(userId, req.vehicleId, req.days, req.startTime));
    }

    @GetMapping("/api/orders/my")
    public Result<List<OrderVO>> my(@RequestAttribute("userId") Long userId,
                                    @RequestParam(required = false) String status) {
        return Result.ok(orderService.myOrders(userId, status));
    }

    @GetMapping("/api/orders/{id}")
    public Result<OrderVO> detail(@RequestAttribute("userId") Long userId,
                                  @RequestAttribute("role") String role,
                                  @PathVariable Long id) {
        return Result.ok(orderService.detail(userId, role, id));
    }

    @PostMapping("/api/orders/{id}/pay")
    public Result<Void> pay(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        orderService.pay(userId, id);
        return Result.ok();
    }

    @GetMapping("/api/admin/orders")
    public Result<List<OrderVO>> all(@RequestParam(required = false) String status) {
        return Result.ok(orderService.allOrders(status));
    }

    public static class CreateOrderReq {
        public Long vehicleId;
        public Integer days;
        public String startTime;
    }
}
