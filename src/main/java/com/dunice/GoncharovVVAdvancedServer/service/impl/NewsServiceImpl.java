package com.dunice.GoncharovVVAdvancedServer.service.impl;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.Mappers.TagMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.GetNewsOutResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PageableResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.TagResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import com.dunice.GoncharovVVAdvancedServer.security.CustomUserDetails;
import com.dunice.GoncharovVVAdvancedServer.service.NewsService;
import com.dunice.GoncharovVVAdvancedServer.service.TagService;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final TagMapper tagMapper;

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

    @Override
    @Transactional
    public BaseSuccessResponse putUserNews(Long id, NewsRequest newsRequest) {

        NewsEntity getNewsEntity = getNewsOrThrowException(id);

        if (!getNewsEntity.getAuthor().getId().equals(getUserIdByToken())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        newsMapper.updateUserNews(newsRequest, getNewsEntity);
        getNewsEntity.setTags(tagService.createSetTagsEntityAndSaveNoExistentTags(newsRequest.getTags()));
        newsRepository.save(getNewsEntity);

        return new BaseSuccessResponse();
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteUserNews(Long id) {

        NewsEntity getNewsEntity = getNewsOrThrowException(id);

        if (!getNewsEntity.getAuthor().getId().equals(getUserIdByToken())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        newsRepository.deleteById(getNewsEntity.getId());

        return new BaseSuccessResponse();
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUserById(String id,
                                                                                             Integer page,
                                                                                             Integer perPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntityListPagination = newsRepository.findByAuthorId(UUID.fromString(id), pageRequest);

        return new CustomSuccessResponse<>(new PageableResponse<>(mapListGetNews(newsEntityListPagination),
                newsEntityListPagination.getTotalElements()));
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUsers(Integer page,
                                                                                          Integer perPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntityListPagination = newsRepository.findAll(pageRequest);

        return new CustomSuccessResponse<>(new PageableResponse<>(mapListGetNews(newsEntityListPagination),
                newsEntityListPagination.getTotalElements()));
    }

    @Override
    public PageableResponse<List<GetNewsOutResponse>> findNews(String author,
                                                               String keywords,
                                                               Integer page,
                                                               Integer perPage,
                                                               List<String> tags) {
        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        int tagCount = (tags == null) ? 0 : tags.size();
        Page<NewsEntity> listNewsFindBy = newsRepository.search(author,
                keywords,
                tags,
                tagCount,
                pageRequest);

        return new PageableResponse<>(mapListGetNews(listNewsFindBy), listNewsFindBy.getTotalElements());
    }

    private UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }

    private List<GetNewsOutResponse> mapListGetNews(Page<NewsEntity> paginationList) {
        return paginationList.getContent()
                .stream()
                .map(newsEntity -> {
                    GetNewsOutResponse response = newsMapper.entityNewsToDtoGetNews(newsEntity);
                    response.setUsername(newsEntity.getAuthor().getName());
                    response.setUserId(newsEntity.getAuthor().getId());

                    List<TagsEntity> tagsEntityList = new ArrayList<>(newsEntity.getTags());
                    Set<TagResponse> tagResponses = tagMapper.entityNewsTagsToDtoTagResponse(tagsEntityList);
                    response.setTags(tagResponses);

                    return response;
                })
                .toList();
    }

    private NewsEntity getNewsOrThrowException(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.NEWS_NOT_FOUND));
        return  newsEntity;
    }
}