package com.gobookee.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private Long scheduleSeq;
    private char scheduleIsConfirm;
    private Timestamp scheduleDeleteTime;
    private Timestamp scheduleCreateTime;
    private Date scheduleDate;
    private char scheduleNoShowYN;
    private Long studySeq;
    private Long placeSeq;

    public static Schedule from(ResultSet rs) throws SQLException {
        return Schedule.builder()
                .scheduleSeq(rs.getLong("SCHEDULE_SEQ"))
                .scheduleIsConfirm(rs.getString("SCHEDULE_IS_CONFIRM").charAt(0))
                .scheduleDeleteTime(rs.getTimestamp("SCHEDULE_DELETE_TIME"))
                .scheduleCreateTime(rs.getTimestamp("SCHEDULE_CREATE_TIME"))
                .scheduleDate(rs.getDate("SCHEDULE_DATE"))
                .scheduleNoShowYN(rs.getString("SCHEDULE_NOSHOW_YN").charAt(0))
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .studySeq(rs.getLong("STUDY_SEQ"))
                .build();
    }
}
