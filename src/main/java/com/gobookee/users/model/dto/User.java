package com.gobookee.users.model.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long UserSeq;

    private String UserId;

    private String UserPwd;

    private String UserName;

    //Gender는 MALE, FEMALE로 구분
    private Gender UserGender;

    private String UserPhone;

    //프로필 사진은 PresignedUrl 방식을 사용해서 url 주소만 DB에 저장
    private String UserProfile;

    private String UserIntro;

    //UserType은 USER, ADMIN, OWNER로 구분
    private UserType UserType;

    private String UserEmail;

    private Timestamp UserCreateTime;

    private Timestamp UserDeleteTime;
}

