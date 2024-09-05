package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface TagsRepository extends JpaRepository<TagsEntity, Integer> {
    Set<TagsEntity> findByTitleIn(Set<String> title);
}
