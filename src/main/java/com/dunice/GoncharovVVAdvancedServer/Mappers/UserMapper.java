package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserView;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    PublicUserView toUserDTO(UsersEntity user);

    List<PublicUserView> toUserDtoList(List<UsersEntity> usersEntities);
}
