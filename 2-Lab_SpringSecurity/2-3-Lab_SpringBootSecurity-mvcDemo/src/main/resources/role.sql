CREATE TABLE `t_role`
(
    `id`          varchar(32) NOT NULL,
    `role_name`   varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `status`      char(1)     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

insert into `t_role`(`id`,`role_name`,`description`,`create_time`,`update_time`,`status`) values ('1','管理员',NULL,NULL,NULL,'');