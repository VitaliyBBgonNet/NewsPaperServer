package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserDtoRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public CustomSuccessResponse<LoginUserDtoResponse> registrationUser(
            RegistrationUserDtoRequest requestForRegistration) {

        String encryptedPassword = passwordEncoder.encode(requestForRegistration.getPassword());

        UsersEntity saveEntity = userMapper.toEntityRegistrationUser(requestForRegistration);

        saveEntity.setPassword(encryptedPassword);

        userRepository.save(saveEntity);

        LoginUserDtoResponse responseLogin = userMapper.toLoginDto(requestForRegistration);

        return new CustomSuccessResponse<>(responseLogin);
    }

}
