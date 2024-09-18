package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.GetNewsOutResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", ignore = true)
    NewsEntity dtoNewToEntityNews(NewsRequest newsRequest);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    GetNewsOutResponse entityNewsToDtoGetNews(NewsEntity newsEntity);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateUserNews(NewsRequest newsRequest, @MappingTarget NewsEntity newsEntity);
}