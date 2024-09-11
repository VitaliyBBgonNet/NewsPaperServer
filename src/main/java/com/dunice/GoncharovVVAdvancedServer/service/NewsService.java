package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.Base.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.GetNewsOutResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PageableResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import java.util.List;

public interface NewsService {
    CreateNewsSuccessResponse creteNews(NewsRequest newsRequest);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUserById(String id,
                                                                                      Integer page,
                                                                                      Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsUsers(Integer page,
                                                                                   Integer perPage);

    PageableResponse<List<GetNewsOutResponse>> findNews(String author,
                                                        String keywords,
                                                        Integer page,
                                                        Integer perPage,
                                                        List<String> tags);

    BaseSuccessResponse putUserNews(Long id, NewsRequest newsRequest);
}