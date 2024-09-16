package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
