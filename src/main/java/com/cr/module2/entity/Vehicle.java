package com.cr.module2.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块2：车辆管理 —— 租赁车辆实体（对应 vehicle 表）
 */
@Data
public class Vehicle {

    private Long id;
    private String vehicleNo;
    private String vehicleName;
    private Long typeId;
    private String plateNo;
    private Integer seats;
    private Integer stock;
    private Integer dailyPrice;
    private String status;
    private String image;
    private String brand;
    private String phone;
    private String address;
    private String description;
    private Date createTime;

    /** 关联查询字段：类型名称（非表字段） */
    private String typeName;
}
