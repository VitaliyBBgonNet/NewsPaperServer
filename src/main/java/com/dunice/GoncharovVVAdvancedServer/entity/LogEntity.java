package com.dunice.GoncharovVVAdvancedServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EntityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String url;
    private String httpMethod;
    private String clientIp;
    private boolean success;
    private LocalDateTime timestamp;

}
