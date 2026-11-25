-- 免费云平台演示库（H2 / MySQL 兼容模式）建表脚本
-- 与 sql/init.sql 结构一致，去掉 MySQL 专有的 ENGINE / CHARSET / 列注释语法

CREATE TABLE `user` (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  account     VARCHAR(200) NOT NULL,
  password    VARCHAR(200) NOT NULL,
  name        VARCHAR(200) NOT NULL,
  phone       VARCHAR(200) DEFAULT NULL,
  id_card     VARCHAR(200) DEFAULT NULL,
  state       INT          DEFAULT 1,
  role        VARCHAR(20)  DEFAULT 'USER',
  create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uk_account UNIQUE (account)
);

CREATE TABLE `vehicle_type` (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  type_name VARCHAR(200) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_type_name UNIQUE (type_name)
);

CREATE TABLE `vehicle` (
  id           BIGINT       NOT NULL AUTO_INCREMENT,
  vehicle_no   VARCHAR(200) NOT NULL,
  vehicle_name VARCHAR(200) NOT NULL,
  type_id      BIGINT       DEFAULT NULL,
  plate_no     VARCHAR(200) DEFAULT NULL,
  seats        INT          DEFAULT NULL,
  stock        INT          DEFAULT 0,
  daily_price  INT          NOT NULL,
  status       VARCHAR(200) DEFAULT '可租',
  image        VARCHAR(500) DEFAULT NULL,
  brand        VARCHAR(200) DEFAULT NULL,
  phone        VARCHAR(200) DEFAULT NULL,
  address      VARCHAR(500) DEFAULT NULL,
  description  TEXT,
  create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uk_vehicle_no UNIQUE (vehicle_no),
  CONSTRAINT uk_plate_no UNIQUE (plate_no)
);

CREATE TABLE `orders` (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  order_no    VARCHAR(32)  NOT NULL,
  user_id     BIGINT       DEFAULT NULL,
  vehicle_id  BIGINT       DEFAULT NULL,
  rent_days   INT          NOT NULL,
  amount      INT          NOT NULL,
  start_time  DATETIME     DEFAULT NULL,
  status      VARCHAR(20)  DEFAULT '待支付',
  is_paid     VARCHAR(20)  DEFAULT '未支付',
  create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uk_order_no UNIQUE (order_no)
);

CREATE TABLE `renewal` (
  id           BIGINT      NOT NULL AUTO_INCREMENT,
  order_id     BIGINT      DEFAULT NULL,
  start_time   DATETIME    DEFAULT NULL,
  renewal_days INT         DEFAULT NULL,
  amount       FLOAT       DEFAULT NULL,
  audit_status VARCHAR(20) DEFAULT '待审核',
  audit_reply  TEXT,
  apply_time   DATETIME    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE `return_record` (
  id             BIGINT      NOT NULL AUTO_INCREMENT,
  order_id       BIGINT      DEFAULT NULL,
  return_time    DATETIME    DEFAULT NULL,
  is_damaged     VARCHAR(10) DEFAULT NULL,
  is_fuel_exceed VARCHAR(10) DEFAULT NULL,
  is_violation   VARCHAR(10) DEFAULT NULL,
  audit_status   VARCHAR(20) DEFAULT '待审核',
  audit_reply    TEXT,
  create_time    DATETIME    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE `penalty` (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  return_id   BIGINT       DEFAULT NULL,
  title       VARCHAR(200) DEFAULT NULL,
  type        VARCHAR(200) DEFAULT NULL,
  fee         INT          DEFAULT NULL,
  is_paid     VARCHAR(20)  DEFAULT '未支付',
  create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE `complaint` (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  user_id     BIGINT       DEFAULT NULL,
  title       VARCHAR(200) DEFAULT NULL,
  content     TEXT         NOT NULL,
  picture     VARCHAR(500) DEFAULT NULL,
  reply       TEXT,
  status      VARCHAR(20)  DEFAULT '待处理',
  create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE `customer_service` (
  id          BIGINT      NOT NULL AUTO_INCREMENT,
  user_id     BIGINT      DEFAULT NULL,
  admin_id    BIGINT      DEFAULT NULL,
  ask         TEXT,
  reply       TEXT,
  is_reply    INT         DEFAULT 0,
  create_time DATETIME    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE `announcement` (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(200) NOT NULL,
  content     TEXT,
  create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_type ON `vehicle` (type_id);
CREATE INDEX idx_order_user ON `orders` (user_id);
CREATE INDEX idx_order_vehicle ON `orders` (vehicle_id);
CREATE INDEX idx_renewal_order ON `renewal` (order_id);
CREATE INDEX idx_return_order ON `return_record` (order_id);
CREATE INDEX idx_penalty_return ON `penalty` (return_id);
CREATE INDEX idx_complaint_user ON `complaint` (user_id);
CREATE INDEX idx_service_user ON `customer_service` (user_id);
