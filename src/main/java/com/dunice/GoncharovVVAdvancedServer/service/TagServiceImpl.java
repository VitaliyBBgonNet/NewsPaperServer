package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.TagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagsRepository tagsRepository;

    @Override
    public Set<TagsEntity> myMapperForTagsNews(Set<String> setForNews) {

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
