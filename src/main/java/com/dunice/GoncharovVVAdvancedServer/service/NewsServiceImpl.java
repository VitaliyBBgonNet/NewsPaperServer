package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import com.dunice.GoncharovVVAdvancedServer.repository.TagsRepository;
import com.dunice.GoncharovVVAdvancedServer.repository.UserRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final UserRepository userRepository;

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final TagsRepository tagsRepository;

    @Override
    public CreateNewsSuccessResponse creteNews(NewsRequest newsRequest) {

        NewsEntity newsEntity = newsMapper.DtoNewToEntityNews(newsRequest);
        newsEntity.setTags(myMapperForTagsNews(newsRequest.getTags()));

        newsEntity.setUser(userRepository.findById(getUserByToken())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND)));

        newsRepository.save(newsEntity);
        return new CreateNewsSuccessResponse(newsEntity.getId());
    }

    private UUID getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }

    private Set<TagsEntity> myMapperForTagsNews(Set<String> setForNews) {

        Set<TagsEntity> setTitleFromRepository = tagsRepository.findByTitleIn(setForNews);

        setForNews.removeAll(setTitleFromRepository.stream()
                .map(title -> title.getTitle())
                .collect(Collectors.toSet()));

        Set<TagsEntity> tagsEntitySetForSave = setForNews.stream()
                .map(tag -> {
                    TagsEntity newTag = new TagsEntity();
                    newTag.setTitle(tag);
                    return newTag;
                }).collect(Collectors.toSet());

        tagsRepository.saveAll(tagsEntitySetForSave);
        setTitleFromRepository.addAll(tagsEntitySetForSave);

        return setTitleFromRepository;
    }
}