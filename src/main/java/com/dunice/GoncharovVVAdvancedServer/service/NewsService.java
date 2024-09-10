package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PageableResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;

public interface NewsService {
    CreateNewsSuccessResponse creteNews(NewsRequest newsRequest);

    CustomSuccessResponse<PageableResponse> getNewsUserById(String id, Integer page, Integer perPage);
}
