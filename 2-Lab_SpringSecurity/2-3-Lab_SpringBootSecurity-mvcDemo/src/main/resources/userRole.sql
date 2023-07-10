CREATE TABLE `t_user_role`
(
    `user_id`     varchar(32) NOT NULL,
    `role_id`     varchar(32) NOT NULL,
    `create_time` datetime     DEFAULT NULL,
    `creator`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

insert into `t_user_role`(`user_id`,`role_id`,`create_time`,`creator`) values ('1','1',NULL,NULL);