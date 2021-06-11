package com.kye.iStudyManageApp.sample.dto;

import lombok.*;

// dto를 만들어 사용할 수도 있지만 모델의 entity를 사용할 수도 있기 때문에 중복해서 만들 필요는 없어 보인다.
//@AllArgsConstructor
//@NoArgsConstructor
//
@Data
public class MemberForm {
    private int id;
    private String username;
    private String password;
    private String email;

    @Builder // 빌더 패턴을 만들어 준다.
    public MemberForm(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
