package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {
    CustomSuccessResponse<List<PublicUserResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserResponse> getUserById(UUID id);

    CustomSuccessResponse<PublicUserResponse> getUserInfo();
}
