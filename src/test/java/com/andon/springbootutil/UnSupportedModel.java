package com.andon.springbootutil;

import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.PersonInfoUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2023/10/20
 * <p>
 * 无人抚养儿童模型
 */
@Slf4j
public class UnSupportedModel {

    public static void main(String[] args) throws IOException {
        int isUnSupportedCount = 100000;
        int sum = isUnSupportedCount * 5 * 3;
        Set<String> idCards = new HashSet<>(sum);
        // 常驻人口
        List<PermanentPopulation> permanentPopulations = new ArrayList<>(sum);
        // 监护人
        List<Guardian> guardians = new ArrayList<>(isUnSupportedCount * 5);
        // 监狱在押人员
        List<PrisonDetainees> prisonDetainees = new ArrayList<>(isUnSupportedCount * 2);
        // 戒毒所人员
        List<DrugAbuser> drugAbusers = new ArrayList<>(isUnSupportedCount * 2);
        // 手机号信息
        List<PhoneInfo> phoneInfos = new ArrayList<>(isUnSupportedCount * 5 * 2);

        for (int i = 1; i <= isUnSupportedCount * 5; i++) {
            PersonInfoUtil.PersonInfo child = new PersonInfoUtil.PersonInfo();
            int ageChild = new Random().nextInt(15) + 3;
            child.setAge(String.valueOf(ageChild));
            child.setHealthStatus("健康");
            PersonInfoUtil.generateInfo(child, idCards);

            boolean isUnSupported = i % 5 == 0;
            int ageMother = ageChild + 24;
            PersonInfoUtil.PersonInfo mother = new PersonInfoUtil.PersonInfo();
            mother.setSex("女");
            mother.setAge(String.valueOf(ageMother));
            mother.setAddress(child.getAddress());
            mother.setHealthStatus("健康");
            PersonInfoUtil.generateInfo(mother, idCards);

            int ageFather = ageChild + 26;
            PersonInfoUtil.PersonInfo father = new PersonInfoUtil.PersonInfo();
            father.setSex("男");
            father.setAge(String.valueOf(ageFather));
            father.setAddress(child.getAddress());
            father.setHealthStatus("健康");
            PersonInfoUtil.generateInfo(father, idCards);

            PhoneInfo motherPhoneInfo = new PhoneInfo(mother.getIdCard(), mother.getName(), mother.getSex(), mother.getAge(), mother.getPhoneNum(), "是");
            PhoneInfo fatherPhoneInfo = new PhoneInfo(father.getIdCard(), father.getName(), father.getSex(), father.getAge(), father.getPhoneNum(), "是");
            if (isUnSupported) {
                int isUnSupported1 = new Random().nextInt(4);
                switch (isUnSupported1) {
                    case 0:
                        String[] healthArr = new String[]{"重残", "死亡"};
                        String healthStatus = healthArr[new Random().nextInt(healthArr.length)];
                        mother.setHealthStatus(healthStatus);
                        break;
                    case 1:
                        prisonDetainees.add(new PrisonDetainees(mother.getIdCard(), mother.getName(), mother.getSex(), mother.getAge()));
                        break;
                    case 2:
                        drugAbusers.add(new DrugAbuser(mother.getIdCard(), mother.getName(), mother.getSex(), mother.getAge()));
                        break;
                    case 3:
                        motherPhoneInfo.setIsConnect("否");
                        break;
                }
                int isUnSupported2 = new Random().nextInt(4);
                switch (isUnSupported2) {
                    case 0:
                        String[] healthArr = new String[]{"重残", "死亡"};
                        String healthStatus = healthArr[new Random().nextInt(healthArr.length)];
                        father.setHealthStatus(healthStatus);
                        break;
                    case 1:
                        prisonDetainees.add(new PrisonDetainees(father.getIdCard(), father.getName(), father.getSex(), father.getAge()));
                        break;
                    case 2:
                        drugAbusers.add(new DrugAbuser(father.getIdCard(), father.getName(), father.getSex(), father.getAge()));
                        break;
                    case 3:
                        fatherPhoneInfo.setIsConnect("否");
                        break;
                }
            }
            phoneInfos.add(motherPhoneInfo);
            phoneInfos.add(fatherPhoneInfo);
            guardians.add(new Guardian(child.getIdCard(), mother.getIdCard(), father.getIdCard()));
            permanentPopulations.add(new PermanentPopulation(child.getIdCard(), child.getName(), child.getSex(), child.getAge(), child.getAddress(), child.getHealthStatus()));
            permanentPopulations.add(new PermanentPopulation(mother.getIdCard(), mother.getName(), mother.getSex(), mother.getAge(), mother.getAddress(), mother.getHealthStatus()));
            permanentPopulations.add(new PermanentPopulation(father.getIdCard(), father.getName(), father.getSex(), father.getAge(), father.getAddress(), father.getHealthStatus()));
        }

        log.info("常驻人口:{}", permanentPopulations.size());
        log.info("监护人:{}", guardians.size());
        log.info("监狱在押人员:{}", prisonDetainees.size());
        log.info("戒毒所人员:{}", drugAbusers.size());
        log.info("手机通联信息:{}", phoneInfos.size());

        List<String> permanentPopulationLines = new ArrayList<>(permanentPopulations.size() + 1);
        permanentPopulationLines.add("id_card,name,sex,age,address,health_status");
        List<String> permanentPopulationsStr = permanentPopulations.stream().map(permanentPopulation -> String.format("%s,%s,%s,%s,%s,%s", permanentPopulation.idCard, permanentPopulation.name, permanentPopulation.sex, permanentPopulation.age, permanentPopulation.address, permanentPopulation.healthStatus))
                .collect(Collectors.toList());
        Collections.shuffle(permanentPopulationsStr);
        permanentPopulationLines.addAll(permanentPopulationsStr);
        FileUtil.createFileWithContent(permanentPopulationLines, "常驻人口.csv");

        List<String> guardiansLines = new ArrayList<>(guardians.size() + 1);
        guardiansLines.add("id_card,id_card_guardian_1,id_card_guardian_2");
        List<String> guardiansStr = guardians.stream().map(guardian -> String.format("%s,%s,%s", guardian.idCard, guardian.idCardGuardian1, guardian.idCardGuardian2))
                .collect(Collectors.toList());
        Collections.shuffle(guardiansStr);
        guardiansLines.addAll(guardiansStr);
        FileUtil.createFileWithContent(guardiansLines, "监护人.csv");

        List<String> prisonDetaineesLines = new ArrayList<>(prisonDetainees.size() + 1);
        prisonDetaineesLines.add("id_card,name,sex,age");
        List<String> prisonDetaineesStr = prisonDetainees.stream().map(prisonDetainee -> String.format("%s,%s,%s,%s", prisonDetainee.idCard, prisonDetainee.name, prisonDetainee.sex, prisonDetainee.age))
                .collect(Collectors.toList());
        Collections.shuffle(prisonDetaineesStr);
        prisonDetaineesLines.addAll(prisonDetaineesStr);
        FileUtil.createFileWithContent(prisonDetaineesLines, "监狱在押人员.csv");

        List<String> drugAbusersLines = new ArrayList<>(drugAbusers.size() + 1);
        drugAbusersLines.add("id_card,name,sex,age");
        List<String> drugAbusersStr = drugAbusers.stream().map(drugAbuser -> String.format("%s,%s,%s,%s", drugAbuser.idCard, drugAbuser.name, drugAbuser.sex, drugAbuser.age))
                .collect(Collectors.toList());
        Collections.shuffle(drugAbusersStr);
        drugAbusersLines.addAll(drugAbusersStr);
        FileUtil.createFileWithContent(drugAbusersLines, "戒毒所人员.csv");

        List<String> phoneInfosLines = new ArrayList<>(phoneInfos.size() + 1);
        phoneInfosLines.add("id_card,name,sex,age,phone_num,is_connect");
        List<String> phoneInfosStr = phoneInfos.stream().map(phoneInfo -> String.format("%s,%s,%s,%s,%s,%s", phoneInfo.idCard, phoneInfo.name, phoneInfo.sex, phoneInfo.age, phoneInfo.phoneNum, phoneInfo.isConnect))
                .collect(Collectors.toList());
        Collections.shuffle(phoneInfosStr);
        phoneInfosLines.addAll(phoneInfosStr);
        FileUtil.createFileWithContent(phoneInfosLines, "手机通联信息.csv");

        log.info("========== 过滤 `全省常驻人口库` 18周岁以下未成年人，补充 `未成年信息表` ==========");
        List<PermanentPopulation> children = permanentPopulations.stream()
                .filter(permanentPopulation -> Integer.parseInt(permanentPopulation.getAge()) <= 18)
                .collect(Collectors.toList());
        log.info("未成年信息表:{}", children.size());

        List<String> childrenLines = new ArrayList<>(children.size() + 1);
        childrenLines.add("id_card,name,sex,age,address");
        List<String> childrenStr = children.stream().map(child -> String.format("%s,%s,%s,%s,%s", child.idCard, child.name, child.sex, child.age, child.address))
                .collect(Collectors.toList());
        Collections.shuffle(childrenStr);
        childrenLines.addAll(childrenStr);
        FileUtil.createFileWithContent(childrenLines, "未成年人信息.csv");

        log.info("========== 联合查询 `监护人信息库`、`全省常驻人口库` 补充 `监护人状况信息表` 中监护人的 `身份证` ==========");
        Set<String> guardiansIdCard = new HashSet<>(guardians.size());
        Set<String> guardiansIdCard1 = guardians.stream()
                .map(Guardian::getIdCardGuardian1)
                .collect(Collectors.toSet());
        guardiansIdCard.addAll(guardiansIdCard1);
        Set<String> guardiansIdCard2 = guardians.stream()
                .map(Guardian::getIdCardGuardian2)
                .collect(Collectors.toSet());
        guardiansIdCard.addAll(guardiansIdCard2);
        List<GuardianStatusInfo> guardianStatusInfos = permanentPopulations.stream()
                .filter(permanentPopulation -> guardiansIdCard.contains(permanentPopulation.getIdCard()))
                .map(permanentPopulation -> {
                    GuardianStatusInfo guardianStatusInfo = new GuardianStatusInfo();
                    guardianStatusInfo.setIdCard(permanentPopulation.idCard);
                    guardianStatusInfo.setName(permanentPopulation.name);
                    guardianStatusInfo.setSex(permanentPopulation.sex);
                    guardianStatusInfo.setAge(permanentPopulation.age);
                    guardianStatusInfo.setAddress(permanentPopulation.address);
                    guardianStatusInfo.setHealthStatus(permanentPopulation.healthStatus);
                    return guardianStatusInfo;
                })
                .collect(Collectors.toList());
        log.info("监护人状况信息表:{}", guardianStatusInfos.size());

        log.info("========== 查询 `监狱在押人员库` 标记 `监护人状况信息表` 监护人 `是否监狱在押` ==========");
        Set<String> prisonDetaineesIdCard = prisonDetainees.stream().map(PrisonDetainees::getIdCard).collect(Collectors.toSet());
        for (GuardianStatusInfo guardianStatusInfo : guardianStatusInfos) {
            guardianStatusInfo.setIsInPrison(prisonDetaineesIdCard.contains(guardianStatusInfo.getIdCard()) ? "1" : "0");
        }
        log.info("监护人状况信息表:{}", guardianStatusInfos.size());

        log.info("========== 查询 `戒毒所人员库` 标记 `监护人状况信息表` 监护人 `是否强制戒毒` ==========");
        Set<String> drugAbusersIdCard = drugAbusers.stream().map(DrugAbuser::getIdCard).collect(Collectors.toSet());
        for (GuardianStatusInfo guardianStatusInfo : guardianStatusInfos) {
            guardianStatusInfo.setIsCompulsoryDetoxification(drugAbusersIdCard.contains(guardianStatusInfo.getIdCard()) ? "1" : "0");
        }
        log.info("监护人状况信息表:{}", guardianStatusInfos.size());

        log.info("========== 查询 `手机号信息库` 标记 `监护人状况信息表` 中监护人 `是否失联` ==========");
        Set<String> notConnectIdCard = phoneInfos.stream()
                .filter(phoneInfo -> phoneInfo.getIsConnect().equals("否")).map(PhoneInfo::getIdCard).collect(Collectors.toSet());
        for (GuardianStatusInfo guardianStatusInfo : guardianStatusInfos) {
            guardianStatusInfo.setIsLossOfContact(notConnectIdCard.contains(guardianStatusInfo.getIdCard()) ? "1" : "0");
        }
        log.info("监护人状况信息表:{}", guardianStatusInfos.size());

        log.info("========== 查询 `监护人状况信息表` 标记 `监护人状况信息表` 中监护人 `是否满足无抚养条件` ==========");
        for (GuardianStatusInfo guardianStatusInfo : guardianStatusInfos) {
            if (guardianStatusInfo.getHealthStatus().equals("重残") || guardianStatusInfo.getHealthStatus().equals("死亡")
                    || guardianStatusInfo.isInPrison.equals("1") || guardianStatusInfo.isCompulsoryDetoxification.equals("1")
                    || guardianStatusInfo.isLossOfContact.equals("1")
            ) {
                guardianStatusInfo.setIsUnSupported("1");
            } else {
                guardianStatusInfo.setIsUnSupported("0");
            }
        }
        log.info("监护人状况信息表:{}", guardianStatusInfos.size());

        List<String> guardianStatusInfoLines = new ArrayList<>(guardianStatusInfos.size() + 1);
        guardianStatusInfoLines.add("id_card,name,sex,age,address,health_status,is_in_prison,is_compulsory_detoxification,is_loss_of_contact,is_un_supported");
        List<String> guardianStatusInfoStr = guardianStatusInfos.stream().map(permanentPopulation -> String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", permanentPopulation.idCard, permanentPopulation.name, permanentPopulation.sex, permanentPopulation.age, permanentPopulation.address, permanentPopulation.healthStatus, permanentPopulation.isInPrison, permanentPopulation.isCompulsoryDetoxification, permanentPopulation.isLossOfContact, permanentPopulation.isUnSupported))
                .collect(Collectors.toList());
        Collections.shuffle(guardianStatusInfoStr);
        guardianStatusInfoLines.addAll(guardianStatusInfoStr);
        FileUtil.createFileWithContent(guardianStatusInfoLines, "监护人状况信息.csv");

        log.info("========== 联合查询  `未成年信息表`、`监护人状况信息表`，监护人一和监护人二均满足 `无抚养条件`，则将 `未成年人信息` 写入结果表 `无人抚养儿童信息表` ==========");
        Set<String> guardianIsUnSupportedIdCard = guardianStatusInfos.stream()
                .filter(guardianStatusInfo -> guardianStatusInfo.getIsUnSupported().equals("1"))
                .map(GuardianStatusInfo::getIdCard)
                .collect(Collectors.toSet());
        List<String> isUnSupportedChildIdCards = guardians.stream()
                .filter(guardian -> guardianIsUnSupportedIdCard.contains(guardian.getIdCardGuardian1()) && guardianIsUnSupportedIdCard.contains(guardian.getIdCardGuardian2()))
                .map(Guardian::getIdCard)
                .collect(Collectors.toList());
        List<PermanentPopulation> isUnSupportedChildren = children.stream()
                .filter(permanentPopulation -> isUnSupportedChildIdCards.contains(permanentPopulation.getIdCard()))
                .collect(Collectors.toList());
        log.info("无人抚养儿童信息表:{}", isUnSupportedChildren.size());

        List<String> isUnSupportedChildrenLines = new ArrayList<>(isUnSupportedChildren.size() + 1);
        isUnSupportedChildrenLines.add("id_card,name,sex,age,address");
        List<String> isUnSupportedChildrenStr = isUnSupportedChildren.stream().map(permanentPopulation -> String.format("%s,%s,%s,%s,%s", permanentPopulation.idCard, permanentPopulation.name, permanentPopulation.sex, permanentPopulation.age, permanentPopulation.address))
                .collect(Collectors.toList());
        Collections.shuffle(isUnSupportedChildrenStr);
        isUnSupportedChildrenLines.addAll(isUnSupportedChildrenStr);
        FileUtil.createFileWithContent(isUnSupportedChildrenLines, "无人抚养儿童信息.csv");
    }

    /**
     * 常驻人口
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class PermanentPopulation implements Serializable {
        private static final long serialVersionUID = 5858431011294057509L;
        @ApiModelProperty(value = "身份证号")
        String idCard;
        @ApiModelProperty(value = "姓名")
        String name;
        @ApiModelProperty(value = "性别")
        String sex;
        @ApiModelProperty(value = "年龄")
        String age;
        @ApiModelProperty(value = "住址")
        String address;
        @ApiModelProperty(value = "健康状况：（健康/重残/死亡）")
        String healthStatus;
    }

    /**
     * 监护人
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class Guardian implements Serializable {
        private static final long serialVersionUID = -4483682114065799011L;
        @ApiModelProperty(value = "身份证号")
        String idCard;
        @ApiModelProperty(value = "监护人一身份证号")
        String idCardGuardian1;
        @ApiModelProperty(value = "监护人二身份证号")
        String idCardGuardian2;
    }

    /**
     * 手机号信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class PhoneInfo implements Serializable {
        private static final long serialVersionUID = -6019704963468119772L;
        @ApiModelProperty(value = "身份证号")
        String idCard;
        @ApiModelProperty(value = "姓名")
        String name;
        @ApiModelProperty(value = "性别")
        String sex;
        @ApiModelProperty(value = "年龄")
        String age;
        @ApiModelProperty(value = "手机号")
        String phoneNum;
        @ApiModelProperty(value = "近六个月是否通联")
        String isConnect;
    }

    /**
     * 监狱在押人员
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class PrisonDetainees implements Serializable {
        private static final long serialVersionUID = -6913327945738998960L;
        @ApiModelProperty(value = "身份证号")
        String idCard;
        @ApiModelProperty(value = "姓名")
        String name;
        @ApiModelProperty(value = "性别")
        String sex;
        @ApiModelProperty(value = "年龄")
        String age;
    }

    /**
     * 戒毒所人员
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class DrugAbuser implements Serializable {
        private static final long serialVersionUID = -1235696559240586098L;
        @ApiModelProperty(value = "身份证号")
        String idCard;
        @ApiModelProperty(value = "姓名")
        String name;
        @ApiModelProperty(value = "性别")
        String sex;
        @ApiModelProperty(value = "年龄")
        String age;
    }

    /**
     * 监护人状况信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class GuardianStatusInfo implements Serializable {
        private static final long serialVersionUID = 1357038239204343080L;
        @ApiModelProperty(value = "监护人身份证号")
        String idCard;
        @ApiModelProperty(value = "监护人身份证号姓名")
        String name;
        @ApiModelProperty(value = "监护人身份证号性别")
        String sex;
        @ApiModelProperty(value = "监护人身份证号年龄")
        String age;
        @ApiModelProperty(value = "监护人身份证号住址")
        String address;
        @ApiModelProperty(value = "监护人健康状况：（健康/重残/死亡）")
        String healthStatus;
        @ApiModelProperty(value = "监护人是否监狱在押：1（是）0（否）")
        String isInPrison;
        @ApiModelProperty(value = "监护人是否强制戒毒：1（是）0（否）")
        String isCompulsoryDetoxification;
        @ApiModelProperty(value = "监护人是否失联：1（是）0（否）")
        String isLossOfContact;
        @ApiModelProperty(value = "是否满足无抚养条件：1（是）0（否）", notes = "（监护人重残/死亡||监狱在押||强制戒毒||失联）")
        String isUnSupported;
    }
}
