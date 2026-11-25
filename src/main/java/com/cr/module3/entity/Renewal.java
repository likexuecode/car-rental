package com.cr.module3.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块3：租赁订单与续租 —— 续租实体（对应 renewal 表）
 */
@Data
public class Renewal {

    private Long id;
    private Long orderId;
    private Date startTime;
    private Integer renewalDays;
    private Float amount;
    private String auditStatus;
    private String auditReply;
    private Date applyTime;
}
