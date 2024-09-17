package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.Mappers.NewsMapper;
import com.dunice.GoncharovVVAdvancedServer.Mappers.TagMapper;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Set;

public class UserServiceTest {

    @Mock
    private TagMapper tagMapper;

    @Mock
    private UserService userService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private TagService tagService;

    @InjectMocks
    private UserServiceTest authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public void testCreteNews() throws CustomException {

        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setImage("com/image/smile.png");
        newsRequest.setTitle("Any title");
        newsRequest.setDescription("Any description");

        Set<String> setTags = Set.of("Tag1", "Tag2", "Tag3");

        newsRequest.setTags(setTags);




    }

}
