package com.gobookee.place.model.dto;

import lombok.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public record PlaceListResponse(
        String placeTitle,
        String placeContents,
        String placeAddress,
        String userNickname,
        Long recommendCount
) {
    public static PlaceListResponse from(ResultSet rs) throws SQLException {
        return PlaceListResponse.builder()
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .userNickname(rs.getString("WRITER_NICKNAME"))
                .recommendCount(rs.getLong("RECOMMEND_COUNT"))
                .build();
    }
}
