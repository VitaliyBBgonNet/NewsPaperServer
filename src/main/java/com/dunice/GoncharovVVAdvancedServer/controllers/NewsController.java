package com.dunice.GoncharovVVAdvancedServer.controllers;

import com.dunice.GoncharovVVAdvancedServer.constants.StringConstants;
import com.dunice.GoncharovVVAdvancedServer.constants.ValidationConstants;
import com.dunice.GoncharovVVAdvancedServer.dto.request.NewsRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.BaseSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.GetNewsOutResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PageableResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CreateNewsSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.service.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNewsForThisUser(
            @RequestBody
            @Valid NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.creteNews(newsRequest));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getUserNews(
            @PathVariable
            @Pattern(regexp = StringConstants.PATTERN_FORMAT_UUID,
                    message = ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION) String id,

            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {
        return ResponseEntity.ok(newsService.getNewsUserById(id, page, perPage));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getUserNews(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {
        return ResponseEntity.ok(newsService.getNewsUsers(page, perPage));
    }

    @GetMapping("/find")
    public ResponseEntity<PageableResponse<List<GetNewsOutResponse>>> findNews(
            @RequestParam (name = "author") String author,

            @RequestParam (name = "keywords") String keywords,

            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage,

            @RequestParam (value = "tags") List<String> tags) {
        return ResponseEntity.ok(newsService.findNews(author, keywords, page, perPage, tags));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> putUserNews(
            @PathVariable
            @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id,
                @RequestBody @Valid NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.putUserNews(id, newsRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteUserNews(
            @PathVariable
            @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id) {

        return ResponseEntity.ok(newsService.deleteUserNews(id));
    }
}