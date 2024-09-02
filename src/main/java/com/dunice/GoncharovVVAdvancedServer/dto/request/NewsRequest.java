package com.dunice.GoncharovVVAdvancedServer.dto.request;

import com.dunice.GoncharovVVAdvancedServer.constants.ValidationConstants;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class NewsRequest {

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String description;

    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String image;

    private List<String> tags;

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String title;
}