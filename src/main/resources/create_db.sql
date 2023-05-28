DROP DATABASE IF EXISTS taskgrid;
    CREATE SCHEMA taskgrid;
DROP TABLE IF EXISTS taskgrid.users;
DROP TABLE IF EXISTS taskgrid.user_info;
DROP TABLE IF EXISTS taskgrid.projects;
DROP TABLE IF EXISTS taskgrid.user_project;
DROP TABLE IF EXISTS taskgrid.board;
DROP TABLE IF EXISTS taskgrid.tasks;

CREATE TABLE taskgrid.users (
                                `user_id` int(11) NOT NULL AUTO_INCREMENT,
                                `username` varchar(45) NOT NULL,
                                `user_email` varchar(45) NOT NULL,
                                `user_password` varchar(200) NOT NULL,
                                PRIMARY KEY (`user_id`),
                                UNIQUE KEY `username_UNIQUE` (`username`),
                                UNIQUE KEY `user_email_UNIQUE` (`user_email`));

CREATE TABLE taskgrid.user_info (
                             `user_id` int(11) NOT NULL,
                             `first_name` varchar(45) NOT NULL,
                             `last_name` varchar(45) NOT NULL,
                             `job_title` varchar(45) NOT NULL,
                             KEY `info_user_id_idx` (`user_id`),
                             CONSTRAINT `info_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION);

CREATE TABLE taskgrid.projects (
                                   `project_id` int(11) NOT NULL AUTO_INCREMENT,
                                   `project_name` varchar(45) NOT NULL,
                                   `project_description` varchar(150) DEFAULT NULL,
                                   PRIMARY KEY (`project_id`));

CREATE TABLE taskgrid.user_project (
                                       `user_id` int(11) NOT NULL,
                                       `project_id` int(11) NOT NULL,
                                       `user_position` varchar(45) NOT NULL,
                                       PRIMARY KEY (`user_id`,`project_id`),
                                       KEY `up_project_id_idx` (`project_id`),
                                       CONSTRAINT `up_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
                                       CONSTRAINT `up_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION);

CREATE TABLE taskgrid.boards (
                                 `board_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `project_id` int(11) NOT NULL,
                                 `board_name` varchar(45) NOT NULL,
                                 `start_date` date NOT NULL,
                                 `end_date` date NOT NULL,
                                 PRIMARY KEY (`board_id`),
                                 KEY `project_id_idx` (`project_id`),
                                 CONSTRAINT `project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE ON UPDATE NO ACTION);

CREATE TABLE taskgrid.tasks (
                                `task_id` int(11) NOT NULL AUTO_INCREMENT,
                                `task_name` varchar(45) NOT NULL,
                                `task_description` varchar(45) DEFAULT NULL,
                                `task_status` varchar(45) NOT NULL,
                                `user_id` int(11) DEFAULT NULL,
                                `board_id` int(11) NOT NULL,
                                `story_points` int(11) NOT NULL,
                                `task_time` int(11) DEFAULT NULL,
                                PRIMARY KEY (`task_id`),
                                KEY `user_id_idx` (`user_id`),
                                KEY `board_id_idx` (`board_id`),
                                CONSTRAINT `board_id` FOREIGN KEY (`board_id`) REFERENCES `boards` (`board_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
                                CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION);



