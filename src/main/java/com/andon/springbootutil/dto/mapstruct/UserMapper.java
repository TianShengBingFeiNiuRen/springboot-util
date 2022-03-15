package com.andon.springbootutil.dto.mapstruct;

import com.andon.springbootutil.domain.User;
import com.andon.springbootutil.dto.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author Andon
 * 2022/3/2
 */
@Mapper(componentModel = "spring") //注入spring容器
public abstract class UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "userId"),
            @Mapping(target = "userName", ignore = true), //不对该字段属性做映射
            @Mapping(source = "age", target = "userAge"), //基本类型-->包装类型
            @Mapping(source = "sex", target = "userSex"), // String-->枚举
            @Mapping(source = "role.name", target = "roleName"), //取自定义类型的属性值
            @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"), // LocalDateTime类型指定格式化
    })
    public abstract UserVO toUserVo(User user);

    @Mappings({
            @Mapping(source = "id", target = "userId"),
            @Mapping(target = "userName", ignore = true),
            @Mapping(source = "age", target = "userAge"),
            @Mapping(source = "sex", target = "userSex"),
            @Mapping(source = "role.name", target = "roleName"),
            @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    public abstract List<UserVO> toUserVoList(List<User> users);
}
