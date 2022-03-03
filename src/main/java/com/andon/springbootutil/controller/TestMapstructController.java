package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.domain.User;
import com.andon.springbootutil.dto.UserVO;
import com.andon.springbootutil.dto.mapstruct.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Andon
 * 2022/3/2
 */
@Api(tags = "mapstruct")
@Slf4j
@RestController
@RequestMapping(value = "/mapstruct")
public class TestMapstructController {

    @Resource
    private UserMapper userMapper;

    @ApiOperation(value = "测试")
    @PostMapping(value = "/test")
    public UserVO test(@RequestBody User user) {
        log.info("user:{}", JSONObject.toJSONString(user));
        UserVO userVO = userMapper.toUserVo(user);
        log.info("userVO:{}", JSONObject.toJSONString(userVO));
        return userVO;
    }

    @ApiOperation(value = "测试2")
    @PostMapping(value = "/test2")
    public List<UserVO> test2(@RequestBody List<User> users) {
        log.info("users:{}", JSONObject.toJSONString(users));
        List<UserVO> userVOs = userMapper.toUserVoList(users);
        log.info("userVOs:{}", JSONObject.toJSONString(userVOs));
        return userVOs;
    }
}
