/*
 Navicat Premium Data Transfer

 Source Server         : dev 
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 39.106.108.190:3306
 Source Schema         : qc

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 24/09/2019 16:20:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `article_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '文章标题',
  `author_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL COMMENT '作者',
  `category` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '分类',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT '正文内容',
  `cover_img` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '封面图片',
  `url_source` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '来源(冗余)',
  `address_id` bigint(20) DEFAULT NULL COMMENT '发表地点id',
  `review_status` int(11) DEFAULT NULL COMMENT '冗余字段',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT '文章摘要',
  `type` int(10) DEFAULT NULL COMMENT '类型:0图文,1音频,2视频（暂时不用）',
  `scope` int(1) DEFAULT NULL COMMENT '0小区可见/1全国可见。。。',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章表';

-- ----------------------------
-- Table structure for article_label
-- ----------------------------
DROP TABLE IF EXISTS `article_label`;
CREATE TABLE `article_label` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '关系主键',
  `article_id` bigint(20) DEFAULT NULL COMMENT '文章主键',
  `label_id` bigint(20) DEFAULT NULL COMMENT '标签主键',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章标签关系表';

-- ----------------------------
-- Table structure for article_poi_index
-- ----------------------------
DROP TABLE IF EXISTS `article_poi_index`;
CREATE TABLE `article_poi_index` (
  `article_id` int(11) NOT NULL,
  `poi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  KEY `index_article_id` (`article_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for bs_poi_article
-- ----------------------------
DROP TABLE IF EXISTS `bs_poi_article`;
CREATE TABLE `bs_poi_article` (
  `poi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  `poi_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT '' COMMENT 'POIid',
  `poi_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  `article_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT 'jsonarticle',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`poi_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for bs_poi_feeds
-- ----------------------------
DROP TABLE IF EXISTS `bs_poi_feeds`;
CREATE TABLE `bs_poi_feeds` (
  `poi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  `poi_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT '' COMMENT 'POIid',
  `poi_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  `feeds_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT 'jsonfeeds',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`poi_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for business_area
-- ----------------------------
DROP TABLE IF EXISTS `business_area`;
CREATE TABLE `business_area` (
  `business_area_id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '商圈Id',
  `business_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商圈名称',
  `ad_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区编码，"110101"',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `t_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  PRIMARY KEY (`business_area_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for click_comment
-- ----------------------------
DROP TABLE IF EXISTS `click_comment`;
CREATE TABLE `click_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '点击关系主键',
  `comment_id` bigint(20) NOT NULL COMMENT '评论主键',
  `uid` bigint(20) DEFAULT NULL COMMENT '点击人id',
  `click_type` int(1) DEFAULT NULL COMMENT '点击类型0喜欢/1反对',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` bigint(40) DEFAULT NULL COMMENT '修改人',
  `modify_time` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论点击关系表';

-- ----------------------------
-- Table structure for click_content
-- ----------------------------
DROP TABLE IF EXISTS `click_content`;
CREATE TABLE `click_content` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '文章交互关系主键',
  `content_id` bigint(255) DEFAULT NULL COMMENT '文章主键',
  `content_type` int(11) DEFAULT NULL COMMENT '内容类型，0文章1动态',
  `to_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '被点击用户',
  `click_type_id` int(20) DEFAULT NULL COMMENT '点击类型，0查看，1点赞，2分享，3转发，5评论',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章点击关系表';

-- ----------------------------
-- Table structure for click_data
-- ----------------------------
DROP TABLE IF EXISTS `click_data`;
CREATE TABLE `click_data` (
  `content_id` bigint(11) NOT NULL COMMENT '内容Id',
  `content_type` int(2) NOT NULL COMMENT '内容类型，0文章article，1动态feeds',
  `click_type` int(2) NOT NULL COMMENT '点击类型，0点赞like，1查看view',
  `sum` bigint(20) NOT NULL COMMENT '点击总数',
  PRIMARY KEY (`content_id`,`content_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for click_feeds
-- ----------------------------
DROP TABLE IF EXISTS `click_feeds`;
CREATE TABLE `click_feeds` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '点赞关系主键',
  `feeds_id` bigint(255) DEFAULT NULL COMMENT '文章主键',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `click_type_id` int(20) DEFAULT NULL COMMENT '点击类型，0查看，1点赞，2分享，3转发，5评论',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for click_record
-- ----------------------------
DROP TABLE IF EXISTS `click_record`;
CREATE TABLE `click_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键（无意义）',
  `to_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被通知人',
  `click_type_id` int(10) DEFAULT NULL COMMENT '点击类型id\n0点赞1查阅',
  `content_id` bigint(20) DEFAULT NULL COMMENT '内容id',
  `content_type` int(10) DEFAULT NULL COMMENT '内容类型,1动态，0文章',
  `view_status` tinyint(4) DEFAULT NULL COMMENT '是否已读',
  `create_uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人（发起人）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `detail` text COLLATE utf8mb4_general_ci COMMENT '信息详情',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for click_type
-- ----------------------------
DROP TABLE IF EXISTS `click_type`;
CREATE TABLE `click_type` (
  `click_type_id` int(10) unsigned NOT NULL COMMENT '点击主键',
  `title` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '点击描述',
  PRIMARY KEY (`click_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='点击表，记载点击类型，如喜欢/反对等信息，例如文章点击表和评论点击表中的0代表喜欢，1代表反对，3代表有价值等等';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '评论表主键',
  `pid` bigint(20) DEFAULT NULL COMMENT '所属评论',
  `article_id` bigint(20) DEFAULT NULL COMMENT '所属文章id',
  `from_uid` bigint(20) DEFAULT NULL COMMENT '发起人id',
  `to_uid` bigint(20) DEFAULT NULL COMMENT '接收人id',
  `level` int(1) DEFAULT NULL COMMENT '运营级别，0正常，1置顶',
  `report_flag` tinyint(1) DEFAULT NULL COMMENT '举报标识。0未举报，1举报',
  `context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT '评论内容',
  `local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '地址信息',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论表';

-- ----------------------------
-- Table structure for feeds_poi_index
-- ----------------------------
DROP TABLE IF EXISTS `feeds_poi_index`;
CREATE TABLE `feeds_poi_index` (
  `feeds_id` int(11) NOT NULL,
  `poi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL,
  KEY `index_feeds_id` (`feeds_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
  `label_id` bigint(20) NOT NULL COMMENT '标签列表主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '标签名称',
  `super_id` bigint(20) DEFAULT NULL COMMENT '上级标签，-1代表顶级标签',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章/信息流标签表';

-- ----------------------------
-- Table structure for poi_business
-- ----------------------------
DROP TABLE IF EXISTS `poi_business`;
CREATE TABLE `poi_business` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `poi_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci NOT NULL COMMENT '社区Id',
  `business_area_id` bigint(20) NOT NULL COMMENT '商圈Id',
  `t_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for rel_user_theme
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_theme`;
CREATE TABLE `rel_user_theme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `theme_id` bigint(20) NOT NULL,
  `address_id` bigint(20) NOT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `report_id` int(11) NOT NULL COMMENT '审核主键',
  `title` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '审核描述',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for report_feeds
-- ----------------------------
DROP TABLE IF EXISTS `report_feeds`;
CREATE TABLE `report_feeds` (
  `report_feeds_id` bigint(20) NOT NULL COMMENT '信息举报/纠错表',
  `feed_type` int(11) DEFAULT NULL COMMENT '信息类型，0文章，1评论，其他内容，指定feed_id类型',
  `feed_id` bigint(20) DEFAULT NULL COMMENT '信息id',
  `report_id` int(11) DEFAULT NULL COMMENT '举报标签，3涉黄，4辱骂，5挑拨等',
  `uid` bigint(20) DEFAULT NULL COMMENT '举报/纠错用户id',
  `reason` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci COMMENT '举报/纠错理由',
  `status` tinyint(1) DEFAULT NULL COMMENT '举报状态。0未举报/1已举报/2已审核',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_uid` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`report_feeds_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci ROW_FORMAT=DYNAMIC COMMENT='举报评论表';

-- ----------------------------
-- Table structure for sys_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_account`;
CREATE TABLE `sys_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户手机号',
  `op_phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小编手机号',
  `op_name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小编名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for sys_account_bak
-- ----------------------------
DROP TABLE IF EXISTS `sys_account_bak`;
CREATE TABLE `sys_account_bak` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户手机号',
  `op_phone` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小编手机号',
  `op_name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小编名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tb_address
-- ----------------------------
DROP TABLE IF EXISTS `tb_address`;
CREATE TABLE `tb_address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '我方库自动生成的自增ID',
  `address` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如”后厂村路与西北旺三街交叉口路南200米“',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `poi_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '例如”B000A7HFVV“',
  `poi_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如”"方恒国际中心B座"',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如990221，代表poi的类型',
  `town_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乡镇街道编码，例如“110101001000”',
  `township` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社区街道，例如“燕园街道”',
  `street` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如“阜通东大街“',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如“朝阳区”',
  `city_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如“010”',
  `city` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如：“北京市”',
  `province_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省级code',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如“北京市”“河北省”',
  `country_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家code',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家名称',
  `ad_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区编码，"110101"',
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '冗余字段，暂时废弃',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '冗余字段，暂时废弃',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '冗余字段，暂时废弃',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '冗余字段，暂时废弃',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `t_aid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `t_cover_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '封面图片',
  `t_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `t_introduce` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '简介',
  `t_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章内容',
  `t_source` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源',
  `t_total` int(11) NOT NULL COMMENT '总浏览量',
  `t_weight` tinyint(4) NOT NULL COMMENT '权重：10最高 默认1',
  `t_user_id` char(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者ID',
  `t_category_id` int(11) NOT NULL COMMENT '类目ID',
  `t_state` tinyint(4) NOT NULL COMMENT '状态[0.删除，1.正常 2.草稿]',
  `t_create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `t_up_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`t_aid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[三农网] 文章表';

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `t_cid` int(11) NOT NULL AUTO_INCREMENT COMMENT '类目id',
  `t_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类目标题',
  `t_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `t_create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `t_up_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `t_region` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区域： gd.广东三农   gx.广西三农 gz.贵州',
  PRIMARY KEY (`t_cid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[三农网] 文章类目表';

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `t_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `t_parent_id` int(11) NOT NULL COMMENT '上级部门',
  `t_level` tinyint(4) NOT NULL COMMENT '部门排序',
  `t_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门描述',
  `t_up_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `t_create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`t_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 部门表';

-- ----------------------------
-- Table structure for tb_error_code
-- ----------------------------
DROP TABLE IF EXISTS `tb_error_code`;
CREATE TABLE `tb_error_code` (
  `code` varchar(50) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `scope` varchar(50) DEFAULT NULL COMMENT '作用域',
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_create_id` varchar(255) DEFAULT NULL,
  `t_up_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci;

-- ----------------------------
-- Table structure for tb_information_flow
-- ----------------------------
DROP TABLE IF EXISTS `tb_information_flow`;
CREATE TABLE `tb_information_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` smallint(6) NOT NULL COMMENT '类型  1、说说 2、运营消息 3、广告位(位置可配) 4、运营消息 5、小程序类 6、运营活动 7、物业的公告',
  `jump_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转url',
  `resource_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `ext_id` bigint(20) DEFAULT NULL COMMENT '信息流业务扩展id',
  `is_like` bit(1) DEFAULT NULL COMMENT '是否可以点赞',
  `is_comment` bit(1) DEFAULT NULL COMMENT '是否可以评论',
  `is_forward` bit(1) DEFAULT NULL COMMENT '是否可以转发',
  `forward_hierarchy` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转发层级',
  `theme_id` bigint(20) DEFAULT NULL COMMENT '主题id',
  `is_delete` bit(6) DEFAULT NULL COMMENT '逻辑删除',
  `address_id` bigint(20) DEFAULT NULL,
  `like_num` bigint(20) DEFAULT '0',
  `comment_num` bigint(20) DEFAULT '0',
  `forward_num` bigint(20) DEFAULT '0',
  `forward_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `scope` smallint(6) DEFAULT NULL COMMENT '0:所有人可见 1:小区验证用户可见',
  `create_id` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `INDEX_address_forward` (`address_id`,`forward_hierarchy`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_information_flow_commit
-- ----------------------------
DROP TABLE IF EXISTS `tb_information_flow_commit`;
CREATE TABLE `tb_information_flow_commit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `information_flow_id` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `is_delete` bit(1) NOT NULL,
  `create_id` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `INDEX_information_flow_id` (`information_flow_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_information_flow_like
-- ----------------------------
DROP TABLE IF EXISTS `tb_information_flow_like`;
CREATE TABLE `tb_information_flow_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `information_flow_id` bigint(20) DEFAULT NULL,
  `create_id` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `INDEX_information_flow_id` (`information_flow_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_information_flow_theme
-- ----------------------------
DROP TABLE IF EXISTS `tb_information_flow_theme`;
CREATE TABLE `tb_information_flow_theme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_create_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_information_trends
-- ----------------------------
DROP TABLE IF EXISTS `tb_information_trends`;
CREATE TABLE `tb_information_trends` (
  `feeds_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `content` text COMMENT '动态内容',
  `address_id` bigint(20) DEFAULT NULL COMMENT '发表地点id',
  `review_status` int(11) DEFAULT NULL COMMENT '状态0审核中（默认），1已经签发，2废止',
  `picture` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '图片url',
  `type` int(10) DEFAULT NULL COMMENT '0本小区可见/1全国可见',
  `delete_flag` int(10) DEFAULT NULL COMMENT '0已删除，1正常',
  `create_uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_ai_ci DEFAULT NULL COMMENT '发布人id',
  `create_name` varchar(100) DEFAULT NULL COMMENT '发布人姓名',
  `t_create_time` datetime DEFAULT NULL COMMENT '发布时间',
  `address_name` varchar(255) DEFAULT NULL COMMENT '所属小区',
  `t_up_time` datetime DEFAULT NULL COMMENT '修改时间',
  `feeds_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `poi_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`feeds_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_ai_ci;

-- ----------------------------
-- Table structure for tb_notification
-- ----------------------------
DROP TABLE IF EXISTS `tb_notification`;
CREATE TABLE `tb_notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` smallint(6) DEFAULT NULL COMMENT '类型 0:转发 1:评论 2:点赞',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务id',
  `flow_id` bigint(20) DEFAULT NULL,
  `notifi_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL COMMENT '状态 0：未读 1：已读',
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `body` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `t_pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限唯一ID',
  `t_parent_id` int(11) NOT NULL COMMENT '上级ID',
  `t_resources` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限资源 ',
  `t_title` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源名称',
  `t_icon` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源图标',
  `t_type` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '类型，menu或者button',
  `t_create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `t_up_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `t_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限描述',
  PRIMARY KEY (`t_pid`) USING BTREE,
  UNIQUE KEY `t_resources` (`t_resources`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 权限表';

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `t_rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统角色ID',
  `t_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统角色描述',
  `t_name` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统角色名称',
  `t_state` tinyint(4) NOT NULL COMMENT '系统角色状态[0.删除，1.正常]',
  `t_up_time` datetime NOT NULL COMMENT '修改时间',
  `t_create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`t_rid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 角色表';

-- ----------------------------
-- Table structure for tb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission` (
  `t_permission_id` int(11) NOT NULL COMMENT '权限ID',
  `t_role_id` int(11) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 角色和权限表';

-- ----------------------------
-- Table structure for tb_sendmanage
-- ----------------------------
DROP TABLE IF EXISTS `tb_sendmanage`;
CREATE TABLE `tb_sendmanage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `identification_code` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `is_send` bit(1) DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `send_user` char(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_setting`;
CREATE TABLE `tb_setting` (
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `t_create_time` datetime DEFAULT NULL,
  `t_up_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `t_uid` char(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `t_avatar` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `t_account` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `t_mail` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `t_open_id` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信open Id',
  `t_nickname` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `t_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `t_gender` tinyint(4) DEFAULT '2' COMMENT '性别[ 0.女  1.男  2.未知]',
  `t_birthday` datetime DEFAULT NULL COMMENT '生日',
  `t_state` tinyint(4) NOT NULL COMMENT '状态 【0.禁用 1.正常 2.注销 3.已注册未完善用户信息未认证-填写地址 4.已注册未通过定位验证第一步 5.已注册未通过定位验证第二步】',
  `t_create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `t_up_time` datetime DEFAULT NULL COMMENT '修改时间',
  `t_dept` int(11) DEFAULT NULL COMMENT '部门id',
  `t_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `t_address_id` bigint(20) DEFAULT NULL COMMENT '社区id',
  `t_house` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '住址id',
  `t_union_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户在腾讯开放平台唯一code',
  `t_wx_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信号',
  `t_wx_nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信昵称',
  `t_auth_state` tinyint(1) DEFAULT '0' COMMENT '身份认证 0:未认证 1:审核中 2:已认证',
  `t_project_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目链接',
  `t_auth_type` tinyint(1) DEFAULT '0' COMMENT '认证类型 0:个人用户 1:企业用户',
  `t_memo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `t_hobbies` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '特长爱好,逗号分隔',
  `t_qualify_url_1` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质1图片url',
  `t_qualify_url_2` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质2图片url',
  `t_qualify_url_3` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质3图片url',
  `t_character_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `t_character_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色图片',
  `t_qualify_time` datetime DEFAULT NULL COMMENT '请求认证时间',
  PRIMARY KEY (`t_uid`) USING BTREE,
  UNIQUE KEY `t_phone` (`t_phone`) USING BTREE,
  UNIQUE KEY `t_nickname` (`t_nickname`) USING BTREE,
  UNIQUE KEY `t_mail` (`t_mail`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 用户表';

-- ----------------------------
-- Table structure for tb_user_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_bak`;
CREATE TABLE `tb_user_bak` (
  `t_uid` char(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `t_avatar` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `t_account` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `t_mail` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `t_open_id` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信open Id',
  `t_nickname` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `t_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `t_gender` tinyint(4) DEFAULT '2' COMMENT '性别[ 0.女  1.男  2.未知]',
  `t_birthday` datetime DEFAULT NULL COMMENT '生日',
  `t_state` tinyint(4) NOT NULL COMMENT '状态 【0.禁用 1.正常 2.注销 3.已注册未完善用户信息未认证-填写地址 4.已注册未通过定位验证第一步 5.已注册未通过定位验证第二步】',
  `t_create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `t_up_time` datetime DEFAULT NULL COMMENT '修改时间',
  `t_dept` int(11) DEFAULT NULL COMMENT '部门id',
  `t_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `t_address_id` bigint(20) DEFAULT NULL COMMENT '社区id',
  `t_house` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '住址id',
  `t_union_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户在腾讯开放平台唯一code',
  `t_wx_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信号',
  `t_wx_nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信昵称',
  `t_auth_state` tinyint(1) DEFAULT '0' COMMENT '身份认证 0:未认证 1:审核中 2:已认证',
  `t_project_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目链接',
  `t_auth_type` tinyint(1) DEFAULT '0' COMMENT '认证类型 0:个人用户 1:企业用户',
  `t_memo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `t_hobbies` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '特长爱好,逗号分隔',
  `t_qualify_url_1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质1图片url',
  `t_qualify_url_2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质2图片url',
  `t_qualify_url_3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资质3图片url',
  `t_character_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `t_character_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色图片',
  PRIMARY KEY (`t_uid`) USING BTREE,
  UNIQUE KEY `t_phone` (`t_phone`) USING BTREE,
  UNIQUE KEY `t_nickname` (`t_nickname`) USING BTREE,
  UNIQUE KEY `t_mail` (`t_mail`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 用户表';

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role` (
  `t_user_id` char(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `t_role_id` int(11) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='[权限管理] 用户表和角色表';

-- ----------------------------
-- View structure for aa
-- ----------------------------
DROP VIEW IF EXISTS `aa`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `aa` AS select cast(`tb_user`.`t_create_time` as date) AS `d`,count(1) AS `count(1)` from `tb_user` where ((cast(`tb_user`.`t_create_time` as date) >= '2019-05-01') and (cast(`tb_user`.`t_create_time` as date) <= '2020-05-30')) group by `d` order by `d` desc;

-- ----------------------------
-- View structure for bb
-- ----------------------------
DROP VIEW IF EXISTS `bb`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `bb` AS select cast(`tb_user`.`t_create_time` as date) AS `date`,count(1) AS `num` from `tb_user` where ((cast(`tb_user`.`t_create_time` as date) >= '2019-05-01') and (cast(`tb_user`.`t_create_time` as date) <= '2020-05-30')) group by `date` order by `date` desc;

SET FOREIGN_KEY_CHECKS = 1;
