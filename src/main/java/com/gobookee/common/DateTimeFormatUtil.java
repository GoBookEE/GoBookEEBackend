package com.gobookee.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TimeStamp 날짜 형식을 연-월-일 시:분 형식으로 변환해주는 템플릿
 */
public class DateTimeFormatUtil {
    //날짜 형식 포맷터
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * TimeStamp 날짜 형식을 연-월-일 시:분 형식으로 변환해주는 메소드
     *
     * @param timestamp
     * @return
     */
    public static String format(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return timestamp.toLocalDateTime().format(FORMATTER);
    }

    /**
     * LocalDateTime 날짜 형식을 연-월-일 시:분 형식으로 변환해주는 메소드
     * @param dateTime
     * @return
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(FORMATTER);
    }
}
