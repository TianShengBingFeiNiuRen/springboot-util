package com.andon.springbootutil.vo;

import com.andon.springbootutil.constant.EnumSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Andon
 * 2022/3/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private String userId;
    private String userName;
    private Integer userAge;
    private EnumSex userSex;
    private String roleName;
    private String createTime;
}
