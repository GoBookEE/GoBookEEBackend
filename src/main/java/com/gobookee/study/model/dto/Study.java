package com.gobookee.study.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Study {
    private Long studySeq;
    private String studyTitle;
    private String studyContent;
    private Date studyDate;
    private Integer studyMemberLimit;
    private Timestamp studyEditTime;
    private Timestamp studyCreateTime;
    private String studyAddress;
    private Long studyLatitude;
    private Long studyLongitude;
    private String studyIsPublic;
    private Timestamp studyDeleteTime;
    private Long userSeq;
    private Long scSeq;
}
