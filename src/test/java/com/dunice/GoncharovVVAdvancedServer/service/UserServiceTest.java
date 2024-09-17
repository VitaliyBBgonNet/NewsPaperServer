package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import com.dunice.GoncharovVVAdvancedServer.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserEntityByIdSuccess() throws CustomException {

        UUID userId = UUID.randomUUID();
        UsersEntity user = new UsersEntity();
        user.setId(userId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UsersEntity userEntityById = userService.findUserEntityById(userId);

        assertNotNull(userEntityById);
        assertEquals(userId, userEntityById.getId());

        verify(userRepository).findById(userId);
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void getAllUsers() throws CustomException {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(UUID.randomUUID());

        UsersEntity usersEntity1 = new UsersEntity();
        usersEntity1.setId(UUID.randomUUID());

        List<UsersEntity> usersEntityList = List.of(usersEntity, usersEntity1);

        PublicUserResponse publicUserResponse = new PublicUserResponse();
        publicUserResponse.setId(String.valueOf(usersEntity.getId()));

        PublicUserResponse publicUserResponse1 = new PublicUserResponse();
        publicUserResponse1.setId(String.valueOf(usersEntity1.getId()));

        List<PublicUserResponse> userResponseList = List.of(publicUserResponse, publicUserResponse1);

        when(userRepository.findAll()).thenReturn(usersEntityList);
        when(userMapper.toPublicViewListDto(usersEntityList)).thenReturn(userResponseList);

        CustomSuccessResponse<List<PublicUserResponse>> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(response.getData(), userResponseList);

        verify(userRepository, times(1)).findAll();
        verify(userMapper).toPublicViewListDto(usersEntityList);
    }

}
