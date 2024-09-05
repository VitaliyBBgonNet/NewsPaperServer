package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final UserService userService;

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final TagService tagService;

    @Override
    public CreateNewsSuccessResponse creteNews(NewsRequest newsRequest) {
        NewsEntity newsEntity = newsMapper.dtoNewToEntityNews(newsRequest);

        newsEntity.setTags(tagService.createSetTagsEntityAndSaveNoExistentTags(newsRequest.getTags()));
        newsEntity.setAuthor(userService.findUserEntityById(getUserIdByToken()));
        newsRepository.save(newsEntity);

        return new CreateNewsSuccessResponse(newsEntity.getId());
    }

    private UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }
}