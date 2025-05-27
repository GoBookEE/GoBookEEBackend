package com.gobookee.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduleConfirm {
    private Long scheduleSeq;
    private char status;
    private String date;
    private Long studySeq;
    private Long placeSeq;

    public Date getSqlDate() {
        return Date.valueOf(date);
    }
}
