package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UsersEntity toEntityRegistrationUser(RegistrationUserRequest registrationDto);

    @Mapping(target = "token", ignore = true)
    LoginUserResponse toLoginDto(UsersEntity usersEntity);

    @Mapping(target = "token", ignore = true)
    List<PublicUserResponse> toPublicViewListDto(List<UsersEntity> usersEntity);

    PublicUserResponse toPublicDto(UsersEntity usersEntity);
}
