package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.PersonInfoUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2023/10/27
 * 社保数据模型
 */
@Slf4j
public class SocialSecuritySZModel {

    public static void main(String[] args) throws IOException {
        int count = 100;
        List<SocialSecuritySZInfo> socialSecuritySZInfos = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            SocialSecuritySZInfo socialSecuritySZInfo = new SocialSecuritySZInfo();
            generateInfo(socialSecuritySZInfo, new HashSet<>(count));
            socialSecuritySZInfos.add(socialSecuritySZInfo);
        }
//        log.info("socialSecuritySZInfos:{}", JSONObject.toJSONString(socialSecuritySZInfos));
        List<String> socialSecuritySZInfosLines = new ArrayList<>(socialSecuritySZInfos.size() + 1);
//        String schema = ("NAME,ID_NO,ID_CODE,LAST_UNIT_NAME,LAST_UNIT_KIND,STATUS,YANGL_BASE,OPEN_DATA," +
//                "YANGL_SECURITY_CAT,YANGL_LAST_DATE,YANGL_FIRST_DATE,YANGL_MONTH_BASE,YANGL_OWN,YANGL_UNIT,YANGL_CTMS,YANGL_TMS," +
//                "YUL_SECURITY_CAT,YUL_LAST_DATE,YUL_FIRST_DATE,YUL_MONTH_BASE,YUL_OWN,YUL_UNIT,YUL_CTMS,YUL_TMS," +
//                "GONGS_LAST_DATE,GONGS_FIRST_DATE,GONGS_MONTH_BASE,GONGS_OWN,GONGS_UNIT,GONGS_CTMS,GONGS_TMS," +
//                "SHIY_LAST_DATE,SHIY_FIRST_DATE,SHIY_MONTH_BASE,SHIY_OWN,SHIY_UNIT,SHIY_CTMS,SHIY_TMS," +
//                "SHENGY_LAST_DATE,SHENGY_FIRST_DATE,SHENGY_MONTH_BASE,SHENGY_OWN,SHENGY_UNIT,SHENGY_CTMS,SHENGY_TMS").toLowerCase();
        String schema = ("NAME,ID_NO,ID_CODE,LAST_UNIT_NAME,LAST_UNIT_KIND,STATUS,YANGL_BASE,OPEN_DATA," +
                "YANGL_SECURITY_CAT,YANGL_LAST_DATE,YANGL_FIRST_DATE,YANGL_MONTH_BASE,YANGL_OWN,YANGL_UNIT,YANGL_CTMS,YANGL_TMS," +
                "YUL_SECURITY_CAT,YUL_LAST_DATE,YUL_FIRST_DATE,YUL_MONTH_BASE,YUL_OWN,YUL_UNIT,YUL_CTMS,YUL_TMS," +
                "GONGS_MONTH_BASE,GONGS_OWN,GONGS_UNIT," +
                "SHIY_MONTH_BASE,SHIY_OWN,SHIY_UNIT," +
                "SHENGY_MONTH_BASE,SHENGY_OWN,SHENGY_UNIT").toLowerCase();
        String schema2 = "name,id_no,id_code,last_unit_name,last_unit_kind,status,yangl_base,open_data,yangl_sec_cat,yangl_last_date,yangl_f_date,yangl_mon_base,yangl_own,yangl_unit,yangl_ctms,yangl_tms,yul_sec_cat,yul_last_date,yul_first_date,yul_month_base,yul_own,yul_unit,yul_ctms,yul_tms,gongs_mon_base,gongs_own,gongs_unit,shiy_month_base,shiy_own,shiy_unit,shengy_mon_base,shengy_own,shengy_unit";
        socialSecuritySZInfosLines.add(schema);
        String[] schemaArr = schema.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < schemaArr.length; i++) {
            stringBuilder.append(i == schemaArr.length - 1 ? "%s" : "%s" + ",");
            stringBuilder2.append("`");
            stringBuilder2.append(schemaArr[i]);
            stringBuilder2.append("`");
            stringBuilder2.append(i == schemaArr.length - 1 ? "" : ",");
        }
        log.info("stringBuilder2:{}", stringBuilder2);
        log.info("schemaArr:{}", JSONObject.toJSONString(schemaArr));
        log.info("schemaArr.length={}", schemaArr.length);
        String format = stringBuilder.toString();
//        List<String> socialSecuritySZInfosStr = socialSecuritySZInfos.stream()
//                .map(socialSecuritySZInfo -> String.format(format, socialSecuritySZInfo.getNAME(), socialSecuritySZInfo.getID_NO(), socialSecuritySZInfo.getID_CODE(), socialSecuritySZInfo.getLAST_UNIT_NAME(), socialSecuritySZInfo.getLAST_UNIT_KIND(), socialSecuritySZInfo.getSTATUS(), socialSecuritySZInfo.getYANGL_BASE(), socialSecuritySZInfo.getOPEN_DATA(),
//                        socialSecuritySZInfo.getYANGL_SECURITY_CAT(), socialSecuritySZInfo.getYANGL_LAST_DATE(), socialSecuritySZInfo.getYANGL_FIRST_DATE(), socialSecuritySZInfo.getYANGL_MONTH_BASE(), socialSecuritySZInfo.getYANGL_OWN(), socialSecuritySZInfo.getYANGL_UNIT(), socialSecuritySZInfo.getYANGL_CTMS(), socialSecuritySZInfo.getYANGL_TMS(),
//                        socialSecuritySZInfo.getYUL_SECURITY_CAT(), socialSecuritySZInfo.getYUL_LAST_DATE(), socialSecuritySZInfo.getYUL_FIRST_DATE(), socialSecuritySZInfo.getYUL_MONTH_BASE(), socialSecuritySZInfo.getGONGS_OWN(), socialSecuritySZInfo.getGONGS_UNIT(), socialSecuritySZInfo.getGONGS_CTMS(), socialSecuritySZInfo.getGONGS_TMS(),
//                        socialSecuritySZInfo.getGONGS_LAST_DATE(), socialSecuritySZInfo.getGONGS_FIRST_DATE(), socialSecuritySZInfo.getGONGS_MONTH_BASE(), socialSecuritySZInfo.getGONGS_OWN(), socialSecuritySZInfo.getGONGS_UNIT(), socialSecuritySZInfo.getGONGS_CTMS(), socialSecuritySZInfo.getGONGS_TMS(),
//                        socialSecuritySZInfo.getSHIY_LAST_DATE(), socialSecuritySZInfo.getSHIY_FIRST_DATE(), socialSecuritySZInfo.getSHIY_MONTH_BASE(), socialSecuritySZInfo.getSHIY_OWN(), socialSecuritySZInfo.getSHIY_UNIT(), socialSecuritySZInfo.getSHIY_CTMS(), socialSecuritySZInfo.getSHIY_TMS(),
//                        socialSecuritySZInfo.getSHENGY_LAST_DATE(), socialSecuritySZInfo.getSHENGY_FIRST_DATE(), socialSecuritySZInfo.getSHENGY_MONTH_BASE(), socialSecuritySZInfo.getSHENGY_OWN(), socialSecuritySZInfo.getSHENGY_UNIT(), socialSecuritySZInfo.getSHENGY_CTMS(), socialSecuritySZInfo.getSHENGY_TMS()))
//                .collect(Collectors.toList());
        List<String> socialSecuritySZInfosStr = socialSecuritySZInfos.stream()
                .map(socialSecuritySZInfo -> String.format(format, socialSecuritySZInfo.getNAME(), socialSecuritySZInfo.getID_NO(), socialSecuritySZInfo.getID_CODE(), socialSecuritySZInfo.getLAST_UNIT_NAME(), socialSecuritySZInfo.getLAST_UNIT_KIND(), socialSecuritySZInfo.getSTATUS(), socialSecuritySZInfo.getYANGL_BASE(), socialSecuritySZInfo.getOPEN_DATA(),
                        socialSecuritySZInfo.getYANGL_SECURITY_CAT(), socialSecuritySZInfo.getYANGL_LAST_DATE(), socialSecuritySZInfo.getYANGL_FIRST_DATE(), socialSecuritySZInfo.getYANGL_MONTH_BASE(), socialSecuritySZInfo.getYANGL_OWN(), socialSecuritySZInfo.getYANGL_UNIT(), socialSecuritySZInfo.getYANGL_CTMS(), socialSecuritySZInfo.getYANGL_TMS(),
                        socialSecuritySZInfo.getYUL_SECURITY_CAT(), socialSecuritySZInfo.getYUL_LAST_DATE(), socialSecuritySZInfo.getYUL_FIRST_DATE(), socialSecuritySZInfo.getYUL_MONTH_BASE(), socialSecuritySZInfo.getGONGS_OWN(), socialSecuritySZInfo.getGONGS_UNIT(), socialSecuritySZInfo.getGONGS_CTMS(), socialSecuritySZInfo.getGONGS_TMS(),
                        socialSecuritySZInfo.getGONGS_MONTH_BASE(), socialSecuritySZInfo.getGONGS_OWN(), socialSecuritySZInfo.getGONGS_UNIT(),
                        socialSecuritySZInfo.getSHIY_MONTH_BASE(), socialSecuritySZInfo.getSHIY_OWN(), socialSecuritySZInfo.getSHIY_UNIT(),
                        socialSecuritySZInfo.getSHENGY_MONTH_BASE(), socialSecuritySZInfo.getSHENGY_OWN(), socialSecuritySZInfo.getSHENGY_UNIT()))
                .collect(Collectors.toList());
        socialSecuritySZInfosLines.addAll(socialSecuritySZInfosStr);
        FileUtil.createFileWithContent(socialSecuritySZInfosLines, "个人社保缴费信息.csv");
    }

    public static void generateInfo(SocialSecuritySZInfo socialSecuritySZInfo, Set<String> idCards) {
        PersonInfoUtil.PersonInfo personInfo = new PersonInfoUtil.PersonInfo();
        PersonInfoUtil.generateIdCard(personInfo);
        while (idCards.contains(personInfo.getIdCard())) {
            PersonInfoUtil.generateIdCard(personInfo);
        }
        PersonInfoUtil.generateName(personInfo);
        socialSecuritySZInfo.setNAME(personInfo.getName());
        socialSecuritySZInfo.setID_NO(personInfo.getIdCard());
        socialSecuritySZInfo.setID_CODE("0102");
        String[] LAST_UNIT_NAME = new String[]{"飞天科技", "金蚂蚁科技", "智慧树科技", "创新未来科技", "高新科技领导者", "科技领航者", "智能先锋", "优品科技", "个人缴费智能"};
        socialSecuritySZInfo.setLAST_UNIT_NAME("深圳" + LAST_UNIT_NAME[new Random().nextInt(LAST_UNIT_NAME.length)] + "有限公司");
        String[] LAST_UNIT_KIND = new String[]{"机关", "事业", "企业"};
        socialSecuritySZInfo.setLAST_UNIT_KIND(LAST_UNIT_KIND[new Random().nextInt(LAST_UNIT_KIND.length)]);
        String[] STATUS = new String[]{"正常缴费", "参保正常", "暂停参保", "停止参保"};
        socialSecuritySZInfo.setSTATUS(STATUS[new Random().nextInt(STATUS.length)]);
        Random random = new Random();
        socialSecuritySZInfo.setYANGL_BASE(String.valueOf(BigDecimal.valueOf(random.nextDouble() + random.nextInt(25000) + 1).setScale(2, RoundingMode.HALF_UP)));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date LAST_DATE = calendar.getTime();
        if (day <= 15) {
            calendar.add(Calendar.MONTH, -1);
            LAST_DATE = calendar.getTime();
        }
        int CTMS = random.nextInt(36) + 1;
        calendar.add(Calendar.MONTH, -CTMS);
        Date FIRST_DATE = calendar.getTime();
        socialSecuritySZInfo.setOPEN_DATA(simpleDateFormat.format(FIRST_DATE) + "/15");
        String[] SECURITY_CAT_ARR = new String[]{"城镇职工", "城乡居民"};
        String SECURITY_CAT = SECURITY_CAT_ARR[new Random().nextInt(SECURITY_CAT_ARR.length)];
        socialSecuritySZInfo.setYANGL_SECURITY_CAT(SECURITY_CAT);
        socialSecuritySZInfo.setYANGL_LAST_DATE(simpleDateFormat.format(LAST_DATE) + "/15");
        socialSecuritySZInfo.setYANGL_FIRST_DATE(simpleDateFormat.format(FIRST_DATE) + "/15");
        double YANGL_OWN = random.nextDouble() + random.nextInt(3000) + 1;
        double YANGL_UNIT = random.nextDouble() + random.nextInt(5000) + 1;
        socialSecuritySZInfo.setYANGL_OWN(String.valueOf(BigDecimal.valueOf(YANGL_OWN).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYANGL_UNIT(String.valueOf(BigDecimal.valueOf(YANGL_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYANGL_MONTH_BASE(String.valueOf(BigDecimal.valueOf(YANGL_OWN + YANGL_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYANGL_CTMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setYANGL_TMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setYUL_SECURITY_CAT(SECURITY_CAT);
        socialSecuritySZInfo.setYUL_LAST_DATE(simpleDateFormat.format(LAST_DATE) + "/15");
        socialSecuritySZInfo.setYUL_FIRST_DATE(simpleDateFormat.format(FIRST_DATE) + "/15");
        double YUL_OWN = random.nextDouble() + random.nextInt(1000) + 1;
        double YUL_UNIT = random.nextDouble() + random.nextInt(3000) + 1;
        socialSecuritySZInfo.setYUL_OWN(String.valueOf(BigDecimal.valueOf(YUL_OWN).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYUL_UNIT(String.valueOf(BigDecimal.valueOf(YUL_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYUL_MONTH_BASE(String.valueOf(BigDecimal.valueOf(YUL_OWN + YUL_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setYUL_CTMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setYUL_TMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setGONGS_LAST_DATE(simpleDateFormat.format(LAST_DATE) + "/15");
        socialSecuritySZInfo.setGONGS_FIRST_DATE(simpleDateFormat.format(FIRST_DATE) + "/15");
        double GONGS_OWN = random.nextDouble() + random.nextInt(1000) + 1;
        double GONGS_UNIT = random.nextDouble() + random.nextInt(3000) + 1;
        socialSecuritySZInfo.setGONGS_OWN(String.valueOf(BigDecimal.valueOf(GONGS_OWN).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setGONGS_UNIT(String.valueOf(BigDecimal.valueOf(GONGS_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setGONGS_MONTH_BASE(String.valueOf(BigDecimal.valueOf(GONGS_OWN + GONGS_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setGONGS_CTMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setGONGS_TMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setSHIY_LAST_DATE(simpleDateFormat.format(LAST_DATE) + "/15");
        socialSecuritySZInfo.setSHIY_FIRST_DATE(simpleDateFormat.format(FIRST_DATE) + "/15");
        double SHIY_OWN = random.nextDouble() + random.nextInt(1000) + 1;
        double SHIY_UNIT = random.nextDouble() + random.nextInt(3000) + 1;
        socialSecuritySZInfo.setSHIY_OWN(String.valueOf(BigDecimal.valueOf(SHIY_OWN).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHIY_UNIT(String.valueOf(BigDecimal.valueOf(SHIY_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHIY_MONTH_BASE(String.valueOf(BigDecimal.valueOf(SHIY_OWN + SHIY_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHIY_CTMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setSHIY_TMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setSHENGY_LAST_DATE(simpleDateFormat.format(LAST_DATE) + "/15");
        socialSecuritySZInfo.setSHENGY_FIRST_DATE(simpleDateFormat.format(FIRST_DATE) + "/15");
        double SHENGY_OWN = random.nextDouble() + random.nextInt(1000) + 1;
        double SHENGY_UNIT = random.nextDouble() + random.nextInt(3000) + 1;
        socialSecuritySZInfo.setSHENGY_OWN(String.valueOf(BigDecimal.valueOf(SHENGY_OWN).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHENGY_UNIT(String.valueOf(BigDecimal.valueOf(SHENGY_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHENGY_MONTH_BASE(String.valueOf(BigDecimal.valueOf(SHENGY_OWN + SHENGY_UNIT).setScale(2, RoundingMode.HALF_UP)));
        socialSecuritySZInfo.setSHENGY_CTMS(String.valueOf(CTMS));
        socialSecuritySZInfo.setSHENGY_TMS(String.valueOf(CTMS));
    }

    /**
     * 社保信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SocialSecuritySZInfo implements Serializable {
        private static final long serialVersionUID = -2946597149536943741L;
        @ApiModelProperty(value = "是否查询到缴存数据", example = "01")
        String DT_FLG;
        @ApiModelProperty(value = "查询机构编号", example = "11440304MB2D26978H")
        String B_CODE;
        @ApiModelProperty(value = "查询机构名称", example = "深圳市福田区政务服务数据管理局")
        String B_NAME;
        @ApiModelProperty(value = "姓名", example = "王小茉")
        String NAME;
        @ApiModelProperty(value = "证件号码", example = "360502198854699999")
        String ID_NO;
        @ApiModelProperty(value = "证件类型", example = "0102")
        String ID_CODE;
        @ApiModelProperty(value = "当前缴存单位名称", example = "深圳******技术有限公司")
        String LAST_UNIT_NAME;
        @ApiModelProperty(value = "当前缴存单位性质", example = "企业")
        String LAST_UNIT_KIND;
        @ApiModelProperty(value = "当前账户状态", example = "02")
        String STATUS;
        @ApiModelProperty(value = "社保缴费基数", example = "14")
        String YANGL_BASE;
        @ApiModelProperty(value = "社保账户开户年月日", example = "2020/06/01")
        String OPEN_DATA;
        @ApiModelProperty(value = "（养老保险）参保性质", example = "01")
        String YANGL_SECURITY_CAT;
        @ApiModelProperty(value = "（养老保险）最近缴存年月日", example = "2023/07/01")
        String YANGL_LAST_DATE;
        @ApiModelProperty(value = "（养老保险）初始缴存年月日", example = "2020/06/01")
        String YANGL_FIRST_DATE;
        @ApiModelProperty(value = "（养老保险）月缴存额", example = "6")
        String YANGL_MONTH_BASE;
        @ApiModelProperty(value = "（养老保险）个人月缴费额", example = "6")
        String YANGL_OWN;
        @ApiModelProperty(value = "（养老保险）单位月缴费额", example = "10")
        String YANGL_UNIT;
        @ApiModelProperty(value = "（养老保险）最近连续缴存次数", example = "5")
        String YANGL_CTMS;
        @ApiModelProperty(value = "（养老保险）累计缴存次数", example = "5")
        String YANGL_TMS;
        @ApiModelProperty(value = "（医疗保险）参保性质", example = "01")
        String YUL_SECURITY_CAT;
        @ApiModelProperty(value = "（医疗保险）最近缴存年月日", example = "2023/07/01")
        String YUL_LAST_DATE;
        @ApiModelProperty(value = "（医疗保险）初始缴存年月日", example = "2020/06/01")
        String YUL_FIRST_DATE;
        @ApiModelProperty(value = "（医疗保险）月缴存额", example = "6")
        String YUL_MONTH_BASE;
        @ApiModelProperty(value = "（医疗保险）个人月缴费额", example = "4")
        String YUL_OWN;
        @ApiModelProperty(value = "（医疗保险）单位月缴费额", example = "9")
        String YUL_UNIT;
        @ApiModelProperty(value = "（医疗保险）最近连续缴存次数", example = "5")
        String YUL_CTMS;
        @ApiModelProperty(value = "（医疗保险）累计缴存次数", example = "5")
        String YUL_TMS;
        @ApiModelProperty(value = "（工伤保险）最近缴存年月日", example = "2023/07/01")
        String GONGS_LAST_DATE;
        @ApiModelProperty(value = "（工伤保险）初始缴存年月日", example = "2020/06/01")
        String GONGS_FIRST_DATE;
        @ApiModelProperty(value = "（工伤保险）月缴存额", example = "1")
        String GONGS_MONTH_BASE;
        @ApiModelProperty(value = "（工伤保险）个人月缴费额", example = "-99")
        String GONGS_OWN;
        @ApiModelProperty(value = "（工伤保险）单位月缴费额", example = "1")
        String GONGS_UNIT;
        @ApiModelProperty(value = "（工伤保险）最近连续缴存次数", example = "5")
        String GONGS_CTMS;
        @ApiModelProperty(value = "（工伤保险）累计缴存次数", example = "5")
        String GONGS_TMS;
        @ApiModelProperty(value = "（失业保险）最近缴存年月日", example = "2023/07/01")
        String SHIY_LAST_DATE;
        @ApiModelProperty(value = "（失业保险）初始缴存年月日", example = "2020/06/01")
        String SHIY_FIRST_DATE;
        @ApiModelProperty(value = "（失业保险）月缴存额", example = "1")
        String SHIY_MONTH_BASE;
        @ApiModelProperty(value = "（失业保险）个人月缴费额", example = "1")
        String SHIY_OWN;
        @ApiModelProperty(value = "（失业保险）单位月缴费额", example = "1")
        String SHIY_UNIT;
        @ApiModelProperty(value = "（失业保险）最近连续缴存次数", example = "5")
        String SHIY_CTMS;
        @ApiModelProperty(value = "（失业保险）累计缴存次数", example = "5")
        String SHIY_TMS;
        @ApiModelProperty(value = "（生育保险）最近缴存年月日", example = "2023/07/01")
        String SHENGY_LAST_DATE;
        @ApiModelProperty(value = "（生育保险）初始缴存年月日", example = "2020/06/01")
        String SHENGY_FIRST_DATE;
        @ApiModelProperty(value = "（生育保险）月缴存额", example = "3")
        String SHENGY_MONTH_BASE;
        @ApiModelProperty(value = "（生育保险）个人月缴费额", example = "-99")
        String SHENGY_OWN;
        @ApiModelProperty(value = "（生育保险）单位月缴费额", example = "3")
        String SHENGY_UNIT;
        @ApiModelProperty(value = "（生育保险）最近连续缴存次数", example = "5")
        String SHENGY_CTMS;
        @ApiModelProperty(value = "（生育保险）累计缴存次数", example = "5")
        String SHENGY_TMS;

        @JsonProperty("DT_FLG")
        public String getDT_FLG() {
            return DT_FLG;
        }

        @JsonProperty("B_CODE")
        public String getB_CODE() {
            return B_CODE;
        }

        @JsonProperty("B_NAME")
        public String getB_NAME() {
            return B_NAME;
        }

        @JsonProperty("NAME")
        public String getNAME() {
            return NAME;
        }

        @JsonProperty("ID_NO")
        public String getID_NO() {
            return ID_NO;
        }

        @JsonProperty("ID_CODE")
        public String getID_CODE() {
            return ID_CODE;
        }

        @JsonProperty("LAST_UNIT_NAME")
        public String getLAST_UNIT_NAME() {
            return LAST_UNIT_NAME;
        }

        @JsonProperty("LAST_UNIT_KIND")
        public String getLAST_UNIT_KIND() {
            return LAST_UNIT_KIND;
        }

        @JsonProperty("STATUS")
        public String getSTATUS() {
            return STATUS;
        }

        @JsonProperty("YANGL_BASE")
        public String getYANGL_BASE() {
            return YANGL_BASE;
        }

        @JsonProperty("OPEN_DATA")
        public String getOPEN_DATA() {
            return OPEN_DATA;
        }

        @JsonProperty("YANGL_SECURITY_CAT")
        public String getYANGL_SECURITY_CAT() {
            return YANGL_SECURITY_CAT;
        }

        @JsonProperty("YANGL_LAST_DATE")
        public String getYANGL_LAST_DATE() {
            return YANGL_LAST_DATE;
        }

        @JsonProperty("YANGL_FIRST_DATE")
        public String getYANGL_FIRST_DATE() {
            return YANGL_FIRST_DATE;
        }

        @JsonProperty("YANGL_MONTH_BASE")
        public String getYANGL_MONTH_BASE() {
            return YANGL_MONTH_BASE;
        }

        @JsonProperty("YANGL_OWN")
        public String getYANGL_OWN() {
            return YANGL_OWN;
        }

        @JsonProperty("YANGL_UNIT")
        public String getYANGL_UNIT() {
            return YANGL_UNIT;
        }

        @JsonProperty("YANGL_CTMS")
        public String getYANGL_CTMS() {
            return YANGL_CTMS;
        }

        @JsonProperty("YANGL_TMS")
        public String getYANGL_TMS() {
            return YANGL_TMS;
        }

        @JsonProperty("YUL_SECURITY_CAT")
        public String getYUL_SECURITY_CAT() {
            return YUL_SECURITY_CAT;
        }

        @JsonProperty("YUL_LAST_DATE")
        public String getYUL_LAST_DATE() {
            return YUL_LAST_DATE;
        }

        @JsonProperty("YUL_FIRST_DATE")
        public String getYUL_FIRST_DATE() {
            return YUL_FIRST_DATE;
        }

        @JsonProperty("YUL_MONTH_BASE")
        public String getYUL_MONTH_BASE() {
            return YUL_MONTH_BASE;
        }

        @JsonProperty("YUL_OWN")
        public String getYUL_OWN() {
            return YUL_OWN;
        }

        @JsonProperty("YUL_UNIT")
        public String getYUL_UNIT() {
            return YUL_UNIT;
        }

        @JsonProperty("YUL_CTMS")
        public String getYUL_CTMS() {
            return YUL_CTMS;
        }

        @JsonProperty("YUL_TMS")
        public String getYUL_TMS() {
            return YUL_TMS;
        }

        @JsonProperty("GONGS_LAST_DATE")
        public String getGONGS_LAST_DATE() {
            return GONGS_LAST_DATE;
        }

        @JsonProperty("GONGS_FIRST_DATE")
        public String getGONGS_FIRST_DATE() {
            return GONGS_FIRST_DATE;
        }

        @JsonProperty("GONGS_MONTH_BASE")
        public String getGONGS_MONTH_BASE() {
            return GONGS_MONTH_BASE;
        }

        @JsonProperty("GONGS_OWN")
        public String getGONGS_OWN() {
            return GONGS_OWN;
        }

        @JsonProperty("GONGS_UNIT")
        public String getGONGS_UNIT() {
            return GONGS_UNIT;
        }

        @JsonProperty("GONGS_CTMS")
        public String getGONGS_CTMS() {
            return GONGS_CTMS;
        }

        @JsonProperty("GONGS_TMS")
        public String getGONGS_TMS() {
            return GONGS_TMS;
        }

        @JsonProperty("SHIY_LAST_DATE")
        public String getSHIY_LAST_DATE() {
            return SHIY_LAST_DATE;
        }

        @JsonProperty("SHIY_FIRST_DATE")
        public String getSHIY_FIRST_DATE() {
            return SHIY_FIRST_DATE;
        }

        @JsonProperty("SHIY_MONTH_BASE")
        public String getSHIY_MONTH_BASE() {
            return SHIY_MONTH_BASE;
        }

        @JsonProperty("SHIY_OWN")
        public String getSHIY_OWN() {
            return SHIY_OWN;
        }

        @JsonProperty("SHIY_UNIT")
        public String getSHIY_UNIT() {
            return SHIY_UNIT;
        }

        @JsonProperty("SHIY_CTMS")
        public String getSHIY_CTMS() {
            return SHIY_CTMS;
        }

        @JsonProperty("SHIY_TMS")
        public String getSHIY_TMS() {
            return SHIY_TMS;
        }

        @JsonProperty("SHENGY_LAST_DATE")
        public String getSHENGY_LAST_DATE() {
            return SHENGY_LAST_DATE;
        }

        @JsonProperty("SHENGY_FIRST_DATE")
        public String getSHENGY_FIRST_DATE() {
            return SHENGY_FIRST_DATE;
        }

        @JsonProperty("SHENGY_MONTH_BASE")
        public String getSHENGY_MONTH_BASE() {
            return SHENGY_MONTH_BASE;
        }

        @JsonProperty("SHENGY_OWN")
        public String getSHENGY_OWN() {
            return SHENGY_OWN;
        }

        @JsonProperty("SHENGY_UNIT")
        public String getSHENGY_UNIT() {
            return SHENGY_UNIT;
        }

        @JsonProperty("SHENGY_CTMS")
        public String getSHENGY_CTMS() {
            return SHENGY_CTMS;
        }

        @JsonProperty("SHENGY_TMS")
        public String getSHENGY_TMS() {
            return SHENGY_TMS;
        }
    }
}
