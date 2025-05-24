package com.gobookee.place.model.dto;

import lombok.Builder;

@Builder
public record PlaceListResponse(
        String placeTitle,
        String placeContents,
        String placeAddress,
        String userNickname,
        Long recommendCount
) {
}
