package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.Base.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private UUID userId;
    private UsersEntity userEntity;
    private PublicUserResponse publicUserResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        userId = UUID.randomUUID();
        userEntity = new UsersEntity();
        userEntity.setId(userId);
        userEntity.setName(ConstantsTest.NAME);
        userEntity.setEmail(ConstantsTest.EMAIL);
        userEntity.setRole(ConstantsTest.ROLE);
        userEntity.setAvatar(ConstantsTest.AVATAR);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));
    }

    private PublicUserResponse createPublicUserResponse(UsersEntity user) {
        PublicUserResponse response = new PublicUserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setAvatar(user.getAvatar());
        return response;
    }

    private PutUserRequest createPutUserRequest(UsersEntity userEntity) {
        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setName(userEntity.getName());
        putUserRequest.setEmail(userEntity.getEmail());
        putUserRequest.setRole(userEntity.getRole());
        putUserRequest.setAvatar(userEntity.getAvatar());
        return putUserRequest;
    }

    private UsersEntity createUserEntity() {
        UsersEntity userEntity = new UsersEntity();
        userEntity.setId(UUID.randomUUID());
        return userEntity;
    }

    private void mockAuthentication(UUID userId) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));
    }

    @Test
    public void testFindUserEntityByIdSuccess() throws CustomException {
        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UsersEntity userEntityById = userService.findUserEntityById(userId);

        assertNotNull(userEntityById);
        assertEquals(userId, userEntityById.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    public void testGetAllUsersSuccess() throws CustomException {

        List<UsersEntity> usersEntityList = List.of(createUserEntity(), createUserEntity());
        List<PublicUserResponse> userResponseList = usersEntityList.stream()
                .map(this::createPublicUserResponse)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(usersEntityList);
        when(userMapper.toPublicViewListDto(usersEntityList)).thenReturn(userResponseList);

        CustomSuccessResponse<List<PublicUserResponse>> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(response.getData(), userResponseList);

        verify(userRepository, times(1)).findAll();
        verify(userMapper).toPublicViewListDto(usersEntityList);
    }

    @Test
    public void testGetUserByIdSuccess() {
        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toPublicDto(userEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(publicUserResponse, response.getData());
        verify(userRepository).findById(userId);
        verify(userMapper).toPublicDto(userEntity);
    }

    @Test
    public void testGetUserInfoSuccess() {
        mockAuthentication(userId);
        when(userRepository.findAllById(userId)).thenReturn(userEntity);
        when(userMapper.toPublicDto(userEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfo();

        assertNotNull(response);
        assertEquals(publicUserResponse, response.getData());
        verify(userRepository).findAllById(userId);
        verify(userMapper).toPublicDto(userEntity);
    }

    @Test
    public void testReplaceUserSuccess() {
        PutUserRequest putUserRequest = createPutUserRequest(userEntity);

        PutUserResponse putUserResponse = new PutUserResponse();
        putUserResponse.setName(userEntity.getName());
        putUserResponse.setEmail(userEntity.getEmail());
        putUserResponse.setRole(userEntity.getRole());
        putUserResponse.setAvatar(userEntity.getAvatar());

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmailAndIdNot(putUserRequest.getEmail(), userEntity.getId())).thenReturn(Optional.empty());
        doNothing().when(userMapper).updateUserFromPut(putUserRequest, userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toPutUserResponseFromUserEntity(userEntity)).thenReturn(putUserResponse);

        CustomSuccessResponse<PutUserResponse> response = userService.replaceUser(putUserRequest);

        assertNotNull(response);
        assertEquals(putUserRequest.getName(), response.getData().getName());
        verify(userRepository).findById(userId);
        verify(userRepository).findByEmailAndIdNot(putUserRequest.getEmail(), userEntity.getId());
        verify(userMapper).updateUserFromPut(putUserRequest, userEntity);
        verify(userRepository).save(userEntity);
    }

    @Test
    public void testDeleteUserSuccess() {
        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(userId);

        BaseSuccessResponse response = userService.deleteUser();

        assertNotNull(response);
        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testFindUserEntityByIdThrowsException() {
        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.findUserEntityById(userId));

        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository).findById(userId);
    }

    @Test
    public void testReplaceUserThrowsExceptionWhenEmailExists() {
        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setEmail(ConstantsTest.EMAIL);

        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmailAndIdNot(putUserRequest.getEmail(), userId)).thenReturn(Optional.of(userEntity));

        CustomException exception = assertThrows(CustomException.class, () -> userService.replaceUser(putUserRequest));

        assertEquals(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getErrorCodes());
        verify(userRepository).findById(userId);
        verify(userRepository).findByEmailAndIdNot(putUserRequest.getEmail(), userId);
    }

    @Test
    public void testDeleteUserThrowsException() {
        mockAuthentication(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser());

        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}
