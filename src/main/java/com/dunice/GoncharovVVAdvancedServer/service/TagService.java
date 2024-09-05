package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import java.util.Set;

public interface TagService {
    Set<TagsEntity> myMapperForTagsNews(Set<String> setForNews);
}
