package com.dunice.GoncharovVVAdvancedServer.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class NewsRequest {

    private String description;

    private String image;

    private List<String> tags;

    private String title;
}