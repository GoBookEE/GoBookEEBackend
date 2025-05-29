package com.gobookee.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Search {
    private String tab;
    private String filter;
    private String keyword;
    private Integer cPage;
}
