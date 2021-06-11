package com.kye.iStudyManageApp.sample.api;

import com.kye.iStudyManageApp.sample.dto.ResponseDto;
import com.kye.iStudyManageApp.sample.model.RoleType;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

// JSON으로 통신하기 윈한 api, 클라이언트에서는 ajax로 통신
// 인증이 필요없는 곳은 auth를 추가
@RestController
public class UserApiController {

    @Autowired
    private UserService userService;
/*

    // DI를 해서 사용할 수도 있지만 메서드의 파라미터로 등록하는 순간 스프링이 빈으로 등록해 줌
    // 여러 메서드에서 파라미터로 등록해야 한다면 여기서 한번 선언해서 사용하는 것이 좋다.
    @Autowired
    private HttpSession session;
*/

    /**************
     * 회원 가입 (인증이 필요없는 경우)
     **************/
    @PostMapping("/api/auth/join")
    public ResponseDto<Integer> save(@RequestBody User user) {

        // 관리자 권한은 서버에서 직접 넣어 줘야 하니 여기서 생성
        user.setRole(RoleType.USER);

        // 서비스 호출
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);  // 자바오브젝트를 JSON으로 변환해서 리턴
    }

    /**********************
     * 로그인 (전통적인 방식)
     **********************/
/*

    @PostMapping("/api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {

        System.out.printf("UserApiContller : login 호출됨 ");

        User principal = userService.로그인(user); // principal은 접근주체
        System.out.printf("principal.getUsername : " + principal.getUsername());

        if (principal != null) {
            session.setAttribute("principal", principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
*/

    /**********************
     * 로그인 (스프링부트 시큐리티 방식)
     * 시큐리티 라이브러리가 적용되면 무조건 스프링 기본 로그인 화면으로 간다.
     **********************/


}
