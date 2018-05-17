
-- ----------------------------
-- Table structure for collection_carb
-- ----------------------------
DROP TABLE IF EXISTS collection_carb;
CREATE TABLE collection_carb (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  bringtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据上报时间',
  value decimal(8,3) NOT NULL COMMENT '二氧化碳浓度',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_carb
-- ----------------------------

-- ----------------------------
-- Table structure for collection_humi
-- ----------------------------
DROP TABLE IF EXISTS collection_humi;
CREATE TABLE collection_humi (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  bringtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据上报时间',
  value decimal(8,3) NOT NULL COMMENT '湿度值',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_humi
-- ----------------------------

-- ----------------------------
-- Table structure for collection_nois
-- ----------------------------
DROP TABLE IF EXISTS collection_nois;
CREATE TABLE collection_nois (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  bringtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据上报时间',
  value decimal(8,3) NOT NULL COMMENT '噪音值',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_nois
-- ----------------------------

-- ----------------------------
-- Table structure for collection_noxy
-- ----------------------------
DROP TABLE IF EXISTS collection_noxy;
CREATE TABLE collection_noxy (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  bringtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据上报时间',
  value decimal(8,3) NOT NULL COMMENT '负氧离子',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_noxy
-- ----------------------------

-- ----------------------------
-- Table structure for collection_temp
-- ----------------------------
DROP TABLE IF EXISTS collection_temp;
CREATE TABLE collection_temp (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  bringtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据上报时间',
  value decimal(8,3) NOT NULL COMMENT '温度值',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_temp
-- ----------------------------

-- ----------------------------
-- Table structure for collection_footfall
-- ----------------------------
DROP TABLE IF EXISTS collection_footfall;
CREATE TABLE collection_footfall (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  siteid varchar(50) NULL COMMENT '设备ID',
  brandid varchar(50) NULL COMMENT '厂商识别ID',
  start_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '统计开始时间',
  end_time timestamp NULL COMMENT '统计结束时间',
  passengers_in int(11) unsigned zerofill DEFAULT NULL COMMENT '进客人数',
  passengers_out int(11) unsigned zerofill DEFAULT NULL COMMENT '出客人数',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of collection_footfall
-- ----------------------------

-- ----------------------------
-- Table structure for site_master
-- ----------------------------
DROP TABLE IF EXISTS site_master;
CREATE TABLE site_master (
  siteid varchar(50) NOT NULL COMMENT '设备ID',
  parkid varchar(50) NOT NULL COMMENT '公园ID',
  brandid varchar(50) DEFAULT NULL COMMENT '厂商识别ID',
  latitude decimal(10,7) DEFAULT NULL COMMENT '纬度',
  longitude decimal(10,7) DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (siteid)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of site_master
-- ----------------------------

-- ----------------------------
-- Table structure for park_master
-- ----------------------------
DROP TABLE IF EXISTS park_master;
CREATE TABLE park_master (
  parkid varchar(50) NOT NULL COMMENT '公园ID',
  namecn varchar(100) NOT NULL COMMENT '公园名称中文',
  nameen varchar(100) DEFAULT NULL COMMENT '公园名称英文',
  max integer DEFAULT NULL COMMENT '最大人数',
  parkdesc  text CHARACTER SET UTF8 NULL COMMENT '公园介绍',
  PRIMARY KEY (parkid)
) ENGINE=InnoDB;

-- ----------------------------
-- Records of park_master
-- ----------------------------