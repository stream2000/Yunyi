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

 Date: 09/10/2020 00:37:20
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`    bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`  varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `age`   int(11)                                 DEFAULT NULL COMMENT '年龄',
    `email` varchar(50) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_phone_uindex` (`phone`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user`
VALUES (1, 'Jone', 18, 'test1@baomidou.com', NULL);
INSERT INTO `user`
VALUES (2, 'Jack', 20, 'test2@baomidou.com', NULL);
INSERT INTO `user`
VALUES (3, 'Tom', 28, 'test3@baomidou.com', NULL);
INSERT INTO `user`
VALUES (4, 'Sandy', 21, 'test4@baomidou.com', NULL);
INSERT INTO `user`
VALUES (5, 'Billie', 24, 'test5@baomidou.com', NULL);
INSERT INTO `user`
VALUES (11, 'user-27a8c1d3-1913-4a51-8087-a99882b0c5d1', 0, NULL, '17721260791');
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;
