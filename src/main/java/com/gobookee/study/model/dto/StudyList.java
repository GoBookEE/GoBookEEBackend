package com.gobookee.study.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StudyList {
    private Long studySeq;
    private String studyTitle;
    private Timestamp studyDate;
    private Integer studyMemberLimit;
    private String studyPlace;
    private Integer confirmedCount;
    private String photoRenamedName;
    private Integer likeCount;
    private Integer dislikeCount;
}
