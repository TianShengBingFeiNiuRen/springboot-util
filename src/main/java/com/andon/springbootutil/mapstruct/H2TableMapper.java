package com.andon.springbootutil.mapstruct;

import com.andon.springbootutil.controller.H2TestController;
import com.andon.springbootutil.entity.H2Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author Andon
 * 2022/10/26
 */
@Mapper(componentModel = "spring")
public abstract class H2TableMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "isDebug", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    public abstract H2Table h2TableAddVOToH2Table(H2TestController.H2TableAddVO h2TableAddVO);

    @Mapping(target = "isDebug", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    public abstract H2Table h2TableUpdateVOToH2Table(H2TestController.H2TableUpdateVO h2TableUpdateVO);
}
