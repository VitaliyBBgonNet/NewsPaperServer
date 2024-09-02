package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements  NewsService {

    private final UserRepository userRepository;

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    @Override
    public CreateNewsSuccessResponse creteNews(NewsRequest newsRequest) {

        UsersEntity user = userRepository.findById(getUserByToken())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));//Проверили наличие пользователя

        NewsEntity newsEntity = newsMapper.DtoNewToEntityNews(newsRequest);// Замапили newsResquest на сушность перед сохранением

        newsEntity.setUser(user); // Закидываем в сушность пользователя полученного по id

        newsRepository.save(newsEntity);// Сохраняем полученную сущность

        return new CreateNewsSuccessResponse(newsEntity.getIdNews());// Возвращаем DTO
    }

    private UUID getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }
}