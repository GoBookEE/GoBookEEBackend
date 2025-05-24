package com.gobookee.place.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Place {
    private Long placeSeq;
    private String placeTitle;
    private String placeContents;
    private Timestamp placeCreateTime;
    private Timestamp placeDeleteTime;
    private String placeAddress;
    private Double placeLatitude;
    private Double placeLongitude;
    private Character placeIsPublic;
    private Long userSeq;

    public static Place from(ResultSet rs) throws SQLException {
        return Place.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeCreateTime(rs.getTimestamp("PLACE_CREATE_TIME"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .placeLatitude(rs.getDouble("PLACE_LATITUDE"))
                .placeLongitude(rs.getDouble("PLACE_LONGITUDE"))
                .placeIsPublic(rs.getString("PLACE_IS_PUBLIC").charAt(0))
                .userSeq(rs.getLong("USER_SEQ"))
                .build();
    }
}
