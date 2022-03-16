package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.domain.Role;
import com.andon.springbootutil.domain.User;
import com.andon.springbootutil.dto.UserVO;
import com.andon.springbootutil.dto.mapstruct.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    @Test
    public void test02() {
        Role role = Role.builder().id(UUID.randomUUID().toString()).name("asd").build();
        User user = User.builder().id(UUID.randomUUID().toString()).name("阿萨德").age(18).sex("MALE").height(1.876).createTime(LocalDateTime.now()).build();
        log.info("user:{}", JSONObject.toJSONString(user));
        UserVO userVO = userMapper.toUserVo(user, role);
        log.info("userVO:{}", JSONObject.toJSONString(userVO));
    }

    @Test
    public void test01() {
        Role role = Role.builder().id(UUID.randomUUID().toString()).name("asd").build();
        User user = User.builder().id(UUID.randomUUID().toString()).name("阿萨德").age(18).sex("MALE").height(1.876).role(role).createTime(LocalDateTime.now()).build();
        log.info("user:{}", JSONObject.toJSONString(user));
        UserVO userVO = userMapper.toUserVo(user);
        log.info("userVO:{}", JSONObject.toJSONString(userVO));
    }
}
