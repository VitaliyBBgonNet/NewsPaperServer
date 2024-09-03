package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagsRepository extends JpaRepository<TagsEntity, Integer> {
    Optional<TagsEntity> findByTitle(String title);
}
