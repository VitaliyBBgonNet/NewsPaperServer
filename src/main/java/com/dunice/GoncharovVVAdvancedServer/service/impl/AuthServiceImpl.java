package com.dunice.GoncharovVVAdvancedServer.service.impl;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.AuthRepository;
import com.dunice.GoncharovVVAdvancedServer.security.TokenSecurity;
import com.dunice.GoncharovVVAdvancedServer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final TokenSecurity jwtToken;

    public CustomSuccessResponse<LoginUserResponse> registrationUser(
        RegistrationUserRequest requestForRegistration) throws CustomException {

        authRepository.findByEmail(requestForRegistration.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
                });

        String encryptedPassword = passwordEncoder.encode(requestForRegistration.getPassword());
        UsersEntity saveEntity = userMapper.toEntityRegistrationUser(requestForRegistration);
        saveEntity.setPassword(encryptedPassword);
        authRepository.save(saveEntity);
        LoginUserResponse responseLogin = userMapper.toLoginDto(saveEntity);
        responseLogin.setToken(jwtToken.generateToken(saveEntity.getId()));

        return new CustomSuccessResponse<>(responseLogin);
    }

    public CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest authUserRequest) {

        UsersEntity getEntityUser = authRepository.findByEmail(authUserRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        if (!passwordEncoder.matches(authUserRequest.getPassword(), getEntityUser.getPassword())) {
            throw new CustomException(ErrorCodes.USER_PASSWORD_NOT_VALID);
        }

        LoginUserResponse loginUserResponse = userMapper.toLoginDto(getEntityUser);
        loginUserResponse.setToken(jwtToken.generateToken(getEntityUser.getId()));

        return new CustomSuccessResponse<>(loginUserResponse);
    }
}