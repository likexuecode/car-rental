package com.cr.module1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 模块1：用户与认证 —— 用户实体（对应 user 表）
 */
@Data
public class User {

    private Long id;
    private String account;

    @JsonIgnore
    private String password;

    private String name;
    private String phone;
    private String idCard;
    private Integer state;
    private String role;
    private Date createTime;
}
