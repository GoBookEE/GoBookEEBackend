package com.gobookee.users.model.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userSeq;

    private String userId;

    private String userPwd;

    private String userNickName;

    //Gender는 MALE, FEMALE로 구분
    private Gender userGender;

    private String userPhone;

    //프로필 사진은 PresignedUrl 방식을 사용해서 url 주소만 DB에 저장
    private String userProfile;

    private String userIntro;

    //UserType은 USER, ADMIN, OWNER로 구분
    private UserType userType;

    private String userEmail;
    
    private Long userSpeed;
    
    private String userAddress;

    private Timestamp userCreateTime;

    private Timestamp UserDeleteTime;

    private Integer UserSpeed;
}

