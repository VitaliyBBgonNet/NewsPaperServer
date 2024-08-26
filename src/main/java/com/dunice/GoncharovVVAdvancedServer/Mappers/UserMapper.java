package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserDtoRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserView;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    PublicUserView toUserDto(UsersEntity user);

    @Mapping(target = "id", ignore = true)
    UsersEntity toEntityRegistrationUser(RegistrationUserDtoRequest registrationDto);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "id", ignore = true)
    LoginUserDtoResponse toLoginDto(RegistrationUserDtoRequest registrationDto);

    List<PublicUserView> toUserDtoList(List<UsersEntity> usersEntities);
}
