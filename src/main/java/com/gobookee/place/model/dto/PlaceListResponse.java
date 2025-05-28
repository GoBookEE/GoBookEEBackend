package com.gobookee.place.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceListResponse {
    private Long placeSeq;
    private String placeTitle;
    private String placeContents;
    private String placeAddress;
    private String userNickname;
    private Long placeRecCount;
    private Long placeNonRecCount;
    private String placeThumbnail;

    public static PlaceListResponse from(ResultSet rs) throws SQLException {
        return PlaceListResponse.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .userNickname(rs.getString("WRITER_NICKNAME"))
                .placeRecCount(rs.getLong("RECOMMEND_COUNT"))
                .placeNonRecCount(rs.getLong("NON_RECOMMEND_COUNT"))
                .build();
    }
}
