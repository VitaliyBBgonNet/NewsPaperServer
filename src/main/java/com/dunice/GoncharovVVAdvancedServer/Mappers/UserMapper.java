package com.dunice.GoncharovVVAdvancedServer.Mappers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    UsersEntity toEntityRegistrationUser(RegistrationUserRequest registrationDto);

    @Mapping(target = "token", ignore = true)
    LoginUserResponse toLoginDto(UsersEntity usersEntity);

    @Mapping(target = "token", ignore = true)
    List<PublicUserResponse> toPublicViewListDto(List<UsersEntity> usersEntity);

    PublicUserResponse toPublicDto(UsersEntity usersEntity);

    PutUserResponse toPutUserResponseFromUserEntity(UsersEntity usersEntity);

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromPut(PutUserRequest putUserRequest, @MappingTarget UsersEntity entity);
}
