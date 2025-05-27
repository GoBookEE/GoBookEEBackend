package com.gobookee.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleReserve {
    private Long studySeq;
    private String studyTitle;
    private Long studyMemberLimit;
    private Long studyCurrCount;
    private String studyContact;
    private char requestConfirm;
    private Long scheduleSeq;

    public static ScheduleReserve from(ResultSet rs) throws SQLException {
        return ScheduleReserve.builder()
                .studySeq(rs.getLong("STUDY_SEQ"))
                .studyTitle(rs.getString("STUDY_TITLE"))
                .studyMemberLimit(rs.getLong("STUDY_MEMBER_LIMIT"))
                .studyCurrCount(rs.getLong("STUDY_CURR_COUNT"))
                .studyContact(rs.getString("USER_PHONE"))
                .requestConfirm(rs.getString("SCHEDULE_IS_CONFIRM").charAt(0))
                .scheduleSeq(rs.getLong("SCHEDULE_SEQ"))
                .build();
    }
}
