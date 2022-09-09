package com.andon.springbootutil.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Andon
 * 2022/9/9
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PairDTO extends Pair {

    private String name;
    private List<PairDTO> pairDTOS;
}
