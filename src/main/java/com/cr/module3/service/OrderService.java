package com.cr.module3.service;

import com.cr.common.BizException;
import com.cr.common.Dates;
import com.cr.module2.entity.Vehicle;
import com.cr.module2.mapper.VehicleMapper;
import com.cr.module3.entity.Order;
import com.cr.module3.entity.OrderVO;
import com.cr.module3.entity.Renewal;
import com.cr.module3.mapper.OrderMapper;
import com.cr.module3.mapper.RenewalMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模块3：租赁订单与续租 —— 下单、支付、订单查询、续租申请与审核
 */
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final RenewalMapper renewalMapper;
    private final VehicleMapper vehicleMapper;

    public OrderService(OrderMapper orderMapper, RenewalMapper renewalMapper, VehicleMapper vehicleMapper) {
        this.orderMapper = orderMapper;
        this.renewalMapper = renewalMapper;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * 契约3：createOrder —— 生成订单并扣减库存（声明式事务保证一致性）
     */
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, Long vehicleId, Integer days, String startTime) {
        if (days == null || days <= 0) {
            throw new BizException("请输入有效的租车天数");
        }
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new BizException("车辆不存在");
        }
        if (vehicle.getStock() == null || vehicle.getStock() < 1) {
            throw new BizException("该车型已租罄，请选择其他车辆");
        }
        if (orderMapper.countUnfinished(userId, vehicleId) > 0) {
            throw new BizException("您已有该车辆的未完成订单");
        }
        if (vehicleMapper.decreaseStock(vehicleId) == 0) {
            throw new BizException("该车型已租罄，请选择其他车辆");
        }

        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setVehicleId(vehicleId);
        order.setRentDays(days);
        order.setAmount(vehicle.getDailyPrice() * days);
        order.setStartTime(Dates.parse(startTime));
        order.setStatus("待支付");
        order.setIsPaid("未支付");
        orderMapper.insert(order);
        return order;
    }

    /**
     * 支付订单：待支付 -> 已取车
     */
    public void pay(Long userId, Long orderId) {
        Order order = getOwnOrder(userId, orderId);
        if (!"待支付".equals(order.getStatus())) {
            throw new BizException("该订单当前状态不可支付");
        }
        orderMapper.markPaid(orderId);
        orderMapper.updateStatus(orderId, "已取车");
    }

    public List<OrderVO> myOrders(Long userId, String status) {
        return orderMapper.listByUser(userId, status);
    }

    public List<OrderVO> allOrders(String status) {
        return orderMapper.listAll(status);
    }

    public OrderVO detail(Long userId, String role, Long orderId) {
        OrderVO vo = orderMapper.findVOById(orderId);
        if (vo == null) {
            throw new BizException("订单不存在");
        }
        if (!"ADMIN".equals(role) && !vo.getUserId().equals(userId)) {
            throw new BizException("无权查看该订单");
        }
        return vo;
    }

    /**
     * 用户申请续租
     */
    public void applyRenewal(Long userId, Long orderId, Integer days, String startTime) {
        Order order = getOwnOrder(userId, orderId);
        if (!"已取车".equals(order.getStatus())) {
            throw new BizException("仅已取车的订单可以申请续租");
        }
        if (days == null || days <= 0) {
            throw new BizException("请输入有效的续租天数");
        }
        if (renewalMapper.countPending(orderId) > 0) {
            throw new BizException("该订单有正在审核的续租申请");
        }
        Vehicle vehicle = vehicleMapper.findById(order.getVehicleId());
        Renewal renewal = new Renewal();
        renewal.setOrderId(orderId);
        renewal.setStartTime(Dates.parse(startTime));
        renewal.setRenewalDays(days);
        renewal.setAmount((float) vehicle.getDailyPrice() * days);
        renewalMapper.insert(renewal);
    }

    public List<?> myRenewals(Long userId) {
        return renewalMapper.listByUser(userId);
    }

    public List<?> renewalsForAudit(String auditStatus) {
        return renewalMapper.listByStatus(auditStatus);
    }

    /**
     * 管理员审核续租：通过后累加订单租期与金额
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditRenewal(Long renewalId, boolean pass, String reply) {
        Renewal renewal = renewalMapper.findById(renewalId);
        if (renewal == null) {
            throw new BizException("续租记录不存在");
        }
        if (!"待审核".equals(renewal.getAuditStatus())) {
            throw new BizException("该续租申请已审核");
        }
        renewalMapper.audit(renewalId, pass ? "通过" : "不通过", reply);
        if (pass) {
            orderMapper.addRent(renewal.getOrderId(), renewal.getRenewalDays(), renewal.getAmount());
        }
    }

    private Order getOwnOrder(Long userId, Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BizException("无权操作该订单");
        }
        return order;
    }
}
