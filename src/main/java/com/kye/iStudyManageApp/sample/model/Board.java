package com.kye.iStudyManageApp.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "user") 다른이름으로 주고자 할때, show variables like 'lower_case_table_names';에서 값이 1이면 대소문자 상관없이 소문자로 인식
@Entity // DB생성 대상
public class Board {

    @Id  // primary Key라는 뜻
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략(atuto_increment)을 따라간다.
    private int id;  // 시퀀스(오라클) , atuto_increment(my Sql)

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // 대용량 데이터 처리할 때 사용
    private String content;  // 섬머노트 라이브러리 사용 (html태그가 섞여 있어서 용량이 큼)

    @ColumnDefault("0")  // default값을 0으로 설정
    private int count;  // 조회수

    // FetchType.EAGER는 데이터를 즉시 사용하겠다는 의미
    // FetchType.LAZY는 데이터를 나중에 사용하겠다는 의미 ( 당장 가져오지 않아도 됨 )
    // 여기서는 한게시판에 유저는 하나밖에 없으므로 fetch전략은 EAGER로 설정한다. 이는 무조건 조회하라는 의미
    @ManyToOne(fetch = FetchType.EAGER) // 연관관계 설정, Many=Board, User=One, 한명의 유저는 여러개 보드를 쓸수 있다(자동으로 FK생성)
    @JoinColumn(name = "userId")  // 실제 DB에 생성할 때는 userId라는 FK로 생성하라는 의미
    private User user;  // DB는 오브젝트를 저장할 수 없기 때문에 DB에서 FK로 만들어진다.

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER) // mappedBy는 연관관계의 주인이 아니기 때문에(FK가 아님) DB에 컬럼을 만들지 말라는 의미
                                   // Reply에 있는 board 변수명을 mappdeBy에 할당한다.
    private List<Reply> reply;     // 연관관계의 주인은 board로서 단순히 여러개의 답변에 대한 데이터만 조인으로 가져온다.

    @CreationTimestamp
    private Timestamp createDate;
}
