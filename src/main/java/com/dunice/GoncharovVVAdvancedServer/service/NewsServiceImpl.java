package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.Mappers.TagMapper;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.GetNewsOutResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PageableResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.TagResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final UserService userService;

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final TagMapper tagMapper;

    private final TagService tagService;

    @Override
    public CreateNewsSuccessResponse creteNews(NewsRequest newsRequest) {

        NewsEntity newsEntity = newsMapper.dtoNewToEntityNews(newsRequest);

        newsEntity.setTags(tagService.createSetTagsEntityAndSaveNoExistentTags(newsRequest.getTags()));
        newsEntity.setAuthor(userService.findUserEntityById(getUserIdByToken()));
        newsRepository.save(newsEntity);

        return new CreateNewsSuccessResponse(newsEntity.getId());
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUserById(String id, Integer page, Integer perPage) {

        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntityListPagination = newsRepository.findByAuthorId(UUID.fromString(id), pageRequest);

        List<GetNewsOutResponse> getNewsOutResponseList = newsEntityListPagination.getContent()
                .stream()
                .map(newsEntity -> {
                    GetNewsOutResponse response = newsMapper.entityNewsToDtoGetNews(newsEntity);
                    response.setUserName(newsEntity.getAuthor().getName());
                    response.setUserId(newsEntity.getAuthor().getId());

                    Set<TagResponse> entityTagsList = newsEntity.getTags()
                            .stream()
                            .map(tagsEntity -> {
                                TagResponse tagResponse = new TagResponse();
                                tagResponse.setId(tagsEntity.getId());
                                tagResponse.setTitle(tagsEntity.getTitle());
                                return tagResponse;
                            })
                            .collect(Collectors.toSet());
                    response.setTags(entityTagsList);
                    return response; }
                )
                .toList();

        return new CustomSuccessResponse<>(new PageableResponse<>(getNewsOutResponseList,
                (long) newsEntityListPagination.getTotalElements()));
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUsers(Integer page, Integer perPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntityListPagination = newsRepository.findAll(pageRequest);

        List<GetNewsOutResponse> getNewsOutResponseList = newsEntityListPagination.getContent()
                .stream()
                .map(newsEntity -> {
                    GetNewsOutResponse response = newsMapper.entityNewsToDtoGetNews(newsEntity);
                    response.setUserName(newsEntity.getAuthor().getName());
                    response.setUserId(newsEntity.getAuthor().getId());

                    Set<TagResponse> entityTagsList = newsEntity.getTags()
                            .stream()
                            .map(tagsEntity -> {
                                TagResponse tagResponse = new TagResponse();
                                tagResponse.setId(tagsEntity.getId());
                                tagResponse.setTitle(tagsEntity.getTitle());
                                return tagResponse;
                            })
                            .collect(Collectors.toSet());
                    response.setTags(entityTagsList);
                    return response; }
                )
                .toList();

        return new CustomSuccessResponse<>(new PageableResponse<>(getNewsOutResponseList,
                (long) newsEntityListPagination.getTotalElements()));
    }


    private UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }
}