package com.cr.module3.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块3：订单展示对象（订单 + 车辆信息）
 */
@Data
public class OrderVO {

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

    private String vehicleName;
    private String plateNo;
    private String vehicleImage;
    private Integer dailyPrice;
    private String brand;
}
