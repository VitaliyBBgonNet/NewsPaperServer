package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import jakarta.validation.Valid;

public interface UserService {
    CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration);

    CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest authUserRequest);
}
