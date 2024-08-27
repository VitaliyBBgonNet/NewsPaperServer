package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;

public interface UserService {
    CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration);
}
