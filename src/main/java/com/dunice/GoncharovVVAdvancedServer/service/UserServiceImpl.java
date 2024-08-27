package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.TokenSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final TokenSecurity jwtToken;

    public CustomSuccessResponse<LoginUserResponse> registrationUser(
        RegistrationUserRequest requestForRegistration) throws CustomException {

        userRepository.findByEmail(requestForRegistration.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
                });

        String encryptedPassword = passwordEncoder.encode(requestForRegistration.getPassword());
        UsersEntity saveEntity = userMapper.toEntityRegistrationUser(requestForRegistration);
        saveEntity.setPassword(encryptedPassword);
        userRepository.save(saveEntity);
        LoginUserResponse responseLogin = userMapper.toLoginDto(saveEntity);
        responseLogin.setToken(jwtToken.generateToken(saveEntity.getId()));

        return new CustomSuccessResponse<>(responseLogin);
    }

    public CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest authUserRequest){

        LoginUserResponse responseLogin = null;
        return new CustomSuccessResponse<>(responseLogin);
    }
}