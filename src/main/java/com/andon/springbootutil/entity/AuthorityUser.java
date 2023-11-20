package com.andon.springbootutil.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Andon
 * 2023/5/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorityUser implements Serializable {

    @ApiModelProperty(value = "用户ID")
    String id;
    @ApiModelProperty(value = "用户名")
    String username;
    @ApiModelProperty(value = "密码")
    String password;
    @ApiModelProperty(value = "默认密码")
    String defaultPassword;
    @ApiModelProperty(value = "手机号")
    String phone;
    @ApiModelProperty(value = "真实姓名")
    String realName;
    @ApiModelProperty(value = "是否锁定")
    Boolean isLock;
    @ApiModelProperty(value = "是否主动注册")
    Boolean isRegister;
    @ApiModelProperty(value = "组织ID")
    String orgId;
    @ApiModelProperty(value = "当前登录token")
    String currentToken;
    @ApiModelProperty(value = "创建时间")
    Date createTime;
    @ApiModelProperty(value = "更新时间")
    Date updateTime;
}
