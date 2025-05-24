package com.gobookee.place.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
