package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.TagsRepository;
import com.dunice.GoncharovVVAdvancedServer.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagsRepository tagsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSetTagsEntityAndSaveNoExistentTags() {

        Set<String> inputTags = new HashSet<>(Set.of(ConstantsTest.FIRST_TAG, ConstantsTest.SECOND_TAG, ConstantsTest.THREE_TAG));

        TagsEntity existingTag = new TagsEntity();
        existingTag.setTitle(ConstantsTest.FIRST_TAG);

        when(tagsRepository.findByTitleIn(inputTags)).thenReturn(Set.of(existingTag));

        Set<TagsEntity> result = tagService.createSetTagsEntityAndSaveNoExistentTags(inputTags);

        verify(tagsRepository).findByTitleIn(inputTags);
        verify(tagsRepository).saveAll(anySet());

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(tag -> tag.getTitle().equals(ConstantsTest.FIRST_TAG)));
        assertTrue(result.stream().anyMatch(tag -> tag.getTitle().equals(ConstantsTest.SECOND_TAG)));
        assertTrue(result.stream().anyMatch(tag -> tag.getTitle().equals(ConstantsTest.THREE_TAG)));
    }
}