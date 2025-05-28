package com.gobookee.study.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StudyList {
    private Long studySeq;
    private String studyTitle;
    private Date studyDate;
    private Integer studyMemberLimit;
    private String studyAddress;
    private Integer confirmedCount;
    private String photoRenamedName;
    private Integer likeCount;
    private Integer dislikeCount;
}
