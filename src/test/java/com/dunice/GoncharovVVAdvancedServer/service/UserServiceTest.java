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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
        SecurityContextHolder.setContext(securityContext);
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
    public void testGetAllUsersSuccess() throws CustomException {

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

    @Test
    public void testGetUserByIdSuccess(){

        UUID uuid = UUID.randomUUID();

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(uuid);
        usersEntity.setName(ConstantsTest.NAME);
        usersEntity.setEmail(ConstantsTest.EMAIL);
        usersEntity.setRole(ConstantsTest.ROLE);
        usersEntity.setAvatar(ConstantsTest.AVATAR);

        PublicUserResponse publicUserResponse = new PublicUserResponse();
        publicUserResponse.setId(String.valueOf(uuid));
        publicUserResponse.setName(usersEntity.getName());
        publicUserResponse.setEmail(usersEntity.getEmail());
        publicUserResponse.setRole(usersEntity.getRole());
        publicUserResponse.setAvatar(usersEntity.getAvatar());

        when(userRepository.findById(uuid)).thenReturn(Optional.of(usersEntity));
        when(userMapper.toPublicDto(usersEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserById(uuid);

        assertNotNull(response);
        assertEquals(publicUserResponse, response.getData());

        verify(userRepository).findById(uuid);
        verify(userMapper).toPublicDto(usersEntity);
    }

    @Test
    public void testGetUserInfoSuccess () {

        UUID uuid = UUID.randomUUID();

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(uuid);
        usersEntity.setName(ConstantsTest.NAME);
        usersEntity.setEmail(ConstantsTest.EMAIL);
        usersEntity.setRole(ConstantsTest.ROLE);
        usersEntity.setAvatar(ConstantsTest.AVATAR);

        PublicUserResponse publicUserResponse = new PublicUserResponse();
        publicUserResponse.setId(String.valueOf(uuid));
        publicUserResponse.setName(usersEntity.getName());
        publicUserResponse.setEmail(usersEntity.getEmail());
        publicUserResponse.setRole(usersEntity.getRole());
        publicUserResponse.setAvatar(usersEntity.getAvatar());


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(uuid.toString()));

        when(userRepository.findAllById(uuid)).thenReturn(usersEntity);
        when(userMapper.toPublicDto(usersEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfo();

        assertNotNull(response);
        assertEquals(publicUserResponse, response.getData());

        verify(userService).getUserIdByToken();
        verify(userRepository).findAllById(uuid);
        verify(userMapper).toPublicDto(usersEntity);
    }

    @Test
    public void testReplaceUserSuccess(){

        UUID uuid = UUID.randomUUID();

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(uuid);
        usersEntity.setName(ConstantsTest.NAME);
        usersEntity.setEmail(ConstantsTest.EMAIL);
        usersEntity.setRole(ConstantsTest.ROLE);
        usersEntity.setAvatar(ConstantsTest.AVATAR);

        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setName(usersEntity.getName());
        putUserRequest.setEmail(usersEntity.getEmail());
        putUserRequest.setRole(usersEntity.getRole());
        putUserRequest.setAvatar(usersEntity.getAvatar());

        PutUserResponse putUserResponse = new PutUserResponse();
        putUserResponse.setName(usersEntity.getName());
        putUserResponse.setEmail(usersEntity.getEmail());
        putUserResponse.setRole(usersEntity.getRole());
        putUserResponse.setAvatar(usersEntity.getAvatar());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(uuid.toString()));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(usersEntity));
        when(userRepository.findByEmailAndIdNot(putUserRequest.getEmail(), usersEntity.getId())).thenReturn(Optional.empty());
        doNothing().when(userMapper).updateUserFromPut(putUserRequest, usersEntity);
        when(userRepository.save(usersEntity)).thenReturn(usersEntity);
        when(userMapper.toPutUserResponseFromUserEntity(usersEntity)).thenReturn(putUserResponse);

        CustomSuccessResponse<PutUserResponse> response = userService.replaceUser(putUserRequest);

        assertNotNull(response);
        assertEquals(putUserRequest.getName(), putUserResponse.getName());
        assertEquals(putUserRequest.getEmail(), putUserResponse.getEmail());
        assertEquals(putUserRequest.getRole(), putUserResponse.getRole());
        assertEquals(putUserRequest.getAvatar(), putUserResponse.getAvatar());

        verify(userRepository).findById(uuid);
        verify(userRepository).findByEmailAndIdNot(putUserRequest.getEmail(), usersEntity.getId());
        verify(userMapper).updateUserFromPut(putUserRequest, usersEntity);
        verify(userRepository).save(usersEntity);
        verify(userMapper).toPutUserResponseFromUserEntity(usersEntity);
    }

    @Test
    public void testDeleteUserSuccess() {

        UUID userId = UUID.randomUUID();

        UsersEntity userEntity = new UsersEntity();
        userEntity.setId(userId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(userId);

        BaseSuccessResponse response = userService.deleteUser();

        assertNotNull(response);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testFindUserEntityByIdThrowsException() {
        UUID uuid = UUID.randomUUID();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(uuid.toString()));

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.findUserEntityById(uuid));

        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());

        verify(userRepository).findById(uuid);
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testReplaceUserThrowsExceptionWhenEmailExists() {

        UUID userId = UUID.randomUUID();

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(userId);
        usersEntity.setEmail(ConstantsTest.EMAIL);

        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setEmail(ConstantsTest.EMAIL);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(usersEntity));
        when(userRepository.findByEmailAndIdNot(putUserRequest.getEmail(), userId)).thenReturn(Optional.of(usersEntity));

        CustomException exception = assertThrows(CustomException.class, () -> userService.replaceUser(putUserRequest));

        assertEquals(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getErrorCodes());

        verify(userRepository).findById(userId);
        verify(userRepository).findByEmailAndIdNot(putUserRequest.getEmail(), userId);
    }

    @Test
    public void testDeleteUserThrowsException() {
        UUID userId = UUID.randomUUID();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser());

        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).deleteById(userId);
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }
}
