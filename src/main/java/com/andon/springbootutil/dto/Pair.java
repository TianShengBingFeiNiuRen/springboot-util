package com.andon.springbootutil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author Andon
 * 2022/9/9
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Pair implements Serializable {

    private String id;
    private String key;
    private String value;
}
