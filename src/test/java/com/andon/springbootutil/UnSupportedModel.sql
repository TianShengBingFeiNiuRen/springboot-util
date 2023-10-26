-- 原始表
DROP TABLE IF EXISTS `permanent_population`;
CREATE TABLE `permanent_population`
(
    `id_card`       varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`          varchar(10) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '姓名',
    `sex`           varchar(10) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '性别',
    `age`           int                                     DEFAULT NULL COMMENT '年龄',
    `address`       varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '住址',
    `health_status` varchar(10) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '健康状况：（健康/重残/死亡）',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='常驻人口';

DROP TABLE IF EXISTS `guardian`;
CREATE TABLE `guardian`
(
    `id_card`            varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `id_card_guardian_1` varchar(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监护人一身份证号',
    `id_card_guardian_2` varchar(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监护人二身份证号',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='监护人';

DROP TABLE IF EXISTS `phone_info`;
CREATE TABLE `phone_info`
(
    `id_card`    varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`       varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `sex`        varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
    `age`        int                                    DEFAULT NULL COMMENT '年龄',
    `phone_num`  varchar(11) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
    `is_connect` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '近六个月是否有通联',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='手机通联信息';

DROP TABLE IF EXISTS `prison_detainees`;
CREATE TABLE `prison_detainees`
(
    `id_card` varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`    varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `sex`     varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
    `age`     int                                    DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='监狱在押人员';

DROP TABLE IF EXISTS `drug_abuser`;
CREATE TABLE `drug_abuser`
(
    `id_card` varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`    varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `sex`     varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
    `age`     int                                    DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='戒毒所人员';

-- 中间表
DROP TABLE IF EXISTS `children_info`;
CREATE TABLE `children_info`
(
    `id_card` varchar(18) COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`    varchar(10) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '姓名',
    `sex`     varchar(10) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '性别',
    `age`     int                                     DEFAULT NULL COMMENT '年龄',
    `address` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '住址',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='未成年信息';

DROP TABLE IF EXISTS `guardian_status_info`;
CREATE TABLE `guardian_status_info`
(
    `id_card`                      varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监护人身份证号',
    `name`                         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '监护人身份证号姓名',
    `sex`                          varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '监护人身份证号性别',
    `age`                          int                                                           DEFAULT NULL COMMENT '监护人身份证号年龄',
    `address`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监护人身份证号住址',
    `health_status`                varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '监护人健康状况：（健康/重残/死亡）',
    `is_in_prison`                 varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT '0' COMMENT '监护人是否监狱在押：1（是）0（否）',
    `is_compulsory_detoxification` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT '0' COMMENT '监护人是否强制戒毒：1（是）0（否）',
    `is_loss_of_contact`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT '0' COMMENT '监护人是否失联：1（是）0（否）',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='监护人状况信息';

DROP TABLE IF EXISTS `guardian_up_supported`;
CREATE TABLE `guardian_up_supported`
(
    `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `sex`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
    `age`     int                                                          DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='无抚养条件监护人信息';

-- 结果表
DROP TABLE IF EXISTS `children_un_supported`;
CREATE TABLE `children_un_supported`
(
    `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '姓名',
    `sex`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '性别',
    `age`     int                                                           DEFAULT NULL COMMENT '年龄',
    `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '住址',
    PRIMARY KEY (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='无人抚养儿童信息';


-- 模型
-- 过滤 `全省常驻人口库` 18周岁以下未成年人，补充 `未成年信息表`
INSERT INTO `children_info`(`id_card`,`name`,`sex`,`age`,`address`) SELECT `id_card`,`name`,`sex`,`age`,`address` FROM `permanent_population` WHERE `age`<=18;

-- 联合查询，补充 `监护人状况信息表`监护人信息 `健康状况`（健康/重残/死亡）、`是否监狱在押`、`是否强制戒毒`、`是否失联`
INSERT INTO `guardian_status_info`(`id_card`,`name`,`sex`,`age`,`address`,`health_status`,`is_in_prison`,`is_compulsory_detoxification`,`is_loss_of_contact`) SELECT `p`.`id_card`,`p`.`name`,`p`.`sex`,`p`.`age`,`address`,`health_status`,IF(`pd`.`id_card` IS NULL,'0','1') AS `is_in_prison`,IF(`da`.`id_card` IS NULL,'0','1') AS `is_compulsory_detoxification`,IF(`pi`.`is_connect`='是','0','1') AS `is_loss_of_contact` FROM `permanent_population`AS`p` LEFT JOIN `prison_detainees`AS`pd` ON `p`.`id_card`=`pd`.`id_card` LEFT JOIN `drug_abuser`AS`da` ON `p`.`id_card`=`da`.`id_card` LEFT JOIN `phone_info`AS`pi` ON `p`.`id_card`=`pi`.`id_card` WHERE p.`age`>18;

-- 查询 `监护人状况信息表` 监护人 `是否满足无抚养条件`，满足以下任意条件，则标记为 `无抚养条件` 则写入 `无抚养条件监护人信息表`
-- 1. `健康状况` : 重残或死亡
-- 2. `是否监狱在押` : 是
-- 3. `是否强制戒毒` : 是
-- 4. `是否失联` : 是
INSERT INTO `guardian_up_supported`(`id_card`,`name`,`sex`,`age`) SELECT `id_card`,`name`,`sex`,`age` FROM `guardian_status_info` WHERE `health_status`='重残' OR `health_status`='死亡' OR `is_in_prison`='1' OR `is_compulsory_detoxification`='1' OR `is_loss_of_contact`='1';

-- 联合查询 `未成年信息表`、`监护人信息库`、`监护人状况信息表`，监护人一和监护人二均满足 `无抚养条件`，则将 `未成年人信息` 写入结果表 `无人抚养儿童信息表`
INSERT INTO `children_un_supported`(`id_card`,`name`,`sex`,`age`,`address`) SELECT `id_card`,`name`,`sex`,`age`,`address` FROM `children_info` WHERE `id_card` IN (SELECT `id_card` FROM `guardian` WHERE `id_card_guardian_1` IN (SELECT `id_card` FROM `guardian_up_supported`) AND `id_card_guardian_2` IN (SELECT `id_card` FROM `guardian_up_supported`));