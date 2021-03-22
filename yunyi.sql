/*
 Navicat Premium Data Transfer

 Source Server         : yunyi
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : yunyi

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 14/03/2021 23:52:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`            int(11)                          NOT NULL AUTO_INCREMENT COMMENT '文章主键，唯一自增id',
    `uploader_id`   int(11)                          DEFAULT NULL COMMENT '上传者',
    `title`         varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
    `trans_title`   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '翻译后的标题',
    `original_text` text COLLATE utf8mb4_bin         NOT NULL COMMENT '原文',
    `genre`         varchar(63) COLLATE utf8mb4_bin  NOT NULL COMMENT '类别',
    `has_trans`     tinyint(1)                       DEFAULT NULL COMMENT '是否有翻译',
    `create_time`   datetime                         DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                         DEFAULT NULL COMMENT '最后一次修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文表';

-- ----------------------------
-- Records of article
-- ----------------------------
BEGIN;
INSERT INTO `article`
VALUES (1, 1, '文章2', NULL, '世界太丑陋🌶，我们毁灭世界！！还是笑嘻了', '历史', 1, '2021-03-14 10:21:45', '2021-03-14 10:35:52');
INSERT INTO `article`
VALUES (2, 1, '文章3', NULL, '这波啊,这波是肉蛋葱鸡', '政治', NULL, '2021-03-14 10:30:21', '2021-03-14 10:30:21');
INSERT INTO `article`
VALUES (3, 1, '文章4', NULL, '这波我必不可能死', '政治', NULL, '2021-03-14 10:30:49', '2021-03-14 10:30:49');
COMMIT;

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论唯一自增id',
    `floor`           int(11)    DEFAULT NULL COMMENT '楼层数',
    `sender_id`       int(11)    NOT NULL COMMENT '发送者id',
    `article_id`      int(11)    NOT NULL COMMENT '评论文章id',
    `has_ref_comment` tinyint(1) DEFAULT NULL COMMENT '是否引用评论',
    `ref_comment_id`  int(11)    DEFAULT NULL COMMENT '评论引用的评论id',
    `content`         text COLLATE utf8mb4_bin COMMENT '评论内容',
    `create_time`     datetime   DEFAULT NULL COMMENT '发送时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文评论表';

-- ----------------------------
-- Records of article_comment
-- ----------------------------
BEGIN;
INSERT INTO `article_comment`
VALUES (1, 1, 1, 1, 0, 1, '一般吧', '2021-03-14 10:22:12');
INSERT INTO `article_comment`
VALUES (2, 2, 1, 1, 1, 1, '我觉得还行', '2021-03-14 10:22:25');
COMMIT;

-- ----------------------------
-- Table structure for article_like
-- ----------------------------
DROP TABLE IF EXISTS `article_like`;
CREATE TABLE `article_like`
(
    `article_id` int(11) NOT NULL,
    `user_id`    int(11) NOT NULL,
    PRIMARY KEY (`article_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文点赞表';

-- ----------------------------
-- Records of article_like
-- ----------------------------
BEGIN;
INSERT INTO `article_like`
VALUES (1, 1);
INSERT INTO `article_like`
VALUES (2, 1);
COMMIT;

-- ----------------------------
-- Table structure for article_seg_trans
-- ----------------------------
DROP TABLE IF EXISTS `article_seg_trans`;
CREATE TABLE `article_seg_trans`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
    `trans_id`  int(11)                          DEFAULT NULL COMMENT '翻译id',
    `trans_seq` int(11)                          DEFAULT NULL COMMENT '翻译的顺序',
    `ref_ids`   varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '引用id',
    `content`   text COLLATE utf8mb4_bin COMMENT '翻译内容',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文切分的翻译表';

-- ----------------------------
-- Records of article_seg_trans
-- ----------------------------
BEGIN;
INSERT INTO `article_seg_trans`
VALUES (1, 1, 0, '1,2', 'trans 1 test 2');
INSERT INTO `article_seg_trans`
VALUES (2, 1, 1, '3', 'trans 2');
COMMIT;

-- ----------------------------
-- Table structure for article_stats
-- ----------------------------
DROP TABLE IF EXISTS `article_stats`;
CREATE TABLE `article_stats`
(
    `article_id`        int(11) NOT NULL,
    `view_num`          int(11) DEFAULT NULL,
    `like_num`          int(11) DEFAULT NULL,
    `comment_num`       int(11) DEFAULT NULL,
    `trans_request_num` int(11) DEFAULT NULL,
    PRIMARY KEY (`article_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文信息统计表';

-- ----------------------------
-- Records of article_stats
-- ----------------------------
BEGIN;
INSERT INTO `article_stats`
VALUES (1, 0, 1, 2, 0);
INSERT INTO `article_stats`
VALUES (2, 0, 1, 0, 0);
INSERT INTO `article_stats`
VALUES (3, 0, 0, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for article_text_seg
-- ----------------------------
DROP TABLE IF EXISTS `article_text_seg`;
CREATE TABLE `article_text_seg`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
    `article_id`      int(11) DEFAULT NULL COMMENT '原文id',
    `sequence_number` int(11) DEFAULT NULL COMMENT '切分序号',
    `content`         text COLLATE utf8mb4_bin COMMENT '切分内容',
    PRIMARY KEY (`id`),
    UNIQUE KEY `article_id` (`article_id`, `sequence_number`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文切分表';

-- ----------------------------
-- Records of article_text_seg
-- ----------------------------
BEGIN;
INSERT INTO `article_text_seg`
VALUES (1, 1, 0, '世界太丑陋🌶，');
INSERT INTO `article_text_seg`
VALUES (2, 1, 1, '我们毁灭世界！！');
INSERT INTO `article_text_seg`
VALUES (3, 1, 2, '还是笑嘻了');
INSERT INTO `article_text_seg`
VALUES (4, 2, 0, '这波啊,');
INSERT INTO `article_text_seg`
VALUES (5, 2, 1, '这波是肉蛋葱鸡');
INSERT INTO `article_text_seg`
VALUES (6, 3, 0, '这波');
INSERT INTO `article_text_seg`
VALUES (7, 3, 1, '我必不可能死');
COMMIT;

-- ----------------------------
-- Table structure for article_trans
-- ----------------------------
DROP TABLE IF EXISTS `article_trans`;
CREATE TABLE `article_trans`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
    `article_id`  int(11)                          DEFAULT NULL COMMENT '翻译对应原文id',
    `uploader_id` int(11)                          DEFAULT NULL COMMENT '上传者id',
    `trans_title` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '翻译标题',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='原文切分的翻译表';

-- ----------------------------
-- Records of article_trans
-- ----------------------------
BEGIN;
INSERT INTO `article_trans`
VALUES (1, 1, 1, 'not blank');
COMMIT;

-- ----------------------------
-- Table structure for request_trans
-- ----------------------------
DROP TABLE IF EXISTS `request_trans`;
CREATE TABLE `request_trans`
(
    `user_id`    int(11) NOT NULL,
    `article_id` int(11) NOT NULL,
    `solved`     tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`user_id`, `article_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- ----------------------------
-- Table structure for trans_comment
-- ----------------------------
DROP TABLE IF EXISTS `trans_comment`;
CREATE TABLE `trans_comment`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论唯一自增id',
    `floor`           int(11)    DEFAULT NULL COMMENT '楼层数',
    `sender_id`       int(11)    NOT NULL COMMENT '发送者id',
    `trans_id`        int(11)    NOT NULL COMMENT '评论文章id',
    `has_ref_comment` tinyint(1) DEFAULT NULL COMMENT '是否引用评论',
    `ref_comment_id`  int(11)    DEFAULT NULL COMMENT '评论引用的评论id',
    `content`         text COLLATE utf8mb4_bin COMMENT '评论内容',
    `create_time`     datetime   DEFAULT NULL COMMENT '发送时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='翻译评论表';

-- ----------------------------
-- Records of trans_comment
-- ----------------------------
BEGIN;
INSERT INTO `trans_comment`
VALUES (1, 1, 1, 1, 0, 2, '翻译评论', NULL);
INSERT INTO `trans_comment`
VALUES (2, 2, 1, 1, 0, 2, '引用翻译评论', NULL);
INSERT INTO `trans_comment`
VALUES (3, 3, 1, 1, 1, 2, '真·引用翻译评论', NULL);
COMMIT;

-- ----------------------------
-- Table structure for trans_item_comment
-- ----------------------------
DROP TABLE IF EXISTS `trans_item_comment`;
CREATE TABLE `trans_item_comment`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '翻译评论唯一自增id',
    `floor`        int(11)  DEFAULT NULL COMMENT '楼层数',
    `sender_id`    int(11)    NOT NULL COMMENT '发送者id',
    `trans_seg_id` int(11)    NOT NULL COMMENT '评论文章id',
    `content`      text COLLATE utf8mb4_bin COMMENT '评论内容',
    `create_time`  datetime DEFAULT NULL COMMENT '发送时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='单句评论表';

-- ----------------------------
-- Records of trans_item_comment
-- ----------------------------
BEGIN;
INSERT INTO `trans_item_comment`
VALUES (1, 1, 1, 1, '好翻译我只能说', NULL);
INSERT INTO `trans_item_comment`
VALUES (2, 1, 1, 2, '确实是好翻译', NULL);
INSERT INTO `trans_item_comment`
VALUES (3, 2, 1, 1, '再顶一下', NULL);
COMMIT;

-- ----------------------------
-- Table structure for trans_like
-- ----------------------------
DROP TABLE IF EXISTS `trans_like`;
CREATE TABLE `trans_like`
(
    `trans_id` int(11) NOT NULL,
    `user_id`  int(11) NOT NULL,
    PRIMARY KEY (`trans_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='翻译点赞表';

-- ----------------------------
-- Records of trans_like
-- ----------------------------
BEGIN;
INSERT INTO `trans_like`
VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for trans_seg_like
-- ----------------------------
DROP TABLE IF EXISTS `trans_seg_like`;
CREATE TABLE `trans_seg_like`
(
    `trans_seg_id` int(11) NOT NULL,
    `user_id`      int(11) NOT NULL,
    PRIMARY KEY (`trans_seg_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='翻译点赞表';

-- ----------------------------
-- Records of trans_seg_like
-- ----------------------------
BEGIN;
INSERT INTO `trans_seg_like`
VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for trans_seg_stats
-- ----------------------------
DROP TABLE IF EXISTS `trans_seg_stats`;
CREATE TABLE `trans_seg_stats`
(
    `trans_seg_id` int(11) NOT NULL,
    `like_num`     int(11) DEFAULT NULL,
    `comment_num`  int(11) DEFAULT NULL,
    PRIMARY KEY (`trans_seg_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='翻译统计表';

-- ----------------------------
-- Records of trans_seg_stats
-- ----------------------------
BEGIN;
INSERT INTO `trans_seg_stats`
VALUES (1, 1, 2);
INSERT INTO `trans_seg_stats`
VALUES (2, 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for trans_stats
-- ----------------------------
DROP TABLE IF EXISTS `trans_stats`;
CREATE TABLE `trans_stats`
(
    `trans_id`    int(11) NOT NULL,
    `like_num`    int(11) DEFAULT NULL,
    `comment_num` int(11) DEFAULT NULL,
    PRIMARY KEY (`trans_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='翻译统计表';

-- ----------------------------
-- Records of trans_stats
-- ----------------------------
BEGIN;
INSERT INTO `trans_stats`
VALUES (1, 1, 3);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`    bigint(20)                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`  varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '姓名',
    `age`   int(11)                         DEFAULT NULL COMMENT '年龄',
    `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) COLLATE utf8mb4_bin  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone` (`phone`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user`
VALUES (1, 'user-0152d57e-ebf5-41a1-a09c-df7bcf8c827b', 0, NULL, '17721260791');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
