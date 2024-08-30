package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.PutUserRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PutUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
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
    public CustomSuccessResponse<List<PublicUserResponse>> getAllUsers() {
        return new CustomSuccessResponse<>(userMapper.toPublicViewListDto(userRepository.findAll()));
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserById(UUID id) {

        UsersEntity getEntityUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

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

        UsersEntity getEntityUser = userRepository.findById(getUserIdByToken())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        userRepository
                .findByEmailAndIdNot(putUserRequest.getEmail(), getEntityUser.getId())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
                });

        userMapper.updateUserFromPut(putUserRequest, getEntityUser);
        userRepository.save(getEntityUser);

        return new CustomSuccessResponse<>(userMapper.toPutUserResponseFromUserEntity(getEntityUser));
    }

    private UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }
}