package com.cr.module4.service;

import com.cr.common.BizException;
import com.cr.common.Dates;
import com.cr.module2.mapper.VehicleMapper;
import com.cr.module3.entity.Order;
import com.cr.module3.mapper.OrderMapper;
import com.cr.module4.entity.Penalty;
import com.cr.module4.entity.PenaltyVO;
import com.cr.module4.entity.ReturnRecord;
import com.cr.module4.entity.ReturnVO;
import com.cr.module4.mapper.PenaltyMapper;
import com.cr.module4.mapper.ReturnRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 模块4：归还与补费 —— 归还登记、归还审核、补费管理
 */
@Service
public class ReturnService {

    private final ReturnRecordMapper returnMapper;
    private final PenaltyMapper penaltyMapper;
    private final OrderMapper orderMapper;
    private final VehicleMapper vehicleMapper;

    public ReturnService(ReturnRecordMapper returnMapper, PenaltyMapper penaltyMapper,
                         OrderMapper orderMapper, VehicleMapper vehicleMapper) {
        this.returnMapper = returnMapper;
        this.penaltyMapper = penaltyMapper;
        this.orderMapper = orderMapper;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * 契约4：submitReturn —— 提交归还登记
     */
    @Transactional(rollbackFor = Exception.class)
    public void submit(Long userId, Long orderId, String returnTime,
                       String isDamaged, String isFuelExceed, String isViolation) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BizException("无权操作该订单");
        }
        if (!"已取车".equals(order.getStatus())) {
            throw new BizException("您当前没有需要归还的车辆");
        }
        if (returnMapper.findByOrderId(orderId) != null) {
            throw new BizException("该订单已提交归还申请，请勿重复操作");
        }
        Date time = Dates.parse(returnTime);
        if (time == null) {
            throw new BizException("请填写归还时间");
        }
        if (order.getStartTime() != null && !time.after(order.getStartTime())) {
            throw new BizException("归还时间不合法");
        }
        if (!StringUtils.hasText(isDamaged) || !StringUtils.hasText(isFuelExceed)
                || !StringUtils.hasText(isViolation)) {
            throw new BizException("请完整填写车辆状况信息");
        }

        ReturnRecord record = new ReturnRecord();
        record.setOrderId(orderId);
        record.setReturnTime(time);
        record.setIsDamaged(isDamaged);
        record.setIsFuelExceed(isFuelExceed);
        record.setIsViolation(isViolation);
        returnMapper.insert(record);
        orderMapper.updateStatus(orderId, "归还审核中");
    }

    public List<ReturnVO> myReturns(Long userId) {
        return returnMapper.listByUser(userId);
    }

    public List<ReturnVO> returnsForAudit(String auditStatus) {
        return returnMapper.listByStatus(auditStatus);
    }

    /**
     * 管理员审核归还：通过则订单完成并回补库存，不通过则订单退回已取车
     */
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long returnId, boolean pass, String reply) {
        ReturnRecord record = returnMapper.findById(returnId);
        if (record == null) {
            throw new BizException("归还记录不存在");
        }
        if (!"待审核".equals(record.getAuditStatus())) {
            throw new BizException("该归还记录已审核");
        }
        returnMapper.audit(returnId, pass ? "通过" : "不通过", reply);
        if (pass) {
            orderMapper.updateStatus(record.getOrderId(), "已完成");
            Order order = orderMapper.findById(record.getOrderId());
            vehicleMapper.increaseStock(order.getVehicleId());
        } else {
            orderMapper.updateStatus(record.getOrderId(), "已取车");
        }
    }

    /**
     * 管理员生成补费记录
     */
    public void addPenalty(Long returnId, String title, String type, Integer fee) {
        ReturnRecord record = returnMapper.findById(returnId);
        if (record == null) {
            throw new BizException("归还记录不存在");
        }
        if (!"通过".equals(record.getAuditStatus())) {
            throw new BizException("请先审核通过该归还记录");
        }
        if (!StringUtils.hasText(title) || fee == null || fee <= 0) {
            throw new BizException("请填写补费标题和正确的金额");
        }
        Penalty penalty = new Penalty();
        penalty.setReturnId(returnId);
        penalty.setTitle(title);
        penalty.setType(type);
        penalty.setFee(fee);
        penaltyMapper.insert(penalty);
    }

    public List<PenaltyVO> myPenalties(Long userId) {
        return penaltyMapper.listByUser(userId);
    }

    public List<PenaltyVO> allPenalties() {
        return penaltyMapper.listAll();
    }

    public List<PenaltyVO> penaltiesByReturn(Long returnId) {
        return penaltyMapper.listByReturn(returnId);
    }

    /**
     * 用户支付补费
     */
    public void payPenalty(Long userId, Long penaltyId) {
        Penalty penalty = penaltyMapper.findById(penaltyId);
        if (penalty == null) {
            throw new BizException("补费记录不存在");
        }
        if ("已支付".equals(penalty.getIsPaid())) {
            throw new BizException("该补费已支付");
        }
        ReturnRecord record = returnMapper.findById(penalty.getReturnId());
        Order order = orderMapper.findById(record.getOrderId());
        if (!order.getUserId().equals(userId)) {
            throw new BizException("无权操作该补费记录");
        }
        penaltyMapper.markPaid(penaltyId);
    }
}
