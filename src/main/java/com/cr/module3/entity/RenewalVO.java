package com.cr.module3.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块3：续租展示对象（续租 + 订单 + 车辆信息）
 */
@Data
public class RenewalVO {

    private Long id;
    private Long orderId;
    private Date startTime;
    private Integer renewalDays;
    private Float amount;
    private String auditStatus;
    private String auditReply;
    private Date applyTime;

    private String orderNo;
    private Long userId;
    private String vehicleName;
    private String plateNo;
}
