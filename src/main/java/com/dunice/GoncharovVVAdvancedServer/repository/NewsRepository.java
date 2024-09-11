package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Page<NewsEntity> findByAuthorId(UUID uuid, Pageable pageable);

    @Query("SELECT n FROM NewsEntity n " +
            "LEFT JOIN n.tags t " +
            "LEFT JOIN n.author a " +
            "WHERE (:authorName IS NULL OR a.name LIKE %:authorName%) " +
            "AND (:keywords IS NULL OR n.title LIKE %:keywords% OR n.description LIKE %:keywords%) " +
            "AND (:tags IS NULL OR t.title IN :tags) " +
            "GROUP BY n " +
            "HAVING (:tags IS NULL OR COUNT(DISTINCT t.title) = :tagCount)")
    Page<NewsEntity> search(@Param("authorName") String authorName,
                            @Param("keywords") String keywords,
                            @Param("tags") List<String> tags,
                            @Param("tagCount") int tagCount,
                            Pageable pageable);
}