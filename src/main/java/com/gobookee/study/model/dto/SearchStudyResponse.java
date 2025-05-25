package com.gobookee.study.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchStudyResponse {
    private Long studySeq;
    private String studyTitle;

    public static SearchStudyResponse from(ResultSet rs) throws SQLException {
        return SearchStudyResponse.builder()
                .studySeq(rs.getLong("STUDY_SEQ"))
                .studyTitle(rs.getString("STUDY_TITLE"))
                .build();
    }
}
