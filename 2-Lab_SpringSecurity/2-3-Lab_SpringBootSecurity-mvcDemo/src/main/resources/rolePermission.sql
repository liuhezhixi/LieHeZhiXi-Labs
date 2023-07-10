CREATE TABLE `t_role_permission`
(
    `role_id`       varchar(32) NOT NULL,
    `permission_id` varchar(32) NOT NULL,
    PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

insert into `t_role_permission`(`role_id`,`permission_id`) values ('1','1'),('1','2');