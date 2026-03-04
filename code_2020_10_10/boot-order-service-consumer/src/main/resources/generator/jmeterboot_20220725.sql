/*
Navicat MySQL Data Transfer

Source Server         : 192_168_88_218_srpingboot
Source Server Version : 50726
Source Host           : 192.168.88.218:3306
Source Database       : jmeterboot

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2022-07-25 09:43:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `apdex`
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
-- Records of apdex
-- ----------------------------

-- ----------------------------
-- Table structure for `contrarian_log_query`
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
-- Records of contrarian_log_query
-- ----------------------------

-- ----------------------------
-- Table structure for `contrarian_log_type`
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
-- Records of contrarian_log_type
-- ----------------------------

-- ----------------------------
-- Table structure for `email_config`
-- ----------------------------
DROP TABLE IF EXISTS `email_config`;
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
  `mail_cc` varchar(50) DEFAULT NULL COMMENT 'email抄送者',
  `mail_bcc` varchar(200) DEFAULT NULL COMMENT 'email密送者',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of email_config
-- ----------------------------
INSERT INTO `email_config` VALUES ('1', 'smtpxxxxx', 'mail.ireadyit.comxx', '25', 'true', 'false', 'dengdagui@ireadyit.com', 'CC888ccXX', 'dengdagui@ireadyit.com', 'dengdagui@ireadyit.com', 'dengdagui@ireadyit.com', 'dengdagui@ireadyit.com', '2022-04-11 00:05:23');

-- ----------------------------
-- Table structure for `errors`
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
-- Records of errors
-- ----------------------------

-- ----------------------------
-- Table structure for `force_perf_report`
-- ----------------------------
DROP TABLE IF EXISTS `force_perf_report`;
CREATE TABLE `force_perf_report` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `jmeterPathBin` varchar(500) DEFAULT 'NULL' COMMENT 'jmeterPathBin',
  `jsFile` varchar(500) DEFAULT 'NULL' COMMENT 'jsFile',
  `thread` varchar(20) DEFAULT NULL COMMENT 'thread',
  `nodeIp` varchar(500) DEFAULT NULL COMMENT 'nodeIp',
  `jtlReportfile` varchar(500) DEFAULT NULL COMMENT 'jtlReportfile',
  `runId` varchar(10) DEFAULT NULL COMMENT 'runId',
  `reportRunTime` varchar(20) DEFAULT NULL COMMENT 'reportRunTime',
  `jmeterScriptName` varchar(500) DEFAULT NULL COMMENT 'jmeterScriptName',
  `scriptNamePath` varchar(500) DEFAULT NULL COMMENT 'scriptNamePath',
  `currentTime` varchar(20) DEFAULT NULL COMMENT 'currentTime',
  `back_1` varchar(10) DEFAULT NULL COMMENT 'back_1',
  `back_2` varchar(10) DEFAULT NULL COMMENT 'back_2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of force_perf_report
-- ----------------------------

-- ----------------------------
-- Table structure for `interface_config`
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
) ENGINE=InnoDB AUTO_INCREMENT=8972 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of interface_config
-- ----------------------------
 



-- ----------------------------
-- Table structure for `interface_current_report`
-- ----------------------------
DROP TABLE IF EXISTS `interface_current_report`;
CREATE TABLE `interface_current_report` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uploadid` int(5) DEFAULT NULL COMMENT '脚本上传id',
  `lastruntime` char(20) DEFAULT NULL,
  `scriptname` varchar(800) NOT NULL COMMENT '脚本名',
  `threads` varchar(5) NOT NULL COMMENT '线程数',
  `label` varchar(800) NOT NULL DEFAULT '' COMMENT 'label',
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
  `ip` varchar(800) NOT NULL COMMENT 'jmeter节点的ip及端口',
  `starttime` varchar(50) NOT NULL COMMENT '运行开始时间',
  `endtime` varchar(50) NOT NULL COMMENT '运行结束时间',
  `jtlpath` varchar(500) DEFAULT NULL COMMENT 'jtl日志路径 ',
  `indexpath` varchar(500) DEFAULT NULL COMMENT '生成报告路径',
  `state` int(1) DEFAULT '0' COMMENT ' 状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6643 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of interface_current_report
-- ----------------------------

-- ----------------------------
-- Table structure for `interface_history_report`
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
) ENGINE=InnoDB AUTO_INCREMENT=7407 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of interface_history_report
-- ----------------------------


-- ----------------------------
-- Table structure for `interface_run_ip`
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
-- Records of interface_run_ip
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_agentip_states`
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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jmeter_agentip_states
-- ----------------------------
INSERT INTO `jmeter_agentip_states` VALUES ('26', '192.168.88.219:21889', 'Finished', 'Inter', '2022-04-08 18:53:43', null, '1');
INSERT INTO `jmeter_agentip_states` VALUES ('27', '192.168.88.223:21889', 'Finished', 'Inter', '2022-07-06 16:14:41', null, '0');
INSERT INTO `jmeter_agentip_states` VALUES ('28', '192.168.88.222:21889', 'Finished', 'Inter', '2022-07-06 11:54:22', null, '0');
INSERT INTO `jmeter_agentip_states` VALUES ('29', '192.168.88.218:21889', 'Created', 'Inter', '2022-05-17 00:38:12', null, '1');
INSERT INTO `jmeter_agentip_states` VALUES ('30', '192.168.88.221:21889', 'Finished', 'Inter', '2022-03-21 18:48:41', null, '1');
INSERT INTO `jmeter_agentip_states` VALUES ('31', '172.21.152.95:21889', 'Mmaped', 'Perf', '2022-03-19 10:56:15', null, '1');
INSERT INTO `jmeter_agentip_states` VALUES ('32', '192.168.0.143:21889', 'Mmaped', 'Inter', '2022-06-29 10:03:03', null, '1');

-- ----------------------------
-- Table structure for `jmeter_interface_agent_source`
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
-- Records of jmeter_interface_agent_source
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_interface_top5_errors`
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
) ENGINE=InnoDB AUTO_INCREMENT=922 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jmeter_interface_top5_errors
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_perf_agent_source`
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
-- Records of jmeter_perf_agent_source
-- ----------------------------
INSERT INTO `jmeter_perf_agent_source` VALUES ('00001', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011203277476386447', '2565', null, '1.8.0_231', '2020-07-17 16:08:28');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00002', '10.193.199.126', 'root', 'Linux', 's01cq-VMWARE0050568f4c04-test-app-199-126-msxf.host', '8.0', '8', '0.005803560535968167', '1435', null, '1.8.0_112', '2020-05-25 14:13:48');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00003', '10.193.199.126', 'root', 'Linux', 's01cq-VMWARE0050568f4c04-test-app-199-126-msxf.host', '8.0', '8', '0.005803571705732232', '1317', null, '1.8.0_112', '2020-05-25 14:13:48');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00004', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011396092470626652', '1883', null, '1.8.0_231', '2020-08-09 23:39:48');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00005', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01139738481485228', '2684', null, '1.8.0_231', '2020-08-09 23:44:00');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00006', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011400350165255108', '1849', null, '1.8.0_231', '2020-08-10 00:14:41');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00007', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011401792895329273', '1925', null, '1.8.0_231', '2020-08-10 00:20:32');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00008', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011403451389170169', '2733', null, '1.8.0_231', '2020-08-10 00:28:43');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00009', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01140505313770668', '1940', null, '1.8.0_231', '2020-08-10 00:37:21');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00010', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011406188460146007', '1897', null, '1.8.0_231', '2020-08-10 00:39:30');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00011', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011407561054382374', '2596', null, '1.8.0_231', '2020-08-10 00:43:15');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00012', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011410118023494491', '1884', null, '1.8.0_231', '2020-08-10 01:04:14');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00013', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011411625009822329', '1842', null, '1.8.0_231', '2020-08-10 01:13:33');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00014', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011413595709564782', '2667', null, '1.8.0_231', '2020-08-10 01:19:22');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00015', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011436801481006797', '2441', null, '1.8.0_231', '2020-08-10 13:11:53');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00016', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011438041709014366', '2535', null, '1.8.0_231', '2020-08-10 13:14:39');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00017', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011439157787622849', '1934', null, '1.8.0_231', '2020-08-10 13:24:36');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00018', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01144030546636177', '2438', null, '1.8.0_231', '2020-08-10 13:26:31');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00019', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011445789389319883', '2338', null, '1.8.0_231', '2020-08-13 10:44:34');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00020', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01144718504250969', '2599', null, '1.8.0_231', '2020-08-13 10:49:41');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00021', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01145316104541505', '2238', null, '1.8.0_231', '2020-08-13 12:59:22');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00022', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.01145637005605896', '2519', null, '1.8.0_231', '2020-08-13 13:03:18');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00023', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.0114584915089881', '2432', null, '1.8.0_231', '2020-08-13 21:00:19');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00024', '10.193.199.122', 'root', 'Linux', 's01cq-VMWARE0050568f4921-test-app-199-122-msxf.host', '8.0', '8', '0.011460972278791082', '2750', null, '1.8.0_231', '2020-08-13 21:02:33');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00025', '192.168.30.141', 'EDZ', 'Windows 10', 'WINDOWS-2P5HEUN', '8.0', '8', '0.0', '68', null, '1.8.0_241', '2022-02-08 16:10:26');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00026', '192.168.88.217', 'root', 'Linux', 'ly', '4.0', '4', '0.026948990161156226', '756', null, '1.8.0_131', '2022-03-05 01:33:51');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00027', '192.168.88.217', 'root', 'Linux', 'ly', '4.0', '4', '0.026948990161156226', '756', null, '1.8.0_131', '2022-03-05 01:33:51');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00028', '192.168.88.218', 'root', 'Linux', 'mysql-slave', '4.0', '4', '0.002418648912142701', '760', null, '1.8.0_131', '2022-05-17 00:38:03');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00029', '192.168.88.219', 'root', 'Linux', 'bf', '4.0', '4', '0.02557418632090441', '761', null, '1.8.0_131', '2022-03-16 10:48:03');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00030', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008114882097314622', '737', null, '1.8.0_131', '2022-03-05 10:41:58');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00031', '192.168.88.222', 'root', 'Linux', 'bogon', '4.0', '4', '0.0062705886707410535', '756', null, '1.8.0_131', '2022-03-16 10:48:04');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00032', '192.168.88.223', 'root', 'Linux', 'bogon', '4.0', '4', '0.009325971631588615', '754', null, '1.8.0_131', '2022-03-16 10:48:03');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00033', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008119260707956611', '738', null, '1.8.0_131', '2022-03-05 10:56:35');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00034', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008108334541333284', '759', null, '1.8.0_131', '2022-03-05 12:29:47');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00035', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008113401358820107', '772', null, '1.8.0_131', '2022-03-05 12:33:48');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00036', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008112260237785215', '762', null, '1.8.0_131', '2022-03-05 13:18:01');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00037', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '1.0', '762', null, '1.8.0_131', '2022-03-05 13:18:01');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00038', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.00811670601678815', '766', null, '1.8.0_131', '2022-03-05 13:30:46');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00039', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008122556980208707', '762', null, '1.8.0_131', '2022-03-05 13:33:48');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00040', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008127617030163174', '756', null, '1.8.0_131', '2022-03-05 13:39:01');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00041', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '1.0', '756', null, '1.8.0_131', '2022-03-05 13:39:01');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00042', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008133404872758739', '757', null, '1.8.0_131', '2022-03-05 13:44:10');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00043', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008130150446617456', '742', null, '1.8.0_131', '2022-03-05 14:40:02');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00044', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008120114451778338', '759', null, '1.8.0_131', '2022-03-05 16:13:18');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00045', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008125796577118597', '752', null, '1.8.0_131', '2022-03-05 16:21:26');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00046', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.00813063532130013', '763', null, '1.8.0_131', '2022-03-05 16:30:45');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00047', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.008123393552754597', '761', null, '1.8.0_131', '2022-03-05 17:48:15');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00048', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.006389412176016937', '748', null, '1.8.0_131', '2022-03-16 10:35:07');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00049', '192.168.88.221', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.006398748760574592', '754', null, '1.8.0_131', '2022-03-16 10:48:04');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00050', '172.21.152.95', 'root', 'Linux', 'iZ2zejeb3epwqz3oz0emw6Z', '2.0', '2', '0.012841976860428515', '78', null, '1.8.0_241', '2022-03-19 10:56:15');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00051', '192.168.0.143', 'EDZ', 'Windows 10', 'WINDOWS-2P5HEUN', '8.0', '8', '1.0', '67', null, '1.8.0_241', '2022-06-29 10:01:11');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00052', '192.168.0.143', 'EDZ', 'Windows 10', 'WINDOWS-2P5HEUN', '8.0', '8', '0.0', '67', null, '1.8.0_241', '2022-06-29 10:03:03');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00053', '192.168.88.222', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '9.446991964487882E-4', '750', null, '1.8.0_131', '2022-06-29 15:35:43');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00054', '192.168.88.223', 'root', 'Linux', 'localhost', '4.0', '4', '8.965229183639349E-4', '779', null, '1.8.0_131', '2022-06-29 15:35:43');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00055', '192.168.88.222', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.0014148844512562177', '754', null, '1.8.0_131', '2022-06-30 09:32:02');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00056', '192.168.88.223', 'root', 'Linux', 'localhost', '4.0', '4', '0.001358623523561751', '755', null, '1.8.0_131', '2022-06-30 09:32:20');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00057', '192.168.88.222', 'root', 'Linux', 'localhost.localdomain', '4.0', '4', '0.0014802242032677425', '735', null, '1.8.0_131', '2022-06-30 09:34:45');
INSERT INTO `jmeter_perf_agent_source` VALUES ('00058', '192.168.88.223', 'root', 'Linux', 'localhost', '4.0', '4', '0.0014257317325045739', '759', null, '1.8.0_131', '2022-06-30 09:34:58');

-- ----------------------------
-- Table structure for `jmeter_perf_run_ip`
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

-- ----------------------------
-- Records of jmeter_perf_run_ip
-- ----------------------------
INSERT INTO `jmeter_perf_run_ip` VALUES ('00001', '127.0.0.1', '294332968@qq.com', '294332968@qq.com', '294332968@qq.com', '0');

-- ----------------------------
-- Table structure for `jmeter_perf_top5_errors`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jmeter_perf_top5_errors
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_perfor_current_report`
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
-- Records of jmeter_perfor_current_report
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_perfor_history_report`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jmeter_perfor_history_report
-- ----------------------------

-- ----------------------------
-- Table structure for `jmeter_runip_states`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jmeter_runip_states
-- ----------------------------

-- ----------------------------
-- Table structure for `mock_config`
-- ----------------------------
DROP TABLE IF EXISTS `mock_config`;
CREATE TABLE `mock_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mock_name` varchar(500) DEFAULT NULL COMMENT 'mock名称',
  `request_type` varchar(500) DEFAULT NULL COMMENT '请求类型',
  `mock_url` varchar(500) DEFAULT NULL COMMENT 'mock的url地址',
  `prefix_option` varchar(200) DEFAULT NULL COMMENT '前置操作',
  `config_rules` varchar(200) DEFAULT NULL COMMENT '配置规则',
  `weight` varchar(4) NOT NULL DEFAULT '1' COMMENT '权重',
  `result_type` varchar(20) DEFAULT NULL COMMENT '返回结果类型',
  `labe` varchar(100) DEFAULT NULL COMMENT '标签',
  `other_mock_service` varchar(200) DEFAULT NULL COMMENT '转向其它的mock服务',
  `mock_result` varchar(9000) DEFAULT NULL COMMENT 'mock返回结果',
  `tag` varchar(2) NOT NULL DEFAULT '0' COMMENT '是否mock服务可用,0可用,1不可用',
  `timeout` varchar(6) NOT NULL DEFAULT '0' COMMENT '超时毫秒',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mock_config
-- ----------------------------
INSERT INTO `mock_config` VALUES ('1', 'mock', 'http://', '/a/b/c', '', 'username=100,gender=男', '1', 'JSON', '1', '1', '{ \"name\":\"runoob\", \"alexa\":10000, \"site\":null }', '0', '1', '2022-05-23 21:17:53', '2022-05-23 21:17:53');
INSERT INTO `mock_config` VALUES ('2', 'mock2', 'http://', '/a/b/c', 'x=\"彩虹车\",y=\"美丽风景图\"', 'name=小明', '7', 'JSON', '1', '1', '{\n    \"sites\": [\n    { \"name\":\"菜鸟教程\" , \"url\":\"www.runoob.com\" }, \n    { \"name\":\"google\" , \"url\":\"www.google.com\" }, \n    { \"name\":\"小明\" , \"url\":\"www.weibo.com\" },\n     {\"result\":$x},\n    {\"风景\":$y}\n    ]\n}', '0', '20', '2022-06-24 17:26:34', '2022-06-24 17:26:34');
INSERT INTO `mock_config` VALUES ('3', 'mock3', 'http://', '/a/b/c', 'a=\"中国人人\",y=\"xxxx\"', 'name=小明', '6', 'JSON', '1', '1', '{\n    \"name\":\"明天上班\",\n    \"url\":\"www.runoob.com\", \n    \"slogan\":\"学的不仅是技术，更是梦想！\",\n     \"result\": $a,\n     \"result\": $y\n}', '0', '1', '2022-06-24 16:53:50', '2022-06-24 16:53:50');
INSERT INTO `mock_config` VALUES ('4', 'mock_from', 'http://', '/a/b/c', 'y=\"mock测试\"', 'name=form小晨', '4', 'JSON', '', '1', '{\n  \"form小晨\": {\n    \"site\": [\n      {\n        \"id\": \"1\",\n        \"name\": $y,\n        \"url\": \"www.runoob.com\"\n      },\n      {\n        \"id\": \"2\",\n        \"name\": \"form小晨1\",\n        \"url\": \"c.runoob.com\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"Google\",\n        \"url\": \"www.google.com\"\n      }\n    ]\n  }\n}', '0', '1', '2022-05-30 14:59:02', '2022-05-30 14:59:02');
INSERT INTO `mock_config` VALUES ('5', 'mock_4', 'http://', '/a/b/c', '', '', '1', 'JSON', '1', '1', '{\n\"result\":\"sucess\"\n\n}', '0', '2', '2022-05-29 18:06:30', '2022-05-29 18:06:30');
INSERT INTO `mock_config` VALUES ('6', 'mock_from_2', 'http://', '/a/b/c', 'y=\"我才不相信呢\"', 'name=form小晨', '8', 'JSON', '', '1', '{\"form小晨\":{\"site\":[{\"id\":\"1\",\"name\":$y,\"url\":\"www.runoob.com\"}]}}', '0', '1', '2022-05-30 23:30:02', '2022-05-30 23:30:02');
INSERT INTO `mock_config` VALUES ('7', 'test', 'http://', '/a/b/e', '', '', '1', 'JSON', '', '', '{\n\"name\":\"runoob\",\n\"alexa\":10000,\n\"sites\":{\n\"site1\":\"www.runoob.com\",\n\"site2\":\"m.runoob.com\",\n\"site3\":\"c.runoob.com\"\n}\n}', '0', '0', '2022-06-24 11:33:42', '2022-06-24 11:33:42');
INSERT INTO `mock_config` VALUES ('8', 'teset2', 'http://', '/a/b/c/e/d', '', '', '1', 'JSON', '', '', '{\n\"name\":\"网站\",\n\"num\":3,\n\"sites\":[\"Google\",\"Runoob\",\"Taobao\"]\n}', '0', '0', '2022-06-24 16:45:58', '2022-06-24 16:45:58');

-- ----------------------------
-- Table structure for `msjr_overpayment`
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
-- Records of msjr_overpayment
-- ----------------------------
INSERT INTO `msjr_overpayment` VALUES ('1', 'http://10.250.120.141:8888', '10.259.120.139', '{\r\n    \"SERVICE\":{\r\n        \"SERVICE_HEADER\":{\r\n            \"VERSION_ID\":\"01\",\r\n            \"SERVICE_ID\":\"TFDVirtualCardAuth\",\r\n            \"ORG\":\"000000000001\",\r\n            \"SERVICESN\":\"BJWN21911910930\",\r\n            \"CHANNEL_ID\":\"BANK\",\r\n            \"SUB_TERMINAL_TYPE\":\"APP\",\r\n            \"ACQ_ID\":\"10000000\",\r\n            \"OP_ID\":\"hexu\",\r\n            \"REQUEST_TIME\":\"20250131202005\",\r\n            \"MAC\":\"\",\r\n            \"UUID\":\"6adcda8fd775417c869481a9a4b885c1\"\r\n        },\r\n        \"SERVICE_BODY\":{\r\n            \"REQUEST\":{\r\n                \"CONTR_NBR\":\"510424111700000027\",\r\n                \"TXN_TYPE\":\"Credit\",\r\n                \"TXN_DIRECTION\":\"Normal\",\r\n                \"TXN_TERMINAL_TYPE\":\"APP\",\r\n                \"TXN_AMT\":\"500\",\r\n                \"CASH_PURPOSE\":\"E\",\r\n                \"TXN_CURRENCY\":\"156\",\r\n                \"APP_SOURCE\":\"51000004\",\r\n                \"PASSWORD_DATA\":\"123\",\r\n                \"OWNING_BRANCH\":\"000000000\",\r\n                \"COOPERATIVE_ID\":\"00130000\",\r\n                \"ORIG_SERVICESN\":\"BJWN21911910930\",\r\n                \"ORIG_REF_NBR\":\"BJWN21911910930\",\r\n                \"AUTH_CODE\":\"000139\",\r\n                \"LOAN_TERM\":\"3\",\r\n                \"DB_CR_IND\":\"\",\r\n                \"MCC\":\"6010\",\r\n                \"MCH_ID\":\"1122334455667\",\r\n                \"MCH_NAME\":\"天坛营业部建行1\",\r\n                \"AUTH_TXN_TERMINAL\":\"00800123\",\r\n                \"MANUAL_AUTH_FLAG\":\"\",\r\n                \"MESSAGE_TYPE_ID\":\"0200\",\r\n                \"PROC_CODE\":\"010000\",\r\n                \"TRANS_TIME\":\"0820151801\",\r\n                \"SYS_TRACE_NBR\":\"201092\",\r\n                \"CARD_EXP_DATE\":\"\",\r\n                \"SETTLE_DATE\":\"0820\",\r\n                \"CAP_DATE\":\"0820\",\r\n                \"POINT_SERVICE_CODE\":\"012\",\r\n                \"CARD_SEQ_NBR\":\"\",\r\n                \"SERVICE_PIN_CODE\":\"\",\r\n                \"TXN_FEE\":\"0\",\r\n                \"TRACK_2_DATA\":\"\",\r\n                \"REF_NBR\":\"\",\r\n                \"CARD_ACPT_TERMINAL_ID\":\"00800123\",\r\n                \"CARD_ACPT_ID_CODE\":\"123456123456000\",\r\n                \"CARD_ACPT_NAME_LOCATION\":\"滴滴出行\",\r\n                \"PIN_DATA\":\"123\",\r\n                \"IC_DATA\":\"\",\r\n                \"ID_NBR\":\"\",\r\n                \"ORGIN_DATA\":\"020020109208051518010001000000000099999999\"\r\n            }\r\n        }\r\n    }\r\n}', '10.259.120.141', null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `perf_config`
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
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of perf_config
-- ----------------------------
INSERT INTO `perf_config` VALUES ('1', '[1]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', '2ac0c749-4', 'start');
INSERT INTO `perf_config` VALUES ('2', '[1]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', '9638e1a8-a', 'start');
INSERT INTO `perf_config` VALUES ('3', '[1]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', 'ae65c9bf-1', 'start');
INSERT INTO `perf_config` VALUES ('4', '[1]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', 'd23cf27f-e', 'start');
INSERT INTO `perf_config` VALUES ('5', '[1]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', '43b560ed-7', 'start');
INSERT INTO `perf_config` VALUES ('6', '[1, 2, 3]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', 'ba8012b5-4', 'start');
INSERT INTO `perf_config` VALUES ('7', '[1, 2, 3]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', 'bae5df61-b', 'start');
INSERT INTO `perf_config` VALUES ('8', '[1, 2, 3]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', '253cc430-a', 'start');
INSERT INTO `perf_config` VALUES ('9', '[1, 2, 3]', '[1, 0]', '[0]', '[No, 1]', '[No, 30]', '[No]', '[Yes]', '[No]', '[No]', 'd2be5a24-3', 'start');


-- ----------------------------
-- Table structure for `report_infor`
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
-- Records of report_infor
-- ----------------------------

-- ----------------------------
-- Table structure for `statistics`
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
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of statistics
-- ----------------------------


-- ----------------------------
-- Table structure for `upload_info`
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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of upload_info
-- ----------------------------


-- ----------------------------
-- Table structure for `user`
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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'dagui.deng@xxx.com', '0');
INSERT INTO `user` VALUES ('2', 'dengdagui', 'e10adc3949ba59abbe56e057f20f883e', 'dagui.deng@xxx.com', '0');
