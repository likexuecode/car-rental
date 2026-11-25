-- ============================================================
-- 汽车租赁管理系统 建库脚本
-- 数据库：car_rental   字符集：utf8mb4
-- 对应《软件开发实践》文档 8.数据库设计（10 张表）
-- ============================================================
DROP DATABASE IF EXISTS car_rental;
CREATE DATABASE car_rental DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE car_rental;

-- 表8-1 用户表
CREATE TABLE `user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account`     VARCHAR(200) NOT NULL COMMENT '账号',
  `password`    VARCHAR(200) NOT NULL COMMENT '密码（MD5）',
  `name`        VARCHAR(200) NOT NULL COMMENT '姓名',
  `phone`       VARCHAR(200) DEFAULT NULL COMMENT '手机号',
  `id_card`     VARCHAR(200) DEFAULT NULL COMMENT '身份证号',
  `state`       INT          DEFAULT 1 COMMENT '状态：1可用 0禁用',
  `role`        VARCHAR(20)  DEFAULT 'USER' COMMENT '角色：USER普通用户 ADMIN管理员',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 表8-2 汽车类型表
CREATE TABLE `vehicle_type` (
  `id`        BIGINT       NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `type_name` VARCHAR(200) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_name` (`type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汽车类型表';

-- 表8-3 租赁车辆表
CREATE TABLE `vehicle` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
  `vehicle_no`   VARCHAR(200) NOT NULL COMMENT '车辆编号',
  `vehicle_name` VARCHAR(200) NOT NULL COMMENT '车辆名称',
  `type_id`      BIGINT       DEFAULT NULL COMMENT '类型ID',
  `plate_no`     VARCHAR(200) DEFAULT NULL COMMENT '牌号',
  `seats`        INT          DEFAULT NULL COMMENT '座位数',
  `stock`        INT          DEFAULT 0 COMMENT '库存',
  `daily_price`  INT          NOT NULL COMMENT '日租价',
  `status`       VARCHAR(200) DEFAULT '可租' COMMENT '出租状态：可租/已租/维修',
  `image`        VARCHAR(500) DEFAULT NULL COMMENT '车辆图片',
  `brand`        VARCHAR(200) DEFAULT NULL COMMENT '品牌',
  `phone`        VARCHAR(200) DEFAULT NULL COMMENT '联系电话',
  `address`      VARCHAR(500) DEFAULT NULL COMMENT '租车地址',
  `description`  TEXT         COMMENT '车辆介绍',
  `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vehicle_no` (`vehicle_no`),
  UNIQUE KEY `uk_plate_no` (`plate_no`),
  KEY `idx_type` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租赁车辆表';

-- 表8-4 租赁订单表
CREATE TABLE `orders` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no`    VARCHAR(32)  NOT NULL COMMENT '订单编号',
  `user_id`     BIGINT       DEFAULT NULL COMMENT '用户ID',
  `vehicle_id`  BIGINT       DEFAULT NULL COMMENT '车辆ID',
  `rent_days`   INT          NOT NULL COMMENT '租车天数',
  `amount`      INT          NOT NULL COMMENT '金额',
  `start_time`  DATETIME     DEFAULT NULL COMMENT '取车时间',
  `status`      VARCHAR(20)  DEFAULT '待支付' COMMENT '状态：待支付/已取车/归还审核中/已完成',
  `is_paid`     VARCHAR(20)  DEFAULT '未支付' COMMENT '是否支付',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user` (`user_id`),
  KEY `idx_vehicle` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租赁订单表';

-- 表8-5 续租信息表
CREATE TABLE `renewal` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '续租ID',
  `order_id`      BIGINT      DEFAULT NULL COMMENT '订单ID',
  `start_time`    DATETIME    DEFAULT NULL COMMENT '续租开始时间',
  `renewal_days`  INT         DEFAULT NULL COMMENT '续租天数',
  `amount`        FLOAT       DEFAULT NULL COMMENT '续租金额',
  `audit_status`  VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态：待审核/通过/不通过',
  `audit_reply`   TEXT        COMMENT '审核意见',
  `apply_time`    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='续租信息表';

-- 表8-6 汽车归还表
CREATE TABLE `return_record` (
  `id`             BIGINT      NOT NULL AUTO_INCREMENT COMMENT '归还ID',
  `order_id`       BIGINT      DEFAULT NULL COMMENT '订单ID',
  `return_time`    DATETIME    DEFAULT NULL COMMENT '归还时间',
  `is_damaged`     VARCHAR(10) DEFAULT NULL COMMENT '是否破损',
  `is_fuel_exceed` VARCHAR(10) DEFAULT NULL COMMENT '汽油是否超出',
  `is_violation`   VARCHAR(10) DEFAULT NULL COMMENT '是否违章',
  `audit_status`   VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态：待审核/通过/不通过',
  `audit_reply`    TEXT        COMMENT '审核回复',
  `create_time`    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汽车归还表';

-- 表8-7 租赁补费表
CREATE TABLE `penalty` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '补费ID',
  `return_id`   BIGINT       DEFAULT NULL COMMENT '归还ID',
  `title`       VARCHAR(200) DEFAULT NULL COMMENT '标题',
  `type`        VARCHAR(200) DEFAULT NULL COMMENT '补费类型',
  `fee`         INT          DEFAULT NULL COMMENT '费用',
  `is_paid`     VARCHAR(20)  DEFAULT '未支付' COMMENT '是否支付',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_return` (`return_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租赁补费表';

-- 表8-8 投诉反馈表
CREATE TABLE `complaint` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `user_id`     BIGINT       DEFAULT NULL COMMENT '用户ID',
  `title`       VARCHAR(200) DEFAULT NULL COMMENT '标题',
  `content`     TEXT         NOT NULL COMMENT '内容',
  `picture`     VARCHAR(500) DEFAULT NULL COMMENT '图片',
  `reply`       TEXT         COMMENT '回复内容',
  `status`      VARCHAR(20)  DEFAULT '待处理' COMMENT '状态：待处理/已回复',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉反馈表';

-- 表8-9 在线客服表
CREATE TABLE `customer_service` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '客服ID',
  `user_id`     BIGINT      DEFAULT NULL COMMENT '用户ID',
  `admin_id`    BIGINT      DEFAULT NULL COMMENT '管理员ID',
  `ask`         TEXT        COMMENT '提问内容',
  `reply`       TEXT        COMMENT '回复内容',
  `is_reply`    INT         DEFAULT 0 COMMENT '是否回复：0否 1是',
  `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线客服表';

-- 表8-10 通知公告表
CREATE TABLE `announcement` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title`       VARCHAR(200) NOT NULL COMMENT '标题',
  `content`     TEXT         COMMENT '内容',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ============================================================
-- 初始化数据
-- 管理员：admin / admin123        普通用户：test / 123456
-- ============================================================
INSERT INTO `user` (`account`, `password`, `name`, `phone`, `id_card`, `state`, `role`) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', '13800000000', '110101199001011234', 1, 'ADMIN'),
('test',  'e10adc3949ba59abbe56e057f20f883e', '测试用户',   '13900000000', '110101199505055678', 1, 'USER');

INSERT INTO `vehicle_type` (`type_name`) VALUES ('轿车'), ('SUV'), ('商务车'), ('跑车');

INSERT INTO `vehicle` (`vehicle_no`, `vehicle_name`, `type_id`, `plate_no`, `seats`, `stock`, `daily_price`, `status`, `brand`, `phone`, `address`, `description`) VALUES
('V001', '大众朗逸',   1, '京A12345', 5, 5, 200, '可租', '大众',   '13800000001', '北京市朝阳区建国路88号', '经济实用型轿车，适合日常代步与商务出行。'),
('V002', '丰田凯美瑞', 1, '京A23456', 5, 3, 350, '可租', '丰田',   '13800000002', '北京市朝阳区建国路88号', '中级轿车，舒适省油，空间宽敞。'),
('V003', '本田CR-V',   2, '京A34567', 5, 2, 400, '可租', '本田',   '13800000003', '北京市海淀区中关村大街1号', '城市SUV，动力强劲，通过性好。'),
('V004', '丰田汉兰达', 2, '京A45678', 7, 2, 550, '可租', '丰田',   '13800000004', '北京市海淀区中关村大街1号', '七座SUV，适合家庭出行与长途自驾。'),
('V005', '别克GL8',    3, '京A56789', 7, 3, 600, '可租', '别克',   '13800000005', '北京市丰台区南三环西路10号', '高端商务MPV，乘坐舒适，接待首选。'),
('V006', '保时捷718',  4, '京A67890', 2, 1, 1200, '可租', '保时捷', '13800000006', '北京市朝阳区三里屯路19号', '双座跑车，驾驶体验出色，适合短途体验。');

INSERT INTO `announcement` (`title`, `content`) VALUES
('系统上线通知', '汽车租赁管理系统正式上线，支持在线选车、下单、续租、归还与投诉反馈，欢迎使用。'),
('节假日租车优惠', '国庆期间租车满3天立减100元，详情请咨询在线客服。');
