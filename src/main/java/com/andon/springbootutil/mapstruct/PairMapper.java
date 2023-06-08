package com.andon.springbootutil.mapstruct;

import com.andon.springbootutil.dto.Pair;
import com.andon.springbootutil.dto.PairDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Andon
 * 2022/9/9
 */
@Mapper(componentModel = "spring")
public abstract class PairMapper {


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "pairDTOS", ignore = true)
    })
    public abstract PairDTO pairToPairDTO(Pair pair);

    @AfterMapping
    public void randomID(@MappingTarget PairDTO.PairDTOBuilder<?, ?> pairDTO) {
        pairDTO.id(UUID.randomUUID().toString());
    }

    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "pairDTOS", ignore = true)
    })
    public abstract List<PairDTO> pairsToPairDTOS(List<Pair> pair);

}
