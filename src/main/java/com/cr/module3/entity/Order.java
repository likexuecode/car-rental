package com.cr.module3.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块3：租赁订单与续租 —— 订单实体（对应 orders 表）
 */
@Data
public class Order {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long vehicleId;
    private Integer rentDays;
    private Integer amount;
    private Date startTime;
    private String status;
    private String isPaid;
    private Date createTime;
}
