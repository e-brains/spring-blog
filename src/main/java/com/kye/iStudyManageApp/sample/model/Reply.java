package com.kye.iStudyManageApp.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "user") 다른이름으로 주고자 할때, show variables like 'lower_case_table_names';에서 값이 1이면 대소문자 상관없이 소문자로 인식
@Entity // DB생성 대상
public class Reply {

    @Id  // primary Key라는 뜻
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략(atuto_increment)을 따라간다.
    private int id;  // 시퀀스(오라클) , atuto_increment(my Sql)

    @Column(nullable = false, length = 200)
    private String content;  // 답변은 용량이 크지 않다고 가정함

    // 어떤 게시글의 답변인지
    @ManyToOne // Many=reply, One=board (여러개의 답변, 하나의 게시글 )
    @JoinColumn(name = "boardId")
    private Board board;

    // 어떤 사람의 답변인지
    @ManyToOne // Many=reply, Many=one (여러개의 답변, 한사람이 쓸 수 있다.)
    @JoinColumn(name = "userId")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;
}
