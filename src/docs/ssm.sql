1、项目初始化SQL 2017年11月20日
CREATE TABLE `sys_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL COMMENT '用户名',
	`password` VARCHAR(50) NOT NULL COMMENT '密码',
	`phone_no` VARCHAR(50) NULL DEFAULT NULL COMMENT '手机号',
	`remark` VARCHAR(50) NULL DEFAULT NULL COMMENT '备注',
	`email` VARCHAR(50) NULL DEFAULT NULL,
	`is_valid` CHAR(1) NOT NULL DEFAULT 'T' COMMENT '是否有效，T：有效、F：无效',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`last_login` DATETIME NULL DEFAULT NULL COMMENT '最后登录时间',
	`last_login_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT '最后登录IP',
	`is_locked` CHAR(1) NOT NULL DEFAULT 'F' COMMENT '是否锁定，T：已锁定、F：未锁定',
	PRIMARY KEY (`id`)
)
COMMENT='系统后台用户'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('zain', '4d1665f2ed248790cc8690f3d34a47ab', NULL, '雍工', 'zain@flyingwings.cn', 'T', '2016-01-07 14:25:48', '2016-07-17 14:20:14', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('minjs', '336c1c2b042a762b06a4762d14552ede', NULL, '闵济松', 'minjs@flyingwings.cn', 'T', '2016-06-07 14:25:48', '2016-07-27 14:20:14', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('devin', 'ac0034b238f5be51b7168ceb6acaaef7', NULL, '丁文武', 'devin@flyingwings.cn', 'T', '2016-02-07 14:25:48', '2016-07-07 14:20:14', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('audit', '82290b52822088638a34bf37b9e7946a', NULL, '审计员', 'audit@flyingwings.cn', 'F', '2016-03-17 14:25:48', '2016-07-23 21:17:04', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('doom', '78496a34800f778d2b7ca46e25546504', NULL, '刘洋', 'doom@flyingwings.cn', 'F', '2016-01-22 14:25:48', '2016-07-07 14:17:15', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('chris', '8ccb0a1b4209616ad379fa91766989bf', NULL, '谢雨婷', 'chris@flyingwings.cn', 'F', '2016-06-24 14:25:48', '2016-07-07 14:25:48', '192.168.1.1', 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('lewis', '03c1ad3e156d397b4f745d945d3bd587', NULL, '刘国庆', 'gq.liu@flyingwings.cn', 'T', '2017-02-23 14:58:27', NULL, NULL, 'F');
INSERT INTO `sys_user` (`username`, `password`, `phone_no`, `remark`, `email`, `is_valid`, `create_time`, `last_login`, `last_login_ip`, `is_locked`) VALUES ('sxzhang', '1e07d95f6a824796720590a46bf847e9', NULL, '章少秀', 'sx.zhang@flyingwings.cn', 'T', '2017-06-27 16:03:41', NULL, NULL, 'F');
