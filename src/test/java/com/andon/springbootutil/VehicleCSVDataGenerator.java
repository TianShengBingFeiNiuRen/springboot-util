package com.andon.springbootutil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Andon
 * 2024/5/23
 */
public class VehicleCSVDataGenerator {

    public static void main(String[] args) {
        // 车辆基本信息
        String vehicles = "车辆基本信息.csv";
        List<String> vinList = generateVehicleData(vehicles, 1000);

        // 车辆违章数据
        String violations = "车辆违章数据.csv";
        generateViolationData(vinList, violations);

        // 车辆维修数据
        String maintenances = "车辆维修数据.csv";
        generateMaintenanceData(vinList, maintenances);

        // 车辆保险数据
        String insurances = "车辆保险数据.csv";
        generateInsuranceData(vinList, insurances);
    }

    public static void generateInsuranceData(List<String> vinList, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            // 写入CSV的标题
//            writer.append("车架号,是否有效商业险,商业险是否连续投保,出保次数,出保时间,出保地点,出保金额\n");
            writer.append("vin,sf_yx_syx,syx_sf_lx,cb_cs,cb_sj,cb_dd,cb_je\n");

            List<String> newVinList = new ArrayList<>(vinList);

            // 生成指定数量的记录
            for (String vin : newVinList) {
                // 生成随机车架号，这里仅为示例，实际车架号有固定格式
                // 生成是否有效商业险
                String isCommercialCoverageValid = "是";
                // 生成商业险是否连续投保
                String isContinuousCommercialCoverage = generateRandomBoolean() ? "是" : "否";
                // 生成出保次数
                int claimCount = new Random().nextInt(10) + 1; // 出保次数在1到10次之间
                // 生成出保时间
                String claimTime = generateRandomDate();
                // 生成出保地点
                String claimLocation = generateRandomClaimLocation();
                // 生成出保金额
                double claimAmount = new Random().nextDouble() * 50000 + 5000; // 出保金额在5000到55000之间

                // 将数据写入CSV文件
                writer.append(vin)
                        .append(",")
                        .append(isCommercialCoverageValid)
                        .append(",")
                        .append(isContinuousCommercialCoverage)
                        .append(",")
                        .append(String.valueOf(claimCount))
                        .append(",")
                        .append(claimTime)
                        .append(",")
                        .append(claimLocation)
                        .append(",")
                        .append(String.valueOf(claimAmount))
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 生成随机布尔值
    private static boolean generateRandomBoolean() {
        return new Random().nextBoolean();
    }

    // 生成随机出保时间
    private static String generateRandomDate() {
        Random random = new Random();
        int daysToAdd = random.nextInt(3650); // 假设随机日期在最近10年内
        Date date = new Date(new Date().getTime() + daysToAdd * 24 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    // 生成随机出保地点
    private static String generateRandomClaimLocation() {
        String[] locations = {"北京", "上海", "广州", "深圳", "成都", "西安", "杭州", "重庆", "武汉", "苏州"};
        Random random = new Random();
        return locations[random.nextInt(locations.length)];
    }

    public static void generateMaintenanceData(List<String> vinList, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            // 写入CSV的标题
//            writer.append("车架号,维修日期,故障诊断,维修项目,维修配件,维修工时\n");
            writer.append("vin,wx_ri,gz_zd,wx_xm,wx_pj,wx_gs\n");

            List<String> newVinList = new ArrayList<>(vinList);
            Collections.shuffle(newVinList);
            int expansionFactor = 2; // 扩容因子
            int expansionSize = vinList.size() / 3; // 需要扩容的部分大小
            // 随机扩容列表的一部分
            expandRandomPart(newVinList, expansionFactor, expansionSize);

            // 生成指定数量的记录
            for (String vin : newVinList) {
                // 生成随机车架号，这里仅为示例，实际车架号有固定格式
                // 生成随机维修日期
                String maintenanceDate = generateRandomMaintenanceDate();
                // 生成随机故障诊断
                String faultDiagnosis = generateRandomFaultDiagnosis();
                // 生成随机维修项目
                String maintenanceItem = generateRandomMaintenanceItem();
                // 生成随机维修配件
                String maintenanceParts = generateRandomMaintenanceParts();
                // 生成随机维修工时
                int maintenanceHours = generateRandomMaintenanceHours();

                // 将数据写入CSV文件
                writer.append(vin)
                        .append(",")
                        .append(maintenanceDate)
                        .append(",")
                        .append(faultDiagnosis)
                        .append(",")
                        .append(maintenanceItem)
                        .append(",")
                        .append(maintenanceParts)
                        .append(",")
                        .append(String.valueOf(maintenanceHours))
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 生成随机维修日期
    private static String generateRandomMaintenanceDate() {
        Random random = new Random();
        int daysToAdd = random.nextInt(3650); // 假设随机日期在最近10年内
        Date date = new Date(new Date().getTime() + daysToAdd * 24 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    // 生成随机故障诊断
    private static String generateRandomFaultDiagnosis() {
        String[] diagnoses = {"发动机故障", "刹车系统问题", "轮胎磨损", "电池老化", "电气系统故障"};
        Random random = new Random();
        return diagnoses[random.nextInt(diagnoses.length)];
    }

    // 生成随机维修项目
    private static String generateRandomMaintenanceItem() {
        String[] items = {"更换机油", "更换刹车片", "轮胎平衡", "电池更换", "电气系统检查"};
        Random random = new Random();
        return items[random.nextInt(items.length)];
    }

    // 生成随机维修配件
    private static String generateRandomMaintenanceParts() {
        String[] parts = {"机油", "刹车片", "轮胎", "电池", "火花塞"};
        Random random = new Random();
        return parts[random.nextInt(parts.length)];
    }

    // 生成随机维修工时
    private static int generateRandomMaintenanceHours() {
        Random random = new Random();
        return random.nextInt(10) + 1; // 维修工时在1到10小时之间
    }

    // 随机扩容列表中的一部分元素
    private static void expandRandomPart(List<String> list, int expansionFactor, int partSize) {
        // 创建一个随机对象
        Random random = new Random();

        // 扩容列表的一部分
        for (int i = 0; i < partSize; i++) {
            // 随机选择一个元素进行扩容
            int randomIndex = random.nextInt(list.size());
            for (int j = 1; j < expansionFactor; j++) {
                // 在随机选中的元素后面插入扩容的元素
                list.add(randomIndex + j, list.get(randomIndex));
            }
        }
    }

    public static void generateViolationData(List<String> vinList, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            // 写入CSV的标题
//            writer.append("车架号,违章内容,违章发生时间,违章类型\n");
            writer.append("vin,wz_nr,wz_sj,wz_lx\n");

            List<String> newVinList = new ArrayList<>(vinList);
            Collections.shuffle(newVinList);
            int expansionFactor = 2; // 扩容因子
            int expansionSize = vinList.size() / 3; // 需要扩容的部分大小
            // 随机扩容列表的一部分
            expandRandomPart(newVinList, expansionFactor, expansionSize);

            // 生成指定数量的记录
            for (String vin : newVinList) {
                // 车架号，这里仅为示例，实际车架号有固定格式
                // 生成随机违章内容
                String violationContent = generateRandomViolationContent();
                // 生成随机违章发生时间
                String violationTime = generateRandomViolationTime();
                // 生成随机违章类型
                String violationType = generateRandomViolationType();

                // 将数据写入CSV文件
                writer.append(vin)
                        .append(",")
                        .append(violationContent)
                        .append(",")
                        .append(violationTime)
                        .append(",")
                        .append(violationType)
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 生成随机违章内容
    private static String generateRandomViolationContent() {
        String[] contents = {"超速", "闯红灯", "违章停车", "未系安全带", "酒驾"};
        Random random = new Random();
        return contents[random.nextInt(contents.length)];
    }

    // 生成随机违章发生时间
    private static String generateRandomViolationTime() {
        Random random = new Random();
        int year = 2020 + random.nextInt(5); // 2020-2024
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28); // 简化为28天
        int hour = 0 + random.nextInt(24);
        int minute = 0 + random.nextInt(60);
        return String.format("%d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
    }

    // 生成随机违章类型
    private static String generateRandomViolationType() {
        String[] types = {"交通信号灯", "交通标志", "交通标线", "交通管制"};
        Random random = new Random();
        return types[random.nextInt(types.length)];
    }

    public static List<String> generateVehicleData(String fileName, int records) {
        List<String> vinList = new ArrayList<>(records);
        try (Writer writer = new FileWriter(fileName)) {
            // 写入CSV的标题
//            writer.append("车架号,车辆所属人姓名,首次登记日期,车辆交易次数\n");
            writer.append("vin,cl_ssr_xm,sc_dj_sj,jy_cs\n");

            Random random = new Random();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            // 生成指定数量的记录
            for (int i = 0; i < records; i++) {
                // 生成随机车架号，这里仅为示例，实际车架号有固定格式
                String vin = "V" + randomString(6) + randomString(10) + randomString(5);
                vinList.add(vin);
                // 随机生成中文姓名
                String name = generateRandomName();
                // 生成随机首次登记日期，这里简化为过去几年内的日期
                calendar.add(Calendar.YEAR, -random.nextInt(10));
                String registrationDate = dateFormat.format(calendar.getTime());
                // 生成随机车辆交易次数
                int transactionCount = random.nextInt(10);

                // 将数据写入CSV文件
                writer.append(vin)
                        .append(",")
                        .append(name)
                        .append(",")
                        .append(registrationDate)
                        .append(",")
                        .append(String.valueOf(transactionCount))
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vinList;
    }

    // 生成指定长度的随机字符串
    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(36);
            char c = (randomChar < 10) ? (char) ('0' + randomChar) : (char) ('A' + randomChar - 10);
            sb.append(c);
        }
        return sb.toString();
    }

    private static String generateRandomName() {
        // 随机生成中文姓名，这里只是示例，实际中姓名生成要复杂得多
        String familyName = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻水云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳鲍史唐费岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计成戴宋茅庞熊纪舒屈项祝董粱杜阮席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田胡凌霍万柯卢莫房缪干解应宗丁宣邓郁单杭洪包诸左石崔吉龚程邢滑裴陆荣翁荀羊甄家封芮储靳邴松井富乌焦巴弓牧隗山谷车侯伊宁仇祖武符刘景詹束龙叶幸司韶黎乔苍双闻莘劳逄姬冉宰桂牛寿通边燕冀尚农温庄晏瞿茹习鱼容向古戈终居衡步都耿满弘国文东殴沃曾关红游盖益桓公晋楚闫";
        String girlName = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽佩阳宣雨";
        String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘洋宣宇";

        StringBuilder sb = new StringBuilder();
        int index = new Random().nextInt(familyName.length());
        String name1 = String.valueOf(familyName.charAt(index));
        //姓
        sb.append(name1);

        //随机产生性别
        int randNum = new Random().nextInt(2) + 1;
        int ind1 = randNum == 1 ? new Random().nextInt(boyName.length() - 1) : new Random().nextInt(girlName.length() - 1);
        int ind2 = randNum == 1 ? new Random().nextInt(boyName.length() - 1) : new Random().nextInt(girlName.length() - 1);
        String s = "";
        String s1 = "";
        if (randNum == 1) {
            s = String.valueOf(boyName.charAt(ind1));
            s1 = String.valueOf(boyName.charAt(ind2));
        } else {
            s = String.valueOf(girlName.charAt(ind1));
            s1 = String.valueOf(girlName.charAt(ind2));
        }
        sb.append(s);
        sb.append(s1);
        return sb.toString();
    }
}
