-- 原始表
DROP TABLE IF EXISTS `social_security_info`;
CREATE TABLE `social_security_info`
(
    `id_no`              varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`               varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `id_code`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '证件类型',
    `last_unit_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前缴存单位名称',
    `last_unit_kind`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前缴存单位性质',
    `status`             varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前账户状态',
    `yangl_base`         decimal(11, 2)                                               DEFAULT NULL COMMENT '社保缴费基数',
    `open_data`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社保账户开户年月日',
    `yangl_security_cat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）参保性质',
    `yangl_last_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）最近缴存年月日',
    `yangl_first_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）初始缴存年月日',
    `yangl_month_base`   decimal(11, 2)                                               DEFAULT NULL COMMENT '（养老保险）月缴存额',
    `yangl_own`          decimal(11, 2)                                               DEFAULT NULL COMMENT '（养老保险）个人月缴费额',
    `yangl_unit`         decimal(11, 2)                                               DEFAULT NULL COMMENT '（养老保险）单位月缴费额',
    `yangl_ctms`         int                                                          DEFAULT NULL COMMENT '（养老保险）最近连续缴存次数',
    `yangl_tms`          int                                                          DEFAULT NULL COMMENT '（养老保险）累计缴存次数',
    `yul_security_cat`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）参保性质',
    `yul_last_date`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）最近缴存年月日',
    `yul_first_date`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）初始缴存年月日',
    `yul_month_base`     decimal(11, 2)                                               DEFAULT NULL COMMENT '（医疗保险）月缴存额',
    `yul_own`            decimal(11, 2)                                               DEFAULT NULL COMMENT '（医疗保险）个人月缴费额',
    `yul_unit`           decimal(11, 2)                                               DEFAULT NULL COMMENT '（医疗保险）单位月缴费额',
    `yul_ctms`           int                                                          DEFAULT NULL COMMENT '（医疗保险）最近连续缴存次数',
    `yul_tms`            int                                                          DEFAULT NULL COMMENT '（医疗保险）累计缴存次数',
    `gongs_last_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（工伤保险）最近缴存年月日',
    `gongs_first_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（工伤保险）初始缴存年月日',
    `gongs_month_base`   decimal(11, 2)                                               DEFAULT NULL COMMENT '（工伤保险）月缴存额',
    `gongs_own`          decimal(11, 2)                                               DEFAULT NULL COMMENT '（工伤保险）个人月缴费额',
    `gongs_unit`         decimal(11, 2)                                               DEFAULT NULL COMMENT '（工伤保险）单位月缴费额',
    `gongs_ctms`         int                                                          DEFAULT NULL COMMENT '（工伤保险）最近连续缴存次数',
    `gongs_tms`          int                                                          DEFAULT NULL COMMENT '（工伤保险）累计缴存次数',
    `shiy_last_date`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（失业保险）最近缴存年月日',
    `shiy_first_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（失业保险）初始缴存年月日',
    `shiy_month_base`    decimal(11, 2)                                               DEFAULT NULL COMMENT '（失业保险）月缴存额',
    `shiy_own`           decimal(11, 2)                                               DEFAULT NULL COMMENT '（失业保险）个人月缴费额',
    `shiy_unit`          decimal(11, 2)                                               DEFAULT NULL COMMENT '（失业保险）单位月缴费额',
    `shiy_ctms`          int                                                          DEFAULT NULL COMMENT '（失业保险）最近连续缴存次数',
    `shiy_tms`           int                                                          DEFAULT NULL COMMENT '（失业保险）累计缴存次数',
    `shengy_last_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（生育保险）最近缴存年月日',
    `shengy_first_date`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（生育保险）初始缴存年月日',
    `shengy_month_base`  decimal(11, 2)                                               DEFAULT NULL COMMENT '（生育保险）月缴存额',
    `shengy_own`         decimal(11, 2)                                               DEFAULT NULL COMMENT '（生育保险）个人月缴费额',
    `shengy_unit`        decimal(11, 2)                                               DEFAULT NULL COMMENT '（生育保险）单位月缴费额',
    `shengy_ctms`        int                                                          DEFAULT NULL COMMENT '（生育保险）最近连续缴存次数',
    `shengy_tms`         int                                                          DEFAULT NULL COMMENT '（生育保险）累计缴存次数',
    PRIMARY KEY (`id_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='个人社保缴费信息';

-- 结果表
DROP TABLE IF EXISTS `result_social_security_info`;
CREATE TABLE `result_social_security_info`
(
    `id_no`              varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `name`               varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
    `id_code`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '证件类型',
    `last_unit_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前缴存单位名称',
    `last_unit_kind`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前缴存单位性质',
    `status`             varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前账户状态',
    `yangl_base`         int                                                          DEFAULT NULL COMMENT '社保缴费基数',
    `open_data`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社保账户开户年月日',
    `yangl_security_cat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）参保性质',
    `yangl_last_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）最近缴存年月日',
    `yangl_first_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（养老保险）初始缴存年月日',
    `yangl_month_base`   int                                                          DEFAULT NULL COMMENT '（养老保险）月缴存额',
    `yangl_own`          int                                                          DEFAULT NULL COMMENT '（养老保险）个人月缴费额',
    `yangl_unit`         int                                                          DEFAULT NULL COMMENT '（养老保险）单位月缴费额',
    `yangl_ctms`         int                                                          DEFAULT NULL COMMENT '（养老保险）最近连续缴存次数',
    `yangl_tms`          int                                                          DEFAULT NULL COMMENT '（养老保险）累计缴存次数',
    `yul_security_cat`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）参保性质',
    `yul_last_date`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）最近缴存年月日',
    `yul_first_date`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（医疗保险）初始缴存年月日',
    `yul_month_base`     int                                                          DEFAULT NULL COMMENT '（医疗保险）月缴存额',
    `yul_own`            int                                                          DEFAULT NULL COMMENT '（医疗保险）个人月缴费额',
    `yul_unit`           int                                                          DEFAULT NULL COMMENT '（医疗保险）单位月缴费额',
    `yul_ctms`           int                                                          DEFAULT NULL COMMENT '（医疗保险）最近连续缴存次数',
    `yul_tms`            int                                                          DEFAULT NULL COMMENT '（医疗保险）累计缴存次数',
    `gongs_last_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（工伤保险）最近缴存年月日',
    `gongs_first_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（工伤保险）初始缴存年月日',
    `gongs_month_base`   int                                                          DEFAULT NULL COMMENT '（工伤保险）月缴存额',
    `gongs_own`          int                                                          DEFAULT NULL COMMENT '（工伤保险）个人月缴费额',
    `gongs_unit`         int                                                          DEFAULT NULL COMMENT '（工伤保险）单位月缴费额',
    `gongs_ctms`         int                                                          DEFAULT NULL COMMENT '（工伤保险）最近连续缴存次数',
    `gongs_tms`          int                                                          DEFAULT NULL COMMENT '（工伤保险）累计缴存次数',
    `shiy_last_date`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（失业保险）最近缴存年月日',
    `shiy_first_date`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（失业保险）初始缴存年月日',
    `shiy_month_base`    int                                                          DEFAULT NULL COMMENT '（失业保险）月缴存额',
    `shiy_own`           int                                                          DEFAULT NULL COMMENT '（失业保险）个人月缴费额',
    `shiy_unit`          int                                                          DEFAULT NULL COMMENT '（失业保险）单位月缴费额',
    `shiy_ctms`          int                                                          DEFAULT NULL COMMENT '（失业保险）最近连续缴存次数',
    `shiy_tms`           int                                                          DEFAULT NULL COMMENT '（失业保险）累计缴存次数',
    `shengy_last_date`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（生育保险）最近缴存年月日',
    `shengy_first_date`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '（生育保险）初始缴存年月日',
    `shengy_month_base`  int                                                          DEFAULT NULL COMMENT '（生育保险）月缴存额',
    `shengy_own`         int                                                          DEFAULT NULL COMMENT '（生育保险）个人月缴费额',
    `shengy_unit`        int                                                          DEFAULT NULL COMMENT '（生育保险）单位月缴费额',
    `shengy_ctms`        int                                                          DEFAULT NULL COMMENT '（生育保险）最近连续缴存次数',
    `shengy_tms`         int                                                          DEFAULT NULL COMMENT '（生育保险）累计缴存次数',
    PRIMARY KEY (`id_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='个人社保缴费信息';

-- 模型
INSERT INTO `result_social_security_info`(`id_no`,`name`,`id_code`,`last_unit_name`,`last_unit_kind`,`status`,`yangl_base`,`open_data`,`yangl_security_cat`,`yangl_last_date`,`yangl_first_date`,`yangl_month_base`,`yangl_own`,`yangl_unit`,`yangl_ctms`,`yangl_tms`,`yul_security_cat`,`yul_last_date`,`yul_first_date`,`yul_month_base`,`yul_own`,`yul_unit`,`yul_ctms`,`yul_tms`,`gongs_last_date`,`gongs_first_date`,`gongs_month_base`,`gongs_own`,`gongs_unit`,`gongs_ctms`,`gongs_tms`,`shiy_last_date`,`shiy_first_date`,`shiy_month_base`,`shiy_own`,`shiy_unit`,`shiy_ctms`,`shiy_tms`,`shengy_last_date`,`shengy_first_date`,`shengy_month_base`,`shengy_own`,`shengy_unit`,`shengy_ctms`,`shengy_tms`)
SELECT
    `id_no`,`name`,`id_code`,`last_unit_name`,
    IF (`last_unit_name` LIKE '%个人缴费%','其他',`last_unit_kind`) AS `last_unit_kind`,
    CASE
        WHEN `status` = '正常缴费' THEN '02'
        WHEN `status` = '参保正常' THEN '02'
        WHEN `status` = '暂停参保' THEN '03'
        WHEN `status` = '停止参保' THEN '04'
        ELSE '06'
        END AS `status`,
    CASE
        WHEN `yangl_base`=0 THEN 0
        WHEN `yangl_base`>0 AND `yangl_base`<1000 THEN 1
        WHEN `yangl_base`>=1000 AND `yangl_base`<1500 THEN 2
        WHEN `yangl_base`>=1500 AND `yangl_base`<2000 THEN 3
        WHEN `yangl_base`>=2000 AND `yangl_base`<2500 THEN 4
        WHEN `yangl_base`>=2500 AND `yangl_base`<3000 THEN 5
        WHEN `yangl_base`>=3000 AND `yangl_base`<4000 THEN 6
        WHEN `yangl_base`>=4000 AND `yangl_base`<5000 THEN 7
        WHEN `yangl_base`>=5000 AND `yangl_base`<6000 THEN 8
        WHEN `yangl_base`>=6000 AND `yangl_base`<7000 THEN 9
        WHEN `yangl_base`>=7000 AND `yangl_base`<8000 THEN 10
        WHEN `yangl_base`>=8000 AND `yangl_base`<9000 THEN 11
        WHEN `yangl_base`>=9000 AND `yangl_base`<10000 THEN 12
        WHEN `yangl_base`>=10000 AND `yangl_base`<12000 THEN 13
        WHEN `yangl_base`>=12000 AND `yangl_base`<14000 THEN 14
        WHEN `yangl_base`>=14000 AND `yangl_base`<16000 THEN 15
        WHEN `yangl_base`>=16000 AND `yangl_base`<18000 THEN 16
        WHEN `yangl_base`>=18000 AND `yangl_base`<20000 THEN 17
        WHEN `yangl_base`>=20000 AND `yangl_base`<25000 THEN 18
        WHEN `yangl_base`>=25000 THEN 19
        ELSE -99
        END AS `yangl_base`,
    `open_data`,
    IF (`last_unit_name` LIKE '%个人缴费%','02','01') AS `yangl_security_cat`,
    `yangl_last_date`,`yangl_first_date`,
    CASE
        WHEN `yangl_month_base`=0 THEN 0
        WHEN `yangl_month_base`>0 AND `yangl_month_base`<500 THEN 1
        WHEN `yangl_month_base`>=500 AND `yangl_month_base`<1000 THEN 2
        WHEN `yangl_month_base`>=1000 AND `yangl_month_base`<1500 THEN 3
        WHEN `yangl_month_base`>=1500 AND `yangl_month_base`<2000 THEN 4
        WHEN `yangl_month_base`>=2000 AND `yangl_month_base`<2500 THEN 5
        WHEN `yangl_month_base`>=2500 AND `yangl_month_base`<3000 THEN 6
        WHEN `yangl_month_base`>=3000 AND `yangl_month_base`<3500 THEN 7
        WHEN `yangl_month_base`>=3500 AND `yangl_month_base`<4000 THEN 8
        WHEN `yangl_month_base`>=4000 AND `yangl_month_base`<4500 THEN 9
        WHEN `yangl_month_base`>=4500 AND `yangl_month_base`<5000 THEN 10
        WHEN `yangl_month_base`>=5000 AND `yangl_month_base`<5500 THEN 11
        WHEN `yangl_month_base`>=5500 AND `yangl_month_base`<6000 THEN 12
        WHEN `yangl_month_base`>=6000 AND `yangl_month_base`<6500 THEN 13
        WHEN `yangl_month_base`>=6500 AND `yangl_month_base`<7000 THEN 14
        WHEN `yangl_month_base`>=7000 AND `yangl_month_base`<7500 THEN 15
        WHEN `yangl_month_base`>=7500 AND `yangl_month_base`<8000 THEN 16
        WHEN `yangl_month_base`>=8000 THEN 17
        ELSE -99
        END AS `yangl_month_base`,
    CASE
        WHEN `yangl_own`=0 THEN 0
        WHEN `yangl_own`>0 AND `yangl_own`<200 THEN 1
        WHEN `yangl_own`>=200 AND `yangl_own`<400 THEN 2
        WHEN `yangl_own`>=400 AND `yangl_own`<600 THEN 3
        WHEN `yangl_own`>=600 AND `yangl_own`<800 THEN 4
        WHEN `yangl_own`>=800 AND `yangl_own`<1000 THEN 5
        WHEN `yangl_own`>=1000 AND `yangl_own`<1200 THEN 6
        WHEN `yangl_own`>=1200 AND `yangl_own`<1400 THEN 7
        WHEN `yangl_own`>=1400 AND `yangl_own`<1600 THEN 8
        WHEN `yangl_own`>=1600 AND `yangl_own`<1800 THEN 9
        WHEN `yangl_own`>=1800 AND `yangl_own`<2000 THEN 10
        WHEN `yangl_own`>=2000 AND `yangl_own`<2200 THEN 11
        WHEN `yangl_own`>=2200 AND `yangl_own`<2400 THEN 12
        WHEN `yangl_own`>=2400 AND `yangl_own`<2600 THEN 13
        WHEN `yangl_own`>=2600 AND `yangl_own`<2800 THEN 14
        WHEN `yangl_own`>=2800 AND `yangl_own`<3000 THEN 15
        WHEN `yangl_own`>=3000 THEN 16
        ELSE -99
        END AS `yangl_own`,
    CASE
        WHEN `yangl_unit`=0 THEN 0
        WHEN `yangl_unit`>0 AND `yangl_unit`<200 THEN 1
        WHEN `yangl_unit`>=200 AND `yangl_unit`<500 THEN 2
        WHEN `yangl_unit`>=500 AND `yangl_unit`<600 THEN 3
        WHEN `yangl_unit`>=600 AND `yangl_unit`<800 THEN 4
        WHEN `yangl_unit`>=800 AND `yangl_unit`<1000 THEN 5
        WHEN `yangl_unit`>=1000 AND `yangl_unit`<1200 THEN 6
        WHEN `yangl_unit`>=1200 AND `yangl_unit`<1400 THEN 7
        WHEN `yangl_unit`>=1400 AND `yangl_unit`<1600 THEN 8
        WHEN `yangl_unit`>=1600 AND `yangl_unit`<1800 THEN 9
        WHEN `yangl_unit`>=1800 AND `yangl_unit`<2000 THEN 10
        WHEN `yangl_unit`>=2000 AND `yangl_unit`<2400 THEN 11
        WHEN `yangl_unit`>=2400 AND `yangl_unit`<2800 THEN 12
        WHEN `yangl_unit`>=2800 AND `yangl_unit`<3200 THEN 13
        WHEN `yangl_unit`>=3200 AND `yangl_unit`<3600 THEN 14
        WHEN `yangl_unit`>=3600 AND `yangl_unit`<4000 THEN 15
        WHEN `yangl_unit`>=4000 AND `yangl_unit`<4500 THEN 16
        WHEN `yangl_unit`>=4500 AND `yangl_unit`<5000 THEN 17
        WHEN `yangl_unit`>=5000 THEN 18
        ELSE -99
        END AS `yangl_unit`,
    CASE
        WHEN `yangl_ctms`=0 THEN 0
        WHEN `yangl_ctms`>0 AND `yangl_ctms`<3 THEN 1
        WHEN `yangl_ctms`>=3 AND `yangl_ctms`<6 THEN 2
        WHEN `yangl_ctms`>=6 AND `yangl_ctms`<9 THEN 3
        WHEN `yangl_ctms`>=9 AND `yangl_ctms`<12 THEN 4
        WHEN `yangl_ctms`>=12 AND `yangl_ctms`<15 THEN 5
        WHEN `yangl_ctms`>=15 AND `yangl_ctms`<18 THEN 6
        WHEN `yangl_ctms`>=18 AND `yangl_ctms`<21 THEN 7
        WHEN `yangl_ctms`>=21 AND `yangl_ctms`<24 THEN 8
        WHEN `yangl_ctms`>=24 AND `yangl_ctms`<27 THEN 9
        WHEN `yangl_ctms`>=27 AND `yangl_ctms`<30 THEN 10
        WHEN `yangl_ctms`>=30 AND `yangl_ctms`<33 THEN 11
        WHEN `yangl_ctms`>=33 AND `yangl_ctms`<36 THEN 12
        WHEN `yangl_ctms`>=36 THEN 13
        ELSE -99
        END AS `yangl_ctms`,
    CASE
        WHEN `yangl_tms`=0 THEN 0
        WHEN `yangl_tms`>0 AND `yangl_tms`<3 THEN 1
        WHEN `yangl_tms`>=3 AND `yangl_tms`<6 THEN 2
        WHEN `yangl_tms`>=6 AND `yangl_tms`<9 THEN 3
        WHEN `yangl_tms`>=9 AND `yangl_tms`<12 THEN 4
        WHEN `yangl_tms`>=12 AND `yangl_tms`<15 THEN 5
        WHEN `yangl_tms`>=15 AND `yangl_tms`<18 THEN 6
        WHEN `yangl_tms`>=18 AND `yangl_tms`<21 THEN 7
        WHEN `yangl_tms`>=21 AND `yangl_tms`<24 THEN 8
        WHEN `yangl_tms`>=24 AND `yangl_tms`<27 THEN 9
        WHEN `yangl_tms`>=27 AND `yangl_tms`<30 THEN 10
        WHEN `yangl_tms`>=30 AND `yangl_tms`<33 THEN 11
        WHEN `yangl_tms`>=33 AND `yangl_tms`<36 THEN 12
        WHEN `yangl_tms`>=36 THEN 13
        ELSE -99
        END AS `yangl_tms`,
    IF (`last_unit_name` LIKE '%个人缴费%','02','01') AS `yul_security_cat`,
    `yul_last_date`,`yul_first_date`,
    CASE
        WHEN `yul_month_base`=0 THEN 0
        WHEN `yul_month_base`>0 AND `yul_month_base`<150 THEN 1
        WHEN `yul_month_base`>=150 AND `yul_month_base`<300 THEN 2
        WHEN `yul_month_base`>=300 AND `yul_month_base`<450 THEN 3
        WHEN `yul_month_base`>=450 AND `yul_month_base`<600 THEN 4
        WHEN `yul_month_base`>=600 AND `yul_month_base`<750 THEN 5
        WHEN `yul_month_base`>=750 AND `yul_month_base`<900 THEN 6
        WHEN `yul_month_base`>=900 AND `yul_month_base`<1050 THEN 7
        WHEN `yul_month_base`>=1050 AND `yul_month_base`<1200 THEN 8
        WHEN `yul_month_base`>=1200 AND `yul_month_base`<1350 THEN 9
        WHEN `yul_month_base`>=1350 AND `yul_month_base`<1500 THEN 10
        WHEN `yul_month_base`>=1500 AND `yul_month_base`<1650 THEN 11
        WHEN `yul_month_base`>=1650 AND `yul_month_base`<1800 THEN 12
        WHEN `yul_month_base`>=1800 AND `yul_month_base`<2000 THEN 13
        WHEN `yul_month_base`>=2000 AND `yul_month_base`<2200 THEN 14
        WHEN `yul_month_base`>=2200 AND `yul_month_base`<2500 THEN 15
        WHEN `yul_month_base`>=2500 AND `yul_month_base`<2800 THEN 16
        WHEN `yul_month_base`>=2800 AND `yul_month_base`<3200 THEN 17
        WHEN `yul_month_base`>=3200 THEN 18
        ELSE -99
        END AS `yul_month_base`,
    CASE
        WHEN `yul_own`=0 THEN 0
        WHEN `yul_own`>0 AND `yul_own`<50 THEN 1
        WHEN `yul_own`>=50 AND `yul_own`<100 THEN 2
        WHEN `yul_own`>=100 AND `yul_own`<200 THEN 3
        WHEN `yul_own`>=200 AND `yul_own`<300 THEN 4
        WHEN `yul_own`>=300 AND `yul_own`<400 THEN 5
        WHEN `yul_own`>=400 AND `yul_own`<500 THEN 6
        WHEN `yul_own`>=500 AND `yul_own`<600 THEN 7
        WHEN `yul_own`>=600 AND `yul_own`<700 THEN 8
        WHEN `yul_own`>=700 AND `yul_own`<800 THEN 9
        WHEN `yul_own`>=800 AND `yul_own`<900 THEN 10
        WHEN `yul_own`>=900 AND `yul_own`<1000 THEN 11
        WHEN `yul_own`>=1000 THEN 12
        ELSE -99
        END AS `yul_own`,
    CASE
        WHEN `yul_unit`=0 THEN 0
        WHEN `yul_unit`>0 AND `yul_unit`<100 THEN 1
        WHEN `yul_unit`>=100 AND `yul_unit`<200 THEN 2
        WHEN `yul_unit`>=200 AND `yul_unit`<300 THEN 3
        WHEN `yul_unit`>=300 AND `yul_unit`<400 THEN 4
        WHEN `yul_unit`>=400 AND `yul_unit`<500 THEN 5
        WHEN `yul_unit`>=500 AND `yul_unit`<600 THEN 6
        WHEN `yul_unit`>=600 AND `yul_unit`<700 THEN 7
        WHEN `yul_unit`>=700 AND `yul_unit`<800 THEN 8
        WHEN `yul_unit`>=800 AND `yul_unit`<900 THEN 9
        WHEN `yul_unit`>=900 AND `yul_unit`<1000 THEN 10
        WHEN `yul_unit`>=1000 AND `yul_unit`<1200 THEN 11
        WHEN `yul_unit`>=1200 AND `yul_unit`<1400 THEN 12
        WHEN `yul_unit`>=1400 AND `yul_unit`<1600 THEN 13
        WHEN `yul_unit`>=1600 AND `yul_unit`<1800 THEN 14
        WHEN `yul_unit`>=1800 AND `yul_unit`<2000 THEN 15
        WHEN `yul_unit`>=2000 AND `yul_unit`<2500 THEN 16
        WHEN `yul_unit`>=2500 AND `yul_unit`<3000 THEN 17
        WHEN `yul_unit`>=3000 THEN 18
        ELSE -99
        END AS `yul_unit`,
    CASE
        WHEN `yul_ctms`=0 THEN 0
        WHEN `yul_ctms`>0 AND `yul_ctms`<3 THEN 1
        WHEN `yul_ctms`>=3 AND `yul_ctms`<6 THEN 2
        WHEN `yul_ctms`>=6 AND `yul_ctms`<9 THEN 3
        WHEN `yul_ctms`>=9 AND `yul_ctms`<12 THEN 4
        WHEN `yul_ctms`>=12 AND `yul_ctms`<15 THEN 5
        WHEN `yul_ctms`>=15 AND `yul_ctms`<18 THEN 6
        WHEN `yul_ctms`>=18 AND `yul_ctms`<21 THEN 7
        WHEN `yul_ctms`>=21 AND `yul_ctms`<24 THEN 8
        WHEN `yul_ctms`>=24 AND `yul_ctms`<27 THEN 9
        WHEN `yul_ctms`>=27 AND `yul_ctms`<30 THEN 10
        WHEN `yul_ctms`>=30 AND `yul_ctms`<33 THEN 11
        WHEN `yul_ctms`>=33 AND `yul_ctms`<36 THEN 12
        WHEN `yul_ctms`>=36 THEN 13
        ELSE -99
        END AS `yul_ctms`,
    CASE
        WHEN `yul_tms`=0 THEN 0
        WHEN `yul_tms`>0 AND `yul_tms`<3 THEN 1
        WHEN `yul_tms`>=3 AND `yul_tms`<6 THEN 2
        WHEN `yul_tms`>=6 AND `yul_tms`<9 THEN 3
        WHEN `yul_tms`>=9 AND `yul_tms`<12 THEN 4
        WHEN `yul_tms`>=12 AND `yul_tms`<15 THEN 5
        WHEN `yul_tms`>=15 AND `yul_tms`<18 THEN 6
        WHEN `yul_tms`>=18 AND `yul_tms`<21 THEN 7
        WHEN `yul_tms`>=21 AND `yul_tms`<24 THEN 8
        WHEN `yul_tms`>=24 AND `yul_tms`<27 THEN 9
        WHEN `yul_tms`>=27 AND `yul_tms`<30 THEN 10
        WHEN `yul_tms`>=30 AND `yul_tms`<33 THEN 11
        WHEN `yul_tms`>=33 AND `yul_tms`<36 THEN 12
        WHEN `yul_tms`>=36 THEN 13
        ELSE -99
        END AS `yul_tms`,
    `gongs_last_date`,`gongs_first_date`,
    CASE
        WHEN `gongs_month_base`=0 THEN 0
        WHEN `gongs_month_base`>0 AND `gongs_month_base`<150 THEN 1
        WHEN `gongs_month_base`>=150 AND `gongs_month_base`<300 THEN 2
        WHEN `gongs_month_base`>=300 AND `gongs_month_base`<450 THEN 3
        WHEN `gongs_month_base`>=450 AND `gongs_month_base`<600 THEN 4
        WHEN `gongs_month_base`>=600 AND `gongs_month_base`<750 THEN 5
        WHEN `gongs_month_base`>=750 AND `gongs_month_base`<900 THEN 6
        WHEN `gongs_month_base`>=900 AND `gongs_month_base`<1050 THEN 7
        WHEN `gongs_month_base`>=1050 AND `gongs_month_base`<1200 THEN 8
        WHEN `gongs_month_base`>=1200 AND `gongs_month_base`<1350 THEN 9
        WHEN `gongs_month_base`>=1350 AND `gongs_month_base`<1500 THEN 10
        WHEN `gongs_month_base`>=1500 AND `gongs_month_base`<1650 THEN 11
        WHEN `gongs_month_base`>=1650 AND `gongs_month_base`<1800 THEN 12
        WHEN `gongs_month_base`>=1800 AND `gongs_month_base`<2000 THEN 13
        WHEN `gongs_month_base`>=2000 AND `gongs_month_base`<2200 THEN 14
        WHEN `gongs_month_base`>=2200 AND `gongs_month_base`<2500 THEN 15
        WHEN `gongs_month_base`>=2500 AND `gongs_month_base`<2800 THEN 16
        WHEN `gongs_month_base`>=2800 AND `gongs_month_base`<3200 THEN 17
        WHEN `gongs_month_base`>=3200 THEN 18
        ELSE -99
        END AS `gongs_month_base`,
    CASE
        WHEN `gongs_own`=0 THEN 0
        WHEN `gongs_own`>0 AND `gongs_own`<50 THEN 1
        WHEN `gongs_own`>=50 AND `gongs_own`<100 THEN 2
        WHEN `gongs_own`>=100 AND `gongs_own`<200 THEN 3
        WHEN `gongs_own`>=200 AND `gongs_own`<300 THEN 4
        WHEN `gongs_own`>=300 AND `gongs_own`<400 THEN 5
        WHEN `gongs_own`>=400 AND `gongs_own`<500 THEN 6
        WHEN `gongs_own`>=500 AND `gongs_own`<600 THEN 7
        WHEN `gongs_own`>=600 AND `gongs_own`<700 THEN 8
        WHEN `gongs_own`>=700 AND `gongs_own`<800 THEN 9
        WHEN `gongs_own`>=800 AND `gongs_own`<900 THEN 10
        WHEN `gongs_own`>=900 AND `gongs_own`<1000 THEN 11
        WHEN `gongs_own`>=1000 THEN 12
        ELSE -99
        END AS `gongs_own`,
    CASE
        WHEN `gongs_unit`=0 THEN 0
        WHEN `gongs_unit`>0 AND `gongs_unit`<100 THEN 1
        WHEN `gongs_unit`>=100 AND `gongs_unit`<200 THEN 2
        WHEN `gongs_unit`>=200 AND `gongs_unit`<300 THEN 3
        WHEN `gongs_unit`>=300 AND `gongs_unit`<400 THEN 4
        WHEN `gongs_unit`>=400 AND `gongs_unit`<500 THEN 5
        WHEN `gongs_unit`>=500 AND `gongs_unit`<600 THEN 6
        WHEN `gongs_unit`>=600 AND `gongs_unit`<700 THEN 7
        WHEN `gongs_unit`>=700 AND `gongs_unit`<800 THEN 8
        WHEN `gongs_unit`>=800 AND `gongs_unit`<900 THEN 9
        WHEN `gongs_unit`>=900 AND `gongs_unit`<1000 THEN 10
        WHEN `gongs_unit`>=1000 AND `gongs_unit`<1200 THEN 11
        WHEN `gongs_unit`>=1200 AND `gongs_unit`<1400 THEN 12
        WHEN `gongs_unit`>=1400 AND `gongs_unit`<1600 THEN 13
        WHEN `gongs_unit`>=1600 AND `gongs_unit`<1800 THEN 14
        WHEN `gongs_unit`>=1800 AND `gongs_unit`<2000 THEN 15
        WHEN `gongs_unit`>=2000 AND `gongs_unit`<2500 THEN 16
        WHEN `gongs_unit`>=2500 AND `gongs_unit`<3000 THEN 17
        WHEN `gongs_unit`>=3000 THEN 18
        ELSE -99
        END AS `gongs_unit`,
    CASE
        WHEN `gongs_ctms`=0 THEN 0
        WHEN `gongs_ctms`>0 AND `gongs_ctms`<3 THEN 1
        WHEN `gongs_ctms`>=3 AND `gongs_ctms`<6 THEN 2
        WHEN `gongs_ctms`>=6 AND `gongs_ctms`<9 THEN 3
        WHEN `gongs_ctms`>=9 AND `gongs_ctms`<12 THEN 4
        WHEN `gongs_ctms`>=12 AND `gongs_ctms`<15 THEN 5
        WHEN `gongs_ctms`>=15 AND `gongs_ctms`<18 THEN 6
        WHEN `gongs_ctms`>=18 AND `gongs_ctms`<21 THEN 7
        WHEN `gongs_ctms`>=21 AND `gongs_ctms`<24 THEN 8
        WHEN `gongs_ctms`>=24 AND `gongs_ctms`<27 THEN 9
        WHEN `gongs_ctms`>=27 AND `gongs_ctms`<30 THEN 10
        WHEN `gongs_ctms`>=30 AND `gongs_ctms`<33 THEN 11
        WHEN `gongs_ctms`>=33 AND `gongs_ctms`<36 THEN 12
        WHEN `gongs_ctms`>=36 THEN 13
        ELSE -99
        END AS `gongs_ctms`,
    CASE
        WHEN `gongs_tms`=0 THEN 0
        WHEN `gongs_tms`>0 AND `gongs_tms`<3 THEN 1
        WHEN `gongs_tms`>=3 AND `gongs_tms`<6 THEN 2
        WHEN `gongs_tms`>=6 AND `gongs_tms`<9 THEN 3
        WHEN `gongs_tms`>=9 AND `gongs_tms`<12 THEN 4
        WHEN `gongs_tms`>=12 AND `gongs_tms`<15 THEN 5
        WHEN `gongs_tms`>=15 AND `gongs_tms`<18 THEN 6
        WHEN `gongs_tms`>=18 AND `gongs_tms`<21 THEN 7
        WHEN `gongs_tms`>=21 AND `gongs_tms`<24 THEN 8
        WHEN `gongs_tms`>=24 AND `gongs_tms`<27 THEN 9
        WHEN `gongs_tms`>=27 AND `gongs_tms`<30 THEN 10
        WHEN `gongs_tms`>=30 AND `gongs_tms`<33 THEN 11
        WHEN `gongs_tms`>=33 AND `gongs_tms`<36 THEN 12
        WHEN `gongs_tms`>=36 THEN 13
        ELSE -99
        END AS `gongs_tms`,
    `shiy_last_date`,`shiy_first_date`,
    CASE
        WHEN `shiy_month_base`=0 THEN 0
        WHEN `shiy_month_base`>0 AND `shiy_month_base`<150 THEN 1
        WHEN `shiy_month_base`>=150 AND `shiy_month_base`<300 THEN 2
        WHEN `shiy_month_base`>=300 AND `shiy_month_base`<450 THEN 3
        WHEN `shiy_month_base`>=450 AND `shiy_month_base`<600 THEN 4
        WHEN `shiy_month_base`>=600 AND `shiy_month_base`<750 THEN 5
        WHEN `shiy_month_base`>=750 AND `shiy_month_base`<900 THEN 6
        WHEN `shiy_month_base`>=900 AND `shiy_month_base`<1050 THEN 7
        WHEN `shiy_month_base`>=1050 AND `shiy_month_base`<1200 THEN 8
        WHEN `shiy_month_base`>=1200 AND `shiy_month_base`<1350 THEN 9
        WHEN `shiy_month_base`>=1350 AND `shiy_month_base`<1500 THEN 10
        WHEN `shiy_month_base`>=1500 AND `shiy_month_base`<1650 THEN 11
        WHEN `shiy_month_base`>=1650 AND `shiy_month_base`<1800 THEN 12
        WHEN `shiy_month_base`>=1800 AND `shiy_month_base`<2000 THEN 13
        WHEN `shiy_month_base`>=2000 AND `shiy_month_base`<2200 THEN 14
        WHEN `shiy_month_base`>=2200 AND `shiy_month_base`<2500 THEN 15
        WHEN `shiy_month_base`>=2500 AND `shiy_month_base`<2800 THEN 16
        WHEN `shiy_month_base`>=2800 AND `shiy_month_base`<3200 THEN 17
        WHEN `shiy_month_base`>=3200 THEN 18
        ELSE -99
        END AS `shiy_month_base`,
    CASE
        WHEN `shiy_own`=0 THEN 0
        WHEN `shiy_own`>0 AND `shiy_own`<50 THEN 1
        WHEN `shiy_own`>=50 AND `shiy_own`<100 THEN 2
        WHEN `shiy_own`>=100 AND `shiy_own`<200 THEN 3
        WHEN `shiy_own`>=200 AND `shiy_own`<300 THEN 4
        WHEN `shiy_own`>=300 AND `shiy_own`<400 THEN 5
        WHEN `shiy_own`>=400 AND `shiy_own`<500 THEN 6
        WHEN `shiy_own`>=500 AND `shiy_own`<600 THEN 7
        WHEN `shiy_own`>=600 AND `shiy_own`<700 THEN 8
        WHEN `shiy_own`>=700 AND `shiy_own`<800 THEN 9
        WHEN `shiy_own`>=800 AND `shiy_own`<900 THEN 10
        WHEN `shiy_own`>=900 AND `shiy_own`<1000 THEN 11
        WHEN `shiy_own`>=1000 THEN 12
        ELSE -99
        END AS `shiy_own`,
    CASE
        WHEN `shiy_unit`=0 THEN 0
        WHEN `shiy_unit`>0 AND `shiy_unit`<100 THEN 1
        WHEN `shiy_unit`>=100 AND `shiy_unit`<200 THEN 2
        WHEN `shiy_unit`>=200 AND `shiy_unit`<300 THEN 3
        WHEN `shiy_unit`>=300 AND `shiy_unit`<400 THEN 4
        WHEN `shiy_unit`>=400 AND `shiy_unit`<500 THEN 5
        WHEN `shiy_unit`>=500 AND `shiy_unit`<600 THEN 6
        WHEN `shiy_unit`>=600 AND `shiy_unit`<700 THEN 7
        WHEN `shiy_unit`>=700 AND `shiy_unit`<800 THEN 8
        WHEN `shiy_unit`>=800 AND `shiy_unit`<900 THEN 9
        WHEN `shiy_unit`>=900 AND `shiy_unit`<1000 THEN 10
        WHEN `shiy_unit`>=1000 AND `shiy_unit`<1200 THEN 11
        WHEN `shiy_unit`>=1200 AND `shiy_unit`<1400 THEN 12
        WHEN `shiy_unit`>=1400 AND `shiy_unit`<1600 THEN 13
        WHEN `shiy_unit`>=1600 AND `shiy_unit`<1800 THEN 14
        WHEN `shiy_unit`>=1800 AND `shiy_unit`<2000 THEN 15
        WHEN `shiy_unit`>=2000 AND `shiy_unit`<2500 THEN 16
        WHEN `shiy_unit`>=2500 AND `shiy_unit`<3000 THEN 17
        WHEN `shiy_unit`>=3000 THEN 18
        ELSE -99
        END AS `shiy_unit`,
    CASE
        WHEN `shiy_ctms`=0 THEN 0
        WHEN `shiy_ctms`>0 AND `shiy_ctms`<3 THEN 1
        WHEN `shiy_ctms`>=3 AND `shiy_ctms`<6 THEN 2
        WHEN `shiy_ctms`>=6 AND `shiy_ctms`<9 THEN 3
        WHEN `shiy_ctms`>=9 AND `shiy_ctms`<12 THEN 4
        WHEN `shiy_ctms`>=12 AND `shiy_ctms`<15 THEN 5
        WHEN `shiy_ctms`>=15 AND `shiy_ctms`<18 THEN 6
        WHEN `shiy_ctms`>=18 AND `shiy_ctms`<21 THEN 7
        WHEN `shiy_ctms`>=21 AND `shiy_ctms`<24 THEN 8
        WHEN `shiy_ctms`>=24 AND `shiy_ctms`<27 THEN 9
        WHEN `shiy_ctms`>=27 AND `shiy_ctms`<30 THEN 10
        WHEN `shiy_ctms`>=30 AND `shiy_ctms`<33 THEN 11
        WHEN `shiy_ctms`>=33 AND `shiy_ctms`<36 THEN 12
        WHEN `shiy_ctms`>=36 THEN 13
        ELSE -99
        END AS `shiy_ctms`,
    CASE
        WHEN `shiy_tms`=0 THEN 0
        WHEN `shiy_tms`>0 AND `shiy_tms`<3 THEN 1
        WHEN `shiy_tms`>=3 AND `shiy_tms`<6 THEN 2
        WHEN `shiy_tms`>=6 AND `shiy_tms`<9 THEN 3
        WHEN `shiy_tms`>=9 AND `shiy_tms`<12 THEN 4
        WHEN `shiy_tms`>=12 AND `shiy_tms`<15 THEN 5
        WHEN `shiy_tms`>=15 AND `shiy_tms`<18 THEN 6
        WHEN `shiy_tms`>=18 AND `shiy_tms`<21 THEN 7
        WHEN `shiy_tms`>=21 AND `shiy_tms`<24 THEN 8
        WHEN `shiy_tms`>=24 AND `shiy_tms`<27 THEN 9
        WHEN `shiy_tms`>=27 AND `shiy_tms`<30 THEN 10
        WHEN `shiy_tms`>=30 AND `shiy_tms`<33 THEN 11
        WHEN `shiy_tms`>=33 AND `shiy_tms`<36 THEN 12
        WHEN `shiy_tms`>=36 THEN 13
        ELSE -99
        END AS `shiy_tms`,
    `shengy_last_date`,`shengy_first_date`,
    CASE
        WHEN `shengy_month_base`=0 THEN 0
        WHEN `shengy_month_base`>0 AND `shengy_month_base`<150 THEN 1
        WHEN `shengy_month_base`>=150 AND `shengy_month_base`<300 THEN 2
        WHEN `shengy_month_base`>=300 AND `shengy_month_base`<450 THEN 3
        WHEN `shengy_month_base`>=450 AND `shengy_month_base`<600 THEN 4
        WHEN `shengy_month_base`>=600 AND `shengy_month_base`<750 THEN 5
        WHEN `shengy_month_base`>=750 AND `shengy_month_base`<900 THEN 6
        WHEN `shengy_month_base`>=900 AND `shengy_month_base`<1050 THEN 7
        WHEN `shengy_month_base`>=1050 AND `shengy_month_base`<1200 THEN 8
        WHEN `shengy_month_base`>=1200 AND `shengy_month_base`<1350 THEN 9
        WHEN `shengy_month_base`>=1350 AND `shengy_month_base`<1500 THEN 10
        WHEN `shengy_month_base`>=1500 AND `shengy_month_base`<1650 THEN 11
        WHEN `shengy_month_base`>=1650 AND `shengy_month_base`<1800 THEN 12
        WHEN `shengy_month_base`>=1800 AND `shengy_month_base`<2000 THEN 13
        WHEN `shengy_month_base`>=2000 AND `shengy_month_base`<2200 THEN 14
        WHEN `shengy_month_base`>=2200 AND `shengy_month_base`<2500 THEN 15
        WHEN `shengy_month_base`>=2500 AND `shengy_month_base`<2800 THEN 16
        WHEN `shengy_month_base`>=2800 AND `shengy_month_base`<3200 THEN 17
        WHEN `shengy_month_base`>=3200 THEN 18
        ELSE -99
        END AS `shengy_month_base`,
    CASE
        WHEN `shengy_own`=0 THEN 0
        WHEN `shengy_own`>0 AND `shengy_own`<50 THEN 1
        WHEN `shengy_own`>=50 AND `shengy_own`<100 THEN 2
        WHEN `shengy_own`>=100 AND `shengy_own`<200 THEN 3
        WHEN `shengy_own`>=200 AND `shengy_own`<300 THEN 4
        WHEN `shengy_own`>=300 AND `shengy_own`<400 THEN 5
        WHEN `shengy_own`>=400 AND `shengy_own`<500 THEN 6
        WHEN `shengy_own`>=500 AND `shengy_own`<600 THEN 7
        WHEN `shengy_own`>=600 AND `shengy_own`<700 THEN 8
        WHEN `shengy_own`>=700 AND `shengy_own`<800 THEN 9
        WHEN `shengy_own`>=800 AND `shengy_own`<900 THEN 10
        WHEN `shengy_own`>=900 AND `shengy_own`<1000 THEN 11
        WHEN `shengy_own`>=1000 THEN 12
        ELSE -99
        END AS `shengy_own`,
    CASE
        WHEN `shengy_unit`=0 THEN 0
        WHEN `shengy_unit`>0 AND `shengy_unit`<100 THEN 1
        WHEN `shengy_unit`>=100 AND `shengy_unit`<200 THEN 2
        WHEN `shengy_unit`>=200 AND `shengy_unit`<300 THEN 3
        WHEN `shengy_unit`>=300 AND `shengy_unit`<400 THEN 4
        WHEN `shengy_unit`>=400 AND `shengy_unit`<500 THEN 5
        WHEN `shengy_unit`>=500 AND `shengy_unit`<600 THEN 6
        WHEN `shengy_unit`>=600 AND `shengy_unit`<700 THEN 7
        WHEN `shengy_unit`>=700 AND `shengy_unit`<800 THEN 8
        WHEN `shengy_unit`>=800 AND `shengy_unit`<900 THEN 9
        WHEN `shengy_unit`>=900 AND `shengy_unit`<1000 THEN 10
        WHEN `shengy_unit`>=1000 AND `shengy_unit`<1200 THEN 11
        WHEN `shengy_unit`>=1200 AND `shengy_unit`<1400 THEN 12
        WHEN `shengy_unit`>=1400 AND `shengy_unit`<1600 THEN 13
        WHEN `shengy_unit`>=1600 AND `shengy_unit`<1800 THEN 14
        WHEN `shengy_unit`>=1800 AND `shengy_unit`<2000 THEN 15
        WHEN `shengy_unit`>=2000 AND `shengy_unit`<2500 THEN 16
        WHEN `shengy_unit`>=2500 AND `shengy_unit`<3000 THEN 17
        WHEN `shengy_unit`>=3000 THEN 18
        ELSE -99
        END AS `shengy_unit`,
    CASE
        WHEN `shengy_ctms`=0 THEN 0
        WHEN `shengy_ctms`>0 AND `shengy_ctms`<3 THEN 1
        WHEN `shengy_ctms`>=3 AND `shengy_ctms`<6 THEN 2
        WHEN `shengy_ctms`>=6 AND `shengy_ctms`<9 THEN 3
        WHEN `shengy_ctms`>=9 AND `shengy_ctms`<12 THEN 4
        WHEN `shengy_ctms`>=12 AND `shengy_ctms`<15 THEN 5
        WHEN `shengy_ctms`>=15 AND `shengy_ctms`<18 THEN 6
        WHEN `shengy_ctms`>=18 AND `shengy_ctms`<21 THEN 7
        WHEN `shengy_ctms`>=21 AND `shengy_ctms`<24 THEN 8
        WHEN `shengy_ctms`>=24 AND `shengy_ctms`<27 THEN 9
        WHEN `shengy_ctms`>=27 AND `shengy_ctms`<30 THEN 10
        WHEN `shengy_ctms`>=30 AND `shengy_ctms`<33 THEN 11
        WHEN `shengy_ctms`>=33 AND `shengy_ctms`<36 THEN 12
        WHEN `shengy_ctms`>=36 THEN 13
        ELSE -99
        END AS `shengy_ctms`,
    CASE
        WHEN `shengy_tms`=0 THEN 0
        WHEN `shengy_tms`>0 AND `shengy_tms`<3 THEN 1
        WHEN `shengy_tms`>=3 AND `shengy_tms`<6 THEN 2
        WHEN `shengy_tms`>=6 AND `shengy_tms`<9 THEN 3
        WHEN `shengy_tms`>=9 AND `shengy_tms`<12 THEN 4
        WHEN `shengy_tms`>=12 AND `shengy_tms`<15 THEN 5
        WHEN `shengy_tms`>=15 AND `shengy_tms`<18 THEN 6
        WHEN `shengy_tms`>=18 AND `shengy_tms`<21 THEN 7
        WHEN `shengy_tms`>=21 AND `shengy_tms`<24 THEN 8
        WHEN `shengy_tms`>=24 AND `shengy_tms`<27 THEN 9
        WHEN `shengy_tms`>=27 AND `shengy_tms`<30 THEN 10
        WHEN `shengy_tms`>=30 AND `shengy_tms`<33 THEN 11
        WHEN `shengy_tms`>=33 AND `shengy_tms`<36 THEN 12
        WHEN `shengy_tms`>=36 THEN 13
        ELSE -99
        END AS `shengy_tms`
FROM
    `social_security_info`;
