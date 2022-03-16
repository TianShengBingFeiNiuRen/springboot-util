package com.andon.springbootutil.dto;

import com.andon.springbootutil.constant.EnumSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andon
 * 2022/3/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private String id;
    private String userId;
    private String userName;
    private Integer userAge;
    private EnumSex userSex;
    private String userHeight;
    private String roleName;
    private String createTime;
}
