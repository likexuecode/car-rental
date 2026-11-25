package com.cr.module2.entity;

import lombok.Data;

/**
 * 模块2：车辆管理 —— 汽车类型实体（对应 vehicle_type 表）
 */
@Data
public class VehicleType {

    private Long id;
    private String typeName;
}
