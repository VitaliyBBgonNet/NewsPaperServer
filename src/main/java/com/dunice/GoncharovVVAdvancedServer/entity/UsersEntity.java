package com.dunice.GoncharovVVAdvancedServer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "newspaper_schema")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String password;

    private String role;

    private String avatar;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<NewsEntity> news;
}