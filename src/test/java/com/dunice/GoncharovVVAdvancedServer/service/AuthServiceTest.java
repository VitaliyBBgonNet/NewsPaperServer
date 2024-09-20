package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.AuthRepository;
import com.dunice.GoncharovVVAdvancedServer.security.TokenSecurity;
import com.dunice.GoncharovVVAdvancedServer.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TokenSecurity jwtToken;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegistrationUserRequest registrationRequest;
    private AuthUserRequest authRequest;
    private UsersEntity userEntity;
    private LoginUserResponse loginResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        registrationRequest = new RegistrationUserRequest();
        registrationRequest.setEmail(ConstantsTest.EMAIL);
        registrationRequest.setPassword(ConstantsTest.PASSWORD);
        registrationRequest.setName(ConstantsTest.NAME);
        registrationRequest.setAvatar(ConstantsTest.AVATAR);
        registrationRequest.setRole(ConstantsTest.ROLE);

        userEntity = new UsersEntity();
        userEntity.setEmail(registrationRequest.getEmail());
        userEntity.setPassword(ConstantsTest.ENCODE_PASSWORD);
        userEntity.setName(registrationRequest.getName());
        userEntity.setAvatar(registrationRequest.getAvatar());
        userEntity.setRole(registrationRequest.getRole());

        loginResponse = new LoginUserResponse();
        loginResponse.setId(ConstantsTest.ID);
        loginResponse.setEmail(registrationRequest.getEmail());
        loginResponse.setAvatar(registrationRequest.getAvatar());
        loginResponse.setName(registrationRequest.getName());
        loginResponse.setRole(registrationRequest.getRole());

        authRequest = new AuthUserRequest();
        authRequest.setEmail(ConstantsTest.EMAIL);
        authRequest.setPassword(ConstantsTest.PASSWORD);
    }

    @Test
    public void testRegistrationUserSuccess() throws CustomException {
        when(authRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(ConstantsTest.ENCODE_PASSWORD);
        when(userMapper.toEntityRegistrationUser(registrationRequest)).thenReturn(userEntity);
        when(authRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toLoginDto(userEntity)).thenReturn(loginResponse);
        when(jwtToken.generateToken(userEntity.getId())).thenReturn(ConstantsTest.TOKEN);

        CustomSuccessResponse<LoginUserResponse> response = authService.registrationUser(registrationRequest);

        assertNotNull(response);
        assertEquals(loginResponse, response.getData());

        verify(authRepository).findByEmail(registrationRequest.getEmail());
        verify(authRepository).save(userEntity);
    }

    @Test
    public void testRegistrationUserThrowsExceptionWhenUserExists() {
        UsersEntity existingUser = new UsersEntity();
        existingUser.setEmail(registrationRequest.getEmail());

        when(authRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(existingUser));

        CustomException thrown = assertThrows(CustomException.class, () -> authService.registrationUser(registrationRequest));
        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, thrown.getErrorCodes());
    }

    @Test
    public void testAuthorizationUserSuccess() {
        when(authRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(userMapper.toLoginDto(userEntity)).thenReturn(loginResponse);
        when(jwtToken.generateToken(userEntity.getId())).thenReturn(ConstantsTest.TOKEN);

        CustomSuccessResponse<LoginUserResponse> response = authService.authorizationUser(authRequest);

        assertNotNull(response);
        assertEquals(loginResponse, response.getData());
        verify(authRepository).findByEmail(authRequest.getEmail());
    }

    @Test
    public void testAuthorizationUserThrowsExceptionWhenUserNotFound() {
        when(authRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () -> authService.authorizationUser(authRequest));
        assertEquals(ErrorCodes.USER_NOT_FOUND, thrown.getErrorCodes());
    }

    @Test
    public void testAuthorizationUserThrowsExceptionWhenPasswordNotValid() {
        when(authRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword())).thenReturn(false);

        CustomException thrown = assertThrows(CustomException.class, () -> authService.authorizationUser(authRequest));
        assertEquals(ErrorCodes.USER_PASSWORD_NOT_VALID, thrown.getErrorCodes());
    }
}
