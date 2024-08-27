package com.dunice.GoncharovVVAdvancedServer.repository;

import com.dunice.GoncharovVVAdvancedServer.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByEmail(String email);
}
