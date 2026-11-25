package com.cr.module5.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块5：在线客服实体（对应 customer_service 表）
 */
@Data
public class CustomerService {

    private Long id;
    private Long userId;
    private Long adminId;
    private String ask;
    private String reply;
    private Integer isReply;
    private Date createTime;

    /** 关联查询字段：提问用户姓名（非表字段） */
    private String userName;
}
