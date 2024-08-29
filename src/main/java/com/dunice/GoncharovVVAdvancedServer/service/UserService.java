package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserViewResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {
    CustomSuccessResponse<List<PublicUserViewResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserViewResponse> getUserById(UUID id);

    CustomSuccessResponse<PublicUserViewResponse> getUserInfo();
}
