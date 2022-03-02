package com.andon.springbootutil.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Andon
 * 2022/3/2
 * <p>
 * 性别（枚举）
 */
@Getter
@AllArgsConstructor
public enum EnumSex {

    MALE("男", "male"),
    FEMALE("女", "female");

    private String name;
    private String value;
}
