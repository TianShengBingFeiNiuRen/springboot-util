package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.dto.Pair;
import com.andon.springbootutil.dto.PairDTO;
import com.andon.springbootutil.dto.UserDTO;
import com.andon.springbootutil.entity.Role;
import com.andon.springbootutil.entity.User;
import com.andon.springbootutil.mapstruct.PairMapper;
import com.andon.springbootutil.mapstruct.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Andon
 * 2022/3/15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapstructTest {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PairMapper pairMapper;

    @Test
    public void test04() {
        List<Pair> pairs = new ArrayList<>();
        pairs.add(Pair.builder().id(UUID.randomUUID().toString()).key("a").value("1").build());
        pairs.add(Pair.builder().id(UUID.randomUUID().toString()).key("b").value("2").build());
        pairs.add(Pair.builder().id(UUID.randomUUID().toString()).key("c").value("3").build());
        List<PairDTO> pairDTOS = pairMapper.pairsToPairDTOS(pairs);
        log.info("pairs:{}", JSONObject.toJSONString(pairs));
        log.info("pairDTOS:{}", JSONObject.toJSONString(pairDTOS));
    }

    @Test
    public void test03() {
        Pair pair = Pair.builder().id(UUID.randomUUID().toString()).key("a").value("1").build();
        PairDTO pairDTO = pairMapper.pairToPairDTO(pair);
        log.info("pair:{}", JSONObject.toJSONString(pair));
        log.info("pairDTO:{}", JSONObject.toJSONString(pairDTO));
        String testStatic = PairMapper.testStatic();
        log.info("{}", testStatic);
    }

    @Test
    public void test02() {
        Role role = Role.builder().id(UUID.randomUUID().toString()).name("asd").build();
        User user = User.builder().id(UUID.randomUUID().toString()).name("阿萨德").age(18).sex("MALE").height(1.876).createTime(LocalDateTime.now()).build();
        log.info("user:{}", JSONObject.toJSONString(user));
        UserDTO userDTO = userMapper.toUserVo(user, role);
        log.info("userVO:{}", JSONObject.toJSONString(userDTO));
    }

    @Test
    public void test01() {
        Role role = Role.builder().id(UUID.randomUUID().toString()).name("asd").build();
        User user = User.builder().id(UUID.randomUUID().toString()).name("阿萨德").age(18).sex("MALE").height(1.876).role(role).createTime(LocalDateTime.now()).build();
        log.info("user:{}", JSONObject.toJSONString(user));
        UserDTO userDTO = userMapper.toUserVo(user);
        log.info("userVO:{}", JSONObject.toJSONString(userDTO));
    }
}
