package com.cr.module5.entity;

import lombok.Data;

/**
 * 模块5：投诉展示对象（投诉 + 用户名）
 */
@Data
public class ComplaintVO {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private String picture;
    private String reply;
    private String status;
    private java.util.Date createTime;
}
