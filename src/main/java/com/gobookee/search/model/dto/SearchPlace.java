package com.gobookee.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchPlace {
    private Long placeSeq;
    private String placeTitle;
    private String placeContents;
    private String placeAddress;
    private Integer placeRecCount;
    private Integer placeNonRecCount;
    private String photoRenamedName;

    public static SearchPlace from(ResultSet rs) throws SQLException {
        return SearchPlace.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .placeRecCount(rs.getInt("REC_COUNT"))
                .placeNonRecCount(rs.getInt("NON_REC_COUNT"))
                .photoRenamedName(rs.getString("PHOTO_RENAMED_NAME"))
                .build();
    }
}
