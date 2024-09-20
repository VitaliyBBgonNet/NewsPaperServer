package com.dunice.GoncharovVVAdvancedServer.service.impl;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public UsersEntity findUserEntityById(UUID id) {
        return getUserOrThrowException(id);
    }

    @Override
    public CustomSuccessResponse<List<PublicUserResponse>> getAllUsers() {
        return new CustomSuccessResponse<>(userMapper.toPublicViewListDto(userRepository.findAll()));
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserById(UUID id) {

        UsersEntity getEntityUser = getUserOrThrowException(id);

        return new CustomSuccessResponse<>(userMapper.toPublicDto(getEntityUser));
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfo() {
        return new CustomSuccessResponse<>(userMapper.toPublicDto(
                userRepository.findAllById(getUserIdByToken())));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest putUserRequest) {

        UsersEntity getEntityUser = getUserOrThrowException(getUserIdByToken());

        userRepository
                .findByEmailAndIdNot(putUserRequest.getEmail(), getEntityUser.getId())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
                });

        userMapper.updateUserFromPut(putUserRequest, getEntityUser);
        userRepository.save(getEntityUser);

        return new CustomSuccessResponse<>(userMapper.toPutUserResponseFromUserEntity(getEntityUser));
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteUser() {
        UUID idFromToken = getUserIdByToken();

        userRepository.findById(idFromToken)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        userRepository.deleteById(idFromToken);

        return new BaseSuccessResponse();
    }

    public UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }

    private UsersEntity getUserOrThrowException(UUID uuid) {
        UsersEntity usersEntity = userRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        return  usersEntity;
    }
}