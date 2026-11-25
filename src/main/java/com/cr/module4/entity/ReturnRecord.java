package com.cr.module4.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块4：归还与补费 —— 归还记录实体（对应 return_record 表）
 */
@Data
public class ReturnRecord {

    private Long id;
    private Long orderId;
    private Date returnTime;
    private String isDamaged;
    private String isFuelExceed;
    private String isViolation;
    private String auditStatus;
    private String auditReply;
    private Date createTime;
}
