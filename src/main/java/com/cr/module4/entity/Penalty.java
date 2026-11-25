package com.cr.module4.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模块4：归还与补费 —— 补费实体（对应 penalty 表）
 */
@Data
public class Penalty {

    private Long id;
    private Long returnId;
    private String title;
    private String type;
    private Integer fee;
    private String isPaid;
    private Date createTime;
}
