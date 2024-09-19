package com.dunice.GoncharovVVAdvancedServer.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.Mappers.TagMapper;
import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
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
import com.dunice.GoncharovVVAdvancedServer.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

public class NewsServiceTest {

    @Mock
    private NewsMapper newsMapper;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private UserService userService;
    @Mock
    private TagService tagService;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        newsService = new NewsServiceImpl(tagMapper, userService, newsRepository, newsMapper, tagService);
    }

    private void setAuthentication(UUID userId) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails(userId.toString()));
    }

    private NewsEntity createNewsEntity(Long id, UUID userId, Set<TagsEntity> tags) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(id);
        newsEntity.setAuthor(new UsersEntity());
        newsEntity.getAuthor().setId(userId);
        newsEntity.setTags(tags);
        return newsEntity;
    }

    private NewsRequest createNewsRequest() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setTags(Set.of(ConstantsTest.FIRST_TAG, ConstantsTest.SECOND_TAG));
        return newsRequest;
    }

    @Test
    void testCreateNews_Success() {
        UUID userId = UUID.randomUUID();
        NewsRequest newsRequest = new NewsRequest();
        NewsEntity newsEntity = createNewsEntity(null, userId, new HashSet<>());

        setAuthentication(userId);
        when(newsMapper.dtoNewToEntityNews(newsRequest)).thenReturn(newsEntity);
        when(userService.findUserEntityById(userId)).thenReturn(newsEntity.getAuthor());
        when(tagService.createSetTagsEntityAndSaveNoExistentTags(newsRequest.getTags())).thenReturn(newsEntity.getTags());

        CreateNewsSuccessResponse response = newsService.creteNews(newsRequest);

        assertNotNull(response);
        assertEquals(newsEntity.getId(), response.getId());

        verify(newsMapper).dtoNewToEntityNews(newsRequest);
        verify(newsRepository).save(newsEntity);
    }

    @Test
    void testPutUserNews_Success() {
        Long idNews = 1L;
        UUID userId = UUID.randomUUID();
        NewsRequest newsRequest = createNewsRequest();
        NewsEntity newsEntity = createNewsEntity(idNews, userId, new HashSet<>());

        setAuthentication(userId);
        when(newsRepository.findById(idNews)).thenReturn(Optional.of(newsEntity));
        when(tagService.createSetTagsEntityAndSaveNoExistentTags(anySet())).thenReturn(new HashSet<>());
        when(userService.findUserEntityById(userId)).thenReturn(newsEntity.getAuthor());

        BaseSuccessResponse response = newsService.putUserNews(idNews, newsRequest);

        verify(newsRepository).findById(idNews);
        verify(tagService).createSetTagsEntityAndSaveNoExistentTags(newsRequest.getTags());
        verify(newsRepository).save(newsEntity);
        assertNotNull(response);
    }

    @Test
    void testDeleteUserNews_Success() {
        Long newsId = 1L;
        UUID userId = UUID.randomUUID();
        NewsEntity newsEntity = createNewsEntity(newsId, userId, new HashSet<>());

        setAuthentication(userId);
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(newsEntity));
        doNothing().when(newsRepository).deleteById(newsId);
        when(userService.findUserEntityById(userId)).thenReturn(newsEntity.getAuthor());

        BaseSuccessResponse response = newsService.deleteUserNews(newsId);

        verify(newsRepository).findById(newsId);
        verify(newsRepository).deleteById(newsId);
        assertNotNull(response);
    }

    @Test
    void testPutUserNewsIfNewsNotFound() {
        Long id = 1L;
        NewsRequest newsRequest = createNewsRequest();

        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () -> newsService.putUserNews(id, newsRequest));
        assertEquals(ErrorCodes.NEWS_NOT_FOUND, thrown.getErrorCodes());
    }

    @Test
    void testDeleteUserNewsNotFound() {
        Long id = 1L;

        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () -> newsService.deleteUserNews(id));
        assertEquals(ErrorCodes.NEWS_NOT_FOUND, thrown.getErrorCodes());
    }

    @Test
    void testGetNewsUserById() {
        UUID authorId = UUID.randomUUID();

        UsersEntity author = new UsersEntity();
        author.setId(authorId);
        author.setName(ConstantsTest.NAME);

        TagsEntity tag1 = new TagsEntity();
        tag1.setId(1L);
        tag1.setTitle(ConstantsTest.FIRST_TAG);

        TagsEntity tag2 = new TagsEntity();
        tag2.setId(2L);
        tag2.setTitle(ConstantsTest.SECOND_TAG);

        Set<TagsEntity> tags = new HashSet<>(Arrays.asList(tag1, tag2));

        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(1L);
        newsEntity.setTitle(ConstantsTest.TITLE);
        newsEntity.setDescription(ConstantsTest.DESCRIPTION);
        newsEntity.setImage(ConstantsTest.IMAGE);
        newsEntity.setAuthor(author);
        newsEntity.setTags(tags);

        Page<NewsEntity> page = new PageImpl<>(Arrays.asList(newsEntity), PageRequest.of(0, 10), 1);

        TagResponse tagResponse1 = new TagResponse();
        tagResponse1.setId(1L);
        tagResponse1.setTitle(ConstantsTest.FIRST_TAG);

        TagResponse tagResponse2 = new TagResponse();
        tagResponse2.setId(2L);
        tagResponse2.setTitle(ConstantsTest.SECOND_TAG);

        Set<TagResponse> tagResponses = new HashSet<>(Arrays.asList(tagResponse1, tagResponse2));

        GetNewsOutResponse responseDto = new GetNewsOutResponse();
        responseDto.setId(newsEntity.getId());
        responseDto.setTitle(newsEntity.getTitle());
        responseDto.setDescription(newsEntity.getDescription());
        responseDto.setImage(newsEntity.getImage());
        responseDto.setUsername(author.getName());
        responseDto.setUserId(author.getId());
        responseDto.setTags(tagResponses);

        when(newsRepository.findByAuthorId(authorId, PageRequest.of(0, 10)))
                .thenReturn(page);
        when(newsMapper.entityNewsToDtoGetNews(newsEntity))
                .thenReturn(responseDto);
        when(tagMapper.entityNewsTagsToDtoTagResponse(anyList()))
                .thenReturn(tagResponses);

        CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> response =
                newsService.getNewsUserById(authorId.toString(), 1, 10);

        assertEquals(1, response.getData().getNumberOfElement());
        GetNewsOutResponse resultDto = response.getData().getContent().get(0);
        assertEquals(ConstantsTest.TITLE, resultDto.getTitle());
        assertEquals(ConstantsTest.NAME, resultDto.getUsername());
        assertEquals(authorId, resultDto.getUserId());
        assertEquals(ConstantsTest.IMAGE, resultDto.getImage());

        Set<String> expectedTagTitles = new HashSet<>(Arrays.asList(ConstantsTest.FIRST_TAG, ConstantsTest.SECOND_TAG));
        Set<String> actualTagTitles = resultDto.getTags().stream()
                .map(TagResponse::getTitle)
                .collect(Collectors.toSet());

        assertEquals(expectedTagTitles, actualTagTitles);
    }

}