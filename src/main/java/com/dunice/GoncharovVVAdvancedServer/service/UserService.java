package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.Base.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {
    CustomSuccessResponse<List<PublicUserResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserResponse> getUserById(UUID id);

    CustomSuccessResponse<PublicUserResponse> getUserInfo();

    CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest putUserRequest);

    BaseSuccessResponse deleteUser();
}