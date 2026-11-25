package com.cr.module5.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块5：客服与公告 —— 投诉反馈实体（对应 complaint 表）
 */
@Data
public class Complaint {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String picture;
    private String reply;
    private String status;
    private Date createTime;
}
