package com.gobookee.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfile {
    private Long userSeq;
    private String userNickname;
    private String userPwd;
    private String userPhone;
    private String profileImage;
    private String userId;
    private String userEmail;
}
