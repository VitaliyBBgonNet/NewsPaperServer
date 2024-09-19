package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;

public interface AuthService {
    CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration);

    CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest authUserRequest);
}