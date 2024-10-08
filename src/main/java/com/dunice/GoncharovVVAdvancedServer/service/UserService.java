package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UsersEntity findUserEntityById(UUID id);

    CustomSuccessResponse<List<PublicUserResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserResponse> getUserById(UUID id);

    CustomSuccessResponse<PublicUserResponse> getUserInfo();

    CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest putUserRequest);

    BaseSuccessResponse deleteUser();
}