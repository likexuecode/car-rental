package com.cr.module4.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块4：补费展示对象（补费 + 订单 + 车辆）
 */
@Data
public class PenaltyVO {

    private Long id;
    private Long returnId;
    private String title;
    private String type;
    private Integer fee;
    private String isPaid;
    private Date createTime;

    private String orderNo;
    private Long orderId;
    private String vehicleName;
    private Long userId;
}
