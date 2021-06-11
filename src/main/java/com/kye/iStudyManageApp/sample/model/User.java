package com.kye.iStudyManageApp.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "user") 다른이름으로 주고자 할때, show variables like 'lower_case_table_names';에서 값이 1이면 대소문자 상관없이 소문자로 인식
//@DynamicInsert // @ColumnDefault가 작동하려면 인서트 항목에서 아예 제외되어야 함, null값이라도 있으면 안됨 => 없는 값들은 모두 제외되는 위험성이 있다.
@Entity // 테이블 생성 대상임을 뜻함
public class User {

    @Id  // primary Key라는 뜻
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략(atuto_increment)을 따라간다.
    private int id;  // 시퀀스(오라클) , atuto_increment(my Sql)

    @Column(nullable = false, length = 30, unique = true)  // 컬럼의 속성 설정, not null, 길이는 30자 이내, 중복방지
    private String username;  // 아이디

    @Column(nullable = false, length = 100)  // 해쉬로 비밀번호를 암호하기 위해 길이는 넉넉하게 준다.
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    //@ColumnDefault("'user'") // default 값 , 홑따옴표로 감싸야 DB에서 문자로 인식
    @Enumerated(EnumType.STRING)  // DB가 인식할 수 있도록 string임을 알려주는 어노테이션을 붙인다.
    private RoleType role;  // 개발자 오류를 방지하기 위해 Enum(범위, 도메인 설정 가능)을 쓰는게 좋다. (ADMIN, USER, MANAGER 도메인으로 설정)

    @CreationTimestamp  // 시간이 자동 입력 (자바에서 현재 시간을 만들어 준다)
    private Timestamp createDate;

}
