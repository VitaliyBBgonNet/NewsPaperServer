package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;

public interface NewsService {
    CreateNewsSuccessResponse creteNews (NewsRequest newsRequest);
}
