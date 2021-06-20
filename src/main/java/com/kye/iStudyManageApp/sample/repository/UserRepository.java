package com.kye.iStudyManageApp.sample.repository;

import com.kye.iStudyManageApp.sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// JpaRepository는 자동으로 bean 등록이 된다. 따라서 @Repository를 붙이지 않아도 된다.
public interface UserRepository extends JpaRepository<User, Integer> {

    /**********************
     *   시큐리티 적용 전
     *   username , password select
     **********************/

    // select JPA 작성 방법 1) 메서드 이름으로 구분해서 생성
    // 부모에게 메서드가 존재하지 않더라도 JPA naming 쿼리 전략에 의해 아래처럼 자동 생성
    // findByUsernameAndPassword에서 대문자를 중심으로 Username, Password를 컬럼명 그리고 파라미터는 ?표에 대입된다.
    // (SELECT * FROM user WHERE username=1? AND password=2?;)

    User findByUsernameAndPassword(String username, String password); // 시큐리티 적용 전

    // select JPA 작성 방법 2) native query를 직접 작성
    @Query(value = "SELECT * FROM user WHERE username=?1 AND password=?2", nativeQuery = true)
    User login(String username, String password);

    /**********************
     *   시큐리티 적용 후
     *   username 이 있는지 여부 확인
     **********************/
    // JPA naming 쿼리 전략에 의해 아래처럼 자동 생성
    // (SELECT * FROM user WHERE username=1?;)
    Optional<User> findByUsername(String username);



}
