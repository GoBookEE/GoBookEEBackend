package com.gobookee.place.model.dto;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceViewResponse {
    Long placeSeq;
    String userNickname;
    Long userSpeed;
    List<String> photoNames;
    String placeTitle;
    String placeContents;
    String placeAddress;
    Double placeLatitude;
    Double placeLongitude;
    Long placeRecCount;
    Long placeNonRecCount;
    Long userSeq;

    public static PlaceViewResponse from(ResultSet rs) throws SQLException {
        return PlaceViewResponse.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .userNickname(rs.getString("USER_NICKNAME"))
                .userSpeed(rs.getLong("USER_SPEED"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .placeLatitude(rs.getDouble("PLACE_LATITUDE"))
                .placeLongitude(rs.getDouble("PLACE_LONGITUDE"))
                .placeRecCount(rs.getLong("RECOMMEND_COUNT"))
                .placeNonRecCount(rs.getLong("NON_RECOMMEND_COUNT"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .userSeq(rs.getLong("USER_SEQ"))
                .build();
    }
}
