package com.gobookee.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchStudy {
    private Long studySeq;
    private String studyTitle;
    private Date studyDate;
    private Integer studyMemberLimit;
    private String studyAddress;
    private Integer confirmCount;
    private String photoRenamedName;
    private Integer likeCount;
    private Integer dislikeCount;
    private String nickname;

    public static SearchStudy from(ResultSet rs) throws SQLException {
        return SearchStudy.builder()
                .studySeq(rs.getLong("STUDY_SEQ"))
                .studyTitle(rs.getString("STUDY_TITLE"))
                .studyDate(rs.getDate("STUDY_DATE"))
                .studyMemberLimit(rs.getInt("STUDY_MEMBER_LIMIT"))
                .studyAddress(rs.getString("STUDY_ADDRESS"))
                .confirmCount(rs.getInt("CONFIRMED_COUNT"))
                .photoRenamedName(rs.getString("PHOTO_NAME"))
                .likeCount(rs.getInt("LIKE_COUNT"))
                .dislikeCount(rs.getInt("DISLIKE_COUNT"))
                .nickname(rs.getString("USER_NICKNAME"))
                .build();
    }
}
