package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    NewsEntity DtoNewToEntityNews(NewsRequest newsRequest);
}
