/*
Navicat MySQL Data Transfer

Source Server         : 192_168_88_218_srpingboot
Source Server Version : 50726
Source Host           : 192.168.88.218:3306
Source Database       : pydb

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2022-07-25 09:44:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gitinfo_dev_deong`
-- ----------------------------
DROP TABLE IF EXISTS `gitinfo_dev_deong`;
CREATE TABLE `gitinfo_dev_deong` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_name` varchar(100) DEFAULT NULL COMMENT '开发整理项目名',
  `project_chandao_id` int(11) DEFAULT NULL COMMENT '禅道项目id',
  `git_dev_type` varchar(200) DEFAULT NULL COMMENT '前端与后端类型,前端为:web,后端为:back',
  `git_repository_name` varchar(60) DEFAULT NULL COMMENT '仓库名称',
  `git_remark` varchar(50) DEFAULT NULL COMMENT '仓库备注',
  `git_branch_https` varchar(200) DEFAULT NULL COMMENT 'gitlab分支https地址',
  `record_name` varchar(20) DEFAULT NULL COMMENT '记录人',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(50) DEFAULT NULL COMMENT '扩展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `gitinspector_report`
-- ----------------------------
DROP TABLE IF EXISTS `gitinspector_report`;
CREATE TABLE `gitinspector_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_name` varchar(200) NOT NULL COMMENT '单类型个项目名',
  `project_type` varchar(15) DEFAULT NULL COMMENT '项目类型',
  `state` varchar(2) NOT NULL COMMENT '项目报告是否可用,0可用，1不可用',
  `report_html_path` varchar(800) NOT NULL COMMENT '报告路径',
  `report_detail` varchar(1000) NOT NULL COMMENT '报告详情',
  `report_date` varchar(20) DEFAULT NULL COMMENT '报告日期',
  `mixed` varchar(3) DEFAULT NULL COMMENT '是否是混合类型,0是混合执行,1则是单个执行',
  `mixed_name` varchar(200) DEFAULT NULL COMMENT '混合类型的名',
  `git_branch` varchar(30) DEFAULT NULL COMMENT '分支名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gitinspector_report
-- ----------------------------

-- ----------------------------
-- Table structure for `gitinspector_timer_report`
-- ----------------------------
DROP TABLE IF EXISTS `gitinspector_timer_report`;
CREATE TABLE `gitinspector_timer_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `report_detail` varchar(25) DEFAULT NULL COMMENT '报告详情',
  `state` varchar(2) DEFAULT NULL COMMENT '项目报告是否可用,0可用，1不可用',
  `git_branch` varchar(60) DEFAULT NULL COMMENT '分支名',
  `project_chandao_id` int(11) DEFAULT NULL COMMENT '禅道项目id',
  `project_chandao_name` varchar(20) DEFAULT NULL COMMENT '禅道项目名',
  `git_branch_https` varchar(200) DEFAULT NULL COMMENT 'gitlab分支https地址',
  `project_chandao_status` varchar(10) DEFAULT NULL COMMENT '禅道项目状态',
  `git_dev_type` varchar(200) DEFAULT NULL COMMENT '前端与后端类型,前端为:web,后端为:back',
  `report_date` varchar(15) DEFAULT NULL COMMENT '报告日期',
  `repository` varchar(200) DEFAULT NULL,
  `changes_name` varchar(50) DEFAULT NULL COMMENT '提交作者名',
  `changes_email` varchar(100) DEFAULT NULL COMMENT '提交者email',
  `changes_commits` varchar(7) DEFAULT NULL COMMENT '提交行数',
  `changes_insertions` varchar(7) DEFAULT NULL COMMENT '提交新增代码行数',
  `changes_deletions` varchar(7) DEFAULT NULL COMMENT '提交删除代码行数',
  `changes_percentage_of_changes` varchar(4) DEFAULT NULL COMMENT '修改百分比',
  `intact_rows` varchar(7) DEFAULT NULL COMMENT '行数',
  `intact_stability` varchar(7) DEFAULT NULL COMMENT '稳定行数',
  `intact_age` varchar(7) DEFAULT NULL COMMENT '保持时间',
  `intact_percentage_in_comments` varchar(4) DEFAULT NULL COMMENT '到目前提交代码百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1510 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gitinspector_timer_report
-- ----------------------------



-- ----------------------------
-- Table structure for `inter_cases`
-- ----------------------------
DROP TABLE IF EXISTS `inter_cases`;
CREATE TABLE `inter_cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(500) NOT NULL,
  `request_url` varchar(800) NOT NULL,
  `request_method` varchar(10) NOT NULL,
  `key` varchar(10) DEFAULT NULL,
  `pre_sql` varchar(10) DEFAULT NULL,
  `request_body_param` varchar(800) DEFAULT NULL,
  `extract` varchar(500) DEFAULT NULL,
  `asert_expr` varchar(500) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `files` varchar(100) DEFAULT NULL,
  `assert_db` varchar(200) DEFAULT NULL,
  `reg_sava_param` varchar(1000) DEFAULT NULL,
  `regular` varchar(1000) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `interface_type` varchar(200) DEFAULT NULL,
  `project_type` varchar(200) DEFAULT NULL,
  `upload_file` varchar(500) DEFAULT NULL,
  `create_date` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inter_cases
-- ----------------------------

-- ----------------------------
-- Table structure for `report`
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `case_name` varchar(500) NOT NULL,
  `key` varchar(10) DEFAULT NULL,
  `request_body` varchar(1000) NOT NULL,
  `request_url` varchar(800) NOT NULL,
  `request_method` varchar(10) NOT NULL,
  `expected` varchar(1000) NOT NULL,
  `actual_result` varchar(100) NOT NULL,
  `responsed` varchar(100) DEFAULT NULL,
  `report_status` varchar(1) DEFAULT NULL,
  `result` varchar(10) DEFAULT NULL,
  `report_date` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of report
-- ----------------------------
