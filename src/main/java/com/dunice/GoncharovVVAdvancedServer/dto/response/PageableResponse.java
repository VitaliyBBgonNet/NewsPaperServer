package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageableResponse<T> {
    private T content;

    private Long numberOfElement;
}