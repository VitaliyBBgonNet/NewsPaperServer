package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.response.TagResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagResponse entityNewsTagsToDtoTagResponse(TagsEntity tagsEntity);
}
