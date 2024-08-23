package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserDtoRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public CustomSuccessResponse<LoginUserDtoResponse> registrationUser(
            RegistrationUserDtoRequest requestForRegistration) {

        userRepository.save(userMapper.toEntityRegistrationUser(requestForRegistration));

        LoginUserDtoResponse responseLogin = userMapper.toLoginDto(requestForRegistration);

        return new CustomSuccessResponse<>(responseLogin);
    }

}
