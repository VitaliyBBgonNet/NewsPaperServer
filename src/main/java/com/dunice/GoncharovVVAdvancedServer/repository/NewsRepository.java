package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, UUID> {

}
