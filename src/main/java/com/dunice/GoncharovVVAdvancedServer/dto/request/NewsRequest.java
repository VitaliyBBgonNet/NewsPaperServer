package com.dunice.GoncharovVVAdvancedServer.dto.request;

import com.dunice.GoncharovVVAdvancedServer.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class NewsRequest {

    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String description;

    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;

    @NotEmpty(message = ValidationConstants.TAGS_NOT_VALID)// Смотреть сюда
    private List<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String title;
}