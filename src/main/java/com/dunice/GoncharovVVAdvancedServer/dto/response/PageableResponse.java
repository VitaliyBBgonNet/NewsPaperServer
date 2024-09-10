package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageableResponse<T> {
    private List<T> content;

    private Long numberOfElement;
}