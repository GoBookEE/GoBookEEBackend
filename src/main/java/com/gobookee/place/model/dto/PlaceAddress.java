package com.gobookee.place.model.dto;

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
public class PlaceAddress {
    private String placeAddress;
    private Double placeLatitude;
    private Double placeLongitude;

    public static PlaceAddress from(ResultSet rs) throws SQLException {
        return PlaceAddress.builder()
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .placeLatitude(rs.getDouble("PLACE_LATITUDE"))
                .placeLongitude(rs.getDouble("PLACE_LONGITUDE"))
                .build();
    }
}
