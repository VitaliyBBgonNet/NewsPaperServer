package com.dunice.GoncharovVVAdvancedServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String url;

    private String httpMethod;

    private String clientIp;

    private String statusCode;

    private LocalDateTime timestamp;

    private String errorMessage;
}