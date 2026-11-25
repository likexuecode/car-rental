package com.cr.module4.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块4：归还记录展示对象（归还 + 订单 + 车辆）
 */
@Data
public class ReturnVO {

    private Long id;
    private Long orderId;
    private Date returnTime;
    private String isDamaged;
    private String isFuelExceed;
    private String isViolation;
    private String auditStatus;
    private String auditReply;
    private Date createTime;

    private String orderNo;
    private Long userId;
    private String userName;
    private String vehicleName;
    private String plateNo;
}
