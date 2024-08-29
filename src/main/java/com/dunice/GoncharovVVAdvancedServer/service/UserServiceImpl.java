package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.UserMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserViewResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public CustomSuccessResponse<List<PublicUserViewResponse>> getAllUsers() {
        return new CustomSuccessResponse<>(userMapper.toPublicViewListDto(userRepository.findAll()));
    }

    @Override
    public CustomSuccessResponse<PublicUserViewResponse> getUserById(UUID id) {

        UsersEntity getEntityUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        return new CustomSuccessResponse<>(userMapper.toPublicViewDto(getEntityUser));
    }

    @Override
    public CustomSuccessResponse<PublicUserViewResponse> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userDetailsId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return new CustomSuccessResponse<>(userMapper.toPublicViewDto(
                userRepository.findAllById(UUID.fromString(userDetailsId))));
    }
}
