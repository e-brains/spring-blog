package com.kye.iStudyManageApp.sample.controller;

import com.kye.iStudyManageApp.sample.model.RoleType;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CrudDummyControllerSample {

    @Autowired  // 본 컨트롤러가 생성될 때 같이 객체를 생성해 준다. (의존성 주입 DI)
    private UserRepository userRepository;

    /*****************************************************
     * Insert (저장)
     * 1. 파라미터 저장
     * 2. 오브젝트 저장
     ****************************************************/
    // 1. 파리미터 name을 ui와 동일하게 사용하면 @Requestparam을 사용하지 않아도 됨
    @PostMapping("/sample/join")
    public String joinParam(String username, String password, String email){  // key=value 약속된 규칙 (스프링이 해줌)
        System.out.println("username = "+username +" password = "+password+" email = "+email);
        return "회원 가입 완료 !!";
    }

    // 2. 파라미터를 entity 오브젝트로 받아 보자
    @PostMapping("/sample/obj")
    public String joinObject(User user){
        System.out.println("ID = "+user.getId()+" username = "+user.getUsername() +
                " password = "+user.getPassword()+" email = "+ user.getEmail());

        user.setRole(RoleType.USER);

        try {
            userRepository.save(user);  // DB에 insert한다.
        }catch (Exception e){
            return "회원 정보 Insert 실패 ===>" + e.getMessage();
        }


        return "회원 정보 Insert 완료 !!";
    }

    /*****************************************************
     * Select 단건 조회
     ****************************************************/
    // {id} get방식으로 파라미터를 전달 받을 수 있습니다.
    @GetMapping("/sample/user/{id}")
    public User selectDetail(@PathVariable int id){

        /*************************************************************************************************

        // id로 user를 찾을때 일반적인 get()은 null이면 return null이 되기 때문에 오류 발생 가능
        // User user = userRepository.findById(id).get();

        // Optional로 user객체를 감싸서 주면 null인지 아닌지 직접 판단해서 return해야 한다.


        // 방법 1) user의 값이 null 이면 익명 객체를 통해서 비어있는 user 객체를 하나 만들어서 리턴한다.
        User user = userRepository.findById(id).orElseGet(new Supplier<User>() {  // user의 값이 null이면
            @Override
            public User get() {
                return new User();  // 빈 객체를 하나 만들어서 리턴하면 적어도 null은 아님
            }
        });

        // 방법 2) IllegalArgumentException을 이용한다.
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                // 아래 메시지는 나중에 AOP를 이용해서 에러처리화면으로 보낼 수 있다.
                return new IllegalArgumentException("해당 유저는 없습니다. id = ["+id+"]");
            }
        });
        **************************************************************************************************/

        // 방법 2) 를 람다식으로 수정
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 사용자는 없습니다. id = ["+id+"]");
        });

        // 웹브라우저 요청 => 자바 오브젝트인 user 객체를 리턴 => 웹브라우저가 이해할 수 있는 타입으로 변환 => JSON (Gson라이브러리)
        // 스프링 부트는 MessageConverter가 응답할 때 자바 오브젝트를 자동으로 Jackson이라는 라이브러리를 이용하여 Json으로 변한한다.

        return user;
    }

    /*****************************************************
     * Select 다건 조회
     ****************************************************/
    @GetMapping("/sample/user/list")
    public List<User> selectAll(){

        return userRepository.findAll();
    }

    /*****************************************************
     * Select 다건 조회 (페이징 처리)
     * 한페이지당 2건씩 보여주는 페이지 처리
     * JPA의 페이징 기능 이용 하기
     * 브라우저 호출 : /sample/user/page1?page=0 부터
     ****************************************************/
    // 1. 페이지와 관련된 부가적인 정보를 모두 리턴 (http://localhost:8080/sample/user/page1?page=0)
    @GetMapping("/sample/user/page1")
    public Page<User> pageList1(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<User> users = userRepository.findAll(pageable);
        return users;
    }

    // 2. 부가적인 정보 없이 순수 데이터베이스 정보만 페이징 처리 (http://localhost:8080/sample/user/page2?page=0)
    @GetMapping("/sample/user/page2")
    public List<User> pageList2(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        List<User> users = userRepository.findAll(pageable).getContent();
        return users;
    }

    // 3. 최종 권장 코드 (http://localhost:8080/sample/user/page3?page=0)
    @GetMapping("/sample/user/page3")
    public List<User> pageList3(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<User> pageUsers = userRepository.findAll(pageable); // 페이징 클래스 이용 (Page클래스의 기능을 이용할 수 있음)
        if (pageUsers.isFirst()) {} // 첫번째 페이지 이면
        if (pageUsers.isLast()) {}  // 마지막 페이지 이면

        List<User> users = pageUsers.getContent(); // 컨텐트만 리스트 형 변수에 옮겨 담는다.
        return users;
    }

    /*******************************************************************
     * Update (수정)
     * Form을 사용하지 않고 JSON으로 데이터를 받아서 수정 (@RequestBody 사용)
     * 클라이언트에서 AJAX 사용하여 요청 => 스프링의 MessageConverter가 Java Object로 변환해서 받아줌
     *
     * 1. save() 함수 사용하는 방법
     * save()함수는 id를 전달하지 않거나 전달 하더라도 해당 id의 데이터가 없으면 insert 실행
     * 키값(id)이 있으면서 해당 데이터가 있을 경우 자동으로 update를 실행한다.
     *
     * 2. @Transactional 을 이용한 더티 체킹 방법
     * 더티 체킹은 @Transactional에 의해 트랜잭션이 종료될 때 자동 commmit이 되는 것을 말함
     *******************************************************************/
    // 1.save() 함수 사용하는 방법  (패스워드와 이메일만 수정 시)
    @PutMapping("/sample/user/update1/{id}")
    public User updateUser1(@PathVariable int id, @RequestBody User requestUser ){

        // 기존 데이터를 먼저 읽어 온다.
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정 실패 !!");
        });

        // 변경된 값을 셋팅한다.
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        userRepository.save(requestUser);
        return null;
    }

    // 2. @Transactional 을 이용한 더티 체킹 방법 (패스워드와 이메일만 수정 시)
    @Transactional // updateUser2 메서드가 종료될 때 commit이 된다.
    @PutMapping("/sample/user/update2/{id}")
    public User updateUser2(@PathVariable int id, @RequestBody User requestUser ){

        // 기존 데이터를 먼저 읽어 온다.
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정 실패 !!");
        });

        // 변경된 값을 셋팅한다.
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        // save() 없어도 더티 체킹을 이용해서 commit
        // user 오브렉트가 변경되어 있으면 스프링이 트랜잭션이 종료될 때 변경여부를
        // 감지하여 자동으로 commit 시켜준다.
        return user;  // user를 리턴해서 변경 내용을 확인한다.
    }

    /*****************************************************
     * Delete 삭제
     ****************************************************/
    @DeleteMapping("/sample/user/{id}")
    public String delete(@PathVariable int id){

        try {
            userRepository.deleteById(id);  // deleteById()메서드는 리턴값이 없기 때문에 try로 묶는다.
        }catch (Exception e){

            // handler패키지에 GlobalExceptionHandler같은 공통 메시지 처리 클래스를 만들어서 사용할 수 있다.
            return e + " 삭제에 실패 하였습니다.";
        }

        return id + " 삭제 완료 !!";
    }

}
