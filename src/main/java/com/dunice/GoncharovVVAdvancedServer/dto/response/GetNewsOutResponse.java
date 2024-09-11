package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsOutResponse {
    private String description;

    private Long id;

    private String image;

    private Set<TagResponse> tags;

    private String title;

    private UUID userId;

    private String username;
}