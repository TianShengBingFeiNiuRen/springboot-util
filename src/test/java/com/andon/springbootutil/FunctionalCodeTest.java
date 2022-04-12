package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.service.MyFunction;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andon
 * 2022/2/10
 * <p>
 * 函数式编程测试
 */
@Slf4j
public class FunctionalCodeTest {

    @Test
    public void functionalInterfaceTest2() {
        MyFunction<String, Integer> s2i = s -> {
            log.info("type:{}", s.getClass().getName());
            return Integer.valueOf(s);
        };
        String string = "128";
        Integer myApply = s2i.myApply(string);
        log.info("myApply >>> {} type:{}", myApply, myApply.getClass().getName());
    }

    /**
     * 函数式接口测试
     */
    @Test
    public void functionalInterfaceTest() {
        String string = "128";
        Integer function = sting2Integer.myApply(string);
        log.info("function >>> {} type:{}", function, function.getClass().getName());
    }

    // 重写函数式接口的唯一抽象方法：将一个字符串转换成Integer
    private MyFunction<String, Integer> sting2Integer = s -> {
        log.info("type:{}", s.getClass().getName());
        return Integer.valueOf(s);
    };

    /**
     * Optional
     * <p>
     * 创建对象：ofNullable() empty()
     * 消费值：ifPresent()
     * 获取值：orElseGet() orElseThrow()
     * 过滤：filter()
     * 判断：isPresent()
     * 数据转换：map()
     */
    @Test
    public void optionalTest() {
        Hero hero = Hero.builder().name("name~~").build();
        System.out.println("=====Optional创建对象=====");
        Optional<Hero> heroOptional = Optional.ofNullable(hero);
        System.out.println("=====Optional消费值ifPresent=====");
        heroOptional.ifPresent(hero1 -> log.info("ifPresent.name:{}", hero1.name));
        System.out.println("=====Optional获取值orElseGet=====");
        heroOptional = Optional.empty();
        hero = heroOptional.orElseGet(() -> Hero.builder().name("name~~").build());
        log.info("orElseGet.name:{}", hero.name);
        System.out.println("=====Optional获取值抛异常orElseThrow=====");
        heroOptional = Optional.empty();
        //hero = heroOptional.orElseThrow(() -> new RuntimeException("数据为null"));
        System.out.println("=====Optional过滤filter=====");
        hero = Hero.builder().name("name~~").build();
        heroOptional = Optional.ofNullable(hero);
        heroOptional = heroOptional.filter(hero12 -> hero12.professional != null);
        heroOptional.ifPresent(hero1 -> log.info("ifPresent.name:{}", hero1.name));
        System.out.println("=====Optional判断isPresent=====");
        hero = Hero.builder().name("name~~").build();
        heroOptional = Optional.ofNullable(hero);
        if (heroOptional.isPresent()) {
            hero = heroOptional.get();
            log.info("isPresent.name:{}", hero.name);
        }
        System.out.println("=====Optional数据转换map=====");
        hero = Hero.builder().name("name~~").build();
        heroOptional = Optional.ofNullable(hero);
        Optional<String> nameOptional = heroOptional.map(hero1 -> hero1.name);
        nameOptional.ifPresent(s -> log.info("map.ifPresent.name:{}", s));
    }

    /**
     * Stream
     * 创建流-中间操作-终结操作
     * <p>
     * 创建流：stream()
     * 中间操作：filter() map() flatMap() distinct() sorted() limit() skip()
     * 终结操作：forEach() count() max() collect() anyMatch() allMatch() noneMatch() findAny() findFirst() reduce()
     */
    @Test
    public void streamTest() {
        List<Hero> heroList = getHeroes();
        log.info("heroList.size={}", heroList.size());
        System.out.println("filter,forEach=====打印所有刺客名字=====");
        heroList.stream()
                .filter(h -> h.road == 4)
                .forEach(hero -> log.info("professional:{} name:{}", hero.professional, hero.name));
        System.out.println("filter,forEach=====打印所有射手名字=====");
        Hero[] heroArr = heroList.toArray(new Hero[0]);
        Stream<Hero> heroArrStream = Arrays.stream(heroArr);
        heroArrStream
                .filter(hero -> hero.road == 3)
                .forEach(hero -> log.info("professional:{} name:{}", hero.professional, hero.name));
        System.out.println("filter,forEach=====打印所有英雄名字是大于两个字的英雄名字=====");
        heroList.stream()
                .filter(hero -> hero.name.length() > 2)
                .forEach(hero -> log.info("hero.name:{}", hero.name));
        System.out.println("map=====打印所有英雄名字=====");
        heroList.stream()
                .map(Hero::getName)
                .forEach(name -> log.info("name:{}", name));
        System.out.println("distinct=====打印所有的刺客名字(去重)=====");
        heroList.stream()
                .filter(h -> h.road == 4)
                .distinct()
                .forEach(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("sorted=====打印所有英雄名字是大于两个字的英雄名字(去重),根据路倒叙进行排序=====");
        heroList.stream()
                .distinct()
                .filter(hero -> hero.name.length() > 2)
                .sorted((o1, o2) -> o2.road - o1.road)
                .forEach(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("limit=====去重,路倒叙,打印路最大的两位英雄=====");
        heroList.stream()
                .distinct()
                .sorted((o1, o2) -> o2.road - o1.road)
                .limit(2)
                .forEach(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("skip=====去重,路倒叙,打印除了路最大的两位英雄=====");
        heroList.stream()
                .distinct()
                .sorted((o1, o2) -> o2.road - o1.road)
                .limit(5)
                .skip(2)
                .forEach(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("=====打印所有的职业(去重)=====");
        heroList.stream()
                .map(hero -> hero.professional)
                .distinct()
                .forEach(name -> log.info("name:{}", name));
        System.out.println("flatMap=====打印所有的路名字(去重)=====");
        heroList.stream()
                .flatMap(hero -> Arrays.stream(hero.roadName.split(",")))
                .distinct()
                .forEach(name -> log.info("name:{}", name));
        System.out.println("count=====所有刺客的英雄名字(去重)个数=====");
        long countCiKe = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .map(hero -> hero.name)
                .distinct()
                .count();
        log.info("countCiKe:{}", countCiKe);
        System.out.println("max=====所有刺客中路最大的英雄名字=====");
        Optional<Integer> max = heroList.stream()
                .map(hero -> hero.road)
                .max((o1, o2) -> o1 - o2);
        max.ifPresent(integer -> log.info("integer:{}", integer));
        System.out.println("collect=====获取所有刺客名字的List集合=====");
        List<String> ciKeList = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .map(hero -> hero.name)
                .distinct()
                .collect(Collectors.toList());
        log.info("ciKeList:{}", JSONObject.toJSONString(ciKeList));
        System.out.println("collect=====获取所有刺客名字的Set集合=====");
        Set<String> ciKeSet = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .map(hero -> hero.name)
                .collect(Collectors.toSet());
        log.info("ciKeSet:{}", JSONObject.toJSONString(ciKeSet));
        System.out.println("collect=====获取一个Map,刺客英雄名字:职业=====");
        Map<String, String> map = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .distinct()
                .collect(Collectors.toMap(hero -> hero.name, hero -> hero.professional));
        log.info("map:{}", JSONObject.toJSONString(map));
        System.out.println("anyMatch=====判断是否有名字是4个字的刺客=====");
        boolean anyMatch = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .anyMatch(hero -> hero.name.length() == 4);
        log.info("anyMatch:{}", anyMatch);
        System.out.println("allMatch=====判断是否法师的所有名字都是大于2个字的=====");
        boolean allMatch = heroList.stream()
                .filter(hero -> hero.professional.equals("法师"))
                .allMatch(hero -> hero.name.length() >= 2);
        log.info("allMatch:{}", allMatch);
        System.out.println("noneMatch=====判断是否所有刺客的名字都是小于4个字的=====");
        boolean noneMatch = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .noneMatch(hero -> hero.name.length() >= 4);
        log.info("noneMatch:{}", noneMatch);
        System.out.println("findAny=====获取任意一个名字是1个字的刺客=====");
        Optional<Hero> anyOptional = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .filter(hero -> hero.name.length() == 1)
                .findAny();
        anyOptional.ifPresent(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("findFirst=====获取第一个名字是3个字的刺客=====");
        Optional<Hero> firstOptional = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .filter(hero -> hero.name.length() == 3)
                .findFirst();
        firstOptional.ifPresent(hero -> log.info("hero:{}", JSONObject.toJSONString(hero)));
        System.out.println("reduce=====所有刺客的名字连接在一起=====");
        Optional<String> reduceOptional = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .distinct()
                .map(hero -> hero.name)
                .reduce((s, s2) -> s + " " + s2);
        reduceOptional.ifPresent(s -> log.info("name:{}", s));
        System.out.println("reduce=====获取刺客中名字最短的名字=====");
        Optional<String> reduceNameOptional = heroList.stream()
                .filter(hero -> hero.professional.equals("刺客"))
                .distinct()
                .map(hero -> hero.name)
                .reduce((s, s2) -> s.length() < s2.length() ? s : s2);
        reduceNameOptional.ifPresent(s -> log.info("name:{}", s));
    }

    private List<Hero> getHeroes() {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(Hero.builder().name("花木兰").professional("战士").roadName("边路").road(1).build());
        heroes.add(Hero.builder().name("吕布").professional("战士").roadName("边路").road(1).build());
        heroes.add(Hero.builder().name("曹操").professional("战士").roadName("边路").road(1).build());
        heroes.add(Hero.builder().name("杨戬").professional("战士").roadName("边路").road(1).build());
        heroes.add(Hero.builder().name("老夫子").professional("战士").roadName("边路").road(1).build());
        heroes.add(Hero.builder().name("妲己").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("安琪拉").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("小乔").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("甄姬").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("貂蝉").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("婉儿").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("金蝉").professional("法师").roadName("中路").road(2).build());
        heroes.add(Hero.builder().name("马可").professional("射手").roadName("下路").road(3).build());
        heroes.add(Hero.builder().name("虞姬").professional("射手").roadName("下路").road(3).build());
        heroes.add(Hero.builder().name("香香").professional("射手").roadName("下路").road(3).build());
        heroes.add(Hero.builder().name("鲁班").professional("射手").roadName("下路").road(3).build());
        heroes.add(Hero.builder().name("李元芳").professional("射手").roadName("下路").road(3).build());
        heroes.add(Hero.builder().name("赵云").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("赵云").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("司马懿").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("司马懿").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("司马懿").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("兰陵王").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("鸟人").professional("刺客").roadName("打野").road(4).build());
        heroes.add(Hero.builder().name("澜").professional("刺客").roadName("打野").road(4).build());
        return heroes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Hero {
        private String name;
        private String professional;
        private String roadName;
        private Integer road;
    }

    /**
     * Stream并行流
     * <p>
     * 创建并行流：stream().parallel()或者parallelStream()
     * 调试：peek()
     */
    @Test
    public void parallelStreamTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        //Optional<Integer> reduce = list.parallelStream()
        Optional<Integer> reduce = list.stream()
                .parallel()
                .peek(integer -> log.info("Thread:[{}] >>> {}", Thread.currentThread().getName(), integer))
                .filter(integer -> integer >= 5)
                .reduce(Integer::sum);
        reduce.ifPresent(integer -> log.info("reduce:{}", integer));
    }
}
