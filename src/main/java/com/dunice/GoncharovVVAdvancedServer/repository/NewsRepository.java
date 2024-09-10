package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Page<NewsEntity> findByAuthorId(UUID uuid, Pageable pageable);
}
