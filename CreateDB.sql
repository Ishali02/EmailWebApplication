CREATE SCHEMA `email_app` ;

USE email_app;


CREATE TABLE `user` (
  `user_id` varchar(10) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_emailId` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_emailId_UNIQUE` (`user_emailId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `role` (
  `role_id` varchar(10) NOT NULL,
  `role_name` varchar(100) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_role` (
  `user_role_id` varchar(45) NOT NULL,
  `user_id` varchar(10) NOT NULL,
  `role_id` varchar(10) NOT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `ur_user_id_fk_idx` (`user_id`),
  KEY `ur_role_id_fk_idx` (`role_id`),
  CONSTRAINT `ur_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `ur_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_staging` (
  `user_staging_id` varchar(10) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_emailId` varchar(100) NOT NULL,
  PRIMARY KEY (`user_staging_id`),
  UNIQUE KEY `user_emailId_UNIQUE` (`user_emailId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `software` (
  `sw_id` varchar(10) NOT NULL,
  `sw_name` varchar(100) NOT NULL,
  `sw_description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`sw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `request` (
  `request_id` varchar(10) NOT NULL,
  `user_id` varchar(10) NOT NULL,
  `manager_id` varchar(10) NOT NULL,
  `it_team_id` varchar(10) NOT NULL,
  `software_team_id` varchar(10) NOT NULL,
  `sw_id` varchar(10) NOT NULL,
  `req_date` datetime NOT NULL,
  `req_status` varchar(1) NOT NULL,
  `req_approved` varchar(1) NOT NULL,
  PRIMARY KEY (`request_id`),
  KEY `r_user_id_fk_idx` (`user_id`),
  KEY `r_manager_id_fk_idx` (`manager_id`),
  KEY `r_it_team_id_fk_idx` (`it_team_id`),
  KEY `r_software_team_id_fk_idx` (`software_team_id`),
  KEY `r_sw_id_fk_idx` (`sw_id`),
  CONSTRAINT `r_it_team_id_fk` FOREIGN KEY (`it_team_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `r_manager_id_fk` FOREIGN KEY (`manager_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `r_software_team_id_fk` FOREIGN KEY (`software_team_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `r_sw_id_fk` FOREIGN KEY (`sw_id`) REFERENCES `software` (`sw_id`),
  CONSTRAINT `r_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `id_control` (
  `table_name` varchar(50) NOT NULL,
  `id_prefix` varchar(5) NOT NULL,
  `id_number` int NOT NULL,
  `id_length` int NOT NULL,
  `id` varchar(20) NOT NULL,
  PRIMARY KEY (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `email_app`.`role` (`role_id`, `role_name`) VALUES ('R001', 'Manager');
INSERT INTO `email_app`.`role` (`role_id`, `role_name`) VALUES ('R002', 'IT_Team');
INSERT INTO `email_app`.`role` (`role_id`, `role_name`) VALUES ('R003', 'Software_Team');
INSERT INTO `email_app`.`role` (`role_id`, `role_name`) VALUES ('R004', 'Employee');


INSERT INTO `email_app`.`software` (`sw_id`, `sw_name`) VALUES ('SW0001', 'JDK');
INSERT INTO `email_app`.`software` (`sw_id`, `sw_name`) VALUES ('SW0002', 'JRE');
INSERT INTO `email_app`.`software` (`sw_id`, `sw_name`) VALUES ('SW0003', 'Python');
INSERT INTO `email_app`.`software` (`sw_id`, `sw_name`) VALUES ('SW0004', 'C++');


INSERT INTO `email_app`.`user` (`user_id`, `user_name`, `user_emailId`, `user_password`) VALUES ('U0001', 'Sam Kumar', 'testuser.samkumar@gmail.com', 'SamKumar123');
INSERT INTO `email_app`.`user` (`user_id`, `user_name`, `user_emailId`, `user_password`) VALUES ('U0002', 'Shamie', 'testuser.shamie@gmail.com', 'Shamie123');
INSERT INTO `email_app`.`user` (`user_id`, `user_name`, `user_emailId`, `user_password`) VALUES ('U0003', 'Ying Ying', 'testuser.yingying@gmail.com', 'YingYing123');
INSERT INTO `email_app`.`user` (`user_id`, `user_name`, `user_emailId`, `user_password`) VALUES ('U0004', 'Suraj Jadhav', 'testuser.surajjadhav@gmail.com', 'SurajJadhav123');
INSERT INTO `email_app`.`user` (`user_id`, `user_name`, `user_emailId`, `user_password`) VALUES ('U0005', 'Hrithik Babu', 'testuser.hrithikbabu@gmail.com', 'HrithikBabu123');


INSERT INTO `email_app`.`user_role` (`user_role_id`, `user_id`, `role_id`) VALUES ('UR00001', 'U0001', 'R001');
INSERT INTO `email_app`.`user_role` (`user_role_id`, `user_id`, `role_id`) VALUES ('UR00002', 'U0002', 'R001');
INSERT INTO `email_app`.`user_role` (`user_role_id`, `user_id`, `role_id`) VALUES ('UR00003', 'U0003', 'R002');
INSERT INTO `email_app`.`user_role` (`user_role_id`, `user_id`, `role_id`) VALUES ('UR00004', 'U0004', 'R002');
INSERT INTO `email_app`.`user_role` (`user_role_id`, `user_id`, `role_id`) VALUES ('UR00005', 'U0005', 'R003');


INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('user', 'U', '5', '4', 'U0005');
INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('role', 'R', '4', '3', 'R004');
INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('user_staging', 'US', '0', '4', 'US0000');
INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('software', 'SW', '4', '4', 'SW0004');
INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('request', 'REQ', '0', '6', 'REQ000000');
INSERT INTO `email_app`.`id_control` (`table_name`, `id_prefix`, `id_number`, `id_length`, `id`) VALUES ('user_role', 'UR', '5', '5', 'UR00005');




SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `Get_Next_ID`(
In_Table_Name varchar(50)) RETURNS varchar(20) CHARSET utf8
BEGIN

DECLARE Var_ID_Prefix VARCHAR(5);
DECLARE Var_ID_Number INT ;
DECLARE Var_ID_Length INT DEFAULT 0;
DECLARE Var_ID VARCHAR(20) DEFAULT null;
DECLARE OUT_Id VARCHAR(20);

    -- get keys for selected table from id_control
SELECT
`ID_Prefix`, `ID_Number`, `ID_Length`, `ID`
INTO Var_ID_Prefix , Var_ID_Number , Var_ID_Length , Var_ID FROM
id_control
WHERE
Table_Name = In_Table_Name;

-- Increment ID_Number by 1
SET Var_ID_Number = Var_ID_Number + 1;

-- Generate Id using Prefix + Incremental Id with padding "0"s
SET Out_ID = CONCAT(Var_ID_Prefix,LPAD(Var_ID_Number,Var_ID_Length,'0'));

-- Update id_control with new Id
UPDATE id_control
SET
ID_Number = Var_ID_Number,
ID = Out_ID
WHERE
Table_Name = In_Table_Name;        
   
RETURN (Out_ID);

END$$
DELIMITER ;