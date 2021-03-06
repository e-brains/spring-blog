package com.kye.iStudyManageApp.sample.api;

import com.kye.iStudyManageApp.sample.auth.PrincipalDetail;
import com.kye.iStudyManageApp.sample.dto.ResponseDto;
import com.kye.iStudyManageApp.sample.model.RoleType;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

// JSON으로 통신하기 윈한 api, 클라이언트에서는 ajax로 통신
// 인증이 필요없는 곳은 auth를 추가
@RestController
public class UserApiController {

    @Autowired  // DI로 주입됨
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

/*

    // DI를 해서 사용할 수도 있지만 메서드의 파라미터로 등록하는 순간 스프링이 빈으로 등록해 줌
    // 여러 메서드에서 파라미터로 등록해야 한다면 여기서 한번 선언해서 사용하는 것이 좋다.
    @Autowired
    private HttpSession session;
*/

    /**************
     * 회원 가입 (전통적인 방식)
     **************/
/*

    @PostMapping("/api/user/join")
    public ResponseDto<Integer> save(@RequestBody User user) {

        // 관리자 권한은 서버에서 직접 넣어 줘야 하니 여기서 생성
        user.setRole(RoleType.USER);

        // 서비스 호출
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);  // 자바오브젝트를 JSON으로 변환해서 리턴
    }
*/

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
    /**************
     * 스프링부트 시큐리티 방식 회원 가입
     **************/
    @PostMapping("/api/auth/join")
    public ResponseDto<Integer> save(@RequestBody User user) {

        System.out.printf("api/auth/join security save ============== \n");

        // 서비스 호출
        userService.시큐리티_회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);  // 자바오브젝트를 JSON으로 변환해서 리턴
    }

    /**************
     * 스프링부트 시큐리티 방식 회원 정보 수정
     **************/
    @PutMapping("/api/auth/userUpdate")
    public ResponseDto<Integer> userUpdate(@RequestBody User user, HttpSession session){
        System.out.printf("api/auth/userUpdate security update ============== \n");

        // 서비스 호출
        userService.회원정보수정(user);

        // 여기서 트랜잭션이 종료되면서 더티체킹에 의해 DB 값이 변경되었지만
        // 시큐리티 세션 값은 변경되지 않은 상태이기 때문에 여기서 세션 값을 직접 갱신하고 생성한다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // mustache 에서 갱신된 데이터를 읽기 위해서 갱신된 값을 session에 넣기
        session.setAttribute("user", user);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }




    /**********************
     * 스프링부트 시큐리티 방식 로그인
     * 별도로 맵핑하지 않고 스프링이 가로채서 로그인 하도록 설정한다.
     * SecurityConfig클래스에서 설정하면 된다.
     * .loginProcessingUrl("/api/auth/login")로 설정 하면
     * 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인을 해준다.
     **********************/



}
