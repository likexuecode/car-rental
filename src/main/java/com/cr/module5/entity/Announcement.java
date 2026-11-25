package com.cr.module5.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块5：通知公告实体（对应 announcement 表）
 */
@Data
public class Announcement {

    private Long id;
    private String title;
    private String content;
    private Date createTime;
}
