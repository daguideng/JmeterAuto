/*
Navicat MySQL Data Transfer

Source Server         : 10.193.199.122_jmeter
Source Server Version : 50718
Source Host           : 10.193.199.122:3306
Source Database       : jmeterboot

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-10-10 10:40:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apdex
-- ----------------------------
DROP TABLE IF EXISTS `apdex`;
CREATE TABLE `apdex` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `apdex` varchar(20) NOT NULL COMMENT 'apdex',
  `tolerationthreshold` varchar(20) NOT NULL COMMENT 'tolerationthreshold',
  `frustrationthreshold` varchar(5) NOT NULL COMMENT 'frustrationthreshold',
  `label` varchar(100) NOT NULL COMMENT 'label',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contrarian_log_query
-- ----------------------------
DROP TABLE IF EXISTS `contrarian_log_query`;
CREATE TABLE `contrarian_log_query` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `time` varchar(100) NOT NULL COMMENT 'time',
  `username` varchar(30) NOT NULL COMMENT 'username',
  `title` varchar(50) NOT NULL COMMENT 'title',
  `lendRequestId` varchar(30) NOT NULL COMMENT 'lendRequestId',
  `worktype` varchar(50) DEFAULT NULL,
  `temp` varchar(50) DEFAULT NULL COMMENT 'temp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contrarian_log_type
-- ----------------------------
DROP TABLE IF EXISTS `contrarian_log_type`;
CREATE TABLE `contrarian_log_type` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL COMMENT 'time',
  `detail` varchar(8000) NOT NULL COMMENT 'temp',
  `temp` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for errors
-- ----------------------------
DROP TABLE IF EXISTS `errors`;
CREATE TABLE `errors` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `typeoferror` varchar(20) NOT NULL COMMENT 'typeoferror',
  `numberoferrors` varchar(20) NOT NULL COMMENT 'numberoferrors',
  `inerrors` varchar(5) NOT NULL COMMENT 'inerrors',
  `inallsamples` varchar(100) NOT NULL COMMENT 'inallsamples',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for interface_config
-- ----------------------------
DROP TABLE IF EXISTS `interface_config`;
CREATE TABLE `interface_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `threads` varchar(200) NOT NULL COMMENT '并发线程数',
  `runtime` varchar(15) NOT NULL COMMENT '运行时间(分)',
  `delaytime` varchar(100) NOT NULL COMMENT '延迟运行场景(秒)',
  `ifretry` varchar(10) NOT NULL COMMENT '是否设置重试次数(Yes/No)',
  `ifoutinterval` varchar(10) NOT NULL COMMENT '是否修改控制台输出时间间隔(Yes/No)',
  `ifcustomlistener` varchar(10) NOT NULL COMMENT '是否追加自定义监听器',
  `ifrecordlogjtl` varchar(10) NOT NULL COMMENT '是否记录日志log.jtl(Yes/No)',
  `ifrecordlogjmeter` varchar(10) NOT NULL COMMENT '是否记录日志jmeter.log(Yes/No)',
  `ifbetweenvalue` varchar(10) NOT NULL COMMENT '是否设置BetweenValue(Yes/No)',
  `threadname` varchar(50) NOT NULL COMMENT '线程名',
  `status` varchar(5) NOT NULL COMMENT '运行状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=266 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for interface_current_report
-- ----------------------------
DROP TABLE IF EXISTS `interface_current_report`;
CREATE TABLE `interface_current_report` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `lastruntime` char(20) DEFAULT NULL,
  `scriptname` varchar(500) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(500) NOT NULL DEFAULT '' COMMENT 'label',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `ko` varchar(10) NOT NULL COMMENT 'ko',
  `error` varchar(800) NOT NULL COMMENT 'error',
  `average` varchar(10) NOT NULL COMMENT 'average',
  `min` varchar(100) NOT NULL COMMENT 'min',
  `max` varchar(100) NOT NULL COMMENT 'max',
  `thpct90` varchar(100) NOT NULL COMMENT '90thpct',
  `thpct95` varchar(100) NOT NULL COMMENT '95thpct',
  `thpct99` varchar(100) NOT NULL COMMENT '99thpct',
  `throughput` varchar(100) NOT NULL COMMENT 'throughput',
  `received` varchar(100) NOT NULL COMMENT 'received',
  `sent` varchar(100) NOT NULL COMMENT 'sent',
  `ip` varchar(150) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1481 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for interface_history_report
-- ----------------------------
DROP TABLE IF EXISTS `interface_history_report`;
CREATE TABLE `interface_history_report` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `lastruntime` char(20) DEFAULT NULL,
  `scriptname` varchar(500) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(500) NOT NULL DEFAULT '' COMMENT 'label',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `ko` varchar(10) NOT NULL COMMENT 'ko',
  `error` varchar(800) NOT NULL COMMENT 'error',
  `average` varchar(100) NOT NULL COMMENT 'average',
  `min` varchar(100) NOT NULL COMMENT 'min',
  `max` varchar(100) NOT NULL COMMENT 'max',
  `thpct90` varchar(100) NOT NULL COMMENT '90thpct',
  `thpct95` varchar(100) NOT NULL COMMENT '95thpct',
  `thpct99` varchar(100) NOT NULL COMMENT '99thpct',
  `throughput` varchar(100) NOT NULL COMMENT 'throughput',
  `received` varchar(100) NOT NULL COMMENT 'received',
  `sent` varchar(100) NOT NULL COMMENT 'sent',
  `ip` varchar(150) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1481 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for interface_run_ip
-- ----------------------------
DROP TABLE IF EXISTS `interface_run_ip`;
CREATE TABLE `interface_run_ip` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `jmeter_load_run` varchar(100) NOT NULL COMMENT '运行负载机',
  `to_email` varchar(100) DEFAULT NULL COMMENT 'to_email',
  `cc_email` varchar(100) DEFAULT NULL COMMENT 'cc_email',
  `bcc_email` varchar(100) DEFAULT NULL COMMENT 'bcc_email',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_agentip_states
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_agentip_states`;
CREATE TABLE `jmeter_agentip_states` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(26) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `states` varchar(50) DEFAULT NULL COMMENT '状态',
  `type` varchar(15) DEFAULT NULL COMMENT '运行类型',
  `time` varchar(26) DEFAULT NULL COMMENT '时间',
  `runusername` varchar(30) DEFAULT NULL COMMENT '运行用户',
  `runstate` int(1) NOT NULL DEFAULT '0' COMMENT '自定义运行状态，如果是1则不能运行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_agentip_states_selectip
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_agentip_states_selectip`;
CREATE TABLE `jmeter_agentip_states_selectip` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(26) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `states` varchar(50) DEFAULT NULL COMMENT '状态',
  `type` varchar(15) DEFAULT NULL COMMENT '运行类型',
  `time` varchar(26) DEFAULT NULL COMMENT '时间',
  `runusername` varchar(30) DEFAULT NULL COMMENT '运行用户',
  `runstate` int(1) NOT NULL DEFAULT '0' COMMENT '自定义运行状态，如果是1则不能运行',
  `select_ip_status` int(1) DEFAULT '0' COMMENT '选择指定ip状态（0未选择，1选择）',
  `script_id` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '脚本id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_interface_agent_source
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_interface_agent_source`;
CREATE TABLE `jmeter_interface_agent_source` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(26) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `osname` varchar(50) DEFAULT NULL COMMENT '系统名',
  `hostname` varchar(50) DEFAULT NULL COMMENT '主机名',
  `cpucount` varchar(26) DEFAULT NULL COMMENT 'cpu核数',
  `systemcpuusage` varchar(50) DEFAULT NULL COMMENT 'cpu使用率',
  `time` varchar(26) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_interface_top5_errors
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_interface_top5_errors`;
CREATE TABLE `jmeter_interface_top5_errors` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `scriptname` varchar(50) DEFAULT NULL COMMENT '脚本名称',
  `threads` varchar(3) DEFAULT NULL COMMENT '线程数',
  `runtime` varchar(20) NOT NULL COMMENT 'time',
  `sample` varchar(500) NOT NULL COMMENT 'sample',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `errors` varchar(500) NOT NULL COMMENT 'errors',
  `error_1` varchar(500) NOT NULL COMMENT 'inallsamples',
  `errors_1` varchar(1000) DEFAULT NULL,
  `error_2` varchar(500) DEFAULT NULL,
  `errors_2` varchar(500) DEFAULT NULL,
  `error_3` varchar(500) DEFAULT NULL,
  `errors_3` varchar(500) DEFAULT NULL,
  `error_4` varchar(500) DEFAULT NULL,
  `errors_4` varchar(500) DEFAULT NULL,
  `error_5` varchar(500) DEFAULT NULL,
  `errors_5` varchar(500) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `jplpath` varchar(500) DEFAULT NULL COMMENT 'jplpath路径',
  `indexpath` varchar(500) DEFAULT NULL COMMENT 'indexpath',
  `states` int(1) DEFAULT '0' COMMENT '状态(入库为0,再次入库为1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_perfor_current_report
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_perfor_current_report`;
CREATE TABLE `jmeter_perfor_current_report` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `lastruntime` char(20) DEFAULT NULL,
  `scriptname` varchar(500) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(500) NOT NULL DEFAULT '' COMMENT 'label',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `ko` varchar(10) NOT NULL COMMENT 'ko',
  `error` varchar(800) NOT NULL COMMENT 'error',
  `average` varchar(100) NOT NULL COMMENT 'average',
  `min` varchar(100) NOT NULL COMMENT 'min',
  `max` varchar(100) NOT NULL COMMENT 'max',
  `thpct90` varchar(100) NOT NULL COMMENT '90thpct',
  `thpct95` varchar(100) NOT NULL COMMENT '95thpct',
  `thpct99` varchar(100) NOT NULL COMMENT '99thpct',
  `throughput` varchar(100) NOT NULL COMMENT 'throughput',
  `received` varchar(100) NOT NULL COMMENT 'received',
  `sent` varchar(100) NOT NULL COMMENT 'sent',
  `ip` varchar(150) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_perfor_history_report
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_perfor_history_report`;
CREATE TABLE `jmeter_perfor_history_report` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `perfstarttime` varchar(20) DEFAULT NULL COMMENT '性能测试起始时间',
  `lastruntime` char(20) DEFAULT NULL,
  `scriptname` varchar(500) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(500) NOT NULL DEFAULT '' COMMENT 'label',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `ko` varchar(10) NOT NULL COMMENT 'ko',
  `error` varchar(800) NOT NULL COMMENT 'error',
  `average` varchar(100) NOT NULL COMMENT 'average',
  `min` varchar(100) NOT NULL COMMENT 'min',
  `max` varchar(100) NOT NULL COMMENT 'max',
  `thpct90` varchar(100) NOT NULL COMMENT '90thpct',
  `thpct95` varchar(100) NOT NULL COMMENT '95thpct',
  `thpct99` varchar(100) NOT NULL COMMENT '99thpct',
  `throughput` varchar(100) NOT NULL COMMENT 'throughput',
  `received` varchar(100) NOT NULL COMMENT 'received',
  `sent` varchar(100) NOT NULL COMMENT 'sent',
  `ip` varchar(150) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_perf_agent_source
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_perf_agent_source`;
CREATE TABLE `jmeter_perf_agent_source` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(26) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `osname` varchar(50) DEFAULT NULL COMMENT '系统名',
  `hostname` varchar(200) DEFAULT NULL COMMENT '主机名',
  `cpucount` varchar(26) DEFAULT NULL COMMENT 'cpu核数',
  `cpufree` varchar(26) DEFAULT NULL COMMENT '空闲cpu',
  `systemcpuusage` varchar(50) DEFAULT NULL COMMENT 'systemcpu使用率',
  `memory` varchar(26) DEFAULT NULL COMMENT '内存数',
  `diskram` varchar(26) DEFAULT NULL COMMENT '硬盘数',
  `javaversion` varchar(26) DEFAULT NULL COMMENT 'java版本',
  `time` varchar(26) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_perf_run_ip
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_perf_run_ip`;
CREATE TABLE `jmeter_perf_run_ip` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `jmeter_load_run` varchar(100) NOT NULL DEFAULT '' COMMENT '运行负载机',
  `to_email` varchar(100) DEFAULT NULL COMMENT 'to_email',
  `cc_email` varchar(100) DEFAULT NULL COMMENT 'cc_email',
  `bcc_email` varchar(100) DEFAULT NULL COMMENT 'bcc_email',
  `specified_state` int(1) DEFAULT '0' COMMENT '指定负载机运行状态：1可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `jmeterboot`.`jmeter_perf_run_ip` (`id`, `jmeter_load_run`, `to_email`, `cc_email`, `bcc_email`, `specified_state`) VALUES ('00001', '127.0.0.1', '294332968@qq.com', '294332968@qq.com', '294332968@qq.com', '0');




-- ----------------------------
-- Table structure for jmeter_perf_top5_errors
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_perf_top5_errors`;
CREATE TABLE `jmeter_perf_top5_errors` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `scriptname` varchar(50) DEFAULT NULL COMMENT '脚本名称',
  `threads` varchar(3) DEFAULT NULL COMMENT '线程数',
  `runtime` varchar(20) NOT NULL COMMENT 'time',
  `sample` varchar(500) NOT NULL COMMENT 'sample',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `errors` varchar(500) NOT NULL COMMENT 'errors',
  `error_1` varchar(500) NOT NULL COMMENT 'inallsamples',
  `errors_1` varchar(1000) DEFAULT NULL,
  `error_2` varchar(500) DEFAULT NULL,
  `errors_2` varchar(500) DEFAULT NULL,
  `error_3` varchar(500) DEFAULT NULL,
  `errors_3` varchar(500) DEFAULT NULL,
  `error_4` varchar(500) DEFAULT NULL,
  `errors_4` varchar(500) DEFAULT NULL,
  `error_5` varchar(500) DEFAULT NULL,
  `errors_5` varchar(500) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `jplpath` varchar(500) DEFAULT NULL COMMENT 'jplpath路径',
  `indexpath` varchar(500) DEFAULT NULL COMMENT 'indexpath',
  `states` int(1) DEFAULT '0' COMMENT '状态(入库为0,再次入库为1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jmeter_runip_states
-- ----------------------------
DROP TABLE IF EXISTS `jmeter_runip_states`;
CREATE TABLE `jmeter_runip_states` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(26) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `states` varchar(50) DEFAULT NULL COMMENT '状态',
  `type` varchar(15) DEFAULT NULL COMMENT '运行类型',
  `time` varchar(26) DEFAULT NULL COMMENT '时间',
  `runusername` varchar(30) DEFAULT NULL COMMENT '运行用户',
  `runstate` int(1) NOT NULL DEFAULT '0' COMMENT '自定义运行状态，如果是1则不能运行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for msjr_overpayment
-- ----------------------------
DROP TABLE IF EXISTS `msjr_overpayment`;
CREATE TABLE `msjr_overpayment` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `accounting_system_ip` varchar(800) NOT NULL COMMENT '帐务系统ip地址',
  `sub_db_table_ip` varchar(800) NOT NULL COMMENT '分库分表ip地址',
  `overpay_json` text NOT NULL COMMENT 'overpay_json_Body',
  `base_info_ip` varchar(100) DEFAULT NULL COMMENT 'str0',
  `str1` varchar(10) DEFAULT NULL COMMENT 'str1',
  `str2` varchar(10) DEFAULT NULL COMMENT 'str2',
  `str3` varchar(10) DEFAULT NULL COMMENT 'str3',
  `str4` varchar(10) DEFAULT NULL COMMENT 'str4',
  `str5` varchar(10) DEFAULT NULL COMMENT 'str5',
  `str6` varchar(10) DEFAULT NULL COMMENT 'str6',
  `str7` varchar(10) DEFAULT NULL COMMENT 'str7',
  `str8` varchar(10) DEFAULT NULL COMMENT 'str8',
  `str9` varchar(10) DEFAULT NULL COMMENT 'str9',
  `str10` varchar(10) DEFAULT NULL COMMENT 'str10',
  `str11` varchar(10) DEFAULT NULL COMMENT 'str11',
  `str12` varchar(10) DEFAULT NULL COMMENT 'str12',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for perf_config
-- ----------------------------
DROP TABLE IF EXISTS `perf_config`;
CREATE TABLE `perf_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `threads` varchar(200) NOT NULL COMMENT '并发线程数',
  `runtime` varchar(15) NOT NULL COMMENT '运行时间(分)',
  `delaytime` varchar(100) NOT NULL COMMENT '延迟运行场景(秒)',
  `ifretry` varchar(10) NOT NULL COMMENT '是否设置重试次数(Yes/No)',
  `ifoutinterval` varchar(10) NOT NULL COMMENT '是否修改控制台输出时间间隔(Yes/No)',
  `ifcustomlistener` varchar(10) NOT NULL COMMENT '是否追加自定义监听器',
  `ifrecordlogjtl` varchar(10) NOT NULL COMMENT '是否记录日志log.jtl(Yes/No)',
  `ifrecordlogjmeter` varchar(10) NOT NULL COMMENT '是否记录日志jmeter.log(Yes/No)',
  `ifbetweenvalue` varchar(10) NOT NULL COMMENT '是否设置BetweenValue(Yes/No)',
  `threadname` varchar(50) NOT NULL COMMENT '线程名',
  `status` varchar(50) NOT NULL COMMENT '运行状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for report_infor
-- ----------------------------
DROP TABLE IF EXISTS `report_infor`;
CREATE TABLE `report_infor` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `sourcefile` varchar(20) NOT NULL COMMENT 'sourcefile',
  `starttime` varchar(20) NOT NULL COMMENT '结束时间',
  `endtime` varchar(5) NOT NULL COMMENT 'endtime',
  `filterdisplay` varchar(100) NOT NULL COMMENT 'filterdisplay',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for statistics
-- ----------------------------
DROP TABLE IF EXISTS `statistics`;
CREATE TABLE `statistics` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `perfstarttime` varchar(20) DEFAULT NULL COMMENT '性能测试线程起始时点',
  `lastruntime` varchar(20) DEFAULT NULL COMMENT '单个线程的起始时间点',
  `scriptname` varchar(500) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(500) NOT NULL DEFAULT '' COMMENT 'label',
  `samples` varchar(20) NOT NULL COMMENT 'samples',
  `ko` varchar(10) NOT NULL COMMENT 'ko',
  `error` varchar(800) NOT NULL COMMENT 'error',
  `average` varchar(100) NOT NULL COMMENT 'average',
  `min` varchar(100) NOT NULL COMMENT 'min',
  `max` varchar(100) NOT NULL COMMENT 'max',
  `thpct90` varchar(100) NOT NULL COMMENT '90thpct',
  `thpct95` varchar(100) NOT NULL COMMENT '95thpct',
  `thpct99` varchar(100) NOT NULL COMMENT '99thpct',
  `throughput` varchar(100) NOT NULL COMMENT 'throughput',
  `received` varchar(100) NOT NULL COMMENT 'received',
  `sent` varchar(100) NOT NULL COMMENT 'sent',
  `ip` varchar(150) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for timer
-- ----------------------------
DROP TABLE IF EXISTS `timer`;
CREATE TABLE `timer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ids` varchar(100) NOT NULL COMMENT 'run上传id的脚本',
  `timertask` varchar(100) NOT NULL COMMENT '定时器运行时间',
  `type` varchar(10) NOT NULL COMMENT '运行类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for timer_type_config
-- ----------------------------
DROP TABLE IF EXISTS `timer_type_config`;
CREATE TABLE `timer_type_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `threads` varchar(200) NOT NULL COMMENT '并发线程数',
  `runtime` varchar(15) NOT NULL COMMENT '运行时间(分)',
  `delaytime` varchar(100) NOT NULL COMMENT '延迟运行场景(秒)',
  `ifretry` varchar(10) NOT NULL COMMENT '是否设置重试次数(Yes/No)',
  `ifoutinterval` varchar(10) NOT NULL COMMENT '是否修改控制台输出时间间隔(Yes/No)',
  `ifcustomlistener` varchar(10) NOT NULL COMMENT '是否追加自定义监听器',
  `ifrecordlogjtl` varchar(10) NOT NULL COMMENT '是否记录日志log.jtl(Yes/No)',
  `ifrecordlogjmeter` varchar(10) NOT NULL COMMENT '是否记录日志jmeter.log(Yes/No)',
  `ifbetweenvalue` varchar(10) NOT NULL COMMENT '是否设置BetweenValue(Yes/No)',
  `threadname` varchar(50) NOT NULL COMMENT '线程名',
  `status` varchar(5) NOT NULL COMMENT '运行状态',
  `type` varchar(5) DEFAULT NULL COMMENT '运行类型',
  `timestatus` varchar(5) DEFAULT NULL COMMENT '定时器开关',
  `ids` varchar(500) DEFAULT NULL COMMENT '待运行的ids',
  `timetask` varchar(100) DEFAULT NULL COMMENT '时间定时器',
  `jobname` varchar(100) DEFAULT NULL COMMENT 'JobName',
  `triggername` varchar(100) DEFAULT NULL COMMENT 'TriggerName',
  `jobgroup` varchar(100) DEFAULT NULL COMMENT 'jobGroup',
  `jobdescribe` varchar(100) DEFAULT NULL COMMENT 'JobDescribe',
  `deletestate` int(1) unsigned zerofill DEFAULT '1' COMMENT '删除状态(0删除，1保留)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for upload_info
-- ----------------------------
DROP TABLE IF EXISTS `upload_info`;
CREATE TABLE `upload_info` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `uploadtime` varchar(20) NOT NULL COMMENT '脚本上传时间',
  `lastruntime` varchar(20) DEFAULT NULL COMMENT '最后运行时间',
  `perflastruntime` varchar(20) DEFAULT NULL COMMENT '性能执行时间',
  `username` varchar(20) DEFAULT NULL COMMENT '脚本所有者',
  `scriptname` varchar(50) NOT NULL COMMENT '上传脚本名',
  `interfacename` varchar(50) NOT NULL COMMENT '接口名',
  `scripttype` varchar(10) NOT NULL COMMENT '性能运行执行时间',
  `operationtype` varchar(200) NOT NULL COMMENT '操作类型，如下载等操作',
  `scriptpath` varchar(800) NOT NULL COMMENT '脚本上传保存目录',
  `runbutton` varchar(10) DEFAULT NULL COMMENT '运行脚本一个控件',
  `scriptrunorder` varchar(5) DEFAULT NULL COMMENT '脚本执行顺序',
  `testname0` varchar(30) DEFAULT NULL,
  `testname1` varchar(30) DEFAULT NULL,
  `testname2` varchar(30) DEFAULT NULL,
  `testname3` varchar(30) DEFAULT NULL,
  `urL0` varchar(200) DEFAULT NULL,
  `urL1` varchar(200) DEFAULT NULL,
  `urL2` varchar(200) DEFAULT NULL,
  `urL3` varchar(200) DEFAULT NULL,
  `urL0replace` varchar(30) DEFAULT NULL,
  `urL1replace` varchar(30) DEFAULT NULL,
  `urL2replace` varchar(30) DEFAULT NULL,
  `urL3replace` varchar(30) DEFAULT NULL,
  `state` varchar(10) DEFAULT '' COMMENT '性能脚本运行状态',
  `interstate` varchar(10) DEFAULT '' COMMENT '接口测试运行状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `emailaddress` varchar(30) DEFAULT NULL COMMENT 'emailaddress',
  `runstate` int(1) DEFAULT '0' COMMENT '用户运行状态0,可运行,1不可运行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `jmeterboot`.`user` (`id`, `username`, `password`, `emailaddress`, `runstate`) VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'dagui.deng@xxx.com', '0');


CREATE TABLE `email_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_transport_protocol` varchar(10) NOT NULL COMMENT '邮件传输协议',
  `mail_smtp_host` varchar(20) NOT NULL COMMENT '发送邮件服务器',
  `mail_smtp_port` varchar(20) NOT NULL COMMENT '发送邮件端口号',
  `mail_smtp_auth` varchar(20) NOT NULL COMMENT '邮件是否认证',
  `mail_debug` varchar(10) NOT NULL COMMENT 'email是否debug',
  `username` varchar(25) NOT NULL COMMENT '发送邮件账号',
  `password` varchar(20) NOT NULL COMMENT '发送邮件密码',
  `mail_from` varchar(25) NOT NULL COMMENT '谁发送邮件',
  `mail_to` varchar(25) NOT NULL COMMENT 'email发给谁',
  `mail_cc` varchar(50)  COMMENT 'email抄送者',
  `mail_bcc` varchar(200)  COMMENT 'email密送者',
  `send_report` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否发送报告邮件：1-发送，0-不发送',
  `create_date` timestamp NOT NULL  default  CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 为现有email_config表添加send_report字段
ALTER TABLE `email_config` ADD COLUMN `send_report` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否发送报告邮件：1-发送，0-不发送' AFTER `mail_bcc`;

INSERT INTO `jmeterboot`.`email_config` (`id`, `mail_transport_protocol`, `mail_smtp_host`, `mail_smtp_port`, `mail_smtp_auth`, `mail_debug`, `username`, `password`, `mail_from`, `mail_to`, `mail_cc`, `mail_bcc`, `send_report`, `create_date`) VALUES (1, 'smtp', 'smtp.qq.com', '465', 'true', 'false', '294332968@qq.com', 'svsacwzyffipcaef', '294332968@qq.com', '294332968@qq.com,dengfu2345678910@sina.com', '294332968@qq.com', '294332968@qq.com', 1, '2022-04-11 00:05:23');
