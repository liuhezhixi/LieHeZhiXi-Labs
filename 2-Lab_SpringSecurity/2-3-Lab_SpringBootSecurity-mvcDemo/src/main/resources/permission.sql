CREATE TABLE `t_permission`
(
    `id`          varchar(32) NOT NULL,
    `code`        varchar(32) NOT NULL COMMENT '权限标识符',
    `description` varchar(64)  DEFAULT NULL COMMENT '描述',
    `url`         varchar(128) DEFAULT NULL COMMENT '请求地址',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


insert into `t_permission`(`id`,`code`,`description`,`url`) values ('1','p1','测试资源 1','/r/r1'),('2','p3','测试资源2','/r/r2');