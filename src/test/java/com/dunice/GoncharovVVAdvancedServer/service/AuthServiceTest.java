package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.AuthUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrationUserSuccess() throws CustomException {

        RegistrationUserRequest request = new RegistrationUserRequest();
        request.setEmail(ConstantsTest.EMAIL);
        request.setPassword(ConstantsTest.PASSWORD);
        request.setName(ConstantsTest.NAME);
        request.setAvatar(ConstantsTest.AVATAR);
        request.setRole(ConstantsTest.ROLE);

        UsersEntity entity = new UsersEntity();
        entity.setEmail(request.getEmail());
        entity.setPassword(ConstantsTest.ENCODE_PASSWORD);
        entity.setName(request.getName());
        entity.setAvatar(request.getAvatar());
        entity.setRole(request.getRole());

        LoginUserResponse loginTestResponse = new LoginUserResponse();

        loginTestResponse.setId(ConstantsTest.ID);
        loginTestResponse.setEmail(request.getEmail());
        loginTestResponse.setAvatar(request.getAvatar());
        loginTestResponse.setName(request.getName());
        loginTestResponse.setRole(request.getRole());

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn(ConstantsTest.ENCODE_PASSWORD);
        when(userMapper.toEntityRegistrationUser(request)).thenReturn(entity);
        when(authRepository.save(entity)).thenReturn(entity);
        when(userMapper.toLoginDto(entity)).thenReturn(loginTestResponse);
        when(jwtToken.generateToken(entity.getId())).thenReturn(ConstantsTest.TOKEN);

        CustomSuccessResponse<LoginUserResponse> response = authService.registrationUser(request);

        assertNotNull(response);
        assertEquals(loginTestResponse, response.getData());

        verify(authRepository).findByEmail(request.getEmail());
        verify(authRepository).save(entity);
    }

    @Test
    public void testRegistrationUserThrowsExceptionWhenUserExists() {

        RegistrationUserRequest request = new RegistrationUserRequest();
        request.setEmail(ConstantsTest.EMAIL);

        UsersEntity existingUser = new UsersEntity();
        existingUser.setEmail(request.getEmail());

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        CustomException thrown = assertThrows(CustomException.class, () -> authService.registrationUser(request));
        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, thrown.getErrorCodes());
    }

    @Test
    public void testAuthorizationUserSuccess() {

        AuthUserRequest request = new AuthUserRequest();
        request.setEmail(ConstantsTest.EMAIL);
        request.setPassword(ConstantsTest.PASSWORD);

        UsersEntity entity = new UsersEntity();
        entity.setEmail(request.getEmail());
        entity.setPassword(ConstantsTest.ENCODE_PASSWORD);

        LoginUserResponse loginTestResponse = new LoginUserResponse();

        loginTestResponse.setId(ConstantsTest.ID);
        loginTestResponse.setEmail(request.getEmail());
        loginTestResponse.setAvatar(ConstantsTest.AVATAR);
        loginTestResponse.setName(ConstantsTest.NAME);
        loginTestResponse.setRole(ConstantsTest.ROLE);

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(request.getPassword(), entity.getPassword())).thenReturn(true);
        when(userMapper.toLoginDto(entity)).thenReturn(loginTestResponse);
        when(jwtToken.generateToken(entity.getId())).thenReturn(ConstantsTest.TOKEN);

        CustomSuccessResponse<LoginUserResponse> response = authService.authorizationUser(request);

        assertNotNull(response);
        assertEquals(loginTestResponse, response.getData());
        verify(authRepository).findByEmail(request.getEmail());
    }

    @Test
    public void testAuthorizationUserThrowsExceptionWhenUserNotFound() {

        AuthUserRequest request = new AuthUserRequest();
        request.setEmail(ConstantsTest.EMAIL);

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () -> authService.authorizationUser(request));
        assertEquals(ErrorCodes.USER_NOT_FOUND, thrown.getErrorCodes());
    }

    @Test
    public void testAuthorizationUserThrowsExceptionWhenPasswordNotValid() {

        AuthUserRequest request = new AuthUserRequest();
        request.setEmail(ConstantsTest.EMAIL);
        request.setPassword(ConstantsTest.PASSWORD);

        UsersEntity entity = new UsersEntity();
        entity.setEmail(request.getEmail());
        entity.setPassword(ConstantsTest.ENCODE_PASSWORD);

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(request.getPassword(), entity.getPassword())).thenReturn(false);

        CustomException thrown = assertThrows(CustomException.class, () -> authService.authorizationUser(request));
        assertEquals(ErrorCodes.USER_PASSWORD_NOT_VALID, thrown.getErrorCodes());
    }
}
