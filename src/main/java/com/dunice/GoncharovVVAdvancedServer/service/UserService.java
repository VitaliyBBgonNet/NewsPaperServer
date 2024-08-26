package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserDtoRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.tokens.TokenSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final TokenSecurity jwtToken;

    public CustomSuccessResponse<LoginUserDtoResponse> registrationUser(

        RegistrationUserDtoRequest requestForRegistration) throws CustomException {

        Optional<UsersEntity> existingUser = userRepository.findByEmail(requestForRegistration.getEmail());

        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        String encryptedPassword = passwordEncoder.encode(requestForRegistration.getPassword());

        UsersEntity saveEntity = userMapper.toEntityRegistrationUser(requestForRegistration);

        saveEntity.setPassword(encryptedPassword);

        userRepository.save(saveEntity);

        LoginUserDtoResponse responseLogin = userMapper.toLoginDto(requestForRegistration);
        responseLogin.setId(String.valueOf(saveEntity.getId()));

        responseLogin.setToken(jwtToken.generateToken(saveEntity.getId()));

        return new CustomSuccessResponse<>(responseLogin);
    }

}
